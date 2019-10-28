package Classes.Rooms;

import Classes.RoomType;

/**
 * @author
 */
public class Auditorium extends AbstractRoom {

    // lowMaxCapacity is the lowest value maxCapacity can be set to.
    private final int lowMaxCapacity = 2;
    // highMaxCapacity is the highest value maxCapacity can be set to.
    private final int highMaxCapacity = 100;

    /**
     * @param roomName name of room example B3-200
     * @param roomBuilding name of building example: B
     * @param maxCapacity max capacity in the room
     */
    public Auditorium(int roomID, String roomName, String roomBuilding, int maxCapacity, boolean tavle, boolean prosjektor) {
        super(roomID, roomName, roomBuilding, RoomType.AUDITORIUM, tavle, prosjektor);
        setMaxCapacity(maxCapacity);
    }

    @Override
    public void setMaxCapacity(int maxCapacity) {
        // TODO: bestemme hva en øvre grense for grupperom skal være. 2 og 100 kan være midlertidig.
        if(maxCapacity > highMaxCapacity) {
            this.maxCapacity = highMaxCapacity;

        } else if(maxCapacity < lowMaxCapacity) {
            this.maxCapacity = lowMaxCapacity;

        } else {
            this.maxCapacity = maxCapacity;
        }
    }
}