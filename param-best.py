import subprocess
import os
import tempfile
import re
from send2trash import send2trash
import shutil
import fileinput
import csv

_ROOT_DIR =  os.getcwd() #"/Users/kelk/workspace/ARC-Test-Suite/"
_TEST_SUITE = os.path.join(_ROOT_DIR, "test_suite")
_CORE = os.path.join(_ROOT_DIR, "test_area", "CORE")
_TEST_RESULTS = os.path.join(_ROOT_DIR, "test_results")


def findBestParam(configEntry, paramValsList, paramIn, cvsReader, cvsWriter):
  # Gather the list of test programs
  # [acount, accounts, ...]
  testProgList = []
  paramIn.seek(0)
  for row in cvsReader:
    if row[2] not in testProgList:
      testProgList.append(row[2])
  print testProgList

  for testProg in testProgList:
    minMin = 1000000
    minSec = 1000000
    valParam = 0
    for paramVal in paramValsList:
      # Look for smallest minutes and seconds for given
      paramIn.seek(0) # Reset to beginning
      for row in cvsReader:
        #print("Looking for {}, {}, {} in {}".format(configEntry, paramVal,
        #  testProg, row))
        if row[0] <> configEntry:
          continue
        if int(row[1]) <> int(paramVal):
          continue
        if row[2] <> testProg:
          continue
        #print("{}".format(row))
        # Smaller in minutes
        if int(row[5]) < int(minMin):
          #print("min {} is < min {}".format(row[5], minMin))
          valParam = row[1]
          minMin = row[5]
          minSec = row[6]
        # Minutes the same, smaller in seconds
        if int(row[5]) == int(minMin) and int(row[6]) < int(minSec):
          valParam = row[1]
          minSec = row[6]
    cvsWriter.writerow([configEntry, testProg, valParam, minMin, minSec])



with open('param.csv', 'rb') as paramIn:
  dialect = csv.Sniffer().sniff(paramIn.read(1024))
  paramIn.seek(0)
  cvsReader = csv.reader(paramIn, dialect)

  with open('param-best.csv', 'w') as paramOut:
    cvsWriter = csv.writer(paramOut, dialect)

    try:
      # Varying time
      findBestParam("_JPF_SEARCH_TIME_SEC", [90, 60, 30, 20, 10], paramIn,
        cvsReader, cvsWriter)
      # Varying population
      findBestParam("_EVOLUTION_POPULATION", [50, 30, 20, 10, 5], paramIn,
        cvsReader, cvsWriter)
      # Varying search depth
      # findBestParam("_JPF_SEARCH_DEPTH", [200, 150, 100, 50, 25], paramIn,
      #  cvsReader, cvsWriter)
      # Varying generations
      # findBestParam("_EVOLUTION_GENERATIONS", [30, 20, 10, 5, 3], paramIn,
      #  cvsReader, cvsWriter)
    finally:
      paramIn.close()
      paramOut.close()