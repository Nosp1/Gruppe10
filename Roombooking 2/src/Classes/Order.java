package Classes;

import Classes.Rooms.AbstractRoom;
import Tools.TimeUtility;

import java.sql.Timestamp;
import java.text.ParseException;

public class Order {

    private int id;
    private AbstractRoom room;
    private int userID;
    private Timestamp timestampStart;
    private Timestamp timestampEnd;

    /**
     * Constructor used for the intersects method
     * The format for the strings are
     * @param stringStart The date and time of the requested booking's beginning as a string.
     * @param stringEnd The date and time of the requested booking's end as a string.
     */
    public Order(String stringStart, String stringEnd) throws ParseException {
        this(getTimestampFromString(stringStart), getTimestampFromString(stringEnd));
    }

    public Order(Timestamp timestampStart, Timestamp timestampEnd) {
        this.timestampStart = timestampStart;
        this.timestampEnd = timestampEnd;
    }

    /**
     * Constructor used for returning an error if a user tries to book a busy room
     * @param room
     * @param stringStart
     * @param stringEnd
     * @throws ParseException
     */
    public Order(AbstractRoom room, String stringStart, String stringEnd) throws ParseException {
        this(getTimestampFromString(stringStart), getTimestampFromString(stringEnd));
        this.room = room;
    }

    /**
     * Constructor used for inserting an Order into the database, after availability has been checked.
     * @param id The id for an order.
     * @param userID The user that places the order.
     * @param room The room which a user wants to book.
     * @param timestampStart The date and time of the requested booking's beginning.
     * @param timestampEnd The date and time of the requested booking's end.
     */
    public Order(int id, int userID, AbstractRoom room, String timestampStart, String timestampEnd) throws ParseException {
        this.id = id;
        //System.out.println(id);
        this.userID = userID;
        //System.out.println(userID);
        this.room = room;
        //System.out.println(roomID);
        this.timestampStart = getTimestampFromString(timestampStart);
        //System.out.println(this.timestampStart.toString());
        this.timestampEnd = getTimestampFromString(timestampEnd);
        //System.out.println(this.timestampEnd.toString());
    }

    /**
     * Constructor used for inserting an Order into the database, after availability has been checked.
     * @param id The id for an order.
     * @param userID The user that places the order.
     * @param room The room which a user wants to book.
     * @param timestampStart The date and time of the requested booking's beginning.
     * @param timestampEnd The date and time of the requested booking's end.
     */
    public Order(int id, int userID, AbstractRoom room, Timestamp timestampStart, Timestamp timestampEnd) throws ParseException {
        this.id = id;
        this.userID = userID;
        this.room = room;
        this.timestampStart = timestampStart;
        this.timestampEnd = timestampEnd;
    }

    public Order(int orderID, String timestampStartTime, String timestampEndTime) throws ParseException {
        this.id = orderID;
        this.timestampStart = getTimestampFromString(timestampStartTime);
        this.timestampEnd = getTimestampFromString(timestampEndTime);


    }

    /**
     *
     * @param other The other Order object to check intersection with
     * @return true if this Order object intersects with "other"
     */
    public boolean intersects(Order other) {
        /* compareTo returnerer mindre enn 0 hvis tidspunktet er før other,
         * 0 hvis tidspunktene er like, og større enn 0 hvis tidspunktet
         * er etter other.
         */
        int comparison1 = timestampEnd.compareTo(other.timestampStart);
        int comparison2 = other.timestampEnd.compareTo(timestampStart);
        /* returnerer true hvis 2 timestamps overlapper
         * ergo, vi ønske false for å booke noe
         */
        return(comparison1 > 0 && comparison2 > 0);
    }

    public boolean intersects(Timestamp otherStart, Timestamp otherEnd) throws ParseException {
        // Samme som over, men med Timestamps og ikke et Order object
        Order temp = new Order(otherStart, otherEnd);
        return intersects(temp);
    }

    /**
     * A private utility method to convert a "yyyy-MM-dd'T'HH:mm" string to "yyyy-MM-dd HH:mm".
     * @param input A date and time String, parsed from a HTML datetime-local type.
     * @return A java.sql.Timestamp object with a date and time correctly formatted.
     * @throws ParseException
     */
    private static Timestamp getTimestampFromString(String input) throws ParseException {
        return TimeUtility.getTimestampFromString(input);
    }

    @Override
    public String toString() {
        return "Order_ID: " + String.valueOf(id) + "\n" +
                "User_ID: " + String.valueOf(userID) + "\n" +
                "Room_ID: " + String.valueOf(room.getRoomID()) + "\n" +
                "Timestamp_start: " + timestampStart.toString() + "\n" +
                "Timestamp_end: " + timestampEnd.toString();

    }

    public int getID() {
        return id;
    }

    public int getUserID() {
        return userID;
    }

    public int getRoomID() {
        return room.getRoomID();
    }

    public String getRoomName() {
        return room.getRoomName();
    }

    public Timestamp getTimestampStart() {
        return timestampStart;
    }

    public Timestamp getTimestampEnd() {
        return timestampEnd;
    }

    public String getBookingDate() {
        return timestampStart.toString().substring(0, 10);
    }

    public String getBookingStartTime() {
        return timestampStart.toString().substring(11, 16);
    }

    public String getBookingEndTime() {
        return timestampEnd.toString().substring(11, 16);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRoomID(int roomID) {
        this.room.setRoomID(roomID);
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
