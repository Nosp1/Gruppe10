package Classes;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Order {

    private int id;
    private int roomID;
    private int userID;
    private Timestamp timestampStart;
    private Timestamp timestampEnd;

    /**
     * Constructor used for booking rooms, while checking if something is vacant.
     * @param timestampStart The date and time of the requested booking's beginning.
     * @param timestampEnd The date and time of the requested booking's end.
     */
    public Order(String timestampStart, String timestampEnd) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy HH:mm");
        Date start = sdf.parse(timestampStart);
        Date end = sdf.parse(timestampEnd);
        this.timestampStart = new Timestamp(start.getTime());
        this.timestampEnd = new Timestamp(end.getTime());
    }

    /**
     * Constructor used for inserting an Order into the database, after availability has been checked.
     * @param id The id for an order.
     * @param userID The user that places the order.
     * @param roomID The room which a user wants to book.
     * @param timestampStart The date and time of the requested booking's beginning.
     * @param timestampEnd The date and time of the requested booking's end.
     */
    public Order(int id, int userID, int roomID, String timestampStart, String timestampEnd) throws ParseException {
        this.id = id;
        this.userID = userID;
        this.roomID = roomID;
        this.timestampStart = getTimestampFromString(timestampStart);
        this.timestampEnd = getTimestampFromString(timestampEnd);
    }

    /**
     * Constructor used for inserting an Order into the database, after availability has been checked.
     * @param id The id for an order.
     * @param userID The user that places the order.
     * @param roomID The room which a user wants to book.
     * @param timestampStart The date and time of the requested booking's beginning.
     * @param timestampEnd The date and time of the requested booking's end.
     */
    public Order(int id, int userID, int roomID, Timestamp timestampStart, Timestamp timestampEnd) throws ParseException {
        this.id = id;
        this.userID = userID;
        this.roomID = roomID;
        this.timestampStart = timestampStart;
        this.timestampEnd = timestampEnd;
    }

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
        return(comparison1 >= 0 && comparison2 >= 0);
    }

    private Timestamp getTimestampFromString(String input) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyyTHH:mm");
        Date output = sdf.parse(input);
        return new Timestamp(output.getTime());
    }

    @Override
    public String toString() {
        return "Order_ID: " + String.valueOf(id) + "\n" +
                "User_ID: " + String.valueOf(userID) + "\n" +
                "Room_ID: " + String.valueOf(roomID) + "\n" +
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
        return roomID;
    }

    public Timestamp getTimestampStart() {
        return timestampStart;
    }

    public Timestamp getTimestampEnd() {
        return timestampEnd;
    }
}
