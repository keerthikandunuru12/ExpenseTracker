import java.io.*;
import java.util.*;

class User {
    String username;
    String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

class Expense {
    String date;
    String category;
    double amount;

    public Expense(String date, String category, double amount) {
        this.date = date;
        this.category = category;
        this.amount = amount;
    }
}

public class ExpenseTracker {
    private static List<User> users = new ArrayList<>();
    private static List<Expense> expenses = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Register\n2. Log In\n3. Exit");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    registerUser(scanner);
                    break;
                case 2:
                    loginUser(scanner);
                    break;
                case 3:
                    saveDataToFile();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void registerUser(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();

        User newUser = new User(username, password);
        users.add(newUser);
        System.out.println("Registration successful!\n");
    }

    private static void loginUser(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();

        for (User user : users) {
            if (user.username.equals(username) && user.password.equals(password)) {
                loadExpenseDataFromFile();
                showMenu(scanner);
                return;
            }
        }

        System.out.println("Invalid username or password. Please try again.\n");
    }

    private static void showMenu(Scanner scanner) {
        while (true) {
            System.out.println("1. Add Expense\n2. View Expenses\n3. Logout");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addExpense(scanner);
                    break;
                case 2:
                    viewExpenses(scanner);
                    break;
                case 3:
                    saveDataToFile();
                    System.out.println("Logged out successfully!\n");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addExpense(Scanner scanner) {
        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = scanner.next();
        System.out.print("Enter category: ");
        String category = scanner.next();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();

        Expense newExpense = new Expense(date, category, amount);
        expenses.add(newExpense);
        System.out.println("Expense added successfully!\n");
    }

    private static void viewExpenses(Scanner scanner) {
        System.out.println("1. View All Expenses\n2. View Category-wise Summation\n3. Go Back");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                showAllExpenses();
                break;
            case 2:
                showCategoryWiseSummation();
                break;
            case 3:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void showAllExpenses() {
        System.out.println("All Expenses:");
        for (Expense expense : expenses) {
            System.out.println("Date: " + expense.date + ", Category: " + expense.category + ", Amount: " + expense.amount);
        }
        System.out.println();
    }

    private static void showCategoryWiseSummation() {
        Map<String, Double> categorySumMap = new HashMap<>();

        for (Expense expense : expenses) {
            categorySumMap.put(expense.category, categorySumMap.getOrDefault(expense.category, 0.0) + expense.amount);
        }

        System.out.println("Category-wise Summation:");
        for (Map.Entry<String, Double> entry : categorySumMap.entrySet()) {
            System.out.println("Category: " + entry.getKey() + ", Total Amount: " + entry.getValue());
        }
        System.out.println();
    }

    private static void saveDataToFile() {
        try (ObjectOutputStream usersStream = new ObjectOutputStream(new FileOutputStream("users.dat"));
             ObjectOutputStream expensesStream = new ObjectOutputStream(new FileOutputStream("expenses.dat"))) {

            usersStream.writeObject(users);
            expensesStream.writeObject(expenses);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadExpenseDataFromFile() {
        try (ObjectInputStream expensesStream = new ObjectInputStream(new FileInputStream("expenses.dat"))) {

            expenses = (List<Expense>) expensesStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}