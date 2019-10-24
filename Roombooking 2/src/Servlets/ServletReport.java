package Servlets;

/**
 * @Author Henriette Andersen, Hanne Sjursen
 */


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

@WebServlet(name = "Servlets.ServletReport", urlPatterns = {"/Servlets.ServletReport"})
public class ServletReport extends AbstractPostServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)  {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            //prints start of html tags.
            printNav(out);
            out.println("<h2 class=\"report_processing\">Your report is under process../>");
            //gets the username -> email in this case.
            HttpSession session = request.getSession();
            String userName = (String) session.getAttribute("userEmail");
            String action = request.getParameter("action").toLowerCase();

            if (action.contains("report")) {
                DbTool dbTool = new DbTool();
                //Establishes connection to database
                Connection connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();
                int userID = dbFunctionality.getUserId(userName, connection);

            } else {
                //adds a return button if the Report fails.

                addHomeButton(out);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}


