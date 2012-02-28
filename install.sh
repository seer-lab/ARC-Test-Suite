echo "Acquiring ARC from GitHub (Using SSH, requires access and credentials)"
git clone git@github.com:sqrg-uoit/arc.git test_area/arc

echo "Adding junit-4.8.1.jar to ARC"
mkdir test_area/arc/lib
wget https://github.com/downloads/KentBeck/junit/junit-4.8.1.jar
mv junit-4.8.1.jar test_area/arc/lib/

echo "Please add the \"ConTest\" directory to \"test_area\arc\lib\""

echo "Then you can execute run.sh"
