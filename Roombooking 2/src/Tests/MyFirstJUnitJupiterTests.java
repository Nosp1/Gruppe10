package Tests;

import Tools.DbFunctionality;
import Tools.DbTool;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class MyFirstJUnitJupiterTests {

    PrintWriter testOut;
    DbTool dbTool;
    DbFunctionality dbFunctionality;
    Connection testConnection;

    @Before
    public void init() {
        System.out.println("test init");
        try {
            testOut = new PrintWriter(new File("test"));
            dbTool = new DbTool();
            dbFunctionality = new DbFunctionality();
            testConnection = dbTool.dbLogIn(testOut);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddUser() {
        String userEmail = "ola.nordmann@gmail.com";
        dbFunctionality.addUser("Ola", "Nordmann", userEmail, "1234", "1900-01-01", testOut, testConnection);
        String statement = "SELECT User_email FROM User WHERE User_email = ?";
        try {
            PreparedStatement preparedStatement = testConnection.prepareStatement(statement);
            preparedStatement.setString(1, userEmail);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                assertEquals(userEmail, resultSet.getString("User_email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
