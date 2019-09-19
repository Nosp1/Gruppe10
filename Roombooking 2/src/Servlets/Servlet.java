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
 * Handles the Register form.
 * Adds user to db based on parameters from index.html
 *
 * @author trym
 * @see java.io.Serializable
 * @see javax.servlet.Servlet
 */

@WebServlet(name = "Servlets.Servlet", urlPatterns = {"/Servlets.Servlet"})
public class Servlet extends AbstractPostServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            printNav(out); //void method that prints start of html from parent class AbstractServlet
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String action = request.getParameter("action");
            String dob = request.getParameter("dob");
            String password = request.getParameter("password");

            if (action.toLowerCase().contains("register")) {
                DbTool dbtool = new DbTool();
                Connection connection = dbtool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();
                dbFunctionality.addUser(firstName, lastName, email, password, dob, out, connection);
                out.println("<p> You have successfully registered</p>");
                out.println("<button class=\"submit btn-default btn-lg\">\n" +
                        "\t\t\t<a href=\"index.html\">return</a>\n" +
                        "\t\t</button>");
                out.print("<p> Or you can login here:\n" +
                        "<button class=\"submit btn-default btn-lg\">\n" + "<a href=#>Login</a\n" +
                        "</button");

            } else if (action.toLowerCase().contains("database")) {
                DbTool dbTool = new DbTool();
                Connection connection = dbTool.dbLogIn(out);
                dbTool.printResults(out);

            } else {
                out.print("something went wrong");
            }

            scriptBootstrap(out); // Prints Javascript connection to Bootstrap.js and other dependencies. See AbstractServlet
            out.println("</body>");
            out.println("</html>");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
