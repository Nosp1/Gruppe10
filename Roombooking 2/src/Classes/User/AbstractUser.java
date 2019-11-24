package Classes.User;

import Classes.Order;
import Classes.UserType;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

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
    protected int amount;


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
        this.orders = orderList;
    }

    public AbstractUser(int id, String firstName, String lastName, String userName, int amount) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.amount = amount;
    }

    public void showOrders() {
        //Viser orders
        for (Order o : orders) {
            System.out.println(o);
        }
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void showOrders(PrintWriter out) {
        // TODO: Endre til å vise rom navn, kanskje lage en display() i Order?
        //Viser orders
        int counter = 0;
        Collections.reverse(orders);
        for (Order o : orders) {
            out.println(
                    "<div class=\"container-reservation-order\">\n" +
                            "<form>\n" +
                            "<h3> Order number: " + (counter+1) + "</h3>" +
                            "<table>\n" +
                            "    <thead>\n" +
                            "        <tr>\n" +
                            "            <th colspan=\"2\">Room: " + o.getRoomName() + "</th>\n" +
                            "        </tr>\n" +
                            "    </thead>\n" +
                            "    <tbody>\n" +
                            "        <tr>\n" +
                            "            <td>From: " + o.getBookingStart() + "</td>\n" +
                                            // &nbsp betyr mellomrom
                            "            <td>&nbspTo: " + o.getBookingEnd() + "</td>" +
                            "        </tr>\n" +
                            "    </tbody>\n" +
                            "</table>\n" +
                            "</form>\n" +
                            "<div class=\"updateOrderButtonContainer\">" +
                            "<span class=\"updateOrderButton\">" +
                            "    <button class=\"btn btn-success btn-lg\" role=\"button\"\n" +
                            "            onclick=\"scrollToUpdate('" + o.getID() + "','" + o.getRoomName() + "','" + o.getBookingStartTime() + "','" + o.getRoomID() + "','" + (counter+1) + "')\">Update this reservation\n" +
                            "    </button>" +
                            "</span>" +
                            "<span class =\"cancelOrderButton\">" +
                            "   <button class=\"btn btn-success btn-lg\" role=\"button\"\n" +
                            "            onclick=\"cancelOrder(" + o.getID() + ")\">Cancel this reservation\n" +
                            "    </button>" +
                            "</span>" +
                            "</div>" +
                            "</div>");

            counter++;
        }
        Collections.reverse(orders);
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
