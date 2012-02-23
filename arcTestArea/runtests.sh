echo ------------------------------------------------------
echo ARC test suite
echo Note that the tests are run in order from shortest to
echo longest.
echo Test order is: 2, 3, 6, 8, 12, 21, 23, 25, 5, 7, 30, 2
echo
echo Tests 7, 30 and 2 take up to 45, 60 and 480 sec.
echo respectively to run!
echo
echo ----------------- Running Test 2 -----------------
cd 02/src/
python ./arc.py
cd ../../

echo ----------------- Running Test 3 -----------------
cd 03/src/
python ./arc.py
cd ../../



echo ----------------- Running Test 6 -----------------
cd 06/src/
python ./arc.py
cd ../../

echo ----------------- Running Test 8 -----------------
cd 08/src/
python ./arc.py
cd ../../

echo ----------------- Running Test 12 -----------------
cd 12/src/
python ./arc.py
cd ../../

echo ----------------- Running Test 21 -----------------
cd 21/src/
python ./arc.py
cd ../../

echo ----------------- Running Test 23 -----------------
cd 23/src/
python ./arc.py
cd ../../

echo ----------------- Running Test 25 -----------------
cd 25/src/
python ./arc.py
cd ../../

echo ----------------- Running Test 5 -----------------
echo timeout 12 sec
cd 05/src/
python ./arc.py
cd ../../

echo ----------------- Running Test 7 -----------------
echo timeout 45 sec
cd 07/src/
python ./arc.py
cd ../../

echo ----------------- Running Test 30 -----------------
echo timeout 60 sec
cd 30/src/
python ./arc.py
cd ../../

echo ----------------- Running Test 2 -----------------
echo timeout 480 sec
cd 04/src/
python ./arc.py
cd ../../
