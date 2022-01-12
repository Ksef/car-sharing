package carsharing.customer;

import carsharing.car.Car;
import carsharing.company.Company;
import carsharing.configuration.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomerDAOImpl implements CustomerDAO {

    private static final String SELECT_NAME_BY_CUSTOMER = "SELECT name from customer ORDER BY ID";
    private static final String SELECT_NAME_AND_CAR_ID = "SELECT name, rented_car_id FROM customer WHERE id = ?";
    private static final String SELECT_NAME_AND_COMPANY_ID = "SELECT name, company_id FROM car WHERE id = ?";
    private static final String SELECT_NAME_BY_COMPANY = "SELECT name FROM company WHERE id = ?";
    private static final String UPDATE_CUSTOMER_WITH_NULL_RENTED_CAR = "UPDATE customer SET rented_car_id = NULL WHERE id = ?";
    private static final String SELECT_ID_BY_CAR_NAME = "SELECT id FROM car WHERE name = ?";
    private static final String UPDATE_CUSTOMER = "UPDATE customer SET rented_car_id = ? WHERE id = ?";
    private static final String SELECT_ID_BY_COMPANY = "SELECT id FROM company WHERE name = ?";
    private static final String SELECT_RENTED_CAR = "SELECT rented_car_id FROM customer WHERE rented_car_id IS NOT NULL";
    private static final String SELECT_ID_NAME_BY_CAR = "SELECT id, name FROM car WHERE company_id = ? ORDER BY id";
    private static final String INSERT_INTO_CUSTOMER_BY_NAME = "INSERT INTO customer (name) VALUES ?";

    private final DBManager dbManager;
    private List<Car> cars;
    private int company_id;
    Set<Integer> idCars;

    public CustomerDAOImpl() {
        dbManager = new DBManager();
    }

    @Override
    public List<Customer> getCustomers() {
        List<Customer> result = new ArrayList<>();
        try (Connection connection = dbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_NAME_BY_CUSTOMER)) {
            connection.setAutoCommit(true);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(new Customer(resultSet.getString(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Customer getInfoAboutRentedCar(int customerId) {

        Customer customer = null;
        try (Connection connection = dbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_NAME_AND_CAR_ID)) {
            connection.setAutoCommit(true);
            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                customer = new Customer(resultSet.getString(1));
                int carId = resultSet.getInt(2);
                if (carId != 0) {
                    setCompanyId(customer, carId, connection);
                    setCompany(customerId, resultSet, customer, connection);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }

    @Override
    public void returnCar(int customerId) {
        try (Connection connection = dbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CUSTOMER_WITH_NULL_RENTED_CAR)) {
            connection.setAutoCommit(true);
            preparedStatement.setInt(1, customerId);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rentCar(String carName, int customerId) {
        try (Connection connection = dbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID_BY_CAR_NAME)) {
            connection.setAutoCommit(true);
            preparedStatement.setString(1, carName);
            ResultSet resultSet = preparedStatement.executeQuery();
            int carId = -1;
            while (resultSet.next()) {
                carId = resultSet.getInt(1);
            }
            customerUpdater(carId, customerId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createCustomer(String name) {
        try (Connection connection = dbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO_CUSTOMER_BY_NAME)) {
            connection.setAutoCommit(true);
            preparedStatement.setString(1, name);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Car> getAvailableCars(Company company) {
        cars = new ArrayList<>();
        try (Connection connection = dbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID_BY_COMPANY)) {
            connection.setAutoCommit(true);
            preparedStatement.setString(1, company.getName());
            ResultSet resultSet = preparedStatement.executeQuery();
            company_id = -1;
            while (resultSet.next()) {
                company_id = resultSet.getInt(1);
            }
            selectRentedCar();
            companyCars();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    private void setCompanyId(Customer customer, int carId, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_NAME_AND_COMPANY_ID)) {
            connection.setAutoCommit(true);
            preparedStatement.setInt(1, carId);
            ResultSet resultSet = preparedStatement.executeQuery();
            int companyId = 0;
            while (resultSet.next()) {
                customer.setCar(new Car(resultSet.getString(1)));
                customer.setHasCar(true);
                companyId = resultSet.getInt(2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setCompany(int companyId, ResultSet resultSet, Customer customer, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_NAME_BY_COMPANY)) {
            connection.setAutoCommit(true);
            preparedStatement.setInt(1, companyId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                customer.setCompany(new Company(resultSet.getString(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void customerUpdater(int carId, int customerId) {
        try {
            Connection connection = dbManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CUSTOMER);
            connection.setAutoCommit(true);
            preparedStatement.setInt(1, carId);
            preparedStatement.setInt(2, customerId);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void selectRentedCar() throws SQLException {
        try (Connection connection = dbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_RENTED_CAR)) {
            connection.setAutoCommit(true);
            ResultSet resultSet = preparedStatement.executeQuery();
            idCars = new HashSet<>();
            while (resultSet.next()) {
                idCars.add(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void companyCars() throws SQLException {
        try (Connection connection = dbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID_NAME_BY_CAR)) {
            connection.setAutoCommit(true);
            preparedStatement.setInt(1, company_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String carName = resultSet.getString(2);
                if (!idCars.contains(id)) {
                    cars.add(new Car(carName));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
