package Classes;

import java.sql.Timestamp;

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
    public Order(Timestamp timestampStart, Timestamp timestampEnd) {
        this.timestampStart = timestampStart;
        this.timestampEnd = timestampEnd;
    }

    /**
     * Constructor used for inserting an Order into the database, after availability has been checked.
     * @param id The id for an order.
     * @param userID The user that places the order.
     * @param roomID The room which a user wants to book.
     * @param timestampStart The date and time of the requested booking's beginning.
     * @param timestampEnd The date and time of the requested booking's end.
     */
    public Order(int id, int userID, int roomID, Timestamp timestampStart, Timestamp timestampEnd) {
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
