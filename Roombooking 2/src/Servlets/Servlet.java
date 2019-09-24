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
            String firstName = request.getParameter("firstName").toLowerCase();
            String lastName = request.getParameter("lastName").toLowerCase();
            String email = request.getParameter("email").toLowerCase();
            String action = request.getParameter("action").toLowerCase();
            String dob = request.getParameter("dob").toLowerCase();
            String password = request.getParameter("password");

            if (action.contains("register")) {
                DbTool dbtool = new DbTool();
                Connection connection = dbtool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();
                dbFunctionality.addUser(firstName, lastName, email, password, dob, connection);
                out.println("<p> You have successfully registered</p>");
                addHomeButton(out);

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
