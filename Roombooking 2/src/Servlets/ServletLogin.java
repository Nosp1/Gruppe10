package Servlets;

import Tools.DbFunctionality;
import Tools.DbTool;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
/**
 * @author trym
 *
 */

@WebServlet(name = "Servlets.ServletLogin", urlPatterns = {"/Servlets.ServletLogin"})
public class ServletLogin extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            Servlet servlet = new Servlet();
            servlet.printNav(out);
            String userName = request.getParameter("username");
            String password = request.getParameter("loginpassword");
            String action = request.getParameter("action");

            if (action.toLowerCase().contains("login")) {
                DbTool dbTool = new DbTool();
                Connection connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();
                dbFunctionality.checkUser(userName.toLowerCase(), out, connection);
                //out.print(userName + password);
                out.print("<button class=\"btn-default btn-lg submit\">\n" +
                        "                <a href=\"index.html\"> return</a>\n" +
                        "            </button>\n");

            }

            servlet.scriptBootstrap(out);
            out.print("</body>");
            out.print("</html>");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
