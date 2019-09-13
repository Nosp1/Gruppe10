package Tools;

import java.io.PrintWriter;
import java.sql.*;

public class DbFunctionality {
    Statement statement;

    public void addUser(String firstName, String lastName, String email, String dob, PrintWriter out, Connection conn) {
        PreparedStatement insertNewUser;
        try {

            String ins = "insert into User (User_firstName, User_lastName, User_email, User_dob) values (?,?,?,?)";
            insertNewUser = conn.prepareStatement(ins);
            insertNewUser.setString(1, firstName);
            insertNewUser.setString(2, lastName);
            insertNewUser.setString(3, email);
            insertNewUser.setString(4, dob);
            insertNewUser.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    Needs fix
     */
    public void checkUser(String userName, PrintWriter out, Connection connection) throws SQLException {
        PreparedStatement stmt;
        String query = "select * from user where User_email = ?";
        stmt = connection.prepareStatement(query);
        stmt.setString(1, userName);
        statement = connection.createStatement();
        //stmt.execute();
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            if (resultSet.getString("User_email").matches(userName)) {
                out.print("Welcome " + userName);
            }
        }
    }
}
