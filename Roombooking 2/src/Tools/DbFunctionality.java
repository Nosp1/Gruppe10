package Tools;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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

}
