package Tools;

import Classes.Order;
import Classes.Rooms.AbstractRoom;
import Classes.Rooms.Grouproom;
import Classes.User.AbstractUser;
import Classes.User.Admin;
import Classes.User.Student;
import Classes.User.Teacher;
import Passwords.PasswordHashAndCheck;
import Reports.Report;
import org.apache.commons.dbutils.DbUtils;

import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * handles the queries to and from the database.
 *
 * @author trym, brisdalen, sæthra & alena
 */
public class DbFunctionality {
    Statement statement;
    PasswordHashAndCheck passwordHashAndCheck;


    public DbFunctionality() {
        passwordHashAndCheck = new PasswordHashAndCheck();
    }

    //connection closes
    public void addUser(AbstractUser user, Connection conn) throws SQLException {
        PreparedStatement insertNewUser = null;
        PreparedStatement insertNewUserRegistry = null;

        try {
            String ins = "insert into `user` (User_firstName, User_lastName, User_email, User_dob, User_password, User_salt, User_Type_ID) values (?,?,?,?,?,?,?);";
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
            // store the salt in the database
            insertNewUser.setString(6, hashParts[0]);
            // Set User_type_ID and add to the prepared statement
            String userType = String.valueOf(user.getUserType());
            String insUserRegistry = "insert into user_type_registry (User_ID, User_type_ID) values (?,?);";
            insertNewUserRegistry = conn.prepareStatement(insUserRegistry);
            int userID = getNewUserID(conn);
            setUserType(userID, userType, insertNewUser, insertNewUserRegistry);
            insertNewUser.execute();
            insertNewUserRegistry.execute();

        } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        } finally {
            //closes the query and connection
            assert insertNewUser != null;
            insertNewUser.close();
            conn.close();
        }
    }

    private void setUserType(int userID, String userType, PreparedStatement insertNewUser, PreparedStatement insertNewUserRegistry) throws SQLException {
        switch (userType) {
            case "TEACHER":
                insertNewUser.setInt(7, 2);
                insertNewUserRegistry.setInt(1, userID);
                insertNewUserRegistry.setInt(2, 2);
                break;

            case "ADMIN":
                insertNewUser.setInt(7, 3);
                insertNewUserRegistry.setInt(1, userID);
                insertNewUserRegistry.setInt(2, 3);
                break;

            default:
                // Otherwise STUDENT
                insertNewUser.setInt(7, 1);
                insertNewUserRegistry.setInt(1, userID);
                insertNewUserRegistry.setInt(2, 1);
                break;
        }
    }

    /**
     * Connection closes in Servlet not here because of connection object being used by multiple queries.
     *
     * @param username   The user's attempted login email
     * @param password   The user's attempted password
     * @param connection The Connection object to a given database
     * @return true if the input password matches the stored password for a given email
     * @throws SQLException
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public boolean checkUser(String username, String password, Connection connection) throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException {
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        try {
            String query = "select * from user where User_email = ?";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            // Check if the attempted email matches any emails in the database
            resultSet = stmt.executeQuery();
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
        } finally {
            assert resultSet != null;
            try {
                resultSet.close();
                if (resultSet.isClosed()) {
                    System.out.println("resultset closed");
                } else {
                    System.out.println("resultset not closed");
                }
                stmt.close();
                if (stmt.isClosed()) {
                    System.out.println("statement is closed");
                } else {
                    System.out.println("statement is not closed");
                }
                DbUtils.closeQuietly(stmt);
                DbUtils.closeQuietly(resultSet);
                if (stmt.isClosed()) {
                    System.out.println("statement is closed");

                } else {
                    System.out.println("stmt is not closed");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // Return false if there is no result set(?)
        return false;
    }

    public AbstractUser getUser(String requestedUserEmail, Connection connection) throws SQLException {
        PreparedStatement selectUser = null;
        ResultSet resultSet = null;
        try {
            //select the User from the User table with the corresponding User_ID
            String select = "select * from `user` where User_email = ?";
            selectUser = connection.prepareStatement(select);
            selectUser.setString(1, requestedUserEmail);
            resultSet = selectUser.executeQuery();
            resultSet.first();
            String firstName = resultSet.getString("User_firstName");
            String lastName = resultSet.getString("User_lastName");
            String userName = resultSet.getString("User_email");
            String dob = resultSet.getNString("User_dob");
            String password = resultSet.getString("User_password");
            int userTypeID = resultSet.getInt("User_type_ID");

            //3 = admin
            if (userTypeID == 3) {
                return new Admin(firstName, lastName, userName, password, dob);
                //2 = teacher
            } else if (userTypeID == 2) {
                return new Teacher(firstName, lastName, userName, password, dob);
                // Ellers student
            } else {
                return new Student(firstName, lastName, userName, password, dob);
            }

        } finally {
            assert selectUser != null;
            selectUser.closeOnCompletion();
            assert resultSet != null;
            resultSet.close();

        }

    }

    /**
     * @param userEmail  the users Email address
     * @param connection connection to db
     * @return userID as int.
     * @throws SQLException
     */
    public int getUserId(String userEmail, Connection connection) throws SQLException {
        PreparedStatement getUser = null;
        ResultSet resultSet = null;
        try {
            String query = "Select User_ID from `user` where User_Email = (?)";
            getUser = connection.prepareStatement(query);
            getUser.setString(1, userEmail);
            resultSet = getUser.executeQuery();
            resultSet.first();
            int result = Integer.parseInt(resultSet.getString(1));
            return result;
        } finally {
            assert getUser != null;
            getUser.close();
            assert resultSet != null;
            resultSet.close();
        }
    }

    public int getNewUserID(Connection connection) throws SQLException {
        String select = "select MAX(User_ID) as User_ID from `user`;";
        Statement stmt = connection.createStatement();
        ResultSet result = stmt.executeQuery(select);

        while (result.next()) {
            return result.getInt("User_ID") + 1;
        }

        return -1;
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
        PreparedStatement insertNewRoom = null;
        try {
            String ins = "insert into Rooms (Room_ID, Room_name, Room_building, Room_maxCapacity, Tavle, Projektor) values (?,?,?,?,?,?)";
            insertNewRoom = connection.prepareStatement(ins);
            insertNewRoom.setInt(1, room.getRoomID());
            insertNewRoom.setString(2, room.getRoomName());
            insertNewRoom.setString(3, room.getRoomBuilding());
            insertNewRoom.setString(4, String.valueOf(room.getMaxCapacity()));

            insertNewRoom.setString(5, String.valueOf(room.getRoomType()));
            boolean tavle = room.hasTavle();
            if (tavle) {
                insertNewRoom.setString(5, "JA");
            } else {
                insertNewRoom.setString(5, "NEI");
            }

            boolean prosjektor = room.hasProjektor();
            if (prosjektor) {
                insertNewRoom.setString(6, "JA");
            } else {
                insertNewRoom.setString(6, "NEI");
            }

            insertNewRoom.execute();
        } finally {
            assert insertNewRoom != null;
            insertNewRoom.closeOnCompletion();
            connection.close();
        }
    }

    public boolean deleteRoom(int roomID, Connection connection) throws SQLException {
        // Delete orders associated with the room first, so there is no foreign key dependency.
        PreparedStatement stmt = null;
        PreparedStatement deleteRoom = null;
        try {
            String deleteOrder = "delete from `order` where Room_ID = ?";
            stmt = connection.prepareStatement(deleteOrder);
            stmt.setInt(1, roomID);
            stmt.executeUpdate();


            String delete = "delete from Rooms where Room_ID = ?";
            deleteRoom = connection.prepareStatement(delete);
            deleteRoom.setInt(1, roomID);
            int result = deleteRoom.executeUpdate();
            // result er 1 hvis noe blir slettet, eller 0 hvis ingenting ble affected
            return result == 1;
        } finally {
            assert stmt != null;
            stmt.closeOnCompletion();
            assert deleteRoom != null;
            deleteRoom.closeOnCompletion();
        }
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
            out.print(" Projektor: " + resultSet.getString("Prosjektor") + "</p>");
            out.println("</div>");
        }
    }

    public AbstractRoom getRoom(int requestedRoomID, Connection connection) throws SQLException {
        System.out.println("getRoom started. requestedRoomID: " + requestedRoomID);
        PreparedStatement selectOrder;
        // Select the Order from the Order table with the corresponding Order_ID
        String select = "select * from rooms where Room_ID = ?";
        selectOrder = connection.prepareStatement(select);
        selectOrder.setInt(1, requestedRoomID);
        // and store it in a ResultSet.
        ResultSet resultSet = selectOrder.executeQuery();

        // The resultSet's pointer starts at "nothing", so move it to the next (first, and only) element.
        resultSet.first();
        // Get and store the data in local variables,
        int roomID = resultSet.getInt("Room_ID");
        //System.out.println("getOrder orderID: " + orderID);
        String roomName = resultSet.getString("Room_name");
        //System.out.println("getOrder userID: " + userID);
        String roomBuilding = resultSet.getString("Room_building");
        //System.out.println("getOrder roomID: " + roomID);
        int roomMaxCapacity = resultSet.getInt("Room_maxCapacity");

        String tavle = resultSet.getString("Tavle");
        String projektor = resultSet.getString("Projektor");

        boolean hasTavle;
        switch (tavle) {

            case "JA":
                hasTavle = true;
                break;

            default:
                hasTavle = false;
                break;
        }

        boolean hasProjektor;
        switch (projektor) {

            case "JA":
                hasProjektor = true;
                break;

            default:
                hasProjektor = false;
                break;
        }

        return new Grouproom(roomID, roomName, roomBuilding, roomMaxCapacity, hasTavle, hasProjektor);
    }

    public void addOrder(Order order, Connection connection) throws SQLException {
        PreparedStatement insertNewOrder = null;
        System.out.println("addOrder started");
        try {
            String ins = "insert into `order` (User_ID, Room_ID, Timestamp_start, Timestamp_end) VALUES (?,?,?,?)";
            insertNewOrder = connection.prepareStatement(ins);
            insertNewOrder.setInt(1, order.getUserID());
            insertNewOrder.setInt(2, order.getRoomID());
            insertNewOrder.setTimestamp(3, order.getTimestampStart());
            insertNewOrder.setTimestamp(4, order.getTimestampEnd());
            insertNewOrder.execute();
            // TODO ADD RECIEPT METHOD
        } finally {
            assert insertNewOrder != null;
            insertNewOrder.closeOnCompletion();
        }
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

        return returnOrder(resultSet, connection);
    }

    public Order getSpecificOrder(int requestedRoomID, Timestamp requestedTimestampStart, Timestamp requestedTimestampEnd, Connection connection) throws SQLException, ParseException {
        System.out.println("getSpecificOrder started with: " + requestedRoomID + " " + requestedTimestampStart + " " + requestedTimestampEnd);
        PreparedStatement selectOrder;
        String select = "select * from `order` where Room_ID = ? AND Timestamp_start = ? AND Timestamp_end = ?;";

        selectOrder = connection.prepareStatement(select);
        selectOrder.setInt(1, requestedRoomID);
        selectOrder.setTimestamp(2, requestedTimestampStart);
        selectOrder.setTimestamp(3, requestedTimestampEnd);

        ResultSet resultSet = selectOrder.executeQuery();

        Order order = returnOrder(resultSet, connection);
        System.out.println("Order returned: " + order.toString());
        return returnOrder(resultSet, connection);
    }

    private Order returnOrder(ResultSet resultSet, Connection connection) throws SQLException, ParseException {
        // The resultSet's pointer starts at "nothing", so move it to the next (first, and only) element.
        resultSet.first();
        // Get and store the data in local variables,
        int orderID = resultSet.getInt("Order_ID");
        //System.out.println("getOrder orderID: " + orderID);
        int userID = resultSet.getInt("User_ID");
        //System.out.println("getOrder userID: " + userID);
        int roomID = resultSet.getInt("Room_ID");
        //System.out.println("getOrder roomID: " + roomID);
        Timestamp timestampStart = resultSet.getTimestamp("Timestamp_start");
        Timestamp timestampEnd = resultSet.getTimestamp("Timestamp_end");

        AbstractRoom room = getRoom(roomID, connection);

        return new Order(orderID, userID, room, timestampStart, timestampEnd);
    }

    public ResultSet getAllOrdersFromRoom(int roomID, Connection connection) throws SQLException {
        PreparedStatement selectOrders = null;
        try {
            String select = "select Order_ID, Timestamp_start, Timestamp_end from `order`\n" +
                    "  inner join rooms\n" +
                    "  on `order`.Room_ID = rooms.Room_ID\n" +
                    "  where `order`.Room_ID = ?";
            selectOrders = connection.prepareStatement(select);
            selectOrders.setInt(1, roomID);
            return selectOrders.executeQuery();
        } finally {
            assert selectOrders != null;
            selectOrders.close();
        }
    }

    //closes connection
    public ResultSet getOrdersFromRoom(int roomID, String date, Connection connection) throws SQLException, ParseException {

        // TODO: date burde kunne ta inn et timestamp, og strings formatert som "yyyy-mm-dd hh:ss" og "yyyy-mm-dd"
        System.out.println("getOrdersFromRoom started");
        System.out.println("Room_ID recieved: " + roomID);
        System.out.println("Date as String recieved: " + date);
        PreparedStatement selectOrders = null;
        try {
            String select = "select Order_ID, Timestamp_start, Timestamp_end from `order`\n" +
                    "  inner join rooms\n" +
                    "  on `order`.Room_ID = rooms.Room_ID\n" +
                    "  where DATE(`order`.Timestamp_start) like ?\n" +
                    "  and `order`.Room_ID = ?";
            selectOrders = connection.prepareStatement(select);
            selectOrders.setString(1, date);
            selectOrders.setInt(2, roomID);
            return selectOrders.executeQuery();
        } finally {
            assert selectOrders != null;
            selectOrders.closeOnCompletion();

        }

    }


    /**
     * @param orderID    The ID of the Order you want to delete
     * @param connection The Connection object with the connection to the database
     * @return true if something was deleted, false if nothing was affected
     * @throws SQLException
     */
    public boolean deleteOrder(int orderID, Connection connection) throws SQLException {
        PreparedStatement deleteOrder = null;
        try {
            String delete = "delete from `Order` where Order_ID = ?";
            deleteOrder = connection.prepareStatement(delete);
            deleteOrder.setInt(1, orderID);
            int result = deleteOrder.executeUpdate();
            return result == 1;
        } finally {
            assert deleteOrder != null;
            deleteOrder.close();
        }
    }

    //TODO closing works for this.
    public int getRoomID(Connection connection) throws SQLException {
        PreparedStatement selectRoomID = null;
        ResultSet resultSet = null;
        try {
            String select = "select Room_ID from rooms order by Room_ID desc limit 1";
            selectRoomID = connection.prepareStatement(select);
            resultSet = selectRoomID.executeQuery();

            while (resultSet.next()) {
                System.out.println("roomID from method: " + (resultSet.getInt("Room_ID") + 1));
                return resultSet.getInt("Room_ID") + 1;

            }
        } finally {
            //closes connection.
            assert selectRoomID != null;
            assert resultSet != null;
            resultSet.close();
            selectRoomID.closeOnCompletion();

        }

        return 1;
    }

    public int getOrderID(Connection connection) throws SQLException {
        System.out.println("getOrderID started");
        PreparedStatement selectOrderID;
        String select = "select Order_ID from `order` order by Order_ID desc limit 1";
        selectOrderID = connection.prepareStatement(select);
        ResultSet resultSet = selectOrderID.executeQuery();

        while (resultSet.next()) {
            System.out.println("orderID returned: " + (resultSet.getInt("Order_ID") + 1));
            return resultSet.getInt("Order_ID") + 1;
        }

        System.out.println("No orders. orderID returned: 1");
        return 1;
    }

    // TODO: Dokumentere metodene under
    public ArrayList<AbstractRoom> getMostBookedRoom(Connection connection) throws SQLException {
        ResultSet mostBooked = null;
        try {
            mostBooked = getMostBookedRoom(5, connection);
            ArrayList<AbstractRoom> rooms = new ArrayList<>();
            int counter = 0;
            int limit = 5;
            while (mostBooked.next()) {
                int roomID = mostBooked.getInt(1);
                int amount = mostBooked.getInt(2);
                rooms.add(new Grouproom(roomID, amount));
            }
            if(rooms.size() < limit) {
                return rooms;
            } else {
                ArrayList<AbstractRoom> returnArray = new ArrayList<>();
                for(int i = 0; i < 5; i++) {
                    returnArray.add(rooms.get(i));
                }

                return returnArray;
            }
        } finally {
            assert mostBooked != null;
            if (mostBooked.isClosed()) {
                System.out.println(mostBooked + "is closed");
            } else {
                mostBooked.close();
            }
        }
    }

    public ResultSet getMostBookedRoom(int howMany, Connection connection) throws SQLException {
        PreparedStatement selectBookedRoom = null;
        try {
            //TODO: teste metoden og implementere i en Servlet
            String select = "SELECT room_id, COUNT(*) as amount FROM `order` GROUP BY room_id ORDER BY amount DESC LIMIT ?";
            selectBookedRoom = connection.prepareStatement(select);
            selectBookedRoom.setInt(1, howMany);

            return selectBookedRoom.executeQuery();
        } finally {
            assert selectBookedRoom != null;
            selectBookedRoom.closeOnCompletion();
        }
    }

    public AbstractUser[] getMostActiveUsers(Connection connection) throws SQLException {
        ResultSet mostActive = null;
        try {
            mostActive = getMostActiveUsers(5, connection);
            AbstractUser[] users = new AbstractUser[5];
            int counter = 0;
            while (mostActive.next()) {
                int userId = mostActive.getInt(1);
                String firstName = mostActive.getString(2);
                String lastName = mostActive.getString(3);
                String userName = mostActive.getString(4);
                int amount = mostActive.getInt(5);
                users[counter++] = new Student(userId, firstName, lastName, userName, amount);
            }
            return users;
        } finally {
            assert mostActive != null;
            if (mostActive.isClosed()) {
                System.out.println(mostActive + "is closed");
            } else {
                mostActive.close();
            }
        }
    }

    public void updateOrderInformation(Order order, Connection connection) throws SQLException {
        System.out.println("update order information started");
        PreparedStatement updateOrderPS;

        String updateOrder = "UPDATE `order` "
                + "set Timestamp_start = ?, Timestamp_end = ?"
                + "WHERE Order_ID = ?";

        updateOrderPS = connection.prepareStatement(updateOrder);
        updateOrderPS.setTimestamp(1, order.getTimestampStart());
        updateOrderPS.setTimestamp(2, order.getTimestampEnd());
        updateOrderPS.setInt(3, order.getID());
        updateOrderPS.execute();

    }

    public void updateOrderOwner(int orderID, int userID, Connection connection) throws SQLException {
        System.out.println("updateOrderOwner started with: " + orderID + " and " + userID);
        PreparedStatement updateOrderOwner;

        String update = "UPDATE `order` set User_ID = ? WHERE Order_ID = ?;";

        updateOrderOwner = connection.prepareStatement(update);
        updateOrderOwner.setInt(1, userID);
        updateOrderOwner.setInt(2, orderID);
        updateOrderOwner.executeUpdate();
    }

    public ResultSet getMostActiveUsers(int howMany, Connection connection) throws SQLException {
        PreparedStatement selectActiveUsers;
        //TODO: teste metoden og implementere i en Servlet
        String select = "SELECT user.user_id, user.User_firstName, user.User_lastName,user.User_email, COUNT(*) as amount FROM `order` LEFT JOIN user ON user.User_ID = `order`.User_ID GROUP BY user_id ORDER BY amount DESC LIMIT ?";
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

            AbstractRoom room = getRoom(roomID, connection);

            Order order = new Order(orderID, userID, room, timestampStart, timestampEnd);
            orders.add(order);
        }
        //returns the list of order objects.
        return orders;
    }

    public boolean checkRoom(int roomID, Connection connection) throws SQLException {
        PreparedStatement stmt;
        String query = "select * from rooms where Room_ID = ?";
        stmt = connection.prepareStatement(query);
        // stmt.setString(1, Integer.toString(roomID));
        stmt.setInt(1, roomID);

        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            return true;
        }
        return false;
    }

    public void searchOrders(int roomId, String date, PrintWriter out, Connection connection) throws SQLException {
        System.out.println("[DbFunc]searchOrders started");
        String strSelect = null;
        if (roomId < 0) {
            strSelect = "SELECT * FROM `order` WHERE DATE_FORMAT(Timestamp_start, '%Y-%m-%d') = '" + date + "'";
        } else {
            strSelect = "SELECT * FROM `order` WHERE DATE_FORMAT(Timestamp_start, '%Y-%m-%d') = '" + date + "' AND Room_ID=" + Integer.toString(roomId);
        }
        PreparedStatement statement = connection.prepareStatement(strSelect);
        ResultSet resultSet = statement.executeQuery(strSelect);
        ArrayList<String> roomNames = new ArrayList<>();
        // Opening JSON bracket
        String json = "[";
        //out.print("[");
        int i = 0;
        while (resultSet.next()) {
            if (i > 0) {
                //out.print(",");
                json = json + ",";
            }
            //out.print("{\"roomId\":" + resultSet.getInt("Room_ID") + ",\"start\": \"" + resultSet.getTimestamp("Timestamp_start") + "\", \"end\": \"" + resultSet.getTimestamp("Timestamp_end") + "\"}");
            json = json + "{\"roomId\":" + resultSet.getInt("Room_ID") + ",\"start\": \"" + resultSet.getTimestamp("Timestamp_start") + "\", \"end\": \"" + resultSet.getTimestamp("Timestamp_end") + "\"}";
            i++;
        }
        if (roomId < 0) {
            strSelect = "SELECT * FROM `rooms`";
            statement = connection.prepareStatement(strSelect);
            resultSet = statement.executeQuery(strSelect);
            String roomNumbers = "";
            int j = 0;
            while (resultSet.next()) {
                System.out.println("[DbFunc] while-loop started");
                if (j > 0) {
                    roomNumbers = roomNumbers + ",";
                }
                j++;
                roomNumbers = roomNumbers + String.valueOf(resultSet.getInt("Room_ID"));
                roomNames.add(resultSet.getString("Room_name"));
            }
            if (i == 0) {
                System.out.println("[DbFunc]i == 0");
                //out.print("[" + roomNumbers + "]");
                json = json + "[" + roomNumbers + "]";
            } else {
                System.out.println("[DbFunc] i != 0");
                //out.print(",[" + roomNumbers + "]");
                json = json + ",[" + roomNumbers + "]";
            }
        }
        // Create a list of room names in the JSON object, if more than one room is requested
        if(roomId < 0) {
            //out.print(",[");
            json = json + ",[";
            int limit = roomNames.size();
            System.out.println("[DbFunc] limit: " + limit);
            for (int k = 0; k < limit; k++) {
                if (k != limit - 1) {
                    System.out.println("[DbFunc]if line 740");
                    //out.print("\"" + roomNames.get(k) + "\",");
                    json = json + "\"" + roomNames.get(k) + "\",";
                } else {
                    System.out.println("[DbFunc]else line 744");
                    //out.print("\"" + roomNames.get(k) + "\"");
                    json = json + "\"" + roomNames.get(k) + "\"";
                }
            }
            //out.print("]");
            json = json + "]";
        }

        // Closing JSON bracket
        //out.print("]");
        json = json + ("]");
        System.out.println(json);
        out.print(json);
    }

    public void insertReport(Report userReport, Connection connection) throws SQLException {
        String strInsert = "Insert into UserReport( Report_Response, User_ID, Room_ID) Values (?,?,?)";
        PreparedStatement statement = connection.prepareStatement(strInsert);
        statement.setString(1, userReport.getReportResponse());
        statement.setInt(2, userReport.getUserID());
        statement.setInt(3, userReport.getRoomID());

        statement.execute();
    }

    public void printReport(PrintWriter out, Connection connection) throws SQLException {
        String strSelect = "Select*From UserReport";
        PreparedStatement statement = connection.prepareStatement(strSelect);
        ResultSet resultSet = statement.executeQuery(strSelect);
        while (resultSet.next()) {
            out.print("<div class=\"room-card\" id=\"room-cards\">");
            out.print("<p>" + resultSet.getString("Report_ID") + " : " + resultSet.getString("Report_Response"));
            out.print(" Room: " + resultSet.getString("Room_ID") + "</p>");
            out.println("</div>");
        }
    }

}

