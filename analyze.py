""" Analyze the bug fixing runs. For each of the directories in
test_results, determine if the fix was successful or not.
Considering only successful fixes:

1. Determine the average real time from time.txt.
2. Determine average fix rate by looking for files in the
   output directory.
3. Determine the average generation that the fix is found in.

"""

import subprocess
import os
import tempfile
import re

os.chdir("test_results")

# Iterate over the fixed programs, by program
for dirs in os.walk(os.getcwd()).next()[1]:
  os.chdir(dirs)

  num_min = 0
  num_sec = 0
  succ = 0
  tot_trials = 0
  tot_gens = 0

  print("{}".format(dirs))

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

    num_min += int(match[0]) # real min
    num_sec += int(match[1]) # real sec

    # 3. Determine the average generation the fix was found in
    if os.path.isdir("tmp" + os.sep):
      os.chdir("tmp")

      for i in xrange(100,0, -1):
        if os.path.isdir(str(i)):
          tot_gens += i
          break;

      os.chdir(os.pardir)

    # 4. Look for time gaps in the log file
    f = open('log.txt')
    o = open('gap-analysis.txt', 'w')

    lines_list = list()
    lines_list = f.readlines()

    #print ("Len: {}".format(len(lines_list)))
    for i in range (1, len(lines_list)):
      #print lines_list[i]
      line1 = re.search("\A\d+", lines_list[i - 1])
      line2 = re.search("\A\d+", lines_list[i])
      if line1 is None or line2 is None:
        continue

      #print ("Comparing {} and {}".format(line2, line1))
      gap = int(line2.group(0)) - int(line1.group(0))
      if gap > 30000:

        o.writelines(lines_list[i - 1])
        o.writelines(lines_list[i])
        o.writelines("Difference: {}\n".format(gap))

    f.close()
    o.close()

    # We're done with this program. Move on to the next
    os.chdir(os.pardir)

  print("  {} success in {} trials".format(succ, tot_trials))

  if (tot_trials > 0):
    print("  Average time of {} trials: {} min {} sec".format(succ,
      num_min / tot_trials, num_sec / tot_trials))

  if (succ > 0):
    # Problem when all fixes are found in 1st gen, avg = 0
    print("  Average generation fix was found in: {}".format(tot_gens / float(succ)))

  os.chdir(os.pardir)