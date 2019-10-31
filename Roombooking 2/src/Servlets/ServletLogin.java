package Servlets;

import Tools.DbFunctionality;
import Tools.DbTool;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * Handles Login from index.html and parses String parameters to {@code DbFunctionality} to check for
 * {@code String} is existing in Database.
 *
 * @author trym, brisdalen
 * @see Servlets.AbstractServlet
 * @see javax.servlet.http.HttpServlet
 * @see javax.servlet.GenericServlet
 * @see DbFunctionality
 * @see DbTool
 */

@WebServlet(name = "Servlets.ServletLogin", urlPatterns = {"/Servlets.ServletLogin"})
public class ServletLogin extends AbstractServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            System.out.println("Login attempt started");
            //prints start of html tags.
            printNav(out);
            //gets the username -> email in this case.
            String userName = request.getParameter("loginemail").toLowerCase();
            String lowercaseUsername = userName.toLowerCase();
            //gets the users password.
            String password = request.getParameter("loginpassword");
            //gets the form value from action
            String action = request.getParameter("action").toLowerCase();

            if (action.contains("login")) {
                DbTool dbTool = new DbTool();
                //Establishes connection to database
                Connection connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();
                //checks whether the users Email matches the stored password
                if (dbFunctionality.checkUser(lowercaseUsername, password, connection)) {
                    // If successful login TODO: make it pop-up
                    System.out.println("success!");
                    invalidateOldSession(request);

                    out.print("Welcome " + lowercaseUsername + "!");
                    out.print("<br>");
                    //redirects the user to the loggedIn.html
                    ServletContext servletContext = getServletContext();
                    HttpSession newSession = generateNewSession(request, 20);
                    /* TODO: Sende til ulike sider ut ifra bruker-type (student, administrator o.l.)
                    f.eks:  sjekk UserType i database utifra brukernavn
                            if(user.userType.equals("admin") {
                                servletContext.getRequestDispatcher("/loggedInAdmin.html").forward(request, response);
                            } else {
                                servletContext.getRequestDispatcher("/loggedInDefault.html").forward(request, response);
                            }
                     NB: Ikke gjøre det mulig å komme til admin-siden ved kun URL eller med parameter
                     */
                    newSession.setAttribute("userEmail", userName);
                    /* Generate 2 cookies, both containing userType information. The first one will persist for
                    for x amount of minutes (for if you accidentally close the browser), while the other one
                    is deleted on browser close. Logging out will clear both of them. */
                    response.addCookie(generateUserTypeCookie(userName, "ADMIN", response));
                    response.addCookie(generatePersistentUserTypeCookie(userName, "ADMIN", response, 60));
                    servletContext.getRequestDispatcher("/loggedIn.html").forward(request, response);

                    debugSession(request);
                    //if the login fails
                    try {
                        System.out.println("attempting to close");
                        connection.close();
                        if (!connection.isClosed()) {
                            System.out.println("connection is not closed ");
                        } else {
                            System.out.println("connection is closed");
                        }
                        out.close();

                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.out.println("connection failed" + e);
                    }
                } else {
                    // If not TODO: Add out.print error message for wrong password vs email
                    if (connection.isClosed()) {
                        System.out.println("connection closed");
                    } else {
                        connection.close();
                        System.out.println("connection not closed");
                    }
                    System.out.println("fail");
                    out.println("Sorry, we do not recognize \"" + lowercaseUsername + "\".");
                    out.print("<br>");

                }
                //adds a return button if the login fails.
                addHomeButton(out);
            }
            //prints script to establish connection between bootstrap and html
            addBootStrapFunctionality(out);
            out.print("</body>");
            out.print("</html>");
            //prints errors: if the database fails, if the password is wrong.
        } catch (SQLException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void invalidateOldSession(HttpServletRequest request) {
        HttpSession oldSession = request.getSession(false);
        if(oldSession != null) {
            oldSession.invalidate();
        }
    }

    private HttpSession generateNewSession(HttpServletRequest request, int minutes) {
        HttpSession newSession = request.getSession(true);
        //TODO: remember to add * 60 later
        newSession.setMaxInactiveInterval(minutes);
        return newSession;
    }

    private void debugSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Enumeration attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String name = (String) attributeNames.nextElement();
            String value = (String) session.getAttribute(name);
            System.out.println(name + "=" + value + " " + session.getId());
        }
    }

    /**
     * Generates a cookie that will persist for maxAge seconds, even if you close the browser window.
     * @param userName
     * @param userType
     * @param response
     * @param minutes
     * @return
     */
    private Cookie generatePersistentUserTypeCookie(String userName, String userType, HttpServletResponse response, int minutes) {
        Cookie userTypeCookie = new Cookie("user_type_persistent", userName + ":" + userType);
        //TODO: Add *60 to minutes
        userTypeCookie.setMaxAge(minutes);
        // Makes the cookie visible to all directories on the server
        userTypeCookie.setPath("/");

        return userTypeCookie;
    }

    /**
     * Generates a cookie that will be deleted when the Web browser exits.
     * @param userName
     * @param userType
     * @param response
     * @return
     */
    private Cookie generateUserTypeCookie(String userName, String userType, HttpServletResponse response) {
        Cookie userTypeCookie = new Cookie("user_type", userName + ":" + userType);
        userTypeCookie.setMaxAge(-1);
        // Makes the cookie visible to all directories on the server
        userTypeCookie.setPath("/");

        return userTypeCookie;
    }
}
