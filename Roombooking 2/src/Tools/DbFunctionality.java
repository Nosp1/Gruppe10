package Tools;

import Classes.Email.TLSEmail;
import Classes.Order;
import Classes.Rooms.AbstractRoom;
import Classes.User.AbstractUser;
import Passwords.PasswordHashAndCheck;

import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.text.ParseException;

/**
 * handles the queries to and from the database.
 *
 * @author trym, brisdalen
 */
public class DbFunctionality {
    Statement statement;
    PasswordHashAndCheck passwordHashAndCheck;


    public DbFunctionality() {
        passwordHashAndCheck = new PasswordHashAndCheck();
    }

    /**
     * Temp method for adding Admin Email for sending out booking confirmation to registered users.
     */
    public void addAdminEmail(String email, String password, Connection connection) {
        PreparedStatement insertEmail;
        try {
            String ins = "insert into Email (Email_name, Email_Password, Email_Salt) values (?,?,?)";
            insertEmail = connection.prepareStatement(ins);
            insertEmail.setString(1, email);
            String hashing = passwordHashAndCheck.stringToSaltedHash(password);
            insertEmail.setString(2, hashing);
            String[] hashParts = hashing.split(":");
            insertEmail.setString(3, hashParts[0]);
            insertEmail.execute();
        } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }
    public boolean deleteAdminEmail (String adminUser, Connection connection) throws SQLException {
        PreparedStatement deleteAdminUser;
        String delete = "delete from  Email where Email_name = ?";
        deleteAdminUser = connection.prepareStatement(delete);
        deleteAdminUser.setString(1,adminUser);
        int result = deleteAdminUser.executeUpdate();
        return result == 1;

    }

    public TLSEmail getAdminEmail(String requestEmail, Connection connection) throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException {
        PreparedStatement getEmail;
        String select = "delete from `Email` where Email_name = ?";
        getEmail = connection.prepareStatement(select);
        getEmail.setString(1, requestEmail);
        ResultSet resultSet = getEmail.executeQuery();
        resultSet.next();
        String email = resultSet.getString("Email_name");
        //String password = passwordHashAndCheck.(resultSet.getString("Email_Password"),resultSet.getString("Email_Salt"));
        return null;
    }

    public void addUser(AbstractUser user, Connection conn) {
        PreparedStatement insertNewUser;

        try {
            String ins = "insert into User (User_firstName, User_lastName, User_email, User_dob, User_password, User_salt) values (?,?,?,?,?,?)";
            insertNewUser = conn.prepareStatement(ins);
            insertNewUser.setString(1, user.getFirstName());
            insertNewUser.setString(2, user.getLastName());
            insertNewUser.setString(3, user.getUserName());
            insertNewUser.setString(4, user.getDob());
            /* We create a hashed version of the password instead of storing the actual password in the database.
            This is to make it impossible to retrieve your password from the database. */
            String hashing = passwordHashAndCheck.stringToSaltedHash(user.getPassword());
            // store the whole string in the database
            insertNewUser.setString(5, hashing);
            // split by ":" because method returns <salts>:<hashed password>
            String[] hashParts = hashing.split(":");
            // store the salt in the databse
            insertNewUser.setString(6, hashParts[0]);
            insertNewUser.execute();

        } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param username   The user's attempted login email
     * @param password   The user's attempted password
     * @param connection The Connection object to a given database
     * @return true if the input password matches the stored password for a given email
     * @throws SQLException
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public boolean checkUser(String username, String password, Connection connection) throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException {
        PreparedStatement stmt;
        String query = "select * from user where User_email = ?";
        stmt = connection.prepareStatement(query);
        stmt.setString(1, username);
        // Check if the attempted email matches any emails in the database
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            if (resultSet.getString("User_email").toLowerCase().matches(username)) {
                // If the email is found, store the hashed string in the variabled "storedPassword"
                String storedPassword = resultSet.getString("User_password");
                /* Return true or false if the input password matches the stored password
                   after hashing. */
                return passwordHashAndCheck.validatePassword(password, storedPassword);
            } else {
                // No user with that email found
                return false;
            }
        }
        // Return false if there is no result set(?)
        return false;
    }

    public boolean deleteUser(String username, Connection connection) throws SQLException {
        PreparedStatement deleteUser;
        String delete = "delete from user where User_email = ?";
        deleteUser = connection.prepareStatement(delete);
        deleteUser.setString(1, username);
        int result = deleteUser.executeUpdate();
        // result er 1 hvis noe blir slettet, eller 0 hvis ingenting ble affected
        return result == 1;
    }

    /**
     * @param room       The room to be added to the database. Must be a subclass of AbstractRoom.
     * @param connection The connection to the database.
     */
    public void addRoom(AbstractRoom room, Connection connection) throws SQLException {
        PreparedStatement insertNewRoom;

        String ins = "insert into Rooms (Room_ID, Room_name, Room_building, Room_maxCapacity) values (?,?,?,?)";
        insertNewRoom = connection.prepareStatement(ins);
        insertNewRoom.setInt(1, room.getRoomID());
        insertNewRoom.setString(2, room.getRoomName());
        insertNewRoom.setString(3, room.getRoomBuilding());
        insertNewRoom.setString(4, String.valueOf(room.getMaxCapacity()));
        insertNewRoom.execute();
    }

    public boolean deleteRoom(int roomID, Connection connection) throws SQLException {
        PreparedStatement deleteRoom;
        String delete = "delete from Rooms where Room_ID = ?";
        deleteRoom = connection.prepareStatement(delete);
        deleteRoom.setInt(1, roomID);
        int result = deleteRoom.executeUpdate();
        // result er 1 hvis noe blir slettet, eller 0 hvis ingenting ble affected
        return result == 1;
    }

    public void printRooms(PrintWriter out, Connection connection) throws SQLException {
        String strSelect = "Select * from Rooms";
        PreparedStatement statement = connection.prepareStatement(strSelect);
        ResultSet resultSet = statement.executeQuery(strSelect);
        out.print("Your results are:" + "<br>");
        while (resultSet.next()) {
            out.print(resultSet.getString("Room_ID") + " : " + resultSet.getString("Room_building") + "<br>");
        }

    }

    public void addOrder(Order order, Connection connection) throws SQLException {
        PreparedStatement insertNewOrder;
        System.out.println("addOrder started");

        String ins = "insert into `order` (User_ID, Room_ID, Timestamp_start, Timestamp_end) VALUES (?,?,?,?)";
        insertNewOrder = connection.prepareStatement(ins);
        insertNewOrder.setInt(1, order.getUserID());
        insertNewOrder.setInt(2, order.getRoomID());
        insertNewOrder.setTimestamp(3, order.getTimestampStart());
        insertNewOrder.setTimestamp(4, order.getTimestampEnd());
        insertNewOrder.execute();
        // TODO ADD RECIEPT METHOD
    }

    /**
     * The method getOrder returns an Order object with the requested orderID
     * @param requestedOrderID The ID of an order in the database
     * @param connection       The Connection object with the connection to the database
     * @return An Order object representing an entry in the Order table of the database
     * @throws SQLException
     */
    public Order getOrder(int requestedOrderID, Connection connection) throws SQLException, ParseException {
        PreparedStatement selectRoom;
        String select = "select * from `Order` where Order_ID = ?";
        selectRoom = connection.prepareStatement(select);
        selectRoom.setInt(1, requestedOrderID);
        ResultSet resultSet = selectRoom.executeQuery();

        resultSet.next();
        int orderID = resultSet.getInt("Order_ID");
        int userID = resultSet.getInt("User_ID");
        int roomID = resultSet.getInt("Room_ID");
        Timestamp timestampStart = resultSet.getTimestamp("Timestamp_start");
        Timestamp timestampEnd = resultSet.getTimestamp("Timestamp_end");

        return new Order(orderID, userID, roomID, timestampStart, timestampEnd);
    }

    public boolean deleteOrder(int orderID, Connection connection) throws SQLException {
        PreparedStatement deleteOrder;
        String delete = "delete from `Order` where Order_ID = ?";
        deleteOrder = connection.prepareStatement(delete);
        deleteOrder.setInt(1, orderID);
        int result = deleteOrder.executeUpdate();
        return result == 1;
    }

    public int getOrderID(Connection connection) throws SQLException {
        PreparedStatement selectOrderID;
        String select = "select Order_ID from `order`";
        selectOrderID = connection.prepareStatement(select);
        ResultSet resultSet = selectOrderID.executeQuery();

        while(resultSet.next()) {
            return resultSet.getInt("Order_ID");
        }

        return 1;
    }
}
