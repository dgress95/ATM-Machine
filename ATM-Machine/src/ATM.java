import java.util.Scanner;

public class ATM {
    public static void main(String[] args) {
        // init Scanner
        Scanner sc = new Scanner(System.in);

        // init Bank
        Bank theBank = new Bank("Bank of Drausin");

        // add a user, which also creates a savings account
        User user = theBank.addUser("John", "Doe", "1234");

        // add a checking account for our user
        Account newAccount = new Account("Checking", user, theBank);
        user.addAccount(newAccount);
        theBank.addAccount(newAccount);

        User curUser;
        while (true) {
            // stay in the login prompt unitil successful login
            curUser = ATM.mainMenuPrompt(theBank, sc);

            // stay in the main menu until user quits
            ATM.printUserMenu(curUser, sc);
        }
    }
}
