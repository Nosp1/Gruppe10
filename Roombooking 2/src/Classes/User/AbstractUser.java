package Classes.User;

import Classes.Order;
import Classes.UserType;

import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 */
public abstract class AbstractUser {
    /*
    fields
     */
    protected String firstName;
    protected String lastName;
    protected String userName;
    protected String dob;
    protected String password;
    protected UserType userType;
    protected ArrayList<Order> orders;


    /*
    Constructor
     */
    public AbstractUser(String firstName, String lastName, String userName, String dob, String password, UserType userType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.dob = dob;
        this.password = password;
        this.userType = userType;

        orders = new ArrayList<>();
    }

    public AbstractUser(int userID, ArrayList<Order> orderList) {
        this.userName = userName;
        this.orders = orderList;
    }

    public void showOrders() {
        //Viser orders
        for (Order o : orders) {
            System.out.println(o);
        }
    }

    public void showOrders(PrintWriter out) {
        //Viser orders
        for (Order o : orders) {
            out.println(
                    "<div class=\"container\">\n" +
                            "<form>\n" +
                            "<table>\n" +
                            "        <tr>\n" +
                            "            <td> \n " + " RoomID " + " " + o.getRoomID() + "</td>" + " " + " \n" +
                            "            <td> \n " + " " + " From " + " " + o.getTimestampStart() + "  " + "</td>\n" +
                                        "<td> \n" + " " + " To " + " " + o.getTimestampEnd() + " " + "</td>\n" +
                            "        </tr>\n" +
                            "</table>" +
                            "</div>" +
                            "</form>");

        }
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + "\n" +
                userName + "\n" +
                dob + "\n" +
                userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
