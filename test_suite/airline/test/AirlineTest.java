import junit.framework.*;

public class AirlineTest extends TestCase {

  public static Test suite() {
  	return new TestSuite(AirlineTest.class);
  }

  // public void testAirlineLot() throws Exception {
  //   AirlineBug AB = new AirlineBug();
  //   boolean result = false;
  //   String[] input = new String[2];
  //   input[0] = "out1.txt";
  //   input[1] = "lot";
  //   result = AB.main(input);
  //   assertTrue(result);
  // }

  // public void testAirlineAverage() throws Exception {
  //   AirlineBug AB2 = new AirlineBug();
  //   boolean result = false;
  //   String[] input = new String[2];
  //   input[0] = "out2.txt";
  //   input[1] = "average";
  //   result = AB2.main(input);
  //   assertTrue(result);
  // }

  public void testAirlineLittle() throws Exception {
    AirlineBug AB3 = new AirlineBug();
    boolean result = false;
    String[] input = new String[2];
    input[0] = "out3.txt";
    input[1] = "little";
    result = AB3.main(input);
    assertTrue(result);
  }
}
