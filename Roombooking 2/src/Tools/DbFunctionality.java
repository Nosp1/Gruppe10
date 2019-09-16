package Tools;

import Passwords.PasswordHashAndCheck;

import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;

public class DbFunctionality {
    Statement statement;
    PasswordHashAndCheck passwordHashAndCheck;

    public void addUser(String firstName, String lastName, String email, String passWord, String dob, PrintWriter out, Connection conn) {
        PreparedStatement insertNewUser;
        passwordHashAndCheck = new PasswordHashAndCheck();
        try {

            String ins = "insert into User (User_firstName, User_lastName, User_email, User_dob,User_password, User_salt) values (?,?,?,?,?,?)";
            insertNewUser = conn.prepareStatement(ins);
            insertNewUser.setString(1, firstName);
            insertNewUser.setString(2, lastName);
            insertNewUser.setString(3, email);
            insertNewUser.setString(4, dob);
            String hashing = passwordHashAndCheck.stringToSaltedHash(passWord);
            System.out.println(hashing);
            //split by ":" because method returns <salts>:<hashed password>
            String[] hashParts = hashing.split(":");
            insertNewUser.setString(5,hashParts[1]);
            insertNewUser.setString(6, hashParts[0]);
            insertNewUser.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
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
            if (resultSet.getString("User_email").toLowerCase().matches(userName)) {
                out.print("Welcome " + userName);
            }
        }
    }
}
