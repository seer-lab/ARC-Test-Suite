#!/bin/bash

clear

PYTHON="python"  # Python's command for version 2.7
WORKING_DIR=`pwd`
TEST_SUITE="$WORKING_DIR/test_suite"
CORE="$WORKING_DIR/test_area/core"
TEST_RESULTS="$WORKING_DIR/test_results"
RUNS="1"
OUTDIR="1"

echo "::Starting CORE Testing (Run $RUN) using the Test-Suite"

# Iterate over all the tests and work through them
cd $TEST_SUITE
for test in *;
do
  for (( i=1; i<=$RUNS; i++ ));
  do
    OUTDIR="1"
    while [[ -d "$TEST_RESULTS/$test/$OUTDIR" ]]; do
      OUTDIR="$(expr "$OUTDIR" '+' '1')"
    done

    rm -Rf $CORE/input
    echo "::Moving $test to test_area"
    cp -R $TEST_SUITE/$test $CORE/input

    echo "::Replacing config.py with $test's"
    cp $CORE/input/config.py $CORE/src/

    mkdir -p $TEST_RESULTS/$test/$OUTDIR

    rm -Rf $CORE/output
    echo "::Executing CORE for Test $test"
    cd $CORE/src

    exec 3>&1 4>&2
    foo=$( { time $PYTHON core.py 1>&3 2>&4; } 2>&1 ) # change some_command
    exec 3>&- 4>&-
    echo $foo > $TEST_RESULTS/$test/$OUTDIR/time.txt

    echo "::Moving $test Results to test_results"

    mv $CORE/src/log.txt $TEST_RESULTS/$test/$OUTDIR/  # Log File
    mv $CORE/src/config.py $TEST_RESULTS/$test/$OUTDIR/ # Config info
    mkdir $TEST_RESULTS/$test/$OUTDIR/output
    mv $CORE/output $TEST_RESULTS/$test/$OUTDIR  # Solution Program
    mkdir $TEST_RESULTS/$test/$OUTDIR/tmp
    mv $CORE/tmp $TEST_RESULTS/$test/$OUTDIR  # Tmp Directory

    echo "::Cleaning up CORE for next test"
    cd $WORKING_DIR
    rm -Rf $CORE/output
    rm -Rf $CORE/project_backup
    rm -f $CORE/src/log.txt
    rm -f $CORE/src/config.py
    rm -f $CORE/src/config.pyc
  done
done
