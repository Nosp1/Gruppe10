package Servlets;

import Tools.DbFunctionality;
import Tools.DbTool;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Handles Login from index.html and parses String parameters to {@code DbFunctionality} to check for
 * {@code String} is existing in Database.
 *
 * @author trym Brisdalen
 * @see Servlets.AbstractServlet
 * @see javax.servlet.http.HttpServlet
 * @see javax.servlet.GenericServlet
 * @see DbFunctionality
 * @see DbTool
 */

@WebServlet(name = "Servlets.ServletLogin", urlPatterns = {"/Servlets.ServletLogin"})
public class ServletLogin extends AbstractServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            printNav(out);
            String userName = request.getParameter("username");
            String lowercaseUsername = userName.toLowerCase();
            String password = request.getParameter("loginpassword");
            String action = request.getParameter("action");

            if (action.toLowerCase().contains("login")) {
                DbTool dbTool = new DbTool();
                Connection connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();

                if (dbFunctionality.checkUser(lowercaseUsername, password, connection)) {
                    // If successful login
                    System.out.println("success!");
                    out.print("Welcome " + lowercaseUsername + "!");
                    out.print("<br>");
                    ServletContext servletContext = getServletContext();
                    servletContext.getRequestDispatcher("/loggedIn.html").forward(request,response);

                } else {
                    // If not TODO: Add outprint error message for wrong password vs email
                    System.out.println("fail");
                    out.println("Sorry, we do not recognize \"" + lowercaseUsername + "\".");
                    out.print("<br>");
                }

                addHomeButton(out);

            }

            scriptBootstrap(out);
            out.print("</body>");
            out.print("</html>");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


}
