package Servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    * @param out
    */
    // TODO: Finnes det et mer forklarende metode-navn her?
    void scriptBootstrap(PrintWriter out) {
        out.println("<script\n" +
                "        src=\"https://code.jquery.com/jquery-3.4.1.js\"\n" +
                "        integrity=\"sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU=\"\n" +
                "        crossorigin=\"anonymous\"></script>\n" +
                "<script src=\"bootstrap.js\"></script>");
    }

    void addHomeButton(PrintWriter out) {
        out.print("<button class=\"btn-default btn-lg submit\">\n" +
                "                <a href=\"index.html\"> return</a>\n" +
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
}

