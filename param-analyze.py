""" See param-study.py for the creation of the analysis data. This program
is run after param-study.py to summarize the data and create a csv file for
further analysis.
The analysis here is similar to analyze.py, but customized to the specific
parameter analysis problem. (See analyze.py.)

In order the fields of the CSV file are:

1. Parameter varied (_JPF_SEARCH_TIME_SECONDS, ...)
2. Parameter value (90, ...)
3. Test program (account, ...)
4. Number of runs
5. Number of successful runs
6. Average number of minutes for a successful run
7. Average number of seconds for a successful run
8. Average generation fix was found in (for successful runs)

An example line is:

_JPF_SEARCH_TIME_SEC, 90, account, 4, 4, 3, 15, 1.0

This reads as, "For the account program, when we set the search time for
JPF to 90 seconds, CORE found fixes 100 percent of the time (4/4). The
average time to find a fix was 3 min 15 sec. On average the fix was found
in the first generation."

Copyright David Kelk 2013

"""

import subprocess
import os
import tempfile
import re
from send2trash import send2trash
import shutil
import fileinput


_ROOT_DIR =  os.getcwd() #"/Users/kelk/workspace/ARC-Test-Suite/"
_TEST_SUITE = os.path.join(_ROOT_DIR, "test_suite")
_CORE = os.path.join(_ROOT_DIR, "test_area", "CORE")
_TEST_RESULTS = os.path.join(_ROOT_DIR, "test_results")
_RUNS = 5


def analyzeParam(valList, configEntry, outCsv):
  # _JPF_SEARCH_TIME_SEC, ...
  print("{}".format(configEntry))
  # [5, 10, 20, ...]
  for val in valList:
    print("  {}".format(val))
    os.chdir(_TEST_SUITE)
    # account, accounts, ...
    for aDir in os.walk(os.getcwd()).next()[1]:
      print("    {}".format(aDir))
      # account-_EVOLUTION_POPULATION-5
      progDir = aDir + "-" + configEntry + "-" + str(val)
      # ARC-Test-Suite/test_results/account-_EVOLUTION_POPULATION-5
      dataDir = os.path.join(_TEST_RESULTS, progDir)
      if os.path.isdir(dataDir):
        mainAnalyze(dataDir, configEntry, val, aDir, outCsv)
      else:
        print("      ERROR: Directory {} is missing".format(dataDir))


def mainAnalyze(dataDir, outConfigEntry, outVal, outTestProg, outCsv):
  os.chdir(dataDir)

  num_min = 0
  num_sec = 0
  succ = 0
  tot_trials = 0
  tot_gens = 0
  #print("{}".format(dirs))

  # Iterate over the number of runs
  for subdirs in os.walk(os.getcwd()).next()[1]:
    os.chdir(subdirs)

    # Determine if the run was successful
    tot_trials += 1
    if os.path.isfile("output" + os.sep + "build.xml"):
      succ += 1  # 2. Fix rate
    else:
      # If it wasn't successful, we're done. Go to next
      os.chdir(os.pardir)
      continue

    # Determine the average time
    if not os.path.isfile("time.txt"):
      print("  ERROR: In directory {}/{}, time.txt is missing."
        .format(dirs,subdirs))
      os.chdir(os.pardir)
      continue

    f = open('time.txt')
    line = f.readline()
    f.close()

    # real 68m49.035s user 77m10.359s sys 10m30.042s
    match = re.findall('(\d+)', line)
    #print("{}".format(match))

    num_min += int(match[0]) # real min
    num_sec += int(match[1]) # real sec

    # Determine the average generation the fix was found in
    if os.path.isdir("tmp" + os.sep):
      os.chdir("tmp")

      for i in xrange(100,0, -1):
        if os.path.isdir(str(i)):
          tot_gens += i
          break;

      os.chdir(os.pardir)

    # Look for time gaps in the log file
    f = open('log.txt')
    o = open('gap-analysis.txt', 'w')

    lines_list = list()
    lines_list = f.readlines()

    for i in range (1, len(lines_list)):
      line1 = re.search("\A\d+", lines_list[i - 1])
      line2 = re.search("\A\d+", lines_list[i])
      if line1 is None or line2 is None:
        continue

      gap = int(line2.group(0)) - int(line1.group(0))
      if gap > 30000:
        o.writelines(lines_list[i - 1])
        o.writelines(lines_list[i])
        o.writelines("Difference: {}\n".format(gap))

    f.close()
    o.close()

    # We're done with this program. Move on to the next
    os.chdir(os.pardir)

  print("      {} success in {} trials".format(succ, tot_trials))

  totTime = 0
  totMin = 0
  totSec = 0
  if (succ > 0):
    totTime = (num_min * 60 + num_sec) / succ
    totMin = int(totTime/60)
    totSec = totTime - totMin * 60
    print("      Average time of {} successful trials: {} min {} sec".format(succ,
      totMin, totSec))
  else:
    print("      There were no successful trials.")
  # Division by zero case
  avgGenFix = 0
  if (succ > 0):
    avgGenFix = tot_gens / float(succ)
    # Problem when all fixes are found in 1st gen, avg = 0
    print("      Average generation fix was successfully found in: {}".format(avgGenFix))

  # _EVOLUTION_POPULATION, 50, account, 4, 4, 7, 44, 1.25

  outCsv.writelines("{}, {}, {}, {}, {}, {}, {}, {}\n".format(outConfigEntry,
    outVal, outTestProg, tot_trials, succ, totMin, totSec, avgGenFix))

  os.chdir(os.pardir)






outCsv = open(os.path.join(_ROOT_DIR,'param-analyze.csv'), 'w')
try:
  # Varying time
  analyzeParam([90, 60, 30, 20, 10], "_JPF_SEARCH_TIME_SEC", outCsv)
  # Varying population
  analyzeParam([50, 30, 20, 10, 5], "_EVOLUTION_POPULATION", outCsv)
  # Varying search depth
  analyzeParam([200, 150, 100, 50, 25], "_JPF_SEARCH_DEPTH", outCsv)
  # Varying generations
  analyzeParam([30, 20, 10, 5, 3], "_EVOLUTION_GENERATIONS", outCsv)
finally:
  outCsv.close()
  print "param.csv created."