import junit.framework.*;

public class Deadlock2Test extends TestCase { 

  public static Test suite() {
  	return new TestSuite(Deadlock2Test.class);
  }
  
  public void testDeadlock2Once() throws Exception {
    //boolean result = false;
    String[] input = new String[2];
    input[0] = "out1.txt";
    input[1] = "lot";      
    //result = Deadlock2.main(input);
    //assertTrue(result);
    Deadlock2.main(input);
  }

  public void testDeadlock2Twice() throws Exception {
    //boolean result = false;
    String[] input = new String[2];
    input[0] = "out1.txt";
    input[1] = "lot";      
    Deadlock2.main(input);
    //assertTrue(result);
  }

  public void testDeadlock2ThirdTime() throws Exception {
    //boolean result = false;
    String[] input = new String[2];
    input[0] = "out1.txt";
    input[1] = "lot";      
    Deadlock2.main(input);
    //assertTrue(result);
  }
}
