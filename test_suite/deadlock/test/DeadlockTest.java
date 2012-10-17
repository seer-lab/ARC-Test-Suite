import junit.framework.*;

public class DeadlockTest extends TestCase {

  public static Test suite() {
  	return new TestSuite(DeadlockTest.class);
  }

  public void testDeadlockLot() throws Exception {
    deadLock AB = new deadLock();
    boolean result = false;
    String[] input = new String[2];
    input[0] = "out1.txt";
    input[1] = "lot";
    result = AB.main(input);
    assertTrue(result);
  }

  public void testDeadlockAverage() throws Exception {
    deadLock AB2 = new deadLock();
    boolean result = false;
    String[] input = new String[2];
    input[0] = "out2.txt";
    input[1] = "average";
    result = AB2.main(input);
    assertTrue(result);
  }

  public void testDeadlockLittle() throws Exception {
    deadLock AB3 = new deadLock();
    boolean result = false;
    String[] input = new String[2];
    input[0] = "out3.txt";
    input[1] = "little";
    result = AB3.main(input);
    assertTrue(result);
  }
}
