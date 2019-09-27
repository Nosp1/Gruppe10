package Classes;

/**
 * @author brisdalen
 */
public abstract class AbstractRoom {

    protected int roomID; //TODO: Endre alle metoder som bruker gammel constructor
    protected String roomName;
    protected String roomBuilding; //TODO: Trenger vi dette feltet?
    protected RoomType roomType;
    protected int maxCapacity;

    public AbstractRoom(int roomID, String roomName, String roomBuilding, RoomType roomType) {
        this.roomID = roomID;
        this.roomName = roomName;
        this.roomBuilding = roomBuilding;
        this.roomType = roomType;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomBuilding() {
        return roomBuilding;
    }

    public void setRoomBuilding(String roomBuilding) {
        this.roomBuilding = roomBuilding;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public abstract void setMaxCapacity(int maxCapacity);
}
