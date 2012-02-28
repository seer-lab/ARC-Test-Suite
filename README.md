# ARC Test Suite

This repository contains all the tests used in ARC. A script is included to automate the execution and collection of the results of this test.

## test_suite
All the tests are located in this directory. ARC's *config.py* file for each test is also contained within these tests.

## test_area
ARC and ConTest are located in this directory. The *run.sh* script moved tests from the *test_suite* to this directory to be executed by ARC one-at-a-time.

## test_results
The results of the tests are placed in this directory. The test's log file, and corrected program (if found) are placed in their respected directory.

## Instructions

* Execute ```install.sh``` to install ARC and the JUnit.jar
* Manually place the *ConTest* directory into ARC under *test_area/arc/lib/*
* Execute ```run.sh```
