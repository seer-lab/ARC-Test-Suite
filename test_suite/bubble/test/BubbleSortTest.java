import junit.framework.*;

public class BubbleSortTest extends TestCase {

  public static Test suite() {
  	return new TestSuite(BubbleSortTest.class);
  }

  public void testBubbleSortLot() throws Exception {
    SoftWareVerificationHW AB = new SoftWareVerificationHW();
    boolean result = false;
    String[] input = new String[2];
    input[0] = "out1.txt";
    input[1] = "lot";
    result = AB.main(input);
    assertTrue(result);
  }

  public void testBubbleSortAverage() throws Exception {
    SoftWareVerificationHW AB2 = new SoftWareVerificationHW();
    boolean result = false;
    String[] input = new String[2];
    input[0] = "out2.txt";
    input[1] = "average";
    result = AB2.main(input);
    assertTrue(result);
  }

  public void testBubbleSortLittle() throws Exception {
    SoftWareVerificationHW AB3 = new SoftWareVerificationHW();
    boolean result = false;
    String[] input = new String[2];
    input[0] = "out3.txt";
    input[1] = "little";
    result = AB3.main(input);
    assertTrue(result);
  }
}
