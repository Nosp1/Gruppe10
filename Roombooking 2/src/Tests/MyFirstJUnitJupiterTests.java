package Tests;

import Classes.AbstractRoom;
import Classes.Grouproom;
import Tools.DbFunctionality;
import Tools.DbTool;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class MyFirstJUnitJupiterTests {

    DbTool dbTool;
    DbFunctionality dbFunctionality;
    Connection testConnection;

    String testUserEmail = "ola.nordmann@gmail.com";

    String testRoomID = "TEST001";

    @Before
    public void init() {
        System.out.println("test init");
        dbTool = new DbTool();
        dbFunctionality = new DbFunctionality();
        testConnection = dbTool.dbLogIn();
    }

    @Test
    public void testAddUser() {
        dbFunctionality.addUser("Ola", "Nordmann", testUserEmail, "1234", "1900-01-01", testConnection);
        String statement = "SELECT User_email FROM User WHERE User_email = ?";
        try {
            PreparedStatement preparedStatement = testConnection.prepareStatement(statement);
            preparedStatement.setString(1, testUserEmail);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                assertEquals(resultSet.getString("User_email"), testUserEmail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoginUser() {
        try {
            assertEquals(dbFunctionality.checkUser(testUserEmail, "1234", testConnection), true);
        } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteUser() {
        try {
            assertEquals(dbFunctionality.deleteUser(testUserEmail, testConnection), true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddRoom() {
        AbstractRoom testRoom = new Grouproom(testRoomID, "TEST_FLOOR", 10);
        try {
            dbFunctionality.addRoom(testRoom, testConnection);
            String statement = "SELECT roomID FROM Rooms WHERE roomID = ?";
            PreparedStatement preparedStatement = testConnection.prepareStatement(statement);
            preparedStatement.setString(1, testRoomID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                assertEquals(resultSet.getString("roomID"), testRoomID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteRoom() {
        try {
            assertEquals(dbFunctionality.deleteRoom(testRoomID, testConnection), true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
