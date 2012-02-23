public class Checker implements Runnable {
    private Buffer _buf;
    private int _writtenCount;

    public Checker (Buffer buf) {
        /* MUTANT : "ASAV (Added Sync Around Variable)" */
        synchronized (this) {
            _buf = buf;
        }
        /* MUTANT : "ASAV (Added Sync Around Variable)" */
        _writtenCount = 0;}

    public void run () {
        while (true) {
            synchronized (_buf) {
                _writtenCount += _buf._pos; _buf._pos = 0;}
        }
    }

    public int getWrittenCount () {
        return _writtenCount;
    }

}

