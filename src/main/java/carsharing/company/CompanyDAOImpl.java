package carsharing.company;

import carsharing.configuration.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAOImpl extends DBManager implements CompanyDAO {

    private final static String SELECT_NAME_BY_COMPANY = "SELECT name FROM company ORDER BY ID";
    private final static String INSERT_INTO_COMPANY = "INSERT INTO company (name) VALUES ?";
    private final static String SELECT_ID_BY_COMPANY = "SELECT id FROM company WHERE name = ?";

    private final DBManager DBManager;

    public CompanyDAOImpl(String name) {
        super(name);
        DBManager = new DBManager();
    }

    @Override
    public List<Company> getCompanies() {
        List<Company> companies = new ArrayList<>();
        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_NAME_BY_COMPANY)) {
            connection.setAutoCommit(true);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                companies.add(new Company(resultSet.getString(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return companies;
    }

    @Override
    public void createCompany(String name) {
        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO_COMPANY)) {
            connection.setAutoCommit(true);
            preparedStatement.setString(1, name);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCompanyId(String name) {
        int id = -1;
        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID_BY_COMPANY)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int i = resultSet.getInt(1);
                if (i != 0) {
                    id = i;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
}
