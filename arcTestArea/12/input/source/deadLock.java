import java.util.*;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: liran klein
 * Date: 02/10/2003
 * Time: 11:06:19
 * To change this template use Options | File Templates.
 */
public class deadLock implements Runnable
 {
    Object a=new file("a");    
    Object b=new file("b");    
    //static Object ALock = new Object();  // DK
    int MAX;                                                                                                    // number of thread particepating
    Thread threadArr[];
    FileOutputStream output;                                                              // the output file
    static Hashtable hash= new Hashtable();                               //  we use this hashtable to see if the deadlock condition occur
    boolean flag =true;

    public static boolean main(String args[]) throws IOException {
        deadLock D = new deadLock(args[0],args[1]);
        return D.RunNow();
        
    }
    
   public deadLock(){
   }
   
   public deadLock(String fileName, String concurrencyParam) throws IOException {
       if (concurrencyParam.equals("little")) MAX=3;
       else if (concurrencyParam.equals("average")) MAX=7;
       else if (concurrencyParam.equals("lot")) MAX=15;
       threadArr=new Thread [MAX];                                                          // we decide how many thread should be in here.
       
       try {
           output =new FileOutputStream(fileName);
       } 
       catch (FileNotFoundException e) {
           e.printStackTrace();
       }
    }
       
  public boolean RunNow() throws IOException {
       for (int i=0;i<MAX;i++)                                                                    //we start the thread
       {
          threadArr[i]=new Thread(this,""+i);
          threadArr[i].start();
       }

       boolean OperatedCorrectly = false;
       
       for (int i=0;i<MAX;i++)
       {
          try{
            threadArr[i].join();                     //here we make sure that all the Thread terminated
          }
          catch (Exception e) 
          {
            System.out.println("Thread no "+ i + "not finished");
            return false;
          }
       }
       System.out.println("after join!!!!!!!!!!!!!!!!!!!!!!");
                                                                                                                           // once we get here it means all went well
       String str1="< bug1,1,none>"; 
       OperatedCorrectly = true;
       System.out.println(str1);                                                     //the tupple when it's working
       output.write(str1.getBytes());
       
       return OperatedCorrectly;
   }

    // -------------------------
    // BUG: Run method needs to synchronize on a common lock
    public void run()
    {
      //synchronized(ALock)  // DK
      //{
        String n=Thread.currentThread().getName();
       int num= Integer.parseInt(n);
        if (num%2==0)
            write(a,b);                                                          //even
        else
            write(b,a); 
      //}                                                       //odd
    }
    

    
    
    public void write(Object from, Object to)
    {
 //   System.out.println(Thread.currentThread().getName()+" before from "+from);               //for debug
    hash.put(from,from);                                                                         // we insert the file to the hash table
    synchronized(from){
        System.out.println(Thread.currentThread().getName()+" before to "+to);                //for debug
        System.out.println(hash.toString());                                                                                        //for debug
        if (hash.contains(to)){                                                              // does our lock is there ?
          System.out.println("deadlock on "+to);                                                                             //for debug
         String str="< bug1,0,deadlock on "+to+" >";                                   //output
            try {
                output.write(str.getBytes());
                output.close();
            } catch (IOException e) {
                System.exit(1);
            }
            System.exit(1);                                                                         //we leave the program
        }
        else
        synchronized(to){
                // here the copying is being done.
                  hash.remove(from);
              }
    }
    }

}
