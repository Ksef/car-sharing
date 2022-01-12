package carsharing.customer;

import carsharing.car.Car;
import carsharing.company.Company;

import java.util.List;

public interface CustomerDAO {

    List<Customer> getCustomers();

    void createCustomer(String name);

    void rentCar(String carName, int customerId);

    List<Car> getAvailableCars(Company company);

    void returnCar(int customerId);

    Customer getInfoAboutRentedCar(int customerId);
}
