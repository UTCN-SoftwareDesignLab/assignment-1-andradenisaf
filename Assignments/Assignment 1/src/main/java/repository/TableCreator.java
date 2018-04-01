package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TableCreator {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:8889/bank_application?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    private static final String USER = "root";
    private static final String PASS = "root";
    private static final int TIMEOUT = 5;

    private Connection connection;

    public TableCreator() {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            createTables();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    private void createTables() throws SQLException {
        Statement statement = connection.createStatement();

        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "  id int(11) NOT NULL AUTO_INCREMENT," +
                "  fullname varchar(50) NOT NULL," +
                "  username varchar(50) NOT NULL," +
                "  password varchar(50) NOT NULL," +
                "  address varchar(50) NOT NULL," +
                "  email varchar(50) NOT NULL," +
                "  role varchar(50) NOT NULL," +
                "  PRIMARY KEY (id)," +
                "  UNIQUE KEY id_UNIQUE (id)" +
                ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";
        statement.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS client (" +
                "  id int(11) NOT NULL AUTO_INCREMENT," +
                "  fullname varchar(50) NOT NULL," +
                "  CNP varchar(14) NOT NULL," +
                "  id_card_no int(11) NOT NULL," +
                "  address varchar(50) NOT NULL," +
                "  email varchar(50) NOT NULL," +
                "  PRIMARY KEY (id)," +
                "  UNIQUE KEY id_UNIQUE (id)" +
                ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";
        statement.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS bank_account (" +
                "  id int(11) NOT NULL AUTO_INCREMENT," +
                "  client_id int(11) NOT NULL," +
                "  account_type varchar(50) NOT NULL," +
                "  amount float(11) NOT NULL," +
                "  creation_date datetime DEFAULT NULL," +
                "  PRIMARY KEY (id)," +
                "  UNIQUE KEY id_UNIQUE (id)," +
                "  FOREIGN KEY (client_id) REFERENCES client(id)" +
                ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";
        statement.execute(sql);


        sql = "CREATE TABLE IF NOT EXISTS utility_bill (" +
                "  id int(11) NOT NULL AUTO_INCREMENT," +
                "  bill_type varchar(500) NOT NULL," +
                "  client_id int(11) NOT NULL," +
                "  amount_to_pay float(11) NOT NULL," +
                "  paid int(1) NOT NULL," +
                "  PRIMARY KEY (id)," +
                "  UNIQUE KEY id_UNIQUE (id)," +
                "  FOREIGN KEY (client_id) REFERENCES client(id)" +
                ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";
        statement.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS activity_log (" +
                "  id int(11) NOT NULL AUTO_INCREMENT," +
                "  employee_id int(11) NOT NULL," +
                "  description varchar(200) NOT NULL," +
                "  PRIMARY KEY (id)," +
                "  UNIQUE KEY id_UNIQUE (id)," +
                "  FOREIGN KEY (employee_id) REFERENCES users(id)" +
                ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";
        statement.execute(sql);

        statement.close();
    }

    public static void main(String[] argv) {
        TableCreator tableCreator = new TableCreator();
    }
}
