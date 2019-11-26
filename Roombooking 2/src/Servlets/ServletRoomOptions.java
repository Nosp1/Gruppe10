package Servlets;

import Classes.Rooms.AbstractRoom;
import Classes.Rooms.Auditorium;
import Classes.Rooms.Grouproom;
import Classes.User.AbstractUser;
import Tools.DbFunctionality;
import Tools.DbTool;
import org.apache.commons.dbutils.DbUtils;

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
        Connection connection = null;
        try (PrintWriter out = response.getWriter()) {
            //prints the beginning of a  html-page.
            printLoggedInNav(out);
            // Duplikat av de 4 neste linjene i ServletRoomBooking
            //retrieves the value of button Add Room
            String action = request.getParameter("action").toLowerCase();
            HttpSession httpSession = request.getSession();
            String userName = (String) httpSession.getAttribute("userEmail");
            System.out.println(userName);

            if (action.contains("add room")) {
                System.out.println("add room started");
                DbTool dbTool = new DbTool();
                //establishes connection to database
                connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();
                // Sjekker om brukeren er en administrator, og returnerer feil om de ikke er det
                AbstractUser user = dbFunctionality.getUser(userName, connection);
                if (!isAdmin(user, connection)) {
                    out.println("<h2> Error: you do not have access to admin.</h2>");
                    addRedirectOnUserType(out, user.getUserType());
                } else {
                    //retrieves the data in the text-box Add RoomID
                    int roomID = dbFunctionality.getRoomID(connection);
                    //retrieves the Room name from the text-box Room Name
                    String roomName = request.getParameter("Add_roomName");
                    //retrieves the Room building from the text-box Room Building
                    String roomBuilding = request.getParameter("Add_roomBuilding");
                    //retrieves the room capacity from the text-box Room Capacity
                    int maxCapacity = Integer.parseInt(request.getParameter("maxCapacity"));
                    System.out.println("maxCapacity: " + maxCapacity);
                    String roomType = request.getParameter("typeRooms");

                    if(roomType == null) {
                        if(maxCapacity <= 12) {
                            roomType = "GROUPROOM";
                        } else if(maxCapacity >= 13 && maxCapacity <= 30) {
                            roomType = "CLASSROOM";
                        } else {
                            roomType = "AUDITORIUM";
                        }
                    } else {
                        roomType = roomType.toUpperCase();
                    }

                    boolean hasTavle = false;
                    boolean hasProjektor = false;
                /*retrieves the checkbox values for "hasTavle" and "hasProsjektor", not sent
                with the request if they are unchecked, so set them to true if they are not null */
                    String[] values = request.getParameterValues("hasTavle");
                    if (values != null) {
                        hasTavle = true;
                    }
                    System.out.println("hasTavle: " + hasTavle);

                    values = request.getParameterValues("hasProjektor");
                    if (values != null) {
                        hasProjektor = true;
                    }
                    System.out.println("hasProjektor: " + hasProjektor);

                    // Opprett et Grouproom objekt fra dataen hentet fra HTML formen
                    // AbstractRoom room = new Grouproom(roomID, roomName, roomBuilding, maxCapacity, hasTavle, hasProjektor);
                    AbstractRoom room;
                    if (roomType.equals("GROUPROOM")) {
                        room = new Grouproom(roomID, roomName, roomBuilding, maxCapacity, hasTavle, hasProjektor);
                    } else {
                        room = new Auditorium(roomID, roomName, roomBuilding, maxCapacity, hasTavle, hasProjektor);
                    }
                    // TODO: Bruker kun grupperom typen for nå
                    //Adds the room to the database
                    dbFunctionality.addRoom(room, connection);
                    //TODO: Kanskje legge til if statement som
                    // dispatcher deg tilbake til loggedin istedenfor knapp? for mer flytt og mindre klikks
                    //prints return button.
                    out.println("Room " + roomName + " " + "has been successfully added");
                    addRedirectOnUserType(out, user.getUserType());
                }
            } else if (action.contains("delete room")) {
                DbTool dbTool = new DbTool();
                connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();
                // Sjekker om brukeren er en administrator, og returnerer feil om de ikke er det
                AbstractUser user = dbFunctionality.getUser(userName, connection);
                if (!isAdmin(user, connection)) {
                    out.println("<h2> Error: you do not have access to admin.</h2>");
                    addRedirectOnUserType(out, user.getUserType());
                } else {
                    //todo add boolean statement to confirm deletion.
                    //todo Cannot delete room because orders with the room exists.

                    int roomID = Integer.parseInt(request.getParameter("Delete_roomID"));
                    dbFunctionality.deleteRoom(roomID, connection);
                    out.println("Room " + roomID + "has been successfully deleted");
                    addRedirectOnUserType(out, user.getUserType());

                    addBootStrapFunctionality(out);
                    loadJSScripts(out);
                }
            } else if (action.contains("cancel")) {
                //todo add documentation
                int orderID = Integer.parseInt(request.getParameter("Cancel_Order_ID"));
                DbTool dbTool = new DbTool();
                connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();

                AbstractUser user = dbFunctionality.getUser(userName, connection);

                if (dbFunctionality.deleteOrder(orderID, connection)) {
                    out.println("Order canceled ");
                    addRedirectOnUserType(out, user.getUserType());
                } else {
                    out.println("That's not a valid order");

                }
                addBootStrapFunctionality(out);
                loadJSScripts(out);
            } else if (action.contains("show")) {
                DbTool dbTool = new DbTool();
                connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();

                dbFunctionality.printRooms(out, connection);
                addHomeLoggedInButton(out);

                addBootStrapFunctionality(out);
                loadJSScripts(out);

            } else if (action.contains("gotoprofile")) {
                ServletContext servletContext = getServletContext();
                HttpSession session = request.getSession();
                session.setAttribute("userEmail", userName);
                servletContext.getRequestDispatcher("/profile.html").forward(request, response);
            }

            addBootStrapFunctionality(out);
            out.print("<script\n" +
                    "        src=\"https://code.jquery.com/jquery-3.4.1.js\"\n" +
                    "        integrity=\"sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU=\"\n" +
                    "        crossorigin=\"anonymous\"></script>");
            out.print("<script src=\"card-rooms.js\"></script>");
            loadJSScripts(out);
            out.print("</body>");
            out.print("</html>");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //DbUtils.closeQuietly(connection);
            assert connection != null;
            closeConnection(connection);
        }
    }
}
