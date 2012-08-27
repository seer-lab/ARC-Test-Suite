import junit.framework.*;
import java.io.*;
import java.util.regex.*;

public class PingPongTest extends TestCase {

  public static Test suite() {
  	return new TestSuite(PingPongTest.class);
  }

  public void testPingPongLot() throws Exception {
    ProgramRunner AB = new ProgramRunner();
    boolean result = false;
    String[] input = new String[2];
    input[0] = "out.txt";
    input[1] = "lot";
    AB.main(input);


  try{
    File file = new File("out.txt");
    byte[] bytes = new byte[(int) file.length()];
    System.out.println("File length " + file.length());
    FileInputStream fstream = new FileInputStream(file);
    //DataInputStream in = new DataInputStream(fstream);
    //BufferedReader br = new BufferedReader(new InputStreamReader(in));

    fstream.read(bytes);
    fstream.close();

    String testOutput = new String(bytes);
    System.out.println("Test output: " + testOutput);
    Pattern regex = Pattern.compile("(\\d+)");
    Matcher intMatch = regex.matcher(testOutput);
    // We're interested in the second integer from the PingPong output
    intMatch.find();
    intMatch.find();
    assertTrue(intMatch.group() == "0");

  } catch (Exception e){
    System.err.println("testPingPongLot error: " + e.getMessage());
  }


  } // testPingPongLot
}  // PingPongTest
