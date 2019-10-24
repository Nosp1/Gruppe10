package Servlets;


import Classes.User.AbstractUser;
import Classes.User.Student;
import Tools.DbFunctionality;
import Tools.DbTool;

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
 * @author hanne, henriette, hedda
 * @see Servlets.AbstractServlet
 * @see javax.servlet.http.HttpServlet
 * @see javax.servlet.GenericServlet
 * @see DbFunctionality
 * @see DbTool
 */

@WebServlet(name = "Servlets.ServletReservations", urlPatterns = {"/Servlets.ServletReservations"})
public class ServletReservations extends AbstractServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)  {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            //prints start of html tags.
            printLoggedInNav(out);
            out.println("Here are the reservations:");
            //gets the username -> email in this case.
            HttpSession session = request.getSession();
            String userName = (String) session.getAttribute("userEmail");
            String action = request.getParameter("action").toLowerCase();

            if (action.contains("myreservations")) {
                DbTool dbTool = new DbTool();
                //Establishes connection to database
                Connection connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();
                int userID = dbFunctionality.getUserId(userName,connection);
                AbstractUser user = new Student(userID,dbFunctionality.getOrderListByUserID(userID,connection));
                user.showOrders(out);
            }

            else{
                //adds a return button if the login fails.

                addHomeButton(out);
            }

            //prints script to establish connection between bootstrap and html
            addBootStrapFunctionality(out);
            out.print("</body>");
            out.print("</html>");
            //prints errors: if the database fails, if the password is wrong.
        } catch (SQLException | IOException | ParseException e) {
            e.printStackTrace();
        }
    }


}