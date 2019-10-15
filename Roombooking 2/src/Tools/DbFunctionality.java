package Tools;

import Classes.Email.TLSEmail;
import Classes.Order;
import Classes.Rooms.AbstractRoom;
import Classes.User.AbstractUser;
import Classes.User.Student;
import Passwords.PasswordHashAndCheck;

import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;

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

    public boolean deleteAdminEmail(String adminUser, Connection connection) throws SQLException {
        PreparedStatement deleteAdminUser;
        String delete = "delete from  Email where Email_name = ?";
        deleteAdminUser = connection.prepareStatement(delete);
        deleteAdminUser.setString(1, adminUser);
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

    public AbstractUser getUser(String requestedUserEmail, Connection connection) throws SQLException {
        PreparedStatement selectUser;
        //select the User from the User table with the corresponding User_ID
        String select = "select * from user where User_email = ?";
        selectUser = connection.prepareStatement(select);
        selectUser.setString(1, requestedUserEmail);
        ResultSet resultSet = selectUser.executeQuery();
        resultSet.next();
        String firstName = resultSet.getString("User_firstName");
        String lastName = resultSet.getString("User_lastName");
        String userName = resultSet.getString("User_email");
        String dob = resultSet.getNString("User_dob");
        String password = resultSet.getString("User_password");

        return new Student(firstName, lastName, userName, password, dob);
    }

    /**
     * @param userEmail  the users Email address
     * @param connection connection to db
     * @return userID as int.
     * @throws SQLException
     */
    public int getUserId(String userEmail, Connection connection) throws SQLException {
        PreparedStatement getUser;
        String query = "Select User_ID from user where User_Email = (?)";
        getUser = connection.prepareStatement(query);
        getUser.setString(1, userEmail);
        ResultSet resultSet = getUser.executeQuery();
        resultSet.next();
        //todo funker dette?
        getUser.closeOnCompletion();
         return Integer.parseInt(resultSet.getString(1));

    }

    /**
     * The method deleteUser is used to delete a user from the database. This method is for admins only
     *
     * @param userEmail  The email of the user you want to delete
     * @param connection The Connection object with the connection to the database
     * @return true if something was deleted, false if nothing was affected
     * @throws SQLException
     */
    public boolean deleteUserByEmail(String userEmail, Connection connection) throws SQLException {
        PreparedStatement deleteUser;
        String delete = "delete from user where User_email = ?";
        deleteUser = connection.prepareStatement(delete);
        deleteUser.setString(1, userEmail);
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

        String ins = "insert into Rooms (Room_ID, Room_name, Room_building, Room_maxCapacity, Tavle, Projektor) values (?,?,?,?,?,?)";
        insertNewRoom = connection.prepareStatement(ins);
        insertNewRoom.setInt(1, room.getRoomID());
        insertNewRoom.setString(2, room.getRoomName());
        insertNewRoom.setString(3, room.getRoomBuilding());
        insertNewRoom.setString(4, String.valueOf(room.getMaxCapacity()));

        boolean tavle = room.hasTavle();
        if(tavle) {
            insertNewRoom.setString(5, "JA");
        } else {
            insertNewRoom.setString(5, "NEI");
        }

        boolean prosjektor = room.hasProjektor();
        if(prosjektor) {
            insertNewRoom.setString(6, "JA");
        } else {
            insertNewRoom.setString(6, "NEI");
        }

        insertNewRoom.execute();
    }

    public boolean deleteRoom(int roomID, Connection connection) throws SQLException {
        // Delete orders associated with the room first, so there is no foreign key dependency.
        PreparedStatement stmt;
        String deleteOrder = "delete from `order` where Room_ID = ?";
        stmt = connection.prepareStatement(deleteOrder);
        stmt.setInt(1, roomID);
        stmt.executeUpdate();

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
        out.print("<h2>Your results are:" + "</h2><br>");
        while (resultSet.next()) {
            out.print("<div class=\"room-card\" id=\"room-cards\">");
            out.print("<p>" + resultSet.getString("Room_ID") + " : " + resultSet.getString("Room_name"));
            out.print(" Plasser: " + resultSet.getString("Room_maxCapacity") + "</p>");
            out.print("<p>Tavle: " + resultSet.getString("Tavle"));
            out.print(" Projektor: " + resultSet.getString("Projektor") + "</p>");
            out.println("</div>");
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
     *
     * @param requestedOrderID The ID of an order in the database
     * @param connection       The Connection object with the connection to the database
     * @return An Order object representing an entry in the Order table of the database
     * @throws SQLException
     */
    public Order getOrder(int requestedOrderID, Connection connection) throws SQLException, ParseException {
        System.out.println("getOrder started. requestedOrderID: " + requestedOrderID);
        PreparedStatement selectOrder;
        // Select the Order from the Order table with the corresponding Order_ID
        String select = "select * from `order` where Order_ID = ?";
        selectOrder = connection.prepareStatement(select);
        selectOrder.setInt(1, requestedOrderID);
        // and store it in a ResultSet.
        ResultSet resultSet = selectOrder.executeQuery();

        // The resultSet's pointer starts at "nothing", so move it to the next (first, and only) element.
        resultSet.first();
        // Get and store the data in local variables,
        int orderID = resultSet.getInt("Order_ID");
        System.out.println("getOrder orderID: " + orderID);
        int userID = resultSet.getInt("User_ID");
        System.out.println("getOrder userID: " + userID);
        int roomID = resultSet.getInt("Room_ID");
        System.out.println("getOrder roomID: " + roomID);
        Timestamp timestampStart = resultSet.getTimestamp("Timestamp_start");
        Timestamp timestampEnd = resultSet.getTimestamp("Timestamp_end");

        return new Order(orderID, userID, roomID, timestampStart, timestampEnd);
    }


    public ResultSet getAllOrdersFromRoom(int roomID, Connection connection) throws SQLException {
        PreparedStatement selectOrders;
        String select = "select Order_ID, Timestamp_start, Timestamp_end from `order`\n" +
                "  inner join rooms\n" +
                "  on `order`.Room_ID = rooms.Room_ID\n" +
                "  where `order`.Room_ID = ?";
        selectOrders = connection.prepareStatement(select);
        selectOrders.setInt(1, roomID);
        return selectOrders.executeQuery();
    }

    public ResultSet getOrdersFromRoom(int roomID, String date, Connection connection) throws SQLException, ParseException {
        // TODO: date burde kunne ta inn et timestamp, og strings formatert som "yyyy-mm-dd hh:ss" og "yyyy-mm-dd"
        System.out.println("Room_ID recieved: " + roomID);
        System.out.println("Date as String recieved: " + date);
        PreparedStatement selectOrders;
        String select = "select Order_ID, Timestamp_start, Timestamp_end from `order`\n" +
                "  inner join rooms\n" +
                "  on `order`.Room_ID = rooms.Room_ID\n" +
                "  where DATE(`order`.Timestamp_start) like ?\n" +
                "  and `order`.Room_ID = ?";
        selectOrders = connection.prepareStatement(select);
        selectOrders.setString(1, date);
        selectOrders.setInt(2, roomID);
        return selectOrders.executeQuery();
    }

    /**
     * @param orderID    The ID of the Order you want to delete
     * @param connection The Connection object with the connection to the database
     * @return true if something was deleted, false if nothing was affected
     * @throws SQLException
     */
    public boolean deleteOrder(int orderID, Connection connection) throws SQLException {
        PreparedStatement deleteOrder;
        String delete = "delete from `Order` where Order_ID = ?";
        deleteOrder = connection.prepareStatement(delete);
        deleteOrder.setInt(1, orderID);
        int result = deleteOrder.executeUpdate();
        return result == 1;
    }

    public int getRoomID(Connection connection) throws SQLException {
        PreparedStatement selectRoomID;
        String select = "select Room_ID from rooms order by Room_ID desc limit 1";
        selectRoomID = connection.prepareStatement(select);
        ResultSet resultSet = selectRoomID.executeQuery();

        while (resultSet.next()) {
            System.out.println("roomID from method: " + (resultSet.getInt("Room_ID") + 1));
            return resultSet.getInt("Room_ID") + 1;
        }

        return 1;
    }

    public int getOrderID(Connection connection) throws SQLException {
        PreparedStatement selectOrderID;
        String select = "select Order_ID from `order` order by Order_ID desc limit 1";
        selectOrderID = connection.prepareStatement(select);
        ResultSet resultSet = selectOrderID.executeQuery();

        while (resultSet.next()) {
            System.out.println("orderID from method: " + (resultSet.getInt("Order_ID") + 1));
            return resultSet.getInt("Order_ID") + 1;
        }

        return 1;
    }

    // TODO: Dokumentere metodene under
    public ResultSet getMostBookedRoom(Connection connection) throws SQLException {
        return getMostBookedRoom(5, connection);
    }

    public ResultSet getMostBookedRoom(int howMany, Connection connection) throws SQLException {
        PreparedStatement selectBookedRoom;
        //TODO: teste metoden og implementere i en Servlet
        String select = "SELECT room_id, COUNT(*) as amount FROM `order` GROUP BY room_id ORDER BY amount DESC LIMIT ?";
        selectBookedRoom = connection.prepareStatement(select);
        selectBookedRoom.setInt(1, howMany);

        return selectBookedRoom.executeQuery();
    }

    public ResultSet getMostActiveUsers(Connection connection) throws SQLException {
        return getMostActiveUsers(5, connection);
    }

    public ResultSet getMostActiveUsers(int howMany, Connection connection) throws SQLException {
        PreparedStatement selectActiveUsers;
        //TODO: teste metoden og implementere i en Servlet
        String select = "SELECT user_id, COUNT(*) as amount FROM `order` GROUP BY user_id ORDER BY amount DESC LIMIT ?";
        selectActiveUsers = connection.prepareStatement(select);
        selectActiveUsers.setInt(1, howMany);

        return selectActiveUsers.executeQuery();
    }

    /**
     * @param requestedUserID
     * @param connection
     * @return ArrayList with order objects.
     * @throws SQLException
     * @throws ParseException
     */
    public ArrayList<Order> getOrderListByUserID(int requestedUserID, Connection connection) throws SQLException, ParseException {
        //creates database query
        PreparedStatement selectUserID;
        //selects all orders by id
        String query = "select * from `order` where User_ID= ?";
        selectUserID = connection.prepareStatement(query);
        selectUserID.setInt(1, requestedUserID);
        ResultSet resultSet = selectUserID.executeQuery();
        //crates list for orders
        ArrayList<Order> orders = new ArrayList<>();
        //inserts order objects into list
        while (resultSet.next()) {
            int orderID = resultSet.getInt("Order_ID");
            int userID = resultSet.getInt("User_ID");
            int roomID = resultSet.getInt("Room_ID");
            Timestamp timestampStart = resultSet.getTimestamp("Timestamp_start");
            Timestamp timestampEnd = resultSet.getTimestamp("Timestamp_end");
            Order order = new Order(orderID, userID, roomID, timestampStart, timestampEnd);
            orders.add(order);
        }
        //returns the list of order objects.
        return orders;
    }
}



