import junit.framework.*;

public class MergesortTest extends TestCase {

  public static Test suite() {
  	return new TestSuite(MergesortTest.class);
  }

  public void testAllocationLot() throws Exception {
    Wrapper AB = new Wrapper();
    boolean result = false;
    String[] input = new String[2];
    input[0] = "out1.txt";
    input[1] = "lot";
    result = AB.main(input);
    assertTrue(result);
  }

  public void testAllocationAverage() throws Exception {
    Wrapper AB2 = new Wrapper();
    boolean result = false;
    String[] input = new String[2];
    input[0] = "out2.txt";
    input[1] = "average";
    result = AB2.main(input);
    assertTrue(result);
  }

  public void testAllocationLittle() throws Exception {
    Wrapper AB3 = new Wrapper();
    boolean result = false;
    String[] input = new String[2];
    input[0] = "out3.txt";
    input[1] = "little";
    result = AB3.main(input);
    assertTrue(result);
  }
}
