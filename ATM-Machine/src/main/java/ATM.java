import javax.sound.midi.Soundbank;
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
            // stay in the login prompt until successful login
            curUser = ATM.mainMenuPrompt(theBank, sc);

            // stay in the main menu until user quits
            ATM.printUserMenu(curUser, sc);
        }
    }

    public static User mainMenuPrompt(Bank theBank, Scanner sc) {
        // inits
        String userID;
        String pin;
        User authUser;
        int counter = 0;

        // prompt the user for user ID/pin combo until a correct one is reached
        do {
            System.out.printf("\n\nWelcome to %s\n\n", theBank.getName());
            System.out.print("Enter user ID: ");
            userID = sc.nextLine();
            System.out.print("Enter pin: ");
            pin = sc.nextLine();
            // get the input and print out a star

            // try to get the user object corresponding to the ID and pin combo
            authUser = theBank.userLogin(userID, pin);
            if (authUser == null) {
                System.out.println("incorrect user ID/pin combo. Please try again");
            }
        } while(authUser == null); // continue looping until successful login

        return authUser;
    }

    public static void printUserMenu(User theUser, Scanner sc) {
        // print a summary of the user's accounts
        theUser.printAccountSummary();

        // init
        int choice;

        // user menu
        do {
            System.out.printf("Welcome %s, what would you like to do?\n",
                    theUser.getFirstName());
            System.out.println("  1) Show account transaction history");
            System.out.println("  2) Withdraw");
            System.out.println("  3) Deposit");
            System.out.println("  4) Transfer");
            System.out.println("  5) Exit");
            System.out.println();
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            if (choice < 1 || choice > 5) {
                System.out.println("Invalid choice. Please choose 1-5");
            }
        } while (choice <1 || choice > 5);

        // process the choice
        switch (choice) {
            case 1 -> ATM.showTransHistory(theUser, sc);
            case 2 -> ATM.withdrawFunds(theUser, sc);
            case 3 -> ATM.depositFunds(theUser, sc);
            case 4 -> ATM.transferFunds(theUser, sc);
            case 5 -> sc.nextLine();
        }

        // resdisplay this menu unless the user wants to quit
        if (choice != 5) {
            ATM.printUserMenu(theUser, sc);
        }
    }

    public static void showTransHistory(User user, Scanner sc) {
        int theAcct;

        // get account whose transaction history to look at
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" + " whose transactions you want to see: ",
                    user.numAccounts());
            theAcct = sc.nextInt()-1;
            if (theAcct < 0 || theAcct >= user.numAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (theAcct < 0 || theAcct >= user.numAccounts());

        // print the transaction history
        user.printAcctTransHistory(theAcct);
    }

    public static void transferFunds(User user, Scanner sc) {
        // inits
        int fromAcct;
        int toAcct;
        double amount;
        double acctBal;

        // get the account to transfer from
        do {
            System.out.printf("Enter the number (1-%d) of the account \n" +
                    "to transfer from: ", user.numAccounts());
            fromAcct = sc.nextInt()-1;
            if (fromAcct < 0 || fromAcct >= user.numAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (fromAcct < 0 || fromAcct >= user.numAccounts());
        acctBal = user.getAcctBalance(fromAcct);

        // get the account to transfer to
        do {
            System.out.printf("Enter the number (1-%d) of the account \n" +
                    "to transfer to: ", user.numAccounts());
            toAcct = sc.nextInt()-1;
            if (toAcct < 0 || toAcct >= user.numAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (toAcct < 0 || toAcct >= user.numAccounts());

        // get the amount to transfer
        do {
            System.out.printf("Enter the amount to transfer (max $%.02f): $",
                    acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than zero.");
            } else if (amount > acctBal) {
                System.out.printf("Amount must not be greater than\n" + "balance of $%.02f.\n", acctBal);
            }
        } while (amount < 0 || amount > acctBal);

        // finally, do the transfer
        user.addAcctTransaction(fromAcct, -1*amount, String.format(
                "Transfer to account %s", user.getAcctUUID(toAcct)));
        user.addAcctTransaction(toAcct, amount, String.format(
                "Transfer to account %s", user.getAcctUUID(fromAcct)));
    }

    public static void withdrawFunds(User user, Scanner sc) {
        // inits
        int fromAcct;
        double amount;
        double acctBal;
        String memo;

        // get the account to withdraw from
        do {
            System.out.printf("Enter the number (1-%d) of the account \n" +
                    "to transfer from: ", user.numAccounts());
            fromAcct = sc.nextInt()-1;
            if (fromAcct < 0 || fromAcct >= user.numAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (fromAcct < 0 || fromAcct >= user.numAccounts());
        acctBal = user.getAcctBalance(fromAcct);

        // get the amount to withdraw
        do {
            System.out.printf("Enter the amount to withdraw (max $%.02f): $",
                    acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than zero.");
            } else if (amount > acctBal) {
                System.out.printf("Amount must not be greater than\n" +
                        "balance of $%.02f.\n", acctBal);
            }
        } while (amount < 0 || amount > acctBal);

        // gobble up the rest of previous input
        sc.nextLine();

        // get a memo
        System.out.print("Enter a memo: ");
        memo = sc.nextLine();

        // do the withdrawal
        user.addAcctTransaction(fromAcct, -1*amount, memo);
    }

    public static void depositFunds(User user, Scanner sc) {
        // inits
        int toAcct;
        double amount;
        double acctBal;
        String memo;

        // get the account to deposit to
        do {
            System.out.printf("Enter the number (1-%d) of the account \n" +
                    "to deposit to: ", user.numAccounts());
            toAcct = sc.nextInt()-1;
            if (toAcct < 0 || toAcct >= user.numAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (toAcct < 0 || toAcct >= user.numAccounts());
        acctBal = user.getAcctBalance(toAcct);

        // get the amount to deposit
        do {
            System.out.printf("Enter the amount to deposit (max $%.02f): $",
                    acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than zero.");
            }
        } while (amount < 0);

        // gobble up rest of input
        sc.nextLine();

        // get a memo
        System.out.print("Enter a memo: ");
        memo = sc.nextLine();

        user.addAcctTransaction(toAcct, amount, memo);
    }


}
