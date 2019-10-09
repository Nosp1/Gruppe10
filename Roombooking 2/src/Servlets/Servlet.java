package Servlets;

import Classes.Email.EmailUtil;
import Classes.Email.TLSEmail;
import Classes.User.AbstractUser;
import Classes.User.Student;
import Classes.Email.EmailTemplates;
import Tools.DbFunctionality;
import Tools.DbTool;

import javax.mail.Session;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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
            //prints html tags: <!Doctype html> <Head> <body> from parent class AbstractServlet
            printNav(out);
            //gets the value of the button register.
            String action = request.getParameter("action").toLowerCase();
            if (action.contains("register")) {
                //Gets the users firstname, lastname, email, date of birth and password.
                String firstName = request.getParameter("firstName").toLowerCase();
                String lastName = request.getParameter("lastName").toLowerCase();
                String email = request.getParameter("email").toLowerCase();
                String dob = request.getParameter("dob").toLowerCase();
                String password = request.getParameter("password");

                DbTool dbtool = new DbTool();
                //establish connection to database
                Connection connection = dbtool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();
                //generates a new user with the information from the form register
                AbstractUser newUser = new Student(firstName, lastName, email, password, dob);
                //sends the new users data and adds it to the database
                //checks for already registered user.
                if (dbFunctionality.checkUser(newUser.getUserName(), newUser.getPassword(), connection)) {
                    out.println("You have already registered with that email");

                    ServletContext servletContext = getServletContext();
                    servletContext.getRequestDispatcher("/index.html").forward(request,response);
                }
                else {

                    dbFunctionality.addUser(newUser, connection);
                    out.println("<p> You have successfully registered</p>");
                    //Generates and sends a welcome email to the newly registered user
                    //todo refactor into method?
                    TLSEmail tlsEmail = new TLSEmail();
                    //creates current email session & returns the session
                    Session session = tlsEmail.NoReplyEmail(newUser.getUserName());
                    EmailUtil newEmail = new EmailUtil();
                    //gets the standard welcome message as subject
                    String welcome = EmailTemplates.getWelcome();
                    //Sets the first letter of the recipients name to a capital letter.
                    String capName = newUser.getFirstName().substring(0, 1).toUpperCase() + newUser.getFirstName().substring(1);
                    //inserts the capitalised name into the body of the email.
                    String body = EmailTemplates.welcomeMessageBody(capName);
                    //sends email
                    newEmail.sendEmail(session, newUser.getUserName(), welcome, body);
                    //prints HomeButton.
                    addHomeButton(out);
                }
            } else {
                //if the user is not registered.
                out.print("something went wrong");
            }
            // Prints Javascript connection to Bootstrap.js and other dependencies. See AbstractServlet
            addBootStrapFunctionality(out);
            out.println("</body>");
            out.println("</html>");
        } catch (NoSuchAlgorithmException | SQLException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }
}
