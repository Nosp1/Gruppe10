package Tools;

import Passwords.PasswordHashAndCheck;

import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;

public class DbFunctionality {
    Statement statement;
    PasswordHashAndCheck passwordHashAndCheck;

    public DbFunctionality() {
        passwordHashAndCheck = new PasswordHashAndCheck();
    }

    public void addUser(String firstName, String lastName, String email, String passWord, String dob, PrintWriter out, Connection conn) {
        PreparedStatement insertNewUser;
        try {

            String ins = "insert into User (User_firstName, User_lastName, User_email, User_dob, User_password, User_salt) values (?,?,?,?,?,?)";
            insertNewUser = conn.prepareStatement(ins);
            insertNewUser.setString(1, firstName);
            insertNewUser.setString(2, lastName);
            insertNewUser.setString(3, email);
            insertNewUser.setString(4, dob);
            String hashing = passwordHashAndCheck.stringToSaltedHash(passWord);
            // store the whole string in the database
            insertNewUser.setString(5, hashing);
            // split by ":" because method returns <salts>:<hashed password>
            String[] hashParts = hashing.split(":");
            // store the salt in the databse
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

    public boolean checkUser(String userName, String password, PrintWriter out, Connection connection) throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException {
        PreparedStatement stmt;
        String query = "select * from user where User_email = ?";
        stmt = connection.prepareStatement(query);
        stmt.setString(1, userName);
        statement = connection.createStatement();
        //stmt.execute();
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            if (resultSet.getString("User_email").toLowerCase().matches(userName)) {
                String storedPassword = resultSet.getString("User_password");
                return passwordHashAndCheck.validatePassword(password, storedPassword);
            } else {
                // No user with that email found
                return false;
            }
        }
        // If there is no result set(?)
        return false;
    }
}
