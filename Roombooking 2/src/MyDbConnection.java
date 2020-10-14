import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class MyDbConnection {
    final static String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    final static String DB_URL = "jdbc:mariadb://localhost:3306";
    final static String root = "root";
    final static String pw = "mariadb";
    static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection(JDBC_DRIVER);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection dbLoggIn(PrintWriter out) {
        try {
            return connection != null ? connection = DriverManager.getConnection(DB_URL, root, pw) : connection;


        } catch (SQLException e) {
            e.printStackTrace();
            out.println("SQL Exception " + e);
        }

    }

}
