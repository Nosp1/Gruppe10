package Servlets;

import Classes.Email.EmailTemplates;
import Classes.Email.EmailUtil;
import Classes.Email.TLSEmail;
import Classes.Order;
import Classes.Rooms.AbstractRoom;
import Classes.Rooms.Grouproom;
import Tools.DbFunctionality;
import Tools.DbTool;

import javax.mail.Session;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;


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

            if(action.contains("add")) {
                //retrieves the data in the text-box Add RoomID
                int roomID = Integer.parseInt(request.getParameter("Add_roomID"));
                //retrieves the Room name from the text-box Room Name
                String roomName = request.getParameter("Add_roomName");
                //retrieves the Room building from the text-box Room Building
                String roomBuilding = request.getParameter("Add_roomBuilding");
                //retrieves the room capacity from the text-box Room Capacity
                int maxCapacity = Integer.parseInt(request.getParameter("maxCapacity"));

                DbTool dbTool = new DbTool();
                //establishes connection to database
                Connection connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();
                //
                AbstractRoom room = new Grouproom(roomID, roomName, roomBuilding, maxCapacity);
                // TODO: Bruker kun grupperom typen for nå
                //Adds the room to the database
                dbFunctionality.addRoom(room, connection);
                //TODO: Kanskje legge til if statement som
                // dispatcher deg tilbake til loggedin istedenfor knapp? for mer flytt og mindre klikks
                //prints return button.
                addHomeLoggedInButton(out);

            // TODO: Lag HTML side med action som fjerner et rom
            } else if(action.contains("delete")) {
                //retrieves the
                int roomID = Integer.parseInt(request.getParameter("Delete_roomID"));
                DbTool dbTool = new DbTool();
                Connection connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();

                dbFunctionality.deleteRoom(roomID, connection);

                addHomeLoggedInButton(out);

            } else if(action.contains("show")) {
                DbTool dbTool = new DbTool();
                Connection connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();
                dbFunctionality.printRooms(out, connection);
                addHomeLoggedInButton(out);

            } else if (action.contains("gotoprofile")) {
                ServletContext servletContext = getServletContext();
                servletContext.getRequestDispatcher("/profile.html").forward(request,response);

            } else if(action.contains("reserve")) {
                //TODO remove souts.
                System.out.println("Reserve started");
                String formRoomID = request.getParameter("Reserve_Room_ID");
                int roomId = Integer.parseInt(formRoomID);
                System.out.println(roomId);
                String timestampStart = request.getParameter("Reserve_timestamp_start");
                System.out.println(timestampStart);
                String timestampEnd = request.getParameter("Reserve_timestamp_end");
                System.out.println(timestampEnd);

                DbTool dbTool = new DbTool();
                Connection connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();

                System.out.println("Attempting to create JAVA Order object");
                // TODO: Her kommer vi oss ikke videre i koden -> fix it ref hvem?
                // TODO ADD AUTOMATIC ORDERID AND USERID
                int orderID = dbFunctionality.getOrderID(connection);
                Order order = new Order(orderID ,6, roomId, timestampStart, timestampEnd);
                System.out.println("Created room: ");
                System.out.println(order.toString());
                dbFunctionality.addOrder(order, connection);
                //todo add epost.
                TLSEmail tlsEmail = new TLSEmail();
                //TODO create db method to retrieve epost with userID from db.
                //Todo: need to get roomName from Db and parse into email not id.
                Session session = tlsEmail.NoReplyEmail("trymerlend@hotmail.no");
                EmailUtil confirmationEmail = new EmailUtil();
                String receipt = EmailTemplates.getBookingReceipt();
                String body = EmailTemplates.bookingConfirmation("Trym",order);
                confirmationEmail.sendEmail(session,"trymerlend@hotmail.no",receipt,body);

            }

            addBootStrapFunctionality(out);
            out.print("</body>");
            out.print("</html>");
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
    }

}
