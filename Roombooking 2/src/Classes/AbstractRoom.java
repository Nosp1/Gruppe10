package Classes;

public abstract class AbstractRoom {

    protected String roomId;
    protected String roomFloor;
    protected RoomType roomType;
    protected int maxCapacity;

    public AbstractRoom(String roomId, String roomFloor, RoomType roomType) {
        this.roomId = roomId;
        this.roomFloor = roomFloor;
        this.roomType = roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomFloor() {
        return roomFloor;
    }

    public void setRoomFloor(String roomFloor) {
        this.roomFloor = roomFloor;
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
