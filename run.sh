#!/bin/bash

PYTHON="python"  # Python's command for version 2.7
WORKING_DIR=`pwd`
TEST_SUITE="$WORKING_DIR/test_suite"
ARC="$WORKING_DIR/test_area/arc"
TEST_RESULTS="$WORKING_DIR/test_results"
RUNS="5"
OUTDIR="1"

echo "::Starting ARC Testing (Run $RUN) using the ARC-Test-Suite"

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

    echo "::Moving $test to test_area"
    cp -R $TEST_SUITE/$test $ARC/input

    echo "::Replacing config.py with $test's"
    cd $ARC
    cp $ARC/input/$test/config.py $ARC/src/config.py

    mkdir -p $TEST_RESULTS/$test/$OUTDIR

    echo "::Executing ARC for Test $test"
    cd $ARC/src

    exec 3>&1 4>&2
    foo=$( { time $PYTHON arc.py 1>&3 2>&4; } 2>&1 ) # change some_command
    exec 3>&- 4>&-
    echo $foo > $TEST_RESULTS/$test/$OUTDIR/time.txt
    #time $PYTHON arc.py

    echo "::Moving $test Results to test_results"

    mv $ARC/src/log.txt $TEST_RESULTS/$test/$OUTDIR/  # Log File
    mv $ARC/src/config.py $TEST_RESULTS/$test/$OUTDIR/ # Config info
    mv $ARC/output $TEST_RESULTS/$test/$OUTDIR  # Solution Program
    #mv $ARC/tmp $TEST_RESULTS/$test/$OUTDIR  # Tmp Directory

    echo "::Cleaning up ARC for next test"
    cd $WORKING_DIR
    rm -Rf $ARC/output
    rm -Rf $ARC/project_backup
    rm -f $ARC/src/log.txt
    rm -f $ARC/src/config.py
    rm -f $ARC/src/config.pyc
  done
done
