public class Account {
    double amount;
    String name;

    public Account (String nm, double amnt) {
        amount = amnt;
        name = nm;
    }

    /* MUTANT : "RSM (Removed Synch around a Method)" */
    void depsite (double money) {
        amount += money;
    }

    synchronized void withdraw (double money) {
        amount -= money;
    }

    synchronized void transfer (Account ac, double mn) {
        amount -= mn;
        ac.amount += mn;
    }

    synchronized void print () {
        System.out.println (name + "--" + amount);
    }

}

