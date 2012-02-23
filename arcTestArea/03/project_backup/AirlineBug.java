import java.io.FileOutputStream;

import java.io.FileNotFoundException;

import java.io.IOException;

public class AirlineBug implements Runnable {
    static int Num_Of_Seats_Sold = 0;
    int Maximum_Capacity, Num_of_tickets_issued;
    boolean StopSales = false;
    Thread threadArr [];
    FileOutputStream output;
    private String fileName;
    boolean OperatedCorrectly = false;

    public boolean AirBug (String fileName, String Concurency) {
        this.fileName = fileName;
        Num_Of_Seats_Sold = 0;
        try {
            output = new FileOutputStream (fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace ();
        }
        if (Concurency.equals ("little")) Num_of_tickets_issued = 10;
        else if (Concurency.equals ("average")) Num_of_tickets_issued = 100;
        else Num_of_tickets_issued = 5000;

        Maximum_Capacity = Num_of_tickets_issued - (Num_of_tickets_issued) / 10;
        threadArr = new Thread [Num_of_tickets_issued];
        System.out.println ("The airline issued " + Num_of_tickets_issued + " tickets for " + Maximum_Capacity +
          " seats to be sold.");
        for (int i = 0;
        i < Num_of_tickets_issued; i ++) {
            threadArr [i] = new Thread (this);
            if (StopSales) {
                Num_Of_Seats_Sold --;
                break;
            }
            threadArr [i].start ();
        }
        System.out.println ("SOLD " + Num_Of_Seats_Sold + " Seats !!!");
        try {
            output = new FileOutputStream (fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace ();
        }
        String str1 = "< SUCCESS! " + fileName + " , Concurency=" + Concurency + " , sold: " + Num_Of_Seats_Sold + " No Bug" +
          " >\n";
        String str2 = "< FAILURE! " + fileName + " , Concurency=" + Concurency + " , sold: " + Num_Of_Seats_Sold +
          " Bug occurred" + " >\n";
        if (Num_Of_Seats_Sold > Maximum_Capacity) try {
            OperatedCorrectly = false;
            System.out.println (str2);
            output.write (str2.getBytes ());
        } catch (IOException e) {
            e.printStackTrace ();
        }
        else try {
            OperatedCorrectly = true;
            System.out.println (str1);
            output.write (str1.getBytes ());
        } catch (IOException e) {
            e.printStackTrace ();
        }

        return OperatedCorrectly;
    }

    public void run () {
        Num_Of_Seats_Sold ++;
        if (Num_Of_Seats_Sold > Maximum_Capacity) StopSales = true;

    }

    
    public static boolean main (String args []) {
        AirlineBug AB = new AirlineBug ();
        return AB.AirBug (args [0], args [1]);
    }

}

