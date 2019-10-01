package Servlets;

import Classes.Email.EmailTemplates;
import Classes.Email.EmailUtil;
import Classes.Email.TLSEmail;
import Classes.Order;
import Tools.DbFunctionality;
import Tools.DbTool;

import javax.mail.Session;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

@WebServlet(name = "Servlets.ServletRoomBooking", urlPatterns = {"/Servlets.ServletRoomBooking"})
public class ServletRoomBooking extends AbstractPostServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            //prints the beginning of a  html-page.
            printNav(out);
            //retrieves the value of the Reserve a Room button
            String action = request.getParameter("action").toLowerCase();

            if(action.contains("reserve")) {
                System.out.println("Reserve started");
                String formRoomID = request.getParameter("Reserve_Room_ID");
                int roomId = Integer.parseInt(formRoomID);
                String timestampStart = request.getParameter("Reserve_Timestamp_start");
                String timestampEnd = request.getParameter("Reserve_Timestamp_end");

                DbTool dbTool = new DbTool();
                Connection connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();

                ResultSet orders = dbFunctionality.getOrdersFromRoom(roomId, connection);

                // TODO ADD AUTOMATIC ORDERID AND USERID
                int orderID = dbFunctionality.getOrderID(connection);
                Order order = new Order(orderID, 6, roomId, timestampStart, timestampEnd);
                System.out.println("Created order: ");
                System.out.println(order.toString());
                TLSEmail tlsEmail = new TLSEmail();
                //TODO create db method to retrieve epost with userID from db.
                /*
                while(resultSet.next()) {
                    Timestamp t1 = resultSet.getTimestamp("Timestamp_start");
                    Timestamp t2 = resultSet.getTimestamp("Timestamp_end");
                    Order other = new Order(t1, t2);

                    if(!order.intersects(t1, t2) {
                        int orderID = dbFunctionality.getOrderID(connection);
                        Order order = new Order(orderID, 5, roomId, timestampStart, timestampEnd);
                        System.out.println("Created order: ");
                        System.out.println(order.toString());

                        dbFunctionality.addOrder(order, connection);
                    } else {
                        Returner en error til brukeren om rommet er opptatt ved tidspunktet valgt
                    }
                }
                 */
                dbFunctionality.addOrder(order, connection);
                Session session = tlsEmail.NoReplyEmail("trymerlend@hotmail.no");
                EmailUtil confirmationEmail = new EmailUtil();
                String receipt = EmailTemplates.getBookingReceipt();
                String body = EmailTemplates.bookingConfirmation("Trym",order);
                confirmationEmail.sendEmail(session,"trymerlend@hotmail.no",receipt,body);
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
    }
}
