package Servlets;

import Classes.Email.EmailTemplates;
import Classes.Email.EmailUtil;
import Classes.Email.TLSEmail;
import Classes.Order;
import Classes.Rooms.AbstractRoom;
import Classes.User.AbstractUser;
import Tools.DbFunctionality;
import Tools.DbTool;

import javax.mail.Session;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * ServletRoomBooking.java is used for handling actions related to room-booking reservations.
 *
 * @author brisdalen, sæthra
 */
@WebServlet(name = "Servlets.ServletRoomBooking", urlPatterns = {"/Servlets.ServletRoomBooking"})

public class ServletRoomBooking extends AbstractPostServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection connection = null;
        try (PrintWriter out = response.getWriter()) {
            //prints the beginning of a  html-page.
            printLoggedInNav(out);
            //retrieves the value of the Reserve a Room button
            String action = request.getParameter("action").toLowerCase();
            HttpSession httpSession = request.getSession();
            String userName = (String) httpSession.getAttribute("userEmail");
            if (action.contains("reserve")) {
                System.out.println("Reserve started");
                // Nødvendige klasser for database samhandling og funksjonalitet
                DbTool dbTool = new DbTool();
                connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();

                // Hent roomID, Timestamp_start og _end for å sjekke reservasjonen
                String formRoomID = request.getParameter("Reserve_Room_ID");
                int roomID = Integer.parseInt(formRoomID);
                AbstractRoom room = dbFunctionality.getRoom(roomID, connection);

                String[] dateTimeStartArray = request.getParameterValues("start-datetimes");
                String[] dateTimeEndArray = request.getParameterValues("end-datetimes");

                /* String timestampStartDate = request.getParameter("Reserve_Timestamp_start_date");
                String timestampStartTime = request.getParameter("Reserve_Timestamp_start_time");
                String timestampStart = timestampStartDate + " " + timestampStartTime;

                String timestampEndDate = request.getParameter("Reserve_Timestamp_end_date");
                String timestampEndTime = request.getParameter("Reserve_Timestamp_end_time");
                String timestampEnd = timestampEndDate + " " + timestampEndTime;
                System.out.println(timestampEnd); */
                int y = 0;
                for (int i = 0; i < dateTimeStartArray.length; i++) {
                    String timestampStart = dateTimeStartArray[i];
                    String timestampEnd = dateTimeEndArray[i];
                    // Sjekker om tidspunktene er de samme
                    if (timestampStart.equals(timestampEnd)) {
                        // Hvis de er det returneres det en feilmelding om dette til brukeren.
                        String sameTimeErrorMessage = "Sorry, your booking can't be made. Start- and end time cannot be the same.";
                        System.out.println(sameTimeErrorMessage);
                        out.println(sameTimeErrorMessage);
                        // Deretter legger vi til en knapp som tar brukeren tilbake til sin hjemmeside.
                        AbstractUser user = dbFunctionality.getUser(userName, connection);
                        addRedirectOnUserType(out, user.getUserType());
                        return;
                    }
                    // Sjekker om slutt-tidspunkt er før start-tidspunkt
                    if (timestampEnd.compareTo(timestampStart) < 0) {
                        String endBeforeStartErrorMessage = "Sorry, your booking can't be made. Start time must be before end time.";
                        System.out.println(endBeforeStartErrorMessage);
                        out.println(endBeforeStartErrorMessage);

                        AbstractUser user = dbFunctionality.getUser(userName, connection);
                        addRedirectOnUserType(out, user.getUserType());
                        return;
                    }
                    // new order to reserve
                    Order order = new Order(dbFunctionality.getRoom(roomID, connection), timestampStart, timestampEnd);


                    TLSEmail tlsEmail = new TLSEmail();
                    ResultSet orders = dbFunctionality.getOrdersFromRoom(roomID, timestampStart.substring(0, 10), connection);
                    // Lag og sett en boolean til true,
                    boolean available = true;
                    int iterations = 0;
                    // og sjekk order opp mot alle andre reservasjoner.
                    while (orders.next()) {
                        System.out.println("iterations: " + ++iterations);
                        Timestamp start = orders.getTimestamp("Timestamp_start");
                        Timestamp end = orders.getTimestamp("Timestamp_end");
                        Order other = new Order(start, end);
                        // Hvis order overlapper med noen settes available til false, og vi avslutter while-løkka
                        if (order.intersects(other)) {
                            available = false;
                            break;
                        }
                    }
                    // Hvis det er ledig etter hele while-løkka,
                    if (available) {
                        // henter vi orderID, lager Order objektet på nytt og legger det til databasen.
                        int orderID = dbFunctionality.getOrderID(connection);
                        AbstractUser user = dbFunctionality.getUser(userName, connection);
                        int userId = dbFunctionality.getUserId(userName, connection);
                        order = new Order(orderID, userId, room, timestampStart, timestampEnd);
                        dbFunctionality.addOrder(order, connection);
                        out.println("<p>You have successfully booked " + room.getRoomName());
                        addRedirectOnUserType(out, user.getUserType());
                        y++;
                        //todo fix email
                        if (y > 1) {
                            System.out.println("ignoring email to many bookings");
                        } else {
                            // Etter reservasjonen er lagt til i databasen sender vi en kvittering på epost.
                            Session session = tlsEmail.NoReplyEmail(user.getUserName());
                            EmailUtil confirmationEmail = new EmailUtil();
                            String receipt = EmailTemplates.getBookingReceipt();
                            String body = EmailTemplates.bookingConfirmation(user.getFirstName().substring(0, 1).toUpperCase() + user.getFirstName().substring(1), order);
                            confirmationEmail.sendEmail(session, user.getUserName(), receipt, body);
                        }
                    } else {
                        // Hvis ikke returneres en error til brukeren
                        String notAvailableErrorMessage = "Sorry, there was an error during your booking.\n " +
                                order.getRoomName() + " at " + order.getBookingDate() + " is already reserved from " +
                                order.getBookingStartTime() + " to " + order.getBookingEndTime();
                        
                        System.out.println(notAvailableErrorMessage);
                        // Deles opp etter new line for å vise beskjeden på hver sin linje
                        String[] parts = notAvailableErrorMessage.split("\\n");
                        for(String s : parts) {
                            out.println("<p>" + s + "</p>");
                        }

                        AbstractUser user = dbFunctionality.getUser(userName, connection);

                        if(user.getUserType().toString().equals("ADMIN")) {
                            out.println("<h4>Would you like to override it?</h4>");
                            out.print("<form action=\"./Servlets.ServletOverride\" method=\"post\">" +
                                    "        <div>\n" +
                                    "            <input class=\"submit btn-default btn-lg\" type=\"submit\" name=\"confirm\" value=\"Yes\"></input>\n" +
                                    "           </form>");
                            httpSession.setAttribute("order", dbFunctionality.getSpecificOrder(room.getRoomID(),
                                                        order.getTimestampStart(), order.getTimestampEnd(), connection));
                            httpSession.setAttribute("room", room);

                            addRedirectOnUserTypeInline(out, user.getUserType());
                            out.println("   </div>");
                        } else {
                            addRedirectOnUserType(out, user.getUserType());
                        }
                    }
                }
            }

            if (action.contains("update")) {
                // ID på hvilken order du vil endre hentes fra request.
                String formOrderID = request.getParameter("Update_orderID");
                int orderID = Integer.parseInt(formOrderID);

                // Endret start tidspunkt hentes fra request,
                String timestampStartDate = request.getParameter("Update_Timestamp_start_date");
                String timestampStartTime = request.getParameter("Update_Timestamp_start_time");
                // og formateres til riktig format i en string.
                String timestampStart = timestampStartDate + " " + timestampStartTime;

                // Det samme gjøres med det nye slutt tidspunktet.
                String timestampEndDate = request.getParameter("Update_Timestamp_end_date");
                String timestampEndTime = request.getParameter("Update_Timestamp_end_time");
                String timestampEnd = timestampEndDate + " " + timestampEndTime;

                // Oppretter en kobling med databasen
                DbTool dbTool = new DbTool();
                connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();

                // Lager en ny ordre og gir den variabelnavn order
                Order originalOrder = dbFunctionality.getOrder(orderID, connection);
                int roomID = originalOrder.getRoomID();

                Order order = new Order(timestampStart, timestampEnd);


                ResultSet orders = dbFunctionality.getOrdersFromRoom(roomID, timestampStart.substring(0, 10), connection);
                System.out.println("Resultset recieved");

                Timestamp startTimeOO = originalOrder.getTimestampStart();
                Timestamp endTimeOO = originalOrder.getTimestampEnd();


                // Lag og sett en boolean til true,
                boolean available = true;
                int iterations = 0;
                // og sjekk order opp mot alle andre reservasjoner.
                while (orders.next()) {
                    System.out.println("iterations: " + ++iterations);
                    Timestamp t1 = orders.getTimestamp("Timestamp_start");
                    Timestamp t2 = orders.getTimestamp("Timestamp_end");
                    Order other = new Order(t1, t2);

                    // Hvis order overlapper med noen settes available til false, og vi avslutter while-løkka
                    if (order.intersects(other) && !startTimeOO.equals(t1)) {
                        available = false;
                        break;
                    }

                }
                // Hvis det er ledig etter hele while-løkka,
                if (available) {
                    order = new Order(orderID, timestampStart, timestampEnd);
                    try {
                        dbFunctionality.updateOrderInformation(order, connection);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    String availableMessage = "Order successfully updated!";
                    System.out.println(availableMessage);
                    out.println(availableMessage);
                    addHomeLoggedInButton(out);
                    addBootStrapFunctionality(out);
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    String notAvailableErrorMessage = "Sorry, that time and room is already taken.";
                    // Hvis ikke returneres en error til brukeren
                    System.out.println(notAvailableErrorMessage);
                    out.println(notAvailableErrorMessage);
                    addHomeLoggedInButton(out);
                    addBootStrapFunctionality(out);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }
}



