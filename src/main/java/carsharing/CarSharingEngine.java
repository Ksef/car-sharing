package carsharing;

import carsharing.customer.CustomerDAO;
import carsharing.customer.CustomerDAOImpl;
import carsharing.roles.CustomerRole;
import carsharing.roles.ManagerRole;
import carsharing.configuration.DBManager;

import java.util.Scanner;

public class CarSharingEngine {

    public static final String BACK = "0. Back";
    private final Scanner scanner;
    private final CustomerDAO customerDao;
    private String pathToDb;
    private final DBManager DBManager;

    public CarSharingEngine(String[] args) throws IllegalArgumentException {
        argumentsManager(args);
        DBManager = new DBManager(pathToDb);
        DBManager.createTables();
        customerDao = new CustomerDAOImpl(pathToDb);
        scanner = new Scanner(System.in);
    }

    public void argumentsManager(String[] args) {
        try {
            if (args.length == 2 && args[0].equals("-databaseFileName")) {
                pathToDb = args[1];
            } else if (args.length == 1 && args[0].equals("-databaseFileName")) {
                pathToDb = "cars";
                System.out.println("""
                        You didn't specify a second argument.
                        Now DB name has default - 'cars'.
                        """);
            } else {
                System.err.println("""
                        APPLICATION CLOSED!
                        You used invalid arguments.
                        Please use '-databaseFileName cars'""");
                System.exit(0);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        ManagerRole manager = new ManagerRole(pathToDb);
        CustomerRole customer = new CustomerRole(pathToDb);
        int action;
        do {
            printMainMenu();
            action = scanner.nextInt();
            scanner.nextLine();
            System.out.println();
            switch (action) {
                case 0:
                    break;
                case 1:
                    manager.loginAsManager();
                    break;
                case 2:
                    customer.logInAsCustomer();
                    break;
                case 3:
                    createCustomer();
                    break;
                default:
                    System.out.println("No such option");
            }
        } while (action != 0);
    }

    private void createCustomer() {
        System.out.println("Enter the customer name:");
        String name = scanner.nextLine();
        customerDao.createCustomer(name);
        System.out.println("The customer was added!\n");
    }

    private static void printMainMenu() {
        System.out.println("""
                1. Log in as a manager
                2. Log in as a customer
                3. Create a customer
                0. Exit""");
    }
}
