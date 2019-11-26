package Servlets;


import Classes.User.AbstractUser;
import Classes.User.Student;
import Tools.DbFunctionality;
import Tools.DbTool;
import org.apache.commons.dbutils.DbUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * Handles My Reservations from profile.html and parses String parameters to {@code DbFunctionality} to check for
 * {@code String} is existing in Database.
 *
 * @author hanne, henriette, hedda, trym
 * @see Servlets.AbstractServlet
 * @see javax.servlet.http.HttpServlet
 * @see javax.servlet.GenericServlet
 * @see DbFunctionality
 * @see DbTool
 */

@WebServlet(name = "Servlets.ServletReservations", urlPatterns = {"/Servlets.ServletReservations"})
public class ServletReservations extends AbstractServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        Connection connection = null;
        try (PrintWriter out = response.getWriter()) {

            //prints start of html tags.
            printLoggedInNav(out);
            //gets the username -> email in this case.
            HttpSession session = request.getSession();
            String userName = (String) session.getAttribute("userEmail");
            String action = request.getParameter("action").toLowerCase();

            if (action.contains("myreservations")) {
                DbTool dbTool = new DbTool();
                //Establishes connection to database
                connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();

                printUpdateOrderPanel(out);
                addHiddenCalendar(out);

                int userID = dbFunctionality.getUserId(userName, connection);
                AbstractUser user = new Student(userID, dbFunctionality.getOrderListByUserID(userID, connection));

                out.println("<h2>Here are your reservations:</h2>");
                user.showOrders(out);
                addBootStrapFunctionality(out);
                out.println("<script src=\"update-order-script.js\"></script>");
            }


            if(action.contains("cancel")) {
                System.out.println("[ServletReservations]Cancel started");
                int orderID = Integer.parseInt(request.getParameter("orderID"));
                System.out.println("[ServletReservations]orderID received: " + orderID);

                DbTool dbTool = new DbTool();
                //Establishes connection to database
                connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();

                dbFunctionality.deleteOrder(orderID, connection);
                System.out.println("[ServletReservations]order deleted.");
            }

            else {
                //adds a return button if the login fails.
                addProfileButton(out);
            }

            //prints script to establish connection between bootstrap and html
            //addBootStrapFunctionality(out);
            out.print("<script src=\"date-time-update.js\"></script>");
            out.print("</body>");
            out.print("</html>");
            //prints errors: if the database fails, if the password is wrong.
        } catch (SQLException | IOException | ParseException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(connection);
            assert connection != null;
            closeConnection(connection);
        }
    }

    private void printUpdateOrderPanel(PrintWriter out) {
        out.print("<div class=\"collapse container\" id=\"collapseUpdateBooking\">\n" +
                "    <form action=\"./Servlets.ServletRoomBooking\" method=\"post\">\n" +
                "        <h1>Update an order</h1>\n" +
                "        <div>\n" +
                "            <label style=\"display: none\" for=\"Update_orderID\">Order ID</label>\n" +
                "            <input type=\"hidden\" id=\"Update_orderID\" name=\"Update_orderID\">\n" +
                "        </div>\n" +
                "\n" +
                "        <div>\n" +
                "            <span style=\"display:inline\" id=\"Update_OrderNumAndName\">" +
                "               <h3 style=\"display:inline\">Order number</h3>" +
                "               <h3 style=\"display:inline\" id=\"Update_orderNumber\"></h3>" +
                "               <h3 style=\"display:inline\">&nbsp:&nbsp</h3>" +
                "               <h3 style=\"display:inline\" id=\"Update_roomName\">No Order chosen</h3>" +
                "            </span>" +
                "        </div>\n" +
                "\n" +
                "        <div>\n" +
                "            <label for=\"Update_Timestamp_start_date\" for=\"Update_Timestamp_start_time\">Update the booking start</label>\n" +
                "            <input type=\"date\" id=\"Update_Timestamp_start_date\" name=\"Update_Timestamp_start_date\"\n" +
                "                   onfocusout=\"update_updateEndDate()\"/>\n" +
                "            <span class=\"validateUpdateTime\">\n" +
                "                <input type=\"time\" id=\"Update_Timestamp_start_time\" class=\"UpdateTime\" name=\"Update_Timestamp_start_time\"\n" +
                "                       min=\"08:00\" max=\"22:00\" onfocusout=\"update_checkInputStart(event)\"/>\n" +
                "            </span>\n" +
                "        </div>\n" +
                "\n" +
                "        <div>\n" +
                "            <label for=\"Update_Timestamp_end_date\" for=\"Update_Timestamp_end_time\">Update the booking end</label>\n" +
                "            <input type=\"date\" id=\"Update_Timestamp_end_date\" name=\"Update_Timestamp_end_date\" onfocusout=\"update_updateEndDate()\"\n" +
                "                   readonly=\"readonly\"/>\n" +
                "            <span class=\"validateUpdateTime\">\n" +
                "                <input type=\"time\" id=\"Update_Timestamp_end_time\" class=\"UpdateTime\" name=\"Update_Timestamp_end_time\"\n" +
                "                       min=\"08:00\" max=\"22:00\" onfocusout=\"update_checkInputEnd(event)\"/>\n" +
                "            </span>\n" +
                "        </div>\n" +
                "\n" +
                "        <div>\n" +
                "            <input class=\"submit btn-default btn-lg\" type=\"submit\" name=\"action\" value=\"Update room\">\n" +
                "        </div>\n" +
                "\n" +
                "        <div class=\"timeLimitMessage\" style=\"display: none;\">\n" +
                "            <p> The time must be between 08:00 and 21:59 </p>\n" +
                "        </div>\n" +
                "    </form>\n" +
                "</div>\n" +
                "</form>\n" +
                "</div>");
    }
}