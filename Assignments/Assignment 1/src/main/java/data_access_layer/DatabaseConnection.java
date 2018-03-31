package data_access_layer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bank";

    private static final String USER = "root";
    private static final String PASS = "adardna19";
    private static final int TIMEOUT = 5;

    private Connection connection;

    private static DatabaseConnection dao = new DatabaseConnection();

    private DatabaseConnection() {
        setupConnection();
    }

    public void setupConnection() {

        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public Connection getConnection() {
        if (connection == null)
            setupConnection();
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public static DatabaseConnection getInstance() {
        return dao;
    }
}
