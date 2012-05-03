import junit.framework.*;

public class AllocationTest extends TestCase {

  public static Test suite() {
  	return new TestSuite(AllocationTest.class);
  }

  public void testAllocationLot() throws Exception {
    AllocTest AB = new AllocTest();
    boolean result = false;
    String[] input = new String[2];
    input[0] = "out1.txt";
    input[1] = "lot";
    result = AB.main(input);
    assertTrue(result);
  }

  public void testAllocationAverage() throws Exception {
    AllocTest AB2 = new AllocTest();
    boolean result = false;
    String[] input = new String[2];
    input[0] = "out2.txt";
    input[1] = "average";
    result = AB2.main(input);
    assertTrue(result);
  }

  public void testAllocationLittle() throws Exception {
    AllocTest AB3 = new AllocTest();
    boolean result = false;
    String[] input = new String[2];
    input[0] = "out3.txt";
    input[1] = "little";
    result = AB3.main(input);
    assertTrue(result);
  }
}
