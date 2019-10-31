package Servlets;

import Classes.Email.EmailUtil;
import Classes.Email.TLSEmail;
import Classes.User.AbstractUser;
import Classes.User.Admin;
import Classes.User.Student;
import Classes.User.Teacher;
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
                String userType = request.getParameter("userType").toUpperCase();
                String password = request.getParameter("password");
                out.println(userType);
                DbTool dbtool = new DbTool();
                //establish connection to database
                Connection connection = dbtool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();

                //generates a new user with the information from the form register
                /* Create a new user, and assign a role depending on the userType
                TODO: UserType in database, and userTypeRegistry*/
                AbstractUser newUser;
                if (userType.contains("STUDENT")) {
                    newUser = new Student(firstName, lastName, email, password, dob);
                } else if(userType.contains("TEACHER")){
                    newUser = new Teacher(firstName, lastName, email, password, dob);
                } else {
                    newUser = new Admin(firstName, lastName, email, password, dob);
                }

                /* First checks if you try to register an already existing user, then
                sends the new users data and adds it to the database */
                if (dbFunctionality.checkUser(newUser.getUserName(), newUser.getPassword(), connection)) {
                    out.println("You have already registered with that email");

                    ServletContext servletContext = getServletContext();
                    servletContext.getRequestDispatcher("/index.html").forward(request, response);
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {

                    dbFunctionality.addUser(newUser, connection);
                    out.println("<p> You have successfully registered</p>");
                    //prints a redirect button according to the userType
                    //TODO: kanskje til SWITCH seinere, om det blir flere userTypes?
                    if(userType.contains("ADMIN")) {
                        addRedirectButton(out, "loggedInAdmin.html");
                    } else {
                        addRedirectButton(out, "loggedIn.html");
                    }
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
                    // closes connection to sql.
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();

                    }
                }

            } else {
                //if the user registration doesn't succeed.
                out.print("something went wrong");
            }
            // Prints Javascript connection to Bootstrap.js and other dependencies. See AbstractServlet
            addBootStrapFunctionality(out);
            out.println("</body>");
            out.println("</html>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
