import java.util.ArrayList;

public class Account {
    /**
     * The name of the account.
     */
    private String name;
    /**
     * The account ID number.
     */
    private String uuid;
    /**
     * The User object that owns this account.
     */
    private User holder;
    /**
     * The list of transactions for this account.
     */
    private ArrayList<Transaction> transactions;

    public Account(String name, User holder, Bank theBank) {
        this.name = name;
        this.holder = holder;

        // get UUID for account
        this.uuid = theBank.getNewAccountUUID();

        // init transaction
        this.transactions = new ArrayList<>();
    }

    public String getUUID() {
        return this.uuid;
    }

    public String getSummaryLine() {
        // get account balance
        double balance = this.getBalance();

        // format the summary line, depending on whether the balance is negative
        if (balance >= 0) {
            return String.format("%s : $%.02f : %s", this.uuid, balance, this.name);
        } else {
            return String.format("%s : $(%.02f) : %s", this.uuid, balance, this.name);
        }
    }

    public double getBalance() {
        double balance = 0;
        for (Transaction t : this.transactions) {
            balance += t.getAmount();
        }
        return balance;
    }

    public void printTransHistory() {
        System.out.printf("\nTransaction history for account %s\n", this.uuid);
        for (int t = this.transactions.size()-1; t >= 0; t--) {
            System.out.println(this.transactions.get(t).getSummaryLine());
        }
        System.out.println();
    }

    public void addTransaction(double amount, String memo) {
        // create new transaction object and add it to our list
        Transaction newTrans = new Transaction(amount, memo, this);
        this.transactions.add(newTrans);
    }


}
