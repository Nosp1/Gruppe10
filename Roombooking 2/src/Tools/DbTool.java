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
    Connection connection;
    Statement statement;

    public Connection dbLogIn(PrintWriter out) {
        try {
            Context context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/localhost/Roombooking");
            connection = dataSource.getConnection();

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
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Roombooking?autoReconnect=true&useSSL=false", "root", "toor");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public void printResults(PrintWriter out) throws SQLException {
        String strSelect = "Select * from User";
        statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(strSelect);
        out.print("Your results are:" + "<br>");
        while (resultSet.next()) {
            out.print(resultSet.getString("User_firstName"));

        }
        out.print("query complete");
    }
}
