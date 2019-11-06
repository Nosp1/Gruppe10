package Tools;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;

/**
 * @author trym
 * @author brisdalen
 */
public class DbTool {
    private Connection connection;

    public Connection dbLogIn(PrintWriter out) {
        try {
            Context context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/localhost/roombooking");
            connection = dataSource.getConnection();

            context.close();
            if (connection != null) {
                return connection;
            }

            return connection;
        } catch (NamingException | SQLException e) {
            e.printStackTrace();
            out.print("naming error" + e);
        }
        return connection;
    }

    public Connection dbLogIn() {
        // dbLogIn without PrintWriter, used for testing
        connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/roombooking?autoReconnect=true&useSSL=false", "root", "dennIS93");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
