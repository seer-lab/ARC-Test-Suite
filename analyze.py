""" Analyze the bug fixing runs. For each of the directories in
test_results:

1. Determine the average real/user/system time from time.txt.
2. Determine average fix rate by looking for files in the
   output directory.

"""

import subprocess
import os
import tempfile
import re
#import fractions

os.chdir("test_results")

for dirs in os.walk(os.getcwd()).next()[1]:
  # Here we are interested in subdirectories only
  #for projSubDir in dirs:
  os.chdir(dirs)

  num_min = 0
  num_sec = 0
  tot_time_trials = 0
  succ = 0
  tot_trials = 0

  print("{}".format(dirs))

  for subdirs in os.walk(os.getcwd()).next()[1]:
    os.chdir(subdirs)

    # 1. Determine the average time
    if not os.path.isfile("time.txt"):
      print("  In directory {}/{}, time.txt is missing."
        .format(dirs,subdirs))
      os.chdir(os.pardir)
      continue

    f = open('time.txt')
    line = f.readline()
    f.close()

    # real 68m49.035s user 77m10.359s sys 10m30.042s
    match = re.findall('(\d+)', line)
    #print("{}".format(match))

    num_min += int(match[0])
    num_sec += int(match[1])
    tot_time_trials += 1

    # 2. Determine if the run was successful
    tot_trials += 1
    if os.path.isfile("output" + os.sep + "build.xml"):
      succ += 1

    os.chdir(os.pardir)

  if (tot_time_trials > 0):
    print("  Average time of {} trials: {} min {} sec".format(tot_time_trials,
      num_min / tot_time_trials, num_sec / tot_time_trials))

  if (tot_trials > 0):
    print("  {} success in {} trials".format(succ, tot_trials))

  os.chdir(os.pardir)