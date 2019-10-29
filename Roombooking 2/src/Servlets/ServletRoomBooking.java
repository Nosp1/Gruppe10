package Servlets;

import Classes.Email.EmailTemplates;
import Classes.Email.EmailUtil;
import Classes.Email.TLSEmail;
import Classes.Order;
import Classes.User.AbstractUser;
import Tools.DbFunctionality;
import Tools.DbTool;
import org.apache.commons.dbutils.DbUtils;

import javax.mail.Session;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.ParseException;

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


            if(action.contains("reserve")) {

                System.out.println("Reserve started");
                // Nødvendige klasser for database samhandling og funksjonalitet
                DbTool dbTool = new DbTool();
                connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();

                // Hent roomID, Timestamp_start og _end for å sjekke reservasjonen
                String formRoomID = request.getParameter("Reserve_Room_ID");
                int roomID = Integer.parseInt(formRoomID);

                String timestampStartDate = request.getParameter("Reserve_Timestamp_start_date");
                String timestampStartTime = request.getParameter("Reserve_Timestamp_start_time");
                String timestampStart = timestampStartDate + " " + timestampStartTime;
                System.out.println(timestampStart);

                String timestampEndDate = request.getParameter("Reserve_Timestamp_end_date");
                String timestampEndTime = request.getParameter("Reserve_Timestamp_end_time");
                String timestampEnd = timestampEndDate + " " + timestampEndTime;
                System.out.println(timestampEnd);

                if (timestampStart.equals(timestampEnd)) {
                    // Ikke reserver, returner feilmelding til brukeren
                    String sameTimeErrorMessage = "Sorry, the start and end-time is the same, and a booking can't be made.";
                    System.out.println(sameTimeErrorMessage);
                    out.println(sameTimeErrorMessage);
                    return;
                }
                // new order to reserve
                Order order = new Order(timestampStart, timestampEnd);

                //TODO create db method to retrieve epost with userID from db.
                TLSEmail tlsEmail = new TLSEmail();

                ResultSet orders = dbFunctionality.getOrdersFromRoom(roomID, timestampStart.substring(0, 10), connection);
                System.out.println("Resultset recieved");
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
                    if (order.intersects(other)) {
                        available = false;
                        break;
                    }
                }
                // Hvis det er ledig etter hele while-løkka,
                if (available) {
                    // henter vi orderID, lager Order objektet på nytt og legger det til databasen.
                    int orderID = dbFunctionality.getOrderID(connection);
                    // TODO ADD AUTOMATIC USERID
                    AbstractUser user = dbFunctionality.getUser(userName, connection);
                    int userId = dbFunctionality.getUserId(userName, connection);
                    order = new Order(orderID, userId, roomID, timestampStart, timestampEnd);
                    dbFunctionality.addOrder(order, connection);

                    // Etter reservasjonen er lagt til i databasen sender vi en kvittering på epost.
                    Session session = tlsEmail.NoReplyEmail(user.getUserName());
                    EmailUtil confirmationEmail = new EmailUtil();
                    String receipt = EmailTemplates.getBookingReceipt();
                    String body = EmailTemplates.bookingConfirmation(user.getFirstName().substring(0, 1).toUpperCase() + user.getFirstName().substring(1), order);
                    confirmationEmail.sendEmail(session, user.getUserName(), receipt, body);
                    out.println("<p>You have successfully booked" + roomID);
                    addHomeLoggedInButton(out);
                } else {
                    // Hvis ikke returneres en error til brukeren
                    String notAvailableErrorMessage = "Sorry, that time and room is already taken.";
                    // TODO: Returner en error til brukeren om rommet er opptatt ved tidspunktet valgt
                    System.out.println(notAvailableErrorMessage);
                    out.println(notAvailableErrorMessage);
                }

            } else if (action.contains("update")) {

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
                Order order = new Order(orderID, timestampStart, timestampEnd);


                //Bruker metoden i dbfunctionality til å update ordren.
                dbFunctionality.updateOrderInformation(order, connection);
                out.println("Order successfully updated!");
                addBootStrapFunctionality(out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                DbUtils.closeQuietly(connection);
                assert connection != null;
                if (connection.isClosed()) {
                    System.out.println("connection is closed ");
                } else {
                    System.out.println("connection is not closed");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

