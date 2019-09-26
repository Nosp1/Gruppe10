package Classes.Rooms;

import Classes.RoomType;

/**
 * @author brisdalen
 */
public class Grouproom extends AbstractRoom {

    // lowMaxCapacity is the lowest value maxCapacity can be set to.
    private final int lowMaxCapacity = 2;
    // highMaxCapacity is the highest value maxCapacity can be set to.
    private final int highMaxCapacity = 12;

    /**
     * @param roomName name of room example B3-200
     * @param roomBuilding name of building example: B
     * @param maxCapacity max capacity in the room
     */
    public Grouproom(int roomID, String roomName, String roomBuilding, int maxCapacity) {
        super(roomID, roomName, roomBuilding, RoomType.GROUPROOM);
        setMaxCapacity(maxCapacity);
    }

    @Override
    public void setMaxCapacity(int maxCapacity) {
        // TODO: bestemme hva en øvre grense for grupperom skal være. 2 og 12 kan være midlertidig.
        if(maxCapacity > highMaxCapacity) {
            this.maxCapacity = highMaxCapacity;

        } else if(maxCapacity < lowMaxCapacity) {
            this.maxCapacity = lowMaxCapacity;

        } else {
            this.maxCapacity = maxCapacity;
        }
    }
}
