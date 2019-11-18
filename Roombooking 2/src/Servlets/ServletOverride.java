package Servlets;

import Classes.Order;
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

@WebServlet(name = "Servlets.ServletOverride", urlPatterns = {"/Servlets.ServletOverride"})
public class ServletOverride extends AbstractPostServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connection = null;
        try (PrintWriter out = response.getWriter()) {
            //prints the beginning of a  html-page.
            printLoggedInNav(out);
            //retrieves the value of the Reserve a Room button
            String action = request.getParameter("confirm").toLowerCase();
            HttpSession httpSession = request.getSession();
            String userName = (String) httpSession.getAttribute("userEmail");

            if(action.contains("yes")) {
                System.out.println("Override started");
                // Nødvendige klasser for database samhandling og funksjonalitet
                DbTool dbTool = new DbTool();
                connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();

                // Sjekker om brukeren er en administrator, og returnerer feil om de ikke er det
                AbstractUser user = dbFunctionality.getUser(userName, connection);
                if (!isAdmin(user, connection)) {
                    out.println("<h2> Error: you do not have access to admin.</h2>");
                    addRedirectOnUserType(out, user.getUserType());
                }

                Order order = (Order) httpSession.getAttribute("order");
                System.out.println("[ServletOverride]Order ID: " + order.getID());
                AbstractRoom room = (AbstractRoom) httpSession.getAttribute("room");

                int userId = dbFunctionality.getUserId(userName, connection);
                System.out.println("[Servletoverride]UserID: " + userId);

                dbFunctionality.updateOrderOwner(order.getID(), userId, connection);
                out.println("<p>You have successfully booked " + room.getRoomName());
                addRedirectOnUserType(out, user.getUserType());
            } else {
                out.print("<p> an error occured </p>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }
}
