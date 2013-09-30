""" Parameters for the genetic algorithm without crossover were chosen based
on experience. We have to ask ourselves if the choices are good ones. This
python program is a varaiation of analyze.py which runs the test suite x times,
each time with a single parameter change.  The 4 parameters changed and the 5
different values of each are:

Model checking time: 90s, 60s, 30s, 20s, 10s
Population         : 50, 30, 20, 10, 5
JPF search depth   : 200, 150, 100, 50, 25
Generations        : 30, 20, 10, 5, 3

Running this script can take a while, depending on the number of runs (_RUNS
below) and the size of the test suite.
After this program finishes, run param-analyze.py to get an analysis of the
results. That script also creates a CSV file of the results for further
analysis. Finally, run param-best.py

NOTE that param.py and analyze.py shouldn't be mixed. That is, one shouldn't
be used to analyze the results of the other.

Copyright David Kelk 2013
"""

import subprocess
import os
import tempfile
import re
from send2trash import send2trash
import shutil
import fileinput


_ROOT_DIR = os.getcwd() #"/Users/kelk/workspace/ARC-Test-Suite/"
_TEST_SUITE = os.path.join(_ROOT_DIR, "test_suite")
_CORE = os.path.join(_ROOT_DIR, "test_area", "CORE")
_TEST_RESULTS = os.path.join(_ROOT_DIR, "test_results")
_RUNS = 20


def runParam(valList, configEntry, defaultValue):
  for val in valList:
    # Set the parameter to the value in valList for each test program
    for root, dirs, files in os.walk(_TEST_SUITE):
      for aDir in dirs:
        modifyConfig(os.path.join(_TEST_SUITE, aDir, "config.py"),
          configEntry, val)
    # Invoke the test suite for a particular valList
    RunCore(configEntry, val)

  # Reset the value back to the defaultValue
  for root, dirs, files in os.walk(_TEST_SUITE):
    for aDir in dirs:
      modifyConfig(os.path.join(_TEST_SUITE, aDir, "config.py"),
        configEntry, defaultValue)


def modifyConfig(cfgFile, key, value):
  if not os.path.exists(cfgFile):
    return

  configRoot = fileinput.FileInput(files=(cfgFile), inplace=1)
  for line in configRoot:
    if line.find(key) is 0:
      line = "{} = {} ".format(key, value) # numbers
    print(line[0:-1]) # A trailing-space must exists in modified lines
  configRoot.close()


def calculateTime(cfgDir):
  f = open(os.path.join(cfgDir, 'log.txt'))
  o = open(os.path.join(cfgDir, 'time.txt'), 'w')

  lines_list = list()
  lines_list = f.readlines()

  smallNum = 1000000
  largeNum = 0

  for i in range (1, len(lines_list)):
    line = re.search("\A\d+", lines_list[i])
    if line is None:
      continue

    if int(line.group(0)) < smallNum:
      smallNum = int(line.group(0))
    if int(line.group(0)) > largeNum:
      largeNum = int(line.group(0))

  numSec = int((largeNum - smallNum) / 1000)
  numMin = int(numSec / 60)
  numSec = numSec - (numMin * 60)

  # real 68m49.035s
  o.writelines("real " + str(numMin) + "m" + str(numSec) + "s")

  f.close()
  o.close()


def RunCore(configEntry, configVal):
  # Iterate over all the tests and work through them
  for root, dirs, files in os.walk(_TEST_SUITE):
    for aDir in dirs:
      print("aDir is {}".format(aDir))
      for i in xrange(0, _RUNS):
        outDir = 1
        # The output directory include the varied parameter in the dirname
        # eg: account-_EVOLUTION_POPULATION-5/1/
        _OUT_DIR = os.path.join(_TEST_RESULTS, aDir + "-" + configEntry
          + "-" + str(configVal), str(outDir))
        while os.path.exists(_OUT_DIR):
          outDir += 1
          _OUT_DIR = os.path.join(_TEST_RESULTS, aDir + "-" + configEntry
            + "-" + str(configVal), str(outDir))

        # Remove the previous test program
        if os.path.isdir(os.path.join(_CORE, "input")):
          send2trash(os.path.join(_CORE, "input"))

        print("-------------------------------")
        # Copy the new program to the CORE input directory
        # print("Copying test program:")
        # print("{} to".format(os.path.join(_TEST_SUITE, aDir)))
        # print("{}".format(os.path.join(_CORE, "input")))
        shutil.copytree(os.path.join(_TEST_SUITE, aDir), os.path.join(_CORE,
          "input"))

        # ARC-Test-Suite/test_results/dining-_EVOLUTION_POPULATION-5/2
        os.makedirs(_OUT_DIR)
        print("_OUT_DIR is {}".format(_OUT_DIR))

        outputDir = os.path.join(_CORE, "output")
        print("Deleting {}, if it exists".format(outputDir))
        if os.path.isdir(outputDir):
          send2trash(outputDir)

        print("Executing CORE on {} test program".format(aDir))

        outFile = tempfile.SpooledTemporaryFile()
        errFile = tempfile.SpooledTemporaryFile()

        process = subprocess.Popen(['time', 'python',
                  os.path.join(_CORE, 'src', 'core.py')], stdout=outFile,
                  stderr=errFile, cwd=os.path.join(_CORE, "src"),
                  shell=False)
        process.wait()

        outFile.seek(0)
        # errFile.seek(0)
        output = outFile.read()
        # error = errFile.read()
        outFile.close()
        # errFile.close()

        # Move the results of the tests to test_results/...
        print("Moving {} Results to {}".format(aDir, _OUT_DIR))

        shutil.move(os.path.join(_CORE, "src", "log.txt"), _OUT_DIR)
        shutil.move(os.path.join(_CORE, "src", "config.py"), _OUT_DIR)
        if os.path.isdir(os.path.join(_CORE, "output")):
          shutil.move(os.path.join(_CORE, "output"), _OUT_DIR)
        #if os.path.isdir(os.path.join(_CORE, "tmp")):
        #  shutil.move(os.path.join(_CORE, "tmp"), _OUT_DIR)

        calculateTime(_OUT_DIR)

        print("Cleaning up CORE for next test")
        send2trash(os.path.join(_CORE, "src", "config.pyc"))

    dirs[:] = []

# Varying time
#runParam([90, 60, 30, 20, 10], "_JPF_SEARCH_TIME_SEC", 30)
# Varying population
#runParam([50, 30, 20, 10, 5], "_EVOLUTION_POPULATION", 30)
# Varying search depth
#runParam([200, 150, 100, 50, 25], "_JPF_SEARCH_DEPTH", 50)
runParam([50, 25], "_JPF_SEARCH_DEPTH", 50)
# Varying generations
runParam([30, 20, 10, 5, 3], "_EVOLUTION_GENERATIONS", 30)
