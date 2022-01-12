package carsharing.roles;

import carsharing.car.Car;
import carsharing.car.CarDAO;
import carsharing.car.CarDAOImpl;
import carsharing.company.Company;
import carsharing.company.CompanyDAO;
import carsharing.company.CompanyDAOImpl;

import java.util.List;
import java.util.Scanner;

public class ManagerRole {
    private final CarDAO carDao;
    private final CompanyDAO companyDao;
    private final Scanner scanner;

    public ManagerRole() {
        carDao = new CarDAOImpl();
        companyDao = new CompanyDAOImpl();
        scanner = new Scanner(System.in);
    }

    public void loginAsManager() {
        int action;
        do {
            printManagerMenu();
            action = scanner.nextInt();
            scanner.nextLine();
            System.out.println();
            switch (action) {
                case 0:
                    break;
                case 1:
                    companyMenu();
                    break;
                case 2:
                    createCompany();
                    break;
                default:
                    System.out.println("No such option");
            }
        } while (action != 0);
    }

    private void companyMenu() {
        List<Company> companies = companyDao.getCompanies();
        boolean flag = true;
        if (companies.size() == 0) {
            System.out.println("The company list is empty!\n");
        } else {
            while (flag) {
                printCompanies(companies);
                int companyId = scanner.nextInt();
                System.out.println();
                if (companyId == 0) {
                    flag = false;
                } else if (companyId < 1 || companyId > companies.size()) {
                    System.out.println("No such company!");
                } else {
                    manageCompany(companies.get(companyId - 1));
                    flag = false;
                }
            }
        }
    }

    private void manageCompany(Company company) {
        int companyId = companyDao.getCompanyId(company.getName());
        if (companyId == -1) {
            System.out.println("There is a big problem id equals -1");
            return;
        }
        System.out.printf("'%s' company \n", company.getName());
        boolean flag = true;
        while (flag) {
            printCarMenu();
            int action = scanner.nextInt();
            scanner.nextLine();
            System.out.println();
            switch (action) {
                case 0 -> flag = false;
                case 1 -> printCars(companyId);
                case 2 -> createCar(companyId);
                default -> System.out.println("No such option");
            }
        }
    }

    private void createCar(int companyId) {
        System.out.println("Enter the car name:");
        String carName = scanner.nextLine();
        carDao.createCar(companyId, carName);
        System.out.println("The car was added!\n");
    }

    private void printCars(int companyId) {
        List<Car> cars = carDao.getCars(companyId);
        if (cars.size() == 0) {
            System.out.println("The car list is empty!");
        } else {
            System.out.println("Car list:");
            int index = 0;
            for (var i : cars) {
                System.out.println(++index + ". " + i.getName());
            }
        }
        System.out.println();
    }

    private void printCompanies(List<Company> companies) {
        System.out.println("Choose the company:");
        int index = 0;
        for (var i : companies) {
            System.out.println(++index + ". " + i.getName());
        }
        System.out.println("0. Back");
    }

    private void createCompany() {
        System.out.println("Enter the company name:");
        String name = scanner.nextLine();
        companyDao.createCompany(name);
        System.out.println("The company was created!\n");
    }

    private static void printManagerMenu() {
        System.out.println("""
                1. Company list
                2. Create a company
                0. Back""");
    }

    private static void printCarMenu() {
        System.out.println("""
                1. Car list
                2. Create a car
                0. Back""");
    }
}