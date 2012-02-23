import junit.framework.*;

public class BufferTest extends TestCase { 

  public static Test suite() {
  	return new TestSuite(BufferTest.class);
  }
  
  public void testBufferLot() throws Exception {
    BufferNotify AB = new BufferNotify();
    boolean result = false;
    String[] input = new String[2];
    input[0] = "out1.txt";
    input[1] = "3";
    result = AB.main(input);
    assertTrue(result);
  }

  public void testBufferAverage() throws Exception {
    BufferNotify AB2 = new BufferNotify();
    boolean result = false;
    String[] input = new String[2];
    input[0] = "out2.txt";
    input[1] = "2";
    result = AB2.main(input);
    assertTrue(result);
  }

  public void testBufferLittle() throws Exception {
    BufferNotify AB3 = new BufferNotify();
    boolean result = false;
    String[] input = new String[2];
    input[0] = "out3.txt";
    input[1] = "1";
    result = AB3.main(input);
    assertTrue(result);
  }
}
