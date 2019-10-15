package Classes.Rooms;

import Classes.RoomType;

/**
 * @author brisdalen
 */
public abstract class AbstractRoom {

    protected int roomID;
    protected String roomName;
    protected String roomBuilding;
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
