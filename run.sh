PYTHON="python2"  # Python's command for version 2.7
WORKING_DIR=`pwd`
TEST_SUITE="$WORKING_DIR/test_suite"
ARC="$WORKING_DIR/test_area/arc"
TEST_RESULTS="$WORKING_DIR/test_results"
RUN="1"

echo "::Starting ARC Testing (Run $RUN) using the ARC-Test-Suite"

# Iterate over all the tests and work through them
cd $TEST_SUITE
for test in *
do

  echo "::Moving $test to test_area"
  cp -R $TEST_SUITE/$test $ARC/input

  echo "::Replacing config.py with $test's"
  cd $ARC
  cp $ARC/input/$test/config.py $ARC/src/config.py

  echo "::Executing ARC for Test $test"
  cd $ARC/src
  time $PYTHON arc.py

  echo "::Moving $test Results to test_results"
  mkdir -p $TEST_RESULTS/$test/$RUN
  mv $ARC/src/log.txt $TEST_RESULTS/$test/$RUN/  # Log File
  mv $ARC/output $TEST_RESULTS/$test/$RUN  # Solution Program
  mv $ARC/tmp $TEST_RESULTS/$test/$RUN  # Tmp Directory

  echo "::Cleaning up ARC for next test"
  cd $WORKING_DIR
  rm -R $ARC/output
  rm -R $ARC/project_backup
  rm $ARC/src/log.txt
  rm $ARC/src/config.py
  rm $ARC/src/config.pyc

done
