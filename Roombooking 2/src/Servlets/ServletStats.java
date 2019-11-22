package Servlets;


import Classes.Rooms.AbstractRoom;
import Classes.User.AbstractUser;
import Tools.DbFunctionality;
import Tools.DbTool;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *  Servlet Stats handles the query and prints the statistical results stored in our database
 *  prints the most active users or the most booked rooms.
 *
 * @author trym
 *
 */
@WebServlet(name = "Servlets.ServletStats", urlPatterns = {"/Servlets.ServletStats"})
public class ServletStats extends AbstractServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connection = null;
        try (PrintWriter out = response.getWriter()) {
            //Prints Start of html and top Navbar.
            printLoggedInNav(out);
            //Gets fields from html
            String action = request.getParameter("action").toLowerCase();
            //stores the users session
            HttpSession httpSession = request.getSession();
            String userName = (String) httpSession.getAttribute("userEmail");
            System.out.println("servlet stats working" + userName);
            //check action field from html
            if (action.contains("get most active users")) {
                System.out.println("Get most active users started");
                //Establishes connection to the database
                DbTool dbTool = new DbTool();
                connection = dbTool.dbLogIn(out);
                //Runs query towards the database
                DbFunctionality dbFunctionality = new DbFunctionality();
                ArrayList<AbstractUser> users = dbFunctionality.getMostActiveUsers(connection);
                //initialise counter
                int counter = 0;
                //print the content inside the body:
                out.println("<h1>" + "The Most active users: " + " </h1>");
                //prints the 5 most used accounts
                for (AbstractUser s : users) {
                    out.println("<div class=\"container stats\">\n" +
                            "<form>\n" +
                            "<table>\n" +
                            "    <thead>\n" +
                            "        <tr>\n" +
                            "            <th colspan=\"2\"> Ranking: " + (counter + 1) + " " + s.getUserName() + "</th>\n" +
                            "        </tr>\n" +
                            "    </thead>\n" +
                            "    <tbody>\n" +
                            "        <tr>\n" +
                            "            <td>Amount of Orders: " + s.getAmount() +  "</td>\n" +
                            "        </tr>\n" +
                            "    </tbody>\n" +
                            "</table>\n" +
                            "</form>\n" +
                            "</div>" +
                            "</div>");
                    //increments the integer to print the next user.
                    counter++;

                }

                //prints the most used rooms
            } else if (action.contains("get most booked room")) {
                //establish connection to the database
                DbTool dbTool = new DbTool();
                connection = dbTool.dbLogIn(out);
                //run query on the database
                DbFunctionality dbFunctionality = new DbFunctionality();
                ArrayList<AbstractRoom> rooms = dbFunctionality.getMostBookedRoom(connection);
                //initialise counter
                int counter = 0;
                //prints the beginning of body
                out.println("<h1>" + "The most Booked Rooms are: " + "</h1>");
                //prints the five most used rooms.
                for (AbstractRoom s: rooms) {
                    out.println("<div class=\"container stats\">\n" +
                            "<form>\n" +
                            "<table>\n" +
                            "    <thead>\n" +
                            "        <tr>\n" +
                            "            <th colspan=\"2\"> Ranking: " + (counter + 1) + " Room ID: " + s.getRoomID() + "</th>\n" +
                            "        </tr>\n" +
                            "    </thead>\n" +
                            "    <tbody>\n" +
                            "        <tr>\n" +
                            "            <td>Amount of bookings: " + s.getAmount() +  "</td>\n" +
                            "        </tr>\n" +
                            "    </tbody>\n" +
                            "</table>\n" +
                            "</form>\n" +
                            "</div>" +
                            "</div>");
                    //increment counter
                    counter++;

                }

            } else {
                out.println("something went wrong");
            }
            //prints return button to logged in homepage
            addRedirectButton(out,"loggedInAdmin.html");
            addBootStrapFunctionality(out);
            loadJSScripts(out);
            out.print("</body>");
            out.print("</html>");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                assert connection != null;
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

