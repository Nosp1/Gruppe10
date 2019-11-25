package Servlets;
import Classes.User.AbstractUser;
import Classes.UserType;
import javax.servlet.http.*;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * Handles necessary hmtl configurations used in Servlet childs for easier outprints.
 * @see javax.servlet.http.HttpServlet
 * @author trym, brisdalen
 *
 */
/* For the servlets to get redirected properly, the following annotation has to be provided:
 @WebServlet(name = "Servlets.ServletName", urlPatterns = {"/Servlets.ServletName"})
  */
public abstract class AbstractServlet extends HttpServlet {
    /**
     * Method allows all children of  {@code AbstractServlet}
     to call the bootstrap.js ref and jquery for responsive Navbar.
     * @param out for printing html
     */
    void addBootStrapFunctionality(PrintWriter out) {
        out.println("<script\n" +
                "        src=\"https://code.jquery.com/jquery-3.4.1.js\"\n" +
                "        integrity=\"sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU=\"\n" +
                "        crossorigin=\"anonymous\"></script>\n" +
                "<script src=\"bootstrap.js\"></script>");
    }

    void loadJSScripts(PrintWriter out) {
        out.println("<script src=\"script.js\"></script>");
    }

    /**
     * Prints a html button that returns you to landing page index.html
     * @param out to print html
     */
    void addHomeButton(PrintWriter out) {
        out.print("<button class=\"btn-default btn-lg submit\">\n" +
                "                <a href=\"index.html\"> return</a>\n" +
                "            </button>\n");
    }

    /**
     * Prints html button that returns you to the logged in page.
     * //TODO: might need cookie to remember which session?
     * @param out to print html
     *
     */
    void addHomeLoggedInButton (PrintWriter out) {
        out.println("<button class=\"btn-default btn-lg submit\">\n" +
                "                <a href=\"loggedIn.html\"> return</a>\n" +
                "            </button>\n");
    }

    void addProfileButton(PrintWriter out) {
        out.println("<button class=\"btn-default btn-lg submit\">\n" +
                "                <a href=\"profile.html\"> return</a>\n" +
                "            </button>\n");
    }

    void addHiddenCalendar(PrintWriter out) {
        out.println("<div id=\"calendar\" hidden>");
        printCalendarRemains(out);
    }

    void addCalendar(PrintWriter out) {
        out.println("<div id=\"calendar\">");
        printCalendarRemains(out);
    }

    private void printCalendarRemains(PrintWriter out) {
        out.print("<button id=\"previousDay\" style=\"color: black;\">Previous day</button>" +
                "<input type=\"date\">" +
                "<button id=\"nextDay\" style=\"color: black;\">Next day</button>" +
                "</div>");
    }

    /**
     *
     * @param out The response body to write to
     * @param redirectTo The html page you want to redirect to
     */
    void addRedirectButton(PrintWriter out, String redirectTo) {
        out.println("<div><button class=\"btn-default btn-lg submit\">\n" +
                "                <a href=\"" + redirectTo + "\"> return</a>\n" +
                "            </button></div>\n");
    }

    /**
     *
     * @param out The response body to write to
     * @param redirectTo The html page you want to redirect to
     */
    void addRedirectButtonInline(PrintWriter out, String redirectTo) {
        out.print("<button class=\"btn-default btn-lg submit\">\n" +
                "                <a href=\"" + redirectTo + "\"> return</a>\n" +
                "            </button>\n");
    }

    /**
     * Adds a redirection button back to the "home" page based on the user type
     * @param out
     * @param userType
     */
    void addRedirectOnUserType(PrintWriter out, UserType userType) {
        switch(userType) {

            case ADMIN:
                addRedirectButton(out, "loggedInAdmin.html");
                break;

            default:
                addRedirectButton(out, "loggedIn.html");
                break;
        }
    }

    /**
     * Adds a redirection button back to the "home" page based on the user type, without its own div
     * @param out
     * @param userType
     */
    void addRedirectOnUserTypeInline(PrintWriter out, UserType userType) {
        switch(userType) {

            case ADMIN:
                addRedirectButtonInline(out, "loggedInAdmin.html");
                break;

            default:
                addRedirectButtonInline(out, "loggedIn.html");
                break;
        }
    }

    boolean isAdmin (AbstractUser user, Connection connection) {
        // Sjekker om brukeren er en administrator, og returnerer feil om de ikke er det
        UserType userType = user.getUserType();
        System.out.println(userType.toString());
        if (userType.toString().equals("ADMIN")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Adds Navigation bar to Servlet landing page.
     *
     * @see PrintWriter
     * @param out Prints html tags;  <!DocType> <html> <title> <meta charset>
     */
    void printNav(PrintWriter out) {
        out.println(
                "<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<head>\n" +
                        "    <title>register form</title>\n" +
                        "    <link rel=\"stylesheet\" type=\"text/css\" href=\"./css/bootstrap.css\">" +
                        "    <link rel=\"stylesheet\" type=\"text/css\" href=\"roombooking.css\">\n" +
                        "    <meta charset=\"utf-8\">\n" +
                        "\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<nav class=\"navbar navbar-default\">\n" +
                        "    <div class=\"container-fluid\">\n" +
                        "        <!-- Brand and toggle get grouped for better mobile display -->\n" +
                        "        <div class=\"navbar-header\">\n" +
                        "            <button type=\"button\" class=\"navbar-toggle collapsed\" data-toggle=\"collapse\"\n" +
                        "                    data-target=\"#bs-example-navbar-collapse-1\" aria-expanded=\"false\">\n" +
                        "                <span class=\"sr-only\">Toggle navigation</span>\n" +
                        "                <span class=\"icon-bar\"></span>\n" +
                        "                <span class=\"icon-bar\"></span>\n" +
                        "                <span class=\"icon-bar\"></span>\n" +
                        "            </button>\n" +
                        "            <a class=\"navbar-brand\" href=\"index.html\">Roombooking</a>\n" +
                        "        </div>\n" +
                        "\n" +
                        "        <!-- Collect the nav links, forms, and other content for toggling -->\n" +
                        "        <div class=\"collapse navbar-collapse\" id=\"bs-example-navbar-collapse-1\">\n" +
                        "            <ul class=\"nav navbar-nav\">\n" +
                        "                <li class=\"active\"><a href=\"#\">Link <span class=\"sr-only\">(current)</span></a></li>\n" +
                        "                <li><a href=\"#\">Link</a></li>\n" +
                        "                <li class=\"dropdown\">\n" +
                        "                    <a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\" role=\"button\" aria-haspopup=\"true\"\n" +
                        "                       aria-expanded=\"false\">Dropdown <span class=\"caret\"></span></a>\n" +
                        "                    <ul class=\"dropdown-menu\">\n" +
                        "                        <li><a href=\"#\">Action</a></li>\n" +
                        "                        <li><a href=\"#\">Another action</a></li>\n" +
                        "                        <li><a href=\"#\">Something else here</a></li>\n" +
                        "                        <li role=\"separator\" class=\"divider\"></li>\n" +
                        "                        <li><a href=\"#\">Separated link</a></li>\n" +
                        "                        <li role=\"separator\" class=\"divider\"></li>\n" +
                        "                        <li><a href=\"#\">One more separated link</a></li>\n" +
                        "                    </ul>\n" +
                        "                </li>\n" +
                        "            </ul>\n" +
                        "            <form class=\"navbar-form navbar-left\">\n" +
                        "                <div class=\"form-group\">\n" +
                        "                    <input type=\"text\" class=\"form-control\" placeholder=\"Search\">\n" +
                        "                </div>\n" +
                        "                <button type=\"submit\" class=\"btn btn-default\">Submit</button>\n" +
                        "            </form>\n" +
                        "            <ul class=\"nav navbar-nav navbar-right\">\n" +
                        "                <li><a href=\"#\">Link</a></li>\n" +
                        "                <li class=\"dropdown\">\n" +
                        "                    <a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\" role=\"button\" aria-haspopup=\"true\"\n" +
                        "                       aria-expanded=\"false\">Dropdown <span class=\"caret\"></span></a>\n" +
                        "                    <ul class=\"dropdown-menu\">\n" +
                        "                        <li><a href=\"#\">Action</a></li>\n" +
                        "                        <li><a href=\"#\">Another action</a></li>\n" +
                        "                        <li><a href=\"#\">Something else here</a></li>\n" +
                        "                        <li role=\"separator\" class=\"divider\"></li>\n" +
                        "                        <li><a href=\"#\">Separated link</a></li>\n" +
                        "                    </ul>\n" +
                        "                </li>\n" +
                        "            </ul>\n" +
                        "        </div><!-- /.navbar-collapse -->\n" +
                        "    </div><!-- /.container-fluid -->\n" +
                        "</nav>"
        );
    }
    void printLoggedInNav(PrintWriter out) {
        out.println("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <title>Logged In</title>\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"css/bootstrap.css\">\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"roombooking.css\">\n" +
                "    <meta charset=\"utf-8\">\n" +
                "\n" +
                "</head>\n" +
                "<body>" +
                "<nav class=\"navbar navbar-default\">\n" +
                "    <div class=\"container-fluid\">\n" +
                "        <!-- Brand and toggle get grouped for better mobile display -->\n" +
                "        <div class=\"navbar-header\">\n" +
                "            <button type=\"button\" class=\"navbar-toggle collapsed\" data-toggle=\"collapse\"\n" +
                "                    data-target=\"#bs-example-navbar-collapse-1\" aria-expanded=\"false\">\n" +
                "                <span class=\"sr-only\">Toggle navigation</span>\n" +
                "                <span class=\"icon-bar\"></span>\n" +
                "                <span class=\"icon-bar\"></span>\n" +
                "                <span class=\"icon-bar\"></span>\n" +
                "            </button>\n" +
                "            <a class=\"navbar-brand\" href=\"loggedIn.html\">Roombooking</a>\n" +
                "        </div>\n" +
                "\n" +
                "        <!-- Collect the nav links, forms, and other content for toggling -->\n" +
                "        <div class=\"collapse navbar-collapse\" id=\"bs-example-navbar-collapse-1\">\n" +
                "            <form class=\"navbar-form navbar-left\" id=\"navbar-search-form\">\n" +
                "                <div class=\"form-group\">\n" +
                "                    <input type=\"text\" class=\"form-control\" id=\"navbar-search-input\" placeholder=\"Search\">\n" +
                "                </div>\n" +
                "                <button type=\"button\" class=\"btn btn-default\" id=\"navbar-search-button\">Submit</button>\n" +
                "            </form>\n" +
                "            <ul class=\"nav navbar-nav navbar-right\">\n" +
                "                <li>\n" +
                "                    <a href=\"./profile.html\">User <span class=\"glyphicon glyphicon-user\"\n" +
                "                                                        aria-hidden=\"true\"></span>\n" +
                "                    </a>\n" +
                "                </li>\n" +
                "\n" +
                "                <li>\n" +
                "                    <div id=\"logout\">\n" +
                "                        <form action=\"./Servlets.ServletLogOut\" method=\"post\">\n" +
                "                            <div>\n" +
                "                                <input class=\"submit btn-default btn-lg\" type=\"submit\" name=\"action\" value=\"Log out\"/>\n" +
                "                            </div>\n" +
                "                        </form>\n" +
                "                    </div>\n" +
                "                </li>\n" +
                "            </ul>\n" +
                "        </div><!-- /.navbar-collapse -->\n" +
                "    </div><!-- /.container-fluid -->\n" +
                "</nav>");
    }


    void invalidateOldSession(HttpServletRequest request) {
        HttpSession oldSession = request.getSession(false);
        if(oldSession != null) {
            oldSession.invalidate();
        }
    }

    HttpSession generateNewSession(HttpServletRequest request, int minutes) {
        HttpSession newSession = request.getSession(true);
        newSession.setMaxInactiveInterval(minutes*60);
        return newSession;
    }

    void debugSession(HttpServletRequest request) {
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
    Cookie generatePersistentUserTypeCookie(String userName, UserType userType, HttpServletResponse response, int minutes) {
        Cookie userTypeCookie = new Cookie("user_type_persistent", userName + ":" + userType.toString());
        userTypeCookie.setMaxAge(minutes*60);
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
    Cookie generateUserTypeCookie(String userName, UserType userType, HttpServletResponse response) {
        Cookie userTypeCookie = new Cookie("user_type", userName + ":" + userType.toString());
        userTypeCookie.setMaxAge(-1);
        // Makes the cookie visible to all directories on the server
        userTypeCookie.setPath("/");

        return userTypeCookie;
    }

    void closeConnection(Connection connection) {
        try {
            assert connection != null;
            if (connection.isClosed()) {
                System.out.println("connection closed");
            } else {
                System.out.println(connection + "is not closed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
