package carsharing.customer;

import carsharing.car.Car;
import carsharing.company.Company;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Customer {

    private final String name;
    private Car car;
    private Company company;
    private boolean hasCar;

    public Customer(String name) {
        this.name = name;
        this.hasCar = false;
    }
}
