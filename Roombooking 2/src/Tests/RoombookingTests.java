package Tests;

import Classes.Rooms.AbstractRoom;
import Classes.Rooms.Grouproom;
import Classes.Order;
import Classes.User.AbstractUser;
import Classes.User.Student;
import Tools.DbFunctionality;
import Tools.DbTool;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RoombookingTests {

    DbTool dbTool;
    DbFunctionality dbFunctionality;
    Connection testConnection;

    String testUserEmail = "ola.nordmann@gmail.com";

    String testRoomName = "TEST001";

    int testOrderID = 1;
    int testUserID = 5;
    int testRoomID = 1;
    Timestamp testTimestampStart = new Timestamp(1569420000000L);
    Timestamp testTimestampEnd = new Timestamp(1569427200000L);
    Order testOrder;

    @Before
    public void init() {
        System.out.println("test init");
        dbTool = new DbTool();
        dbFunctionality = new DbFunctionality();
        testConnection = dbTool.dbLogIn();
    }
    @Test
    public void testAddUser() {
        AbstractUser testUser = new Student("Ola", "Nordmann", testUserEmail, "1234", "1900-01-01");
        dbFunctionality.addUser(testUser, testConnection);
        String statement = "SELECT User_email FROM User WHERE User_email = ?";
        try {
            PreparedStatement preparedStatement = testConnection.prepareStatement(statement);
            preparedStatement.setString(1, testUser.getUserName());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                assertEquals(resultSet.getString("User_email"), testUser.getUserName());
            }

            assertEquals(dbFunctionality.checkUser(testUserEmail, "1234", testConnection), true);

            assertEquals(dbFunctionality.deleteUser(testUserEmail, testConnection), true);

        } catch (SQLException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRoom() {
        AbstractRoom testRoom = new Grouproom(testRoomID, testRoomName, "TEST", 10);
        try {
            dbFunctionality.addRoom(testRoom, testConnection);
            String statement = "SELECT Room_name FROM Rooms WHERE Room_ID = ?";
            PreparedStatement preparedStatement = testConnection.prepareStatement(statement);
            preparedStatement.setString(1, testRoomName);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                assertEquals(resultSet.getString("Room_name"), testRoomName);
            }

            assertTrue(dbFunctionality.deleteRoom(testRoomID, testConnection));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOrder() {
        AbstractRoom testRoom = new Grouproom(testRoomID, testRoomName, "TEST", 10);
        testOrder = new Order(testOrderID, testUserID, testRoomID, testTimestampStart, testTimestampEnd);
        try {
            // Adding a room to test the orders with
            dbFunctionality.addRoom(testRoom, testConnection);

            // ----- Testing addOrder -----
            dbFunctionality.addOrder(testOrder, testConnection);
            String statement = "SELECT Room_ID FROM `Order` WHERE Room_ID = ?";
            PreparedStatement preparedStatement = testConnection.prepareStatement(statement);
            preparedStatement.setInt(1, testRoomID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                assertEquals(resultSet.getInt("Room_ID"), testRoomID);
            }

            try {
                // ----- Testing getOrder -----
                assertEquals(dbFunctionality.getOrder(1, testConnection).getID(), testOrder.getID());
                assertEquals(dbFunctionality.getOrder(1, testConnection).getUserID(), testOrder.getUserID());
                assertEquals(dbFunctionality.getOrder(1, testConnection).getRoomID(), testOrder.getRoomID());
                assertEquals(dbFunctionality.getOrder(1, testConnection).getTimestampStart(), testOrder.getTimestampStart());
                assertEquals(dbFunctionality.getOrder(1, testConnection).getTimestampEnd(), testOrder.getTimestampEnd());

            // ----- Testing order.intersects() -----
            // Add new Orders, 16-18, 15-17, 17-19 and 10-12
            dbFunctionality.addOrder(new Order(2, testUserID, 1, new Timestamp(1569427200000L), new Timestamp(1569434400000L)), testConnection);
            dbFunctionality.addOrder(new Order(3, testUserID, 1, new Timestamp(1569423600000L), new Timestamp(1569430800000L)), testConnection);
            dbFunctionality.addOrder(new Order(4, testUserID, 1, new Timestamp(1569430800000L), new Timestamp(1569438000000L)), testConnection);
            dbFunctionality.addOrder(new Order(5, testUserID, 1, new Timestamp(1569405600000L), new Timestamp(1569412800000L)), testConnection);

                Order testOrder1 = dbFunctionality.getOrder(1, testConnection);
                Order testOrder2 = dbFunctionality.getOrder(2, testConnection);
                Order testOrder3 = dbFunctionality.getOrder(3, testConnection);
                Order testOrder4 = dbFunctionality.getOrder(4, testConnection);

                assertTrue(testOrder1.intersects(testOrder2));
                assertTrue(testOrder1.intersects(testOrder3));
                assertFalse(testOrder1.intersects(testOrder4));

                // ----- Testing deleteOrder -----
                assertTrue(dbFunctionality.deleteOrder(1, testConnection));
                assertTrue(dbFunctionality.deleteOrder(2, testConnection));
                assertTrue(dbFunctionality.deleteOrder(3, testConnection));
                assertTrue(dbFunctionality.deleteOrder(4, testConnection));
                assertTrue(dbFunctionality.deleteOrder(5, testConnection));
            }
            // Deleting the room that was tested on
            finally {
                dbFunctionality.deleteRoom(testRoomID, testConnection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Ignore
    public void bookRoom() {
        // if(!intersects(testOrder1.intersects(testorder2)
    }

}
