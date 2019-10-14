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
import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for all features related to user functionality.
 * @author brisdalen, (trym)
 */
public class RoombookingTests {

    DbTool dbTool;
    DbFunctionality dbFunctionality;
    Connection testConnection;

    String testUserEmail = "ola.nordmann@gmail.com";

    String testRoomName = "TEST001";

    int testUserID = 5;
    int testRoomID = 10;
    String testTimestampStart = "2019-09-26 16:00";
    String testTimestampEnd = "2019-09-26 18:00";
    Order testOrder;

    @Before
    public void init() {
        System.out.println("test init");
        dbTool = new DbTool();
        dbFunctionality = new DbFunctionality();
        testConnection = dbTool.dbLogIn();
    }
    @Test
    public void testUser() {
        System.out.println("testAddUser");
        AbstractUser testUser = new Student("Ola", "Nordmann", testUserEmail, "1234", "1900-01-01");
        System.out.println(testUser.toString());
        dbFunctionality.addUser(testUser, testConnection);
        String statement = "SELECT * FROM User WHERE User_email = ?";
        try {
            PreparedStatement preparedStatement = testConnection.prepareStatement(statement);
            preparedStatement.setString(1, testUser.getUserName());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                assertEquals(resultSet.getString("User_email"), testUser.getUserName());
            }

            assertTrue(dbFunctionality.checkUser(testUserEmail, "1234", testConnection));

            assertEquals(dbFunctionality.getUserId(testUserEmail,testConnection),testUserID);

        } catch (SQLException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            try {
                assertTrue(dbFunctionality.deleteUserByEmail(testUserEmail, testConnection));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testRoom() {
        System.out.println("testRoom");
        AbstractRoom testRoom = new Grouproom(testRoomID, testRoomName, "TEST", 10, true, true);
        try {
            dbFunctionality.addRoom(testRoom, testConnection);
            String statement = "SELECT Room_name FROM Rooms WHERE Room_ID = ?";
            PreparedStatement preparedStatement = testConnection.prepareStatement(statement);
            preparedStatement.setString(1, testRoomName);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                assertEquals(resultSet.getString("Room_name"), testRoomName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                assertTrue(dbFunctionality.deleteRoom(testRoomID, testConnection));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testOrder() throws SQLException {
        System.out.println("testOrder");
        AbstractRoom testRoom = new Grouproom(testRoomID, testRoomName, "TEST", 10, true, true);

        int orderID = dbFunctionality.getOrderID(testConnection);
        int firstOrderID = orderID;

        try {
            System.out.println("orderID: " + orderID);
            testOrder = new Order(orderID, testUserID, testRoomID, testTimestampStart, testTimestampEnd);

            try {
                // Adding a room to test the orders with
                dbFunctionality.addRoom(testRoom, testConnection);

                try {
                    // ----- Testing addOrder -----
                    dbFunctionality.addOrder(testOrder, testConnection);
                    String statement = "SELECT Room_ID FROM `order` WHERE Room_ID = ?";
                    PreparedStatement preparedStatement = testConnection.prepareStatement(statement);
                    preparedStatement.setInt(1, testRoomID);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        System.out.println(resultSet.getInt("Room_ID") + ":" + testRoomID);
                        assertEquals(resultSet.getInt("Room_ID"), testRoomID);
                    }

                    try {
                        // ----- Testing getOrder -----
                        assertEquals(dbFunctionality.getOrder(orderID, testConnection).getID(), testOrder.getID());
                        assertEquals(dbFunctionality.getOrder(orderID, testConnection).getUserID(), testOrder.getUserID());
                        assertEquals(dbFunctionality.getOrder(orderID, testConnection).getRoomID(), testOrder.getRoomID());
                        assertEquals(dbFunctionality.getOrder(orderID, testConnection).getTimestampStart(), testOrder.getTimestampStart());
                        assertEquals(dbFunctionality.getOrder(orderID, testConnection).getTimestampEnd(), testOrder.getTimestampEnd());

                        try {
                            /* ----- Testing order.intersects() -----
                            Add new Orders, 16-18, 15-17, 17-19 and 10-12 */
                            orderID++;
                            System.out.println("orderID increment: " + orderID);
                            dbFunctionality.addOrder(new Order(orderID, testUserID, testRoomID, "2019-09-26 16:00", "2019-09-26 18:00"), testConnection);

                            orderID++;
                            System.out.println("orderID increment: " + orderID);
                            dbFunctionality.addOrder(new Order(orderID, testUserID, testRoomID, "2019-09-26 15:00", "2019-09-26 17:00"), testConnection);

                            orderID++;
                            System.out.println("orderID increment: " + orderID);
                            dbFunctionality.addOrder(new Order(orderID, testUserID, testRoomID, "2019-09-26 17:00", "2019-09-26 19:00"), testConnection);

                            orderID++;
                            System.out.println("orderID increment: " + orderID);
                            dbFunctionality.addOrder(new Order(orderID, testUserID, testRoomID, "2019-09-26 10:00", "2019-09-26 12:00"), testConnection);

                            System.out.println(dbFunctionality.getOrder(firstOrderID + 1, testConnection).toString() + "\n");
                            System.out.println(dbFunctionality.getOrder(firstOrderID + 2, testConnection).toString() + "\n");
                            System.out.println(dbFunctionality.getOrder(firstOrderID + 3, testConnection).toString() + "\n");
                            System.out.println(dbFunctionality.getOrder(firstOrderID + 4, testConnection).toString() + "\n");

                            Order testOrder1 = dbFunctionality.getOrder(firstOrderID + 1, testConnection);
                            Order testOrder2 = dbFunctionality.getOrder(firstOrderID + 2, testConnection);
                            Order testOrder3 = dbFunctionality.getOrder(firstOrderID + 3, testConnection);
                            Order testOrder4 = dbFunctionality.getOrder(firstOrderID + 4, testConnection);

                            assertTrue(testOrder1.intersects(testOrder2));
                            assertTrue(testOrder1.intersects(testOrder3));
                            assertFalse(testOrder1.intersects(testOrder4));

                            try {
                                // ----- Testing deleteOrder -----
                                System.out.println("delete order " + firstOrderID);
                                assertTrue(dbFunctionality.deleteOrder(firstOrderID, testConnection));

                                System.out.println("delete order " + (firstOrderID + 1));
                                assertTrue(dbFunctionality.deleteOrder(firstOrderID + 1, testConnection));

                                System.out.println("delete order " + (firstOrderID + 2));
                                assertTrue(dbFunctionality.deleteOrder(firstOrderID + 2, testConnection));

                                System.out.println("delete order " + (firstOrderID + 3));
                                assertTrue(dbFunctionality.deleteOrder(firstOrderID + 3, testConnection));

                                System.out.println("delete order " + (firstOrderID + 4));
                                assertTrue(dbFunctionality.deleteOrder(firstOrderID + 4, testConnection));
                                // Catch errors for deleteOrder
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            // Catch errors for order.intersects()
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                            try {
                                dbFunctionality.deleteOrder(firstOrderID + 1, testConnection);
                                dbFunctionality.deleteOrder(firstOrderID + 2, testConnection);
                                dbFunctionality.deleteOrder(firstOrderID + 3, testConnection);
                                dbFunctionality.deleteOrder(firstOrderID + 4, testConnection);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        // Catch errors for getOrder
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                        try {
                            dbFunctionality.deleteOrder(firstOrderID, testConnection);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    // Catch erros for addOrder
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    try {
                        dbFunctionality.deleteOrder(firstOrderID, testConnection);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                // Catch the error if the initial addRoom fails
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            try {
                // Helt til slutt sletter vi rommet, uansett om testen passer eller failer
                dbFunctionality.deleteRoom(testRoomID, testConnection);
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    dbFunctionality.deleteOrder(firstOrderID, testConnection);
                    dbFunctionality.deleteRoom(testRoomID, testConnection);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testGetAllOrdersFromRoom() {
        String testTimestampStart2 = "2019-01-01 16:00:00";
        String testTimestampEnd2 = "2019-01-01 18:00:00";

        try {
            ResultSet resultSet = dbFunctionality.getAllOrdersFromRoom(1, testConnection);

            while(resultSet.next()) {
                System.out.println(resultSet.getString("Order_ID") + ": " + resultSet.getTimestamp("Timestamp_start") + " - " + resultSet.getTimestamp("Timestamp_end"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetOrdersFromRoom() {
        String testTimestampStart2 = "2019-01-01";

        try {
            ResultSet resultSet = dbFunctionality.getOrdersFromRoom(3, testTimestampStart2, testConnection);

            while(resultSet.next()) {
                System.out.println(resultSet.getString("Order_ID") + ": " + resultSet.getTimestamp("Timestamp_start") + " - " + resultSet.getTimestamp("Timestamp_end"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Ignore
    public void bookRoom() {
        // if(!intersects(testOrder1.intersects(testorder2)
    }

}
