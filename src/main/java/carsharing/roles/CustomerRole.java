package carsharing.roles;

import carsharing.CarSharingEngine;
import carsharing.car.Car;
import carsharing.company.Company;
import carsharing.company.CompanyDAO;
import carsharing.company.CompanyDAOImpl;
import carsharing.customer.Customer;
import carsharing.customer.CustomerDAO;
import carsharing.customer.CustomerDAOImpl;

import java.util.List;
import java.util.Scanner;

public class CustomerRole {
    private final CustomerDAO customerDao;
    private final Scanner scanner;
    private final CompanyDAO companyDao;
    private List<Customer> customers;
    private List<Company> companies;
    private List<Car> cars;

    public CustomerRole(String pathToDb) {
        this.customerDao = new CustomerDAOImpl(pathToDb);
        this.scanner = new Scanner(System.in);
        this.companyDao = new CompanyDAOImpl(pathToDb);
    }

    public void logInAsCustomer() {
        customers = customerDao.getCustomers();
        if (customers.size() == 0) {
            System.out.println("The customer list is empty!\n");
        } else {
            boolean flag = true;
            customersFlagSwitcher(flag);
        }
    }

    private void customersFlagSwitcher(boolean flag) {
        while (flag) {
            printCustomers(customers);
            int customerId = scanner.nextInt();
            System.out.println();
            if (customerId == 0) {
                flag = false;
            } else if (customerId > 0 && customerId <= customers.size()) {
                clientActs(customerId);
                flag = false;
            } else {
                System.out.println("No such customer");
            }
        }
    }

    private void clientActs(int customerId) {
        boolean flag = true;
        while (flag) {
            printServices();
            int action = scanner.nextInt();
            System.out.println();
            switch (action) {
                case 0 -> flag = false;
                case 1 -> rentCar(customerId);
                case 2 -> returnRentedCar(customerId);
                case 3 -> getInfoAboutCar(customerId);
                default -> System.out.println("No such service");
            }
        }
    }

    private void rentCar(int customerId) {
        Customer customer = customerDao.getInfoAboutRentedCar(customerId);
        if (customer != null) {
            if (customer.isHasCar()) {
                System.out.println("You've already rented a car!");
            } else {
                companies = companyDao.getCompanies();
                boolean flag = true;
                companiesFlagSwitcher(flag, customerId);
            }
        } else {
            System.out.println("There is no such customer");
        }
        System.out.println();
    }

    private void companiesFlagSwitcher(boolean flag, int customerId) {
        while (flag) {
            int companyId = pickCompany(companies);
            if (companyId > 0 && companyId <= companies.size()) {
                pickCar(companies.get(companyId - 1), customerId);
                flag = false;
            } else if (companyId == 0) {
                flag = false;
            } else {
                System.out.println("No such company!");
            }
        }
    }

    private void pickCar(Company company, int customerId) {
        cars = customerDao.getAvailableCars(company);
        if (cars.size() == 0) {
            System.out.printf("No available cars in the '%s' company\n", company.getName());
        } else {
            boolean flag = true;
            carFlagSwitcher(flag, customerId);
        }
    }

    private void carFlagSwitcher(boolean flag, int customerId) {
        while (flag) {
            printCars(cars);
            int carId = scanner.nextInt();
            System.out.println();
            if (carId == 0) {
                flag = false;
            } else if (carId < 0 || carId > cars.size()) {
                System.out.println("There is no such car\n");
            } else {
                customerDao.rentCar(cars.get(carId - 1).getName(), customerId);
                System.out.println("You rented '" + cars.get(carId - 1).getName() + "'");
                flag = false;
            }
        }
    }


    private int pickCompany(List<Company> companies) {
        int index = 0;
        if (companies.size() == 0) {
            System.out.println("Company list is empty!");
        } else {
            System.out.println("Choose a company:");
            int i = 0;
            for (Company e : companies) {
                System.out.println(++i + ". " + e.getName());
            }
            System.out.println(CarSharingEngine.BACK);
            index = scanner.nextInt();
            System.out.println();
        }
        return index;
    }

    private void getInfoAboutCar(int customerId) {
        Customer customer = customerDao.getInfoAboutRentedCar(customerId);
        if (customer == null) {
            System.out.println("There is no such customer!");
        } else {
            if (customer.isHasCar()) {
                System.out.println("Your rented car:");
                System.out.println(customer.getCar().getName());
                System.out.println("Company:");
                System.out.println(customer.getCompany().getName());
            } else {
                System.out.println("You didn't rent a car!");
            }
        }
        System.out.println();
    }

    private void returnRentedCar(int customerId) {
        Customer customer = customerDao.getInfoAboutRentedCar(customerId);
        if (customer == null) {
            System.out.println("There is no such customer");
        } else {
            if (customer.isHasCar()) {
                customerDao.returnCar(customerId);
                System.out.println("You've returned a rented car!");
            } else {
                System.out.println("You didn't rent a car!");
            }

        }
        System.out.println();
    }

    private void printCars(List<Car> cars) {
        int index = 0;
        for (Car i : cars) {
            System.out.println(++index + ". " + i.getName());
        }
        System.out.println(CarSharingEngine.BACK);
    }

    private static void printServices() {
        System.out.println("""
                1. Rent a car
                2. Return a rented car
                3. My rented car
                0. Back""");
    }

    private void printCustomers(List<Customer> customers) {
        System.out.println("Customer list:");
        int i = 0;
        for (Customer e : customers) {
            System.out.println(++i + ". " + e.getName());
        }
        System.out.println(CarSharingEngine.BACK);
    }
}
