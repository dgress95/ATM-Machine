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



}
