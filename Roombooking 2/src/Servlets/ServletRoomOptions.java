package Servlets;

import Classes.Rooms.AbstractRoom;
import Classes.Rooms.Grouproom;
import Tools.DbFunctionality;
import Tools.DbTool;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;


/**
 * Author Hanne, Henriette, Hedda, Trym, Brisdalen
 */

@WebServlet(name = "Servlets.ServletRoomOptions", urlPatterns = {"/Servlets.ServletRoomOptions"})
public class ServletRoomOptions extends AbstractPostServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            //prints the beginning of a  html-page.
            printNav(out);
            //retrieves the value of button Add Room
            String action = request.getParameter("action").toLowerCase();
            HttpSession httpSession = request.getSession();
            String userName = (String) httpSession.getAttribute("userEmail");
            System.out.println(userName
            );

            if (action.contains("add")) {
                DbTool dbTool = new DbTool();
                //establishes connection to database
                Connection connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();
                //retrieves the data in the text-box Add RoomID
                int roomID = Integer.parseInt(request.getParameter("Add_roomID"));
                //retrieves the Room name from the text-box Room Name
                String roomName = request.getParameter("Add_roomName");
                //retrieves the Room building from the text-box Room Building
                String roomBuilding = request.getParameter("Add_roomBuilding");
                //retrieves the room capacity from the text-box Room Capacity
                int maxCapacity = Integer.parseInt(request.getParameter("maxCapacity"));

                // Opprett et Grouproom objekt fra dataen hentet fra HTML formen
                AbstractRoom room = new Grouproom(roomID, roomName, roomBuilding, maxCapacity);
                // TODO: Bruker kun grupperom typen for nå
                //Adds the room to the database
                dbFunctionality.addRoom(room, connection);
                //TODO: Kanskje legge til if statement som
                // dispatcher deg tilbake til loggedin istedenfor knapp? for mer flytt og mindre klikks
                //prints return button.
                addHomeLoggedInButton(out);
            } else if (action.contains("delete room")) {
                DbTool dbTool = new DbTool();
                Connection connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();
                //todo add boolean statement to confirm deletion.
                //todo Cannot delete room because orders with the room exists.
                int roomID = Integer.parseInt(request.getParameter("Add_roomID"));

                dbFunctionality.deleteRoom(roomID, connection);
                addHomeLoggedInButton(out);

            } else if (action.contains("cancel")) {
                //todo add documentation
                int orderID = Integer.parseInt( request.getParameter("Cancel_Order_ID"));
                DbTool dbTool = new DbTool();
                Connection connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();

                if (dbFunctionality.deleteOrder(orderID,connection)) {
                    out.println("Order canceled ");
                   addHomeLoggedInButton(out);

                }

            }
            else if (action.contains("show")) {
                DbTool dbTool = new DbTool();
                Connection connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();

                dbFunctionality.printRooms(out, connection);
                addHomeLoggedInButton(out);

            } else if (action.contains("gotoprofile")) {
                ServletContext servletContext = getServletContext();
                //todo Send session with Username
                HttpSession session = request.getSession();
                session.setAttribute("userEmail", userName);
                servletContext.getRequestDispatcher("/profile.html").forward(request, response);

            }

            addBootStrapFunctionality(out);
            out.print("</body>");
            out.print("</html>");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
