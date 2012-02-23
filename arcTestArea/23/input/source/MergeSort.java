//package MergeSort;
import java.io.*;
import java.lang.Thread;


/**
 * <p>Title: <MultiThreading Merge Sort/p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

/**
 * Summary description for MainFRM.
 */



/**
 * Summary description for Class1.
 */
public class MergeSort extends Thread
{
        private static String outputFile;
        private static BufferedWriter bWriter;
        private static FileWriter fWriter;

        private static boolean OperatedCorrectly = true;   // DK

        //Constractor - silence
        public MergeSort()
        {
                bIsInit = false;
        }

         //Constractor-get's the vector
        public MergeSort(int[] iArray)
        {
                m_iArray = iArray;
                bIsInit = true;
        }

        //Constractor-get's the command line string
        public MergeSort(String[] sCommandLine,String fileName)
        {
              OperatedCorrectly = false; // DK

              if(!Init(sCommandLine))
                System.exit(-1);

               outputFile=fileName;
               bIsInit = true;
               try
               {
                  fWriter = new FileWriter(outputFile);
                  bWriter = new BufferedWriter(fWriter);
               }
               catch(IOException e)
               {
                 System.out.println("jj");
                 System.exit(-1);
               }
        }

        //Get's command line,might be invoked with silence Cnstr from main
        private boolean Init(String[] sCommandLine)
        {
                int iLength = sCommandLine.length;
                if (iLength < 3)//First is reserved for Threads limit,at least 2 params required for sorting
                {
                  System.out.println("The program input should consist of at least 3 parameters !");
                  return false;
                }
                m_iThreadLimit = Integer.parseInt(sCommandLine[0]);
                int iInputs = iLength-1;
                if ((m_iThreadLimit > (iInputs/2)) || (m_iThreadLimit < 2))
                {
                  System.out.println("The given threads limit exceeds the maximum/minimum allowed !");
                  return false;
                }

                m_iArray = new int[iLength-1];
                for (int iCnt = 0;iCnt < iLength-1;iCnt++)
                        m_iArray[iCnt] = Integer.parseInt(sCommandLine[iCnt+1]);
                bIsInit = true;
                return true;
        }

        //Reset currently available threads number to 1,should be done in main,before starting of sorting
        public void ResetThreadCounter()
        {
                m_iCurrentThreadsAlive = 1;
        }

        public void Sorting()
        {
                if (!bIsInit)//If invoked with String/Silence Constractor,initialization is a mandatory demand
                {
                        System.out.println("The data isn't initialized !");
                        return;
                }
                int iSize = m_iArray.length;
                if (iSize == 1)//Recursion stop condition
                        return;
                int[] iA = new int[iSize/2];//Split a source array to Left/Right arrays
                int[] iB = new int[iSize - (iSize/2)];
                CopyArrays(m_iArray,iA,iB);

                // -----------------------------------------
                // BUG: Multiple threads can call AvailableThreadState and
                //      find an inconcsistent number of threads available
                //      For this to work, the lines from int iMultiThreadedSons =
                //      through the second DecreaseThreadCounter() in case 2 have
                //      to be synchronized.
                //      IE, threads created in AvailableThreadState() have to match
                //      threads destroyed in DecreasethreadCounter()
                //
                // ARC can't fix this because Sorting() is called recursively by
                // each thread.  Bug pattern not supported.

                int iMultiThreadedSons = AvailableThreadsState();//Get available threads state
                MergeSort leftSon=new MergeSort(iA);
                MergeSort rightSon=new MergeSort(iB);

                if (iMultiThreadedSons < 0)  // DK
                  OperatedCorrectly = false;

                switch (iMultiThreadedSons)
                {
                        case 0://No available threads in system
                                leftSon.Sorting();//Recursive sorting of the left tree
                                rightSon.Sorting();//Recursive sorting of the right tree
                                Merge(iA,iB,m_iArray);//Merging
                        break;
                        case 1://Only 1 thread available
                                leftSon.start();//Sort left tree as a new independent process
                                DecreaseThreadCounter();
                                rightSon.Sorting();//Recursive sorting of the right tree
                                try
                                {
                                        leftSon.join();//Wait here until left tree is sorting
                                        IncreaseThreadCounter();//This thread is ended,update available thread counter
                                }
                                catch (InterruptedException r)//Catch Process-related exceptions
                                {
                                        System.out.println("!!!! Thread Execution Failed !!!!!");
                                        System.exit(-1);
                                }
                                Merge(iA,iB,m_iArray);//Merging
                        break;
                        case 2:

                                leftSon.start();//Sort left tree as a new independent process
                                DecreaseThreadCounter();

                                rightSon.start();//Sort right tree as a new independent process
                                DecreaseThreadCounter();

                                try
                                {
                                        leftSon.join();//Wait here until left tree is sorting
                                        IncreaseThreadCounter();//This thread is ended,update available thread counter
                                        rightSon.join();//Wait here until right tree is sorting
                                        IncreaseThreadCounter();//This thread is ended,update available thread counter
                                }
                                catch (InterruptedException r)//Catch Process-related exceptions
                                {
                                        System.out.println("!!!! Thread Execution Failed !!!!!");
                                        System.exit(-1);
                                }
                                Merge(iA,iB,m_iArray);
                        break;
                      // default://I don't see any reason this section will be executed,but just to be sure...
                        // System.out.println("!!!! Thread Execution Failed !!!!!");
//                         System.exit(-1);
//                         break;
                }

        }

        //Print sorted vector - invoked from main()
        public boolean PrintResults()   // DK
        {
          try
          {
            fWriter = new FileWriter("sortedoutput.txt");
            bWriter = new BufferedWriter(fWriter);
          }
          catch (IOException e) {
            System.exit( -1);
          }
          try {
            bWriter.write("Sorted using " + m_iThreadLimit + " thread/s");
            bWriter.newLine();
            for (int iCnt = 0; iCnt < m_iArray.length; iCnt++)
            {
              bWriter.write(iCnt + " : " + m_iArray[iCnt]);
              bWriter.newLine();
            }
            bWriter.close();
          }
          catch (IOException e) {

            System.out.println("jj");
            System.exit( -1);
          }
          return OperatedCorrectly;
        }

        //Increase available threads number by 1,usually done after child thread died
        private static  synchronized void IncreaseThreadCounter()
        {
                          m_iCurrentThreadsAlive--;
        }

        //Decrease available threads number by 1,usually done before runnning a child thread
        private static  synchronized void DecreaseThreadCounter()//Decrease
        {
                m_iCurrentThreadsAlive++;
        }

        //Get a state of available threads in the system and allocate them
        private static synchronized int AvailableThreadsState()//0 - No threads,1 - 1 thread available,2 - 2 threads avilable
        {
                int availableThreads=m_iThreadLimit - m_iCurrentThreadsAlive;
                if ((availableThreads)==0)
                    {
                        return 0;
                    }
                if ((availableThreads) == 1)
                {
                      //  m_iCurrentThreadsAlive++;//Allocate the requested thread
                        return 1;
                }
                if ((availableThreads) >= 2)
                {
                       // m_iCurrentThreadsAlive += 2;//Allocate 2 requested threads
                        return 2;
                }

                System.out.print("BUG  ");
                OperatedCorrectly = false; // DK

                try
                {
                   fWriter = new FileWriter(outputFile,true);
                   bWriter = new BufferedWriter(fWriter);

                }
                catch(IOException e)
                {
                  System.out.println("jj");
                  System.exit(-1);
                }
                String number;
                number=Integer.toString(availableThreads);
                try
                {
                  bWriter.write("<MergeSort," + number+">");
                  bWriter.newLine();
                  bWriter.close();
                }
                catch(IOException e)
                {
                  System.exit(-1);
                }

                return (availableThreads); //The else of all - BUG
        }



        //Internal MergeSort procedure
        private void CopyArrays(int[] iSource,int[] iDest1,int[] iDest2)
        {
                for (int iCnt = 0;iCnt < iDest1.length;iCnt++)
                        iDest1[iCnt] = iSource[iCnt];
                for (int iCnt = 0;iCnt < iDest2.length;iCnt++)
                        iDest2[iCnt] = iSource[iCnt+iDest1.length];
        }

        public void run()//Implements .start() methode of the class
        {
                Sorting(); //Invoke sorting methode
        }

        //An internal recursive sort procedure
        private void Merge(int[] iA,int[] iB,int[] iVector)
        {
                int iALength = iA.length;
                int iBLength = iB.length;

                if ((iALength + iBLength) == 1)
                {
                        if (iALength == 1)
                                iVector[0] = iA[0];
                        else
                                iVector[0] = iB[0];

                        return;
                }

                int iVecCnt = 0;
                int iACnt = 0;
                int iBCnt = 0;

                while ((iACnt < iALength) && (iBCnt < iBLength))
                {
                        if (iA[iACnt] < iB[iBCnt])
                        {
                                iVector[iVecCnt] = iA[iACnt];
                                iACnt++;
                        }
                        else
                        {
                                iVector[iVecCnt] = iB[iBCnt];
                                iBCnt++;
                        }
                        iVecCnt++;
                }
                for (int iCnt = iACnt;iCnt < iALength;iCnt++)
                {
                        iVector[iVecCnt] = iA[iCnt];
                        iVecCnt++;
                }
                for (int iCnt = iBCnt;iCnt < iBLength;iCnt++)
                {
                        iVector[iVecCnt] = iB[iCnt];
                        iVecCnt++;
                }
                return;
        }

        private int[] m_iArray; //Vector to be sorted
        private static int   m_iThreadLimit;//Tread limit - have to be static
        private static int   m_iCurrentThreadsAlive;//Thread Counter - have to be static
        private boolean bIsInit;//If in false state - the sorting wouldn't perform
}
