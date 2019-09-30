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
            // TODO: Endre denne og alt i databasen til til lowercase, siden det ikke funker i Windows
            DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/localhost/roombooking");
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
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/roombooking?autoReconnect=true&useSSL=false", "root", "dennIS93");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }
//todo delete this method redundant
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
