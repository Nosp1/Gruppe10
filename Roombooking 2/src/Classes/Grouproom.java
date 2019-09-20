package Classes;

/**
 * @author brisdalen
 */
public class Grouproom extends AbstractRoom {

    // lowMaxCapacity is the lowest value maxCapacity can be set to.
    private final int lowMaxCapacity = 2;
    // highMaxCapacity is the highest value maxCapacity can be set to.
    private final int highMaxCapacity = 12;

    /**
     *
     * @param roomId
     * @param roomFloor
     * @param maxCapacity
     */
    public Grouproom(String roomId, String roomFloor, int maxCapacity) {
        super(roomId, roomFloor, RoomType.GROUPROOM);
        setMaxCapacity(maxCapacity);
    }

    @Override
    public void setMaxCapacity(int maxCapacity) {
        // TODO: bestemme hva en øvre grense for grupperom skal være. 2 og 12 kan være midlertidig.
        if(maxCapacity > highMaxCapacity) {
            this.maxCapacity = highMaxCapacity;
        }

        if(maxCapacity < lowMaxCapacity) {
            this.maxCapacity = lowMaxCapacity;
        }
    }
}
