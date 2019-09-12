import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
        }
        return connection;
    }
    public void printResults (PrintWriter out) throws SQLException {
        String strSelect = "Select * from Rooms";
        ResultSet resultSet = statement.executeQuery(strSelect);
        while (resultSet.next()) {
            out.print("Your results are:" + "\n");
            out.print(resultSet.getString(1));

        }
        out.print("query complete");
    }
}
