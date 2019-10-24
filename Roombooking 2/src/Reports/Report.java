package Reports;

public class Report {
    private int reportID;
    private String reportResponse;
    private int userID;
    private int roomID;

    public Report(int reportID, String reportResponse, int userID, int roomID) {
        this.reportID = reportID;
        this.reportResponse = reportResponse;
        this.userID = userID;
        this.roomID = roomID;
    }

    public int getReportID() {
        return reportID;
    }

    public void setReportID(int reportID) {
        this.reportID = reportID;
    }

    public String getReportResponse() {
        return reportResponse;
    }

    public void setReportResponse(String reportResponse) {
        this.reportResponse = reportResponse;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }
}
