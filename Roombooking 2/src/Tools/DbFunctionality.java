package Tools;

import Classes.Rooms.AbstractRoom;
import Classes.Email.TLSEmail;
import Classes.Order;
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

    /**
     * The method addUser is used to insert a User object into the database
     * @param user The user object to be inserted into the database. Must be a subclass of AbstractUser
     * @param conn The Connection object with the connection to the database
     * @throws SQLException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public void addUser(AbstractUser user, Connection conn) {
        PreparedStatement insertNewUser;

        try {
            String ins = "insert into User (User_firstName, User_lastName, User_email, User_dob, User_password, User_salt) " +
                    "values (?,?,?,?,?,?)";
            /* conn.prepareStatement takes a String with an SQL query, and returns a PreparedStatement object
            with the query's parameters and values. */
            insertNewUser = conn.prepareStatement(ins);
            /* setString applies the User object's data to the values of the SQL query.
            setInt can be used for integer numbers, and there are other set methods for different data types. */
            insertNewUser.setString(1, user.getFirstName());
            insertNewUser.setString(2, user.getLastName());
            insertNewUser.setString(3, user.getUserName());
            insertNewUser.setString(4, user.getDob());
            /* We create a hashed version of the password instead of storing the actual password in the database.
            This is to make it impossible to retrieve your password from the database. */
            String hashing = passwordHashAndCheck.stringToSaltedHash(user.getPassword());
            // The whole string is stored in the database as "<salt>:<hash-code>"
            insertNewUser.setString(5, hashing);
            // To store the salt we split the string by ":", which returns a String array with the salt on [0].
            String[] hashParts = hashing.split(":");
            insertNewUser.setString(6, hashParts[0]);
            // Call the execute method to perform the query.
            insertNewUser.execute();

        } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO: Refactor to use the User object
     * @param userEmail  The user's attempted login email
     * @param password   The user's attempted password
     * @param connection The Connection object with the connection to the database
     * @return true if the input password matches the stored password for a given email
     * @throws SQLException
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public boolean checkUser(String userEmail, String password, Connection connection)
            throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException {
        PreparedStatement checkUser;
        String query = "select * from user where User_email = ?";
        checkUser = connection.prepareStatement(query);
        checkUser.setString(1, userEmail);
        // Check if the attempted email matches any emails in the database.
        ResultSet resultSet = checkUser.executeQuery();
        // Loop through the result of the query to look for an email matching the attempted login.
        while (resultSet.next()) {
            if (resultSet.getString("User_email").toLowerCase().matches(userEmail)) {
                // If the email is found, get the hashed password from the database,
                String storedPassword = resultSet.getString("User_password");
                // and check if it matches the attempted password.
                return passwordHashAndCheck.validatePassword(password, storedPassword);
            } else {
                // If no matching email is found, return false.
                return false;
            }
        }
        // Return false if there are no elements in the ResultSet.
        return false;
    }

    /**
     * The method deleteUser is used to delete a user from the database. This method is for admins only
     * @param userEmail  The email of the user you want to delete
     * @param connection The Connection object with the connection to the database
     * @return true if something was deleted, false if nothing was affected
     * @throws SQLException
     */
    public boolean deleteUser(String userEmail, Connection connection) throws SQLException {
        PreparedStatement deleteUser;
        // Prepare a delete-query for execution,
        String delete = "delete from user where User_email = ?";
        deleteUser = connection.prepareStatement(delete);
        // and set the value of User_email to the given userEmail.
        deleteUser.setString(1, userEmail);
        // We need to use the executeUpdate method to execute and delete rows from the database.
        int result = deleteUser.executeUpdate();
        // ExecuteUpdate returns 1 if something is deleted, or 0 otherwise, and we return this as a boolean.
        return result == 1;
    }

    /**
     * The method deleteUser is used to delete a user from the database. This method is for admins only
     * @param userID     The ID of the user you want to delete
     * @param connection The Connection object with the connection to the database
     * @return true if something was deleted, false if nothing was affected
     * @throws SQLException
     */
    public boolean deleteUser(int userID, Connection connection) throws SQLException {
        PreparedStatement deleteUser;
        // Prepare a delete-query for execution,
        String delete = "delete from user where User_ID = ?";
        deleteUser = connection.prepareStatement(delete);
        // and set the value of User_ID to the given userID.
        deleteUser.setInt(1, userID);
        // We need to use the executeUpdate method to execute and delete rows from the database.
        int result = deleteUser.executeUpdate();
        // ExecuteUpdate returns 1 if something is deleted, or 0 otherwise, and we return this as a boolean.
        return result == 1;
    }

    /**
     * The method addRoom is used to insert a Room object into the database.
     * @param room       The Room object to be added to the database. Must be a subclass of AbstractRoom.
     * @param connection The Connection object with the connection to the database
     */
    public void addRoom(AbstractRoom room, Connection connection) throws SQLException {
        PreparedStatement insertNewRoom;

        String insert = "insert into Rooms (Room_ID, Room_name, Room_building, Room_maxCapacity) values (?,?,?,?)";
        insertNewRoom = connection.prepareStatement(insert);
        // Get the appropriate values from the Room object, and set the query's values accordingly.
        insertNewRoom.setInt(1, room.getRoomID());
        insertNewRoom.setString(2, room.getRoomName());
        insertNewRoom.setString(3, room.getRoomBuilding());
        insertNewRoom.setInt(4, room.getMaxCapacity());
        // Call the execute method to add the Room to the database.
        insertNewRoom.execute();
    }

    /**
     * The method deleteRoom is used to delete the room with the given roomID. This method is for admins only
     * @param roomID     The ID of the Room you want to delete
     * @param connection The Connection object with the connection to the database
     * @return true if something was deleted, false if nothing was affected
     * @throws SQLException
     */
    public boolean deleteRoom(int roomID, Connection connection) throws SQLException {
        PreparedStatement deleteRoom;
        // Prepare a delete-query for execution,
        String delete = "delete from Rooms where Room_ID = ?";
        deleteRoom = connection.prepareStatement(delete);
        // and set the value of Room_ID to the given roomID.
        deleteRoom.setInt(1, roomID);
        // We need to use the executeUpdate method to execute and delete rows from the database.
        int result = deleteRoom.executeUpdate();
        // ExecuteUpdate returns 1 if something is deleted, or 0 otherwise, and we return this as a boolean.
        return result == 1;
    }

    /**
     * The method printRooms is used to print all Rooms in the database to a given PrintWriter
     * @param out           Where to print the results of the method
     * @param connection    The Connection object with the connection to the database
     * @throws SQLException
     */
    public void printRooms(PrintWriter out, Connection connection) throws SQLException {
        // Select all elements from the Rooms table,
        String strSelect = "Select * from Rooms";
        PreparedStatement statement = connection.prepareStatement(strSelect);
        // and store them in a ResultSet.
        ResultSet resultSet = statement.executeQuery(strSelect);
        out.print("Your results are:" + "<br>");
        // Loop through all elements of the ResultSet
        while (resultSet.next()) {
            // and print the Room name and building of the current result.
            out.print(resultSet.getString("Room_name") + " : " + resultSet.getString("Room_building") + "<br>");
        }

    }

    /**
     * The method addOrder is used to insert an Order object into the database
     * @param order         The Order object to be added to the database.
     * @param connection    The Connection object with the connection to the database
     * @throws SQLException
     */
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
        // Select the Order from the Order table with the corresponding Order_ID
        String select = "select * from `Order` where Order_ID = ?";
        selectRoom = connection.prepareStatement(select);
        selectRoom.setInt(1, requestedOrderID);
        // and store it in a ResultSet.
        ResultSet resultSet = selectRoom.executeQuery();

        // The resultSet's pointer starts at "nothing", so move it to the next (first, and only) element.
        resultSet.next();
        // Get and store the data in local variables,
        int orderID = resultSet.getInt("Order_ID");
        int userID = resultSet.getInt("User_ID");
        int roomID = resultSet.getInt("Room_ID");
        Timestamp timestampStart = resultSet.getTimestamp("Timestamp_start");
        Timestamp timestampEnd = resultSet.getTimestamp("Timestamp_end");
        // and return a new Order object with these variables.
        return new Order(orderID, userID, roomID, timestampStart, timestampEnd);
    }

    /**
     *
     * @param orderID    The ID of the Order you want to delete
     * @param connection The Connection object with the connection to the database
     * @return true if something was deleted, false if nothing was affected
     * @throws SQLException
     */
    public boolean deleteOrder(int orderID, Connection connection) throws SQLException {
        PreparedStatement deleteOrder;
        // Prepare a delete-query for execution,
        String delete = "delete from `Order` where Order_ID = ?";
        deleteOrder = connection.prepareStatement(delete);
        // and set the value of Order_ID to the given orderID.
        deleteOrder.setInt(1, orderID);
        // We need to use the executeUpdate method to execute and delete rows from the database.
        int result = deleteOrder.executeUpdate();
        // ExecuteUpdate returns 1 if something is deleted, or 0 otherwise, and we return this as a boolean.
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
