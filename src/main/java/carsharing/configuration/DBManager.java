package carsharing.configuration;

import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@NoArgsConstructor
public class DBManager {

    private static final String CREATE_TABLE_COMPANY = """
            CREATE TABLE IF NOT EXISTS company
            (id INTEGER PRIMARY KEY AUTO_INCREMENT,
            name VARCHAR(100) UNIQUE NOT NULL);""";

    private static final String CREATE_TABLE_CAR = """
            CREATE TABLE IF NOT EXISTS car
            (id INTEGER PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100) UNIQUE NOT NULL,
            company_id INTEGER NOT NULL,
            CONSTRAINT fk_company FOREIGN KEY(company_id)
            REFERENCES company(id));""";

    private static final String CREATE_TABLE_CUSTOMER = """
            CREATE TABLE IF NOT EXISTS customer
            (id INTEGER PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100) UNIQUE NOT NULL,
            rented_car_id INTEGER DEFAULT NULL,
            CONSTRAINT fk_car FOREIGN KEY(rented_car_id)
            REFERENCES car(id));
            """;

    private static String DB_URL;
    private static final java.lang.String JDBC_DRIVER = "org.h2.Driver";

    public DBManager(String name) {
        DB_URL = "jdbc:h2:./src/main/java/carsharing/db/" + name;
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public final void createTables() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            connection.setAutoCommit(true);
            statement.executeUpdate(CREATE_TABLE_COMPANY);
            statement.execute(CREATE_TABLE_CAR);
            statement.execute(CREATE_TABLE_CUSTOMER);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
