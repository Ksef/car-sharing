package carsharing.car;

import carsharing.configuration.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarDAOImpl extends DBManager implements CarDAO {

    private static final String SELECT_NAME_BY_CAR = "SELECT name from car WHERE company_id = ?";
    private static final String INSERT_INTO_CAR = "INSERT INTO car (name, company_id) VALUES (?, ?)";

    private final DBManager DBManager;

    public CarDAOImpl(String name) {
        super(name);
        DBManager = new DBManager();
    }

    @Override
    public List<Car> getCars(int companyId) {
        List<Car> result = new ArrayList<>();
        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_NAME_BY_CAR)) {
            connection.setAutoCommit(true);
            preparedStatement.setInt(1, companyId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(new Car(resultSet.getString(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void createCar(int companyId, String name) {
        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO_CAR)) {
            connection.setAutoCommit(true);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, companyId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
