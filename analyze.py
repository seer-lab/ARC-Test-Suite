""" Analyze the bug fixing runs. For each of the directories in
test_results, determine if the fix was successful or not.
Considering only successful fixes:

- Determine the number of successes.
- Determine the average time, max time and min time for a fix, all from
  time.txt.
- Determine average fix rate by looking for files in the
  output directory.
- Determine the average generation that the fix is found in.
- Optionally, display large time gaps. These are interesting as they
  could indicate something has gone wrong, or something could be improved.

Copyright David Kelk 2013

"""

import subprocess
import os
import tempfile
import re

# Look for gaps in the log files larger than this value (ms)
_gapSize = 40000

os.chdir("test_results")

# Iterate over the fixed programs, by program
for dirs in os.walk(os.getcwd()).next()[1]:
  os.chdir(dirs)

  num_sec = 0
  max_min = max_sec = 0
  min_min = min_sec = 999999
  succ = 0
  tot_trials = 0
  tot_gens = 0

  print("{}".format(dirs))

  # Iterate over the number of runs
  for subdirs in os.walk(os.getcwd()).next()[1]:
    os.chdir(subdirs)

    # Determine if the run was successful
    tot_trials += 1
    if os.path.isfile(os.path.join("output", "build.xml")):
      succ += 1  # 2. Fix rate
    else:
      # If it wasn't successful, we're done. Go to next
      os.chdir(os.pardir)
      continue

    # Determine the average time (To fix a program)
    if not os.path.isfile("time.txt"):
      print("  In directory {}, time.txt is missing."
        .format(os.path.join(dirs,subdirs)))
      os.chdir(os.pardir)
      continue

    f = open('time.txt')
    line = f.readline()
    f.close()

    # real 68m49.035s user 77m10.359s sys 10m30.042s
    match = re.findall('(\d+)', line)
    #print("{}".format(match))

    runMin = int(match[0]) # real min
    runSec = int(match[1]) # real sec

    # Average time
    num_sec += runMin * 60
    num_sec += runSec

    # Max/min time
    if (runMin > max_min or (runMin == max_min and runSec > max_sec)):
      max_min = runMin
      max_sec = runSec
    if (runMin < min_min or (runMin == min_min and runSec < min_sec)):
      min_min = runMin
      min_sec = runSec

    # Determine the average generation the fix was found in
    genFH = open('log.txt')
    lineList = list()
    lineList = genFH.readlines()

    for i in range (1, len(lineList)):
      findLine = re.search("Copying fixed (\w+),? Individual:(\d+) Generation:(\d+) to",
        lineList[i])
      if findLine:
        #print("Generation: {}".format(findLine.group(3)))
        tot_gens += int(findLine.group(3))
    genFH.close()


    # 4. Look for time gaps in the log file
    f = open('log.txt')
    o = open('gap-analysis.txt', 'w')

    lines_list = list()
    lines_list = f.readlines()

    for i in range (1, len(lines_list)):
      line1 = re.search("\A\d+", lines_list[i - 1])
      line2 = re.search("\A\d+", lines_list[i])
      if line1 is None or line2 is None:
        continue


      gap = int(line2.group(0)) - int(line1.group(0))
      #print("{} - {} = {}".format(line2.group(0), line1.group(0), str(gap)))
      # The static analysis takes a while and is usually around lines 55-65
      # of the log file. It is a known gap so we don't want to see it in the
      # output
      if gap > _gapSize and (i < 55 or i > 65):
        o.writelines(lines_list[i - 1])
        o.writelines(lines_list[i])
        o.writelines("Difference: {}\n".format(gap))
        print("  Gap of {}sec found in path {} at lines {}, {}"
          .format(str(gap / 1000), os.path.join(dirs,subdirs), str(i),
          str(i+1)))

    f.close()
    o.close()

    # We're done with this program. Move on to the next
    os.chdir(os.pardir)

  print("  {} success in {} trials".format(succ, tot_trials))

  if (tot_trials > 0):
    if (succ > 0):
      num_sec = int(num_sec / succ)
    else:
      num_sec = 0
    avgMin = int(num_sec / 60)
    avgSec = num_sec - avgMin*60
    print("  Average time of {} trials: {} min {} sec".format(succ,
      avgMin, avgSec))
    print("  Max time: {} min {} sec".format(max_min, max_sec))
    print("  Min time: {} min {} sec".format(min_min, min_sec))

  if (succ > 0):
    print("  Average generation fix was found in: {}".format(tot_gens / float(succ)))

  os.chdir(os.pardir)

print("\nGaps occuring around lnes 55-60 are due to the static analysis. ")