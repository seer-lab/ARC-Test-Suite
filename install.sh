echo "::Acquiring ARC from GitHub (Using SSH, requires access and credentials)"
git clone git@github.com:sqrg-uoit/arc.git test_area/arc
cd test_area/arc
git checkout 89207b53cbb6a95f633e1162e027a73c7a7c5c93  # May not be required eventually
cd ../..

mkdir test_area/arc/lib
mkdir test_area/arc/input
mkdir test_results

echo "::Adding junit-4.8.1.jar to ARC"
wget https://github.com/downloads/KentBeck/junit/junit-4.8.1.jar
mv junit-4.8.1.jar test_area/arc/lib/

echo "::Please add the \"ConTest\" directory to \"test_area\arc\lib\""

echo "::Then you can execute run.sh"
