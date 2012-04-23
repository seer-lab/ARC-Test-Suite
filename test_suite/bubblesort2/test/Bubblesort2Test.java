import junit.framework.*;

public class Bubblesort2Test extends TestCase {

  public static Test suite() {
  	return new TestSuite(Bubblesort2Test.class);
  }

  // public void testBubblesort2Lot() throws Exception {
  //   //Main M = new Main();
  //   boolean result = false;
  //   String[] input = new String[2];
  //   input[0] = "out.txt";
  //   input[1] = "lot";
  //   result = Loader.main(input);
  //   assertTrue(result);
  // }

  // public void testBubblesort2Average() throws Exception {
  //   //Main M = new Main();
  //   boolean result = false;
  //   String[] input = new String[2];
  //   input[0] = "out.txt";
  //   input[1] = "average";
  //   result = Loader.main(input);
  //   assertTrue(result);
  // }

  public void testBubblesort2Little() throws Exception {
    //Main M = new Main();
    boolean result = false;
    String[] input = new String[2];
    input[0] = "out.txt";
    input[1] = "little";
    result = Loader.main(input);
    assertTrue(result);
  }
}
