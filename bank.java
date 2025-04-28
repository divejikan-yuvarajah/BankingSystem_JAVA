import java.util.InputMismatchException;
import java.util.Scanner;

public class bank {

    public static final String ADMINUSERNAME = "ADMIN";
    public static final String ADMINPASSWORD = "Admin";
    public static String[][] users = {
        {"Divejikan", "Diveji", "user1", "22114036", "001", "10000.00", "Rs.40000.00"},
        {"Kokulraj", "Kokul", "user2", "20459623", "002", "22000.00", "EMPTY"},
        {"Hafrath", "Hafrath", "user3", "21654782", "003", "03000.00", "EMPTY"}
    };

    public static enum LoginUsers {
        ADMIN, USER
    }

    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void printUserDetails(String[] array) {
        System.out.println("FULL NAME : " + array[0]);
        System.out.println("USER NAME : " + array[1]);
        System.out.println("PASSWORD : " + array[2]);
        System.out.println("NIC : " + array[3]);
        System.out.println("ACCOUNT NUMBER : " + array[4]);
        System.out.println("BALANCE : " + array[5]);
        System.out.println("LAST TRANSACTION : " + array[6]);
        System.out.println("");
    }

    public static void loginPage(Scanner input) {
        String userName = "";
        String password = "";
        String choice = "";

        System.out.println("=====================================================");
        System.out.println(" NEXT-GEN BANK");
        System.out.println("=====================================================");
        System.out.println("Press 'A' for login as ADMIN , Press 'U' for login as User");

        choice = input.nextLine().toUpperCase();

        switch (choice) {
            case "A":
                System.out.print("ENTER ADMIN USER NAME: ");
                userName = input.nextLine();
                if (userName.equalsIgnoreCase("exit")) {
                    clearConsole();
                    System.out.println("THANK YOU BYE !!!");
                    System.exit(0);
                }
                System.out.print("ENTER ADMIN PASSWORD: ");
                password = input.nextLine();
                if (ADMINUSERNAME.equalsIgnoreCase(userName) && ADMINPASSWORD.equals(password)) {
                    clearConsole();
                    System.out.println("SUCCESSFULLY LOGGED IN AS ADMIN");
                    dashboard(input, "ADMIN", "");
                } else {
                    clearConsole();
                    System.out.println("LOGIN FAILED! USER NAME OR PASSWORD WRONG. TRY AGAIN LATER.\n\n");
                    loginPage(input);  // Recur if login fails
                }
                break;
            case "U":
                System.out.print("ENTER USER NAME: ");
                userName = input.nextLine();
                if (userName.equalsIgnoreCase("exit")) {
                    clearConsole();
                    System.out.println("THANK YOU BYE !!!");
                    System.exit(0);
                }
                System.out.print("ENTER PASSWORD: ");
                password = input.nextLine();
                for (String[] temp : users) {
                    if (temp[1].equalsIgnoreCase(userName)) {
                        if (temp[2].equals(password)) {
                            clearConsole();
                            System.out.println("WELCOME LOGGED IN AS USER: " + temp[0]);
                            dashboard(input, "USER", temp[4]);
                            return;
                        } else {
                            clearConsole();
                            System.out.println("WRONG PASSWORD, TRY AGAIN.");
                            loginPage(input);  // Recur if password is incorrect
                        }
                    }
                }
                clearConsole();
                System.out.println("USER NOT FOUND.");
                loginPage(input);  // Recur if user not found
                break;
            default:
                System.out.println("INVALID CHOICE.");
                loginPage(input);  // Recur if invalid choice
                break;
        }
    }

    public static void viewUserDetail(String acno) {
        boolean isAvailable = false;
        for (String[] user : users) {
            if (acno.equals(user[4])) {
                printUserDetails(user);
                isAvailable = true;
                break;
            }
        }
        if (!isAvailable) {
            System.out.println("USER NOT FOUND. CHECK ACCOUNT NUMBER.");
        }
    }

    public static int getIndex(String acNum) {
        for (int i = 0; i < users.length; i++) {
            if (acNum.equals(users[i][4])) {
                return i;
            }
        }
        System.out.println("USER NOT FOUND. CHECK ACCOUNT NUMBER.");
        return -1;
    }

    public static void deposit(String acno, double amount) {
        int index = getIndex(acno);
        if (index != -1) {
            double total = amount + Double.parseDouble(users[index][5]);
            users[index][5] = String.valueOf(total);
        }
    }

    public static void withdraw(String acno, double amount) {
        int index = getIndex(acno);
        if (index != -1) {
            double total = Double.parseDouble(users[index][5]) - amount;
            users[index][5] = String.valueOf(total);
        }
    }

    public static void transfer(String acno, String receiver, double amount) {
        int senderIndex = getIndex(acno);
        int receiverIndex = getIndex(receiver);
        if (senderIndex != -1 && receiverIndex != -1) {
            withdraw(acno, amount);
            deposit(receiver, amount);
            users[senderIndex][6] = amount + ".Rs TRANSFERRED TO " + users[receiverIndex][0];
        }
    }

    public static void dashboard(Scanner input, String user, String acno) {
        switch (user) {
            case "ADMIN":
                System.out.println("\nEnter 'a' : List All Users");
                System.out.println("Enter 'b' : View User");
                System.out.println("Enter 'c' : Log out");
                String adminChoice = input.nextLine();
                switch (adminChoice.toLowerCase()) {
                    case "a":
                        clearConsole();
                        System.out.println("============ All User Details =====================");
                        for (String[] array : users) {
                            printUserDetails(array);
                        }
                        dashboard(input, "ADMIN", "");
                        break;
                    case "b":
                        System.out.println("Enter User Account Num: ");
                        String acnum = input.nextLine();
                        viewUserDetail(acnum);
                        dashboard(input, "ADMIN", "");
                        break;
                    case "c":
                        clearConsole();
                        loginPage(input);
                        break;
                    default:
                        break;
                }
                break;
            case "USER":
                int index = getIndex(acno);
                Scanner userInput = new Scanner(System.in);
                System.out.println("\nEnter 'a' : View Personal Details");
                System.out.println("Enter 'b' : View Bank Balance");
                System.out.println("Enter 'c' : View Last Transaction");
                System.out.println("Enter 'd' : Deposit");
                System.out.println("Enter 'e' : Withdrawal");
                System.out.println("Enter 'f' : Transfer");
                System.out.println("Enter 'g' : Log out");
                String userChoice = userInput.nextLine();
                switch (userChoice.toLowerCase()) {
                    case "a" -> {
                        clearConsole();
                        viewUserDetail(acno);
                        dashboard(input, "USER", acno);
                }
                    case "b" -> {
                        clearConsole();
                        System.out.println("YOUR BALANCE IS: " + users[index][5] + ".Rs");
                        dashboard(input, "USER", acno);
                }
                    case "c" -> {
                        clearConsole();
                        System.out.println("YOUR LAST TRANSACTION IS: " + users[index][6]);
                        dashboard(input, "USER", acno);
                }
                    case "d" -> {
                        clearConsole();
                        System.out.print("ENTER AMOUNT TO DEPOSIT: ");
                        try {
                            double amount = userInput.nextDouble();
                            deposit(acno, amount);
                            System.out.println("DEPOSIT SUCCESSFUL. NEW AMOUNT IS: " + users[index][5]);
                        } catch (InputMismatchException e) {
                            System.out.println("INVALID AMOUNT.");
                        }
                        dashboard(input, "USER", acno);
                }
                    case "e" -> {
                        clearConsole();
                        System.out.print("ENTER AMOUNT TO WITHDRAW: ");
                        try {
                            double amountW = userInput.nextDouble();
                            withdraw(acno, amountW);
                            System.out.println("WITHDRAWAL SUCCESSFUL. NEW AMOUNT IS: " + users[index][5]);
                        } catch (InputMismatchException e) {
                            System.out.println("INVALID AMOUNT.");
                        }
                        dashboard(input, "USER", acno);
                }
                    case "f" -> {
                        clearConsole();
                        System.out.print("ENTER ACCOUNT NUMBER TO TRANSFER TO: ");
                        String receiverAcc = userInput.nextLine();
                        System.out.print("ENTER AMOUNT TO TRANSFER: ");
                        try {
                            double amountT = userInput.nextDouble();
                            transfer(acno, receiverAcc, amountT);
                        } catch (InputMismatchException e) {
                            System.out.println("INVALID AMOUNT.");
                        }
                        dashboard(input, "USER", acno);
                }
                    case "g" -> {
                        clearConsole();
                        System.out.println("Log out");
                        loginPage(input);
                }
                    default -> {
                        clearConsole();
                        System.out.println("Wrong Selection, Try Again!");
                        dashboard(input, "USER", acno);
                }
                }
                break;

            default:
                break;
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        loginPage(input);
    }
}
