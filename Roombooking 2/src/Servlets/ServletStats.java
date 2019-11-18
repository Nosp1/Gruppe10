package Servlets;


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
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "Servlets.ServletStats", urlPatterns = {"/Servlets.ServletStats"})

public class ServletStats extends AbstractServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connection;
        try (PrintWriter out = response.getWriter()) {
            printLoggedInNav(out);
            String action = request.getParameter("action").toLowerCase();
            HttpSession httpSession = request.getSession();
            String userName = (String) httpSession.getAttribute("userEmail");
            System.out.println("servlet stats working" + userName);

            if (action.contains("get most active users")) {
                System.out.println("Get most active users started");
                DbTool dbTool = new DbTool();
                connection = dbTool.dbLogIn(out);

                DbFunctionality dbFunctionality = new DbFunctionality();
                AbstractUser[] users = dbFunctionality.getMostActiveUsers(connection);
                int counter = 0;
                out.println("<h1>" + "The most active users: " + " </h1>");
                for (AbstractUser s : users) {
                    out.println("<div class=\"container stats\">\n" +
                            "<form>\n" +
                            "<table>\n" +
                            "    <thead>\n" +
                            "        <tr>\n" +
                            "            <th colspan=\"2\"> Ranking: " + (counter + 1) + " " + s.getUserName() + "</th>\n" +
                            "        </tr>\n" +
                            "    </thead>\n" +
                            "    <tbody>\n" +
                            "        <tr>\n" +
                            "            <td>Amount: " + s.getAmount() + "</td>\n" +
                            "        </tr>\n" +
                            "    </tbody>\n" +
                            "</table>\n" +
                            "</form>\n" +
                            "</div>" +
                            "</div>");
                    counter++;

                }
            } else {
                out.println("something went wrong");
            }
            addBootStrapFunctionality(out);
            loadJSScripts(out);
            out.print("</body>");
            out.print("</html>");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

