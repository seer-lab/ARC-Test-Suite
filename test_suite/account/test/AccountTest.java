import junit.framework.*;

public class AccountTest extends TestCase { 

  public static Test suite() {
  	return new TestSuite(AccountTest.class);
  }
  
  public void testAccountLot() throws Exception {
    Main M = new Main();
    boolean result = false;
    String[] input = new String[2];
    input[0] = "out.txt";
    input[1] = "lot";
    result = M.main(input);
    assertTrue(result);
  }

  public void testAccountAverage() throws Exception {
    Main M = new Main();
    boolean result = false;
    String[] input = new String[2];
    input[0] = "out.txt";
    input[1] = "average";
    result = M.main(input);
    assertTrue(result);
  }

  public void testAccountLittle() throws Exception {
    Main M = new Main();
    boolean result = false;
    String[] input = new String[2];
    input[0] = "out.txt";
    input[1] = "little";
    result = M.main(input);
    assertTrue(result);
  }
}
