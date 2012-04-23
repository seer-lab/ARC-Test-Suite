import junit.framework.*;

public class LotteryTest extends TestCase {

  public static Test suite() {
  	return new TestSuite(LotteryTest.class);
  }

  // public void testLotteryLot() throws Exception {
  //   //LotteryTest AB = new LotteryTest();
  //   boolean result = false;
  //   String[] input = new String[2];
  //   input[0] = "out1.txt";
  //   input[1] = "lot";
  //   result = BuggyProgram.main(input);
  //   assertTrue(result);
  // }

  // public void testLotteryAverage() throws Exception {
  //   //LotteryTest AB2 = new LotteryTest();
  //   boolean result = false;
  //   String[] input = new String[2];
  //   input[0] = "out2.txt";
  //   input[1] = "average";
  //   result = BuggyProgram.main(input);
  //   assertTrue(result);
  // }

  public void testLotteryLittle() throws Exception {
    //LotteryTest AB3 = new LotteryTest();
    boolean result = false;
    String[] input = new String[2];
    input[0] = "out3.txt";
    input[1] = "little";
    result = BuggyProgram.main(input);
    assertTrue(result);
  }
}
