package Servlets;




import Reports.Report;
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

/**
 * @author Henriette Andersen, Hanne Sjursen
 */
@WebServlet(name = "Servlets.ServletReport", urlPatterns = {"/Servlets.ServletReport"})
public class ServletReport extends AbstractPostServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        Connection connection = null;
        try (PrintWriter out = response.getWriter()) {

            //prints start of html tags.
            printLoggedInNav(out);
            out.println("<h2 class=\"report_processing\">Your report is sent</h2>");
            out.println("<h2>Thank you for submitting a report</h2>");
            //gets the username -> email in this case.
            HttpSession session = request.getSession();
            String userName = (String) session.getAttribute("userEmail");
            String action = request.getParameter("action").toLowerCase();

            if (action.contains("report")) {
                DbTool dbTool = new DbTool();
                //Establishes connection to database
                connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();
                int userID = dbFunctionality.getUserId(userName, connection);
                String roomIDString = request.getParameter("Report_Room_ID");
                int roomID = Integer.parseInt(roomIDString);
                String responseString = request.getParameter("Report_TextArea");
                if (responseString.isEmpty()) {
                    responseString = "No value.";
                }
                int reportID = 1;

                Report newReport = new Report(responseString, userID, roomID);
                dbFunctionality.insertReport(newReport, connection);
            } else {
                //adds a return button if the Report fails.
                out.println("An error occured in sending the report, please try again");
                addHomeLoggedInButton(out);
            }
            out.println("  ");
            addHomeLoggedInButton(out);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(connection);
            try {
                if (connection.isClosed()) {
                    System.out.println("connection closed");
                } else {
                    System.out.println("connection is not closed");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}


