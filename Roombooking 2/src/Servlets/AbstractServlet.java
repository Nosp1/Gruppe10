package Servlets;

import javax.servlet.http.HttpServlet;
import java.io.PrintWriter;

/**
 * Handles necessary hmtl configurations used in Servlet childs for easier outprints.
 * @see javax.servlet.http.HttpServlet
 * @author trym
 *
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
                "    <title>loggedIn</title>\n" +
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
                "            <form class=\"navbar-form navbar-left\" id=\"search-form\">\n" +
                "                <div class=\"form-group\">\n" +
                "                    <input type=\"text\" class=\"form-control\" placeholder=\"Search\">\n" +
                "                </div>\n" +
                "                <button type=\"submit\" class=\"btn btn-default\">Submit</button>\n" +
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
}
