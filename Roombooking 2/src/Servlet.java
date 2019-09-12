import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
/**
 * @author trym
 * @see java.io.Serializable
 * @see javax.servlet.Servlet
 *

 */

@WebServlet(name = "Servlet", urlPatterns = {"/Servlet"})
public class Servlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            printNav(out);
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String day = request.getParameter("day");
            String month = request.getParameter("month");
            String year = request.getParameter("year");
            String action = request.getParameter("action");
            String dob = day + "/" + month + "/" + year;

            if (action.toLowerCase().contains("register")) {
                DbTool dbtool = new DbTool();
                Connection connection = dbtool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();
                dbFunctionality.addUser(firstName, lastName, email, dob, out, connection);
                out.println("<p> You have successfully registered</p>");
                out.println("<button class=\"submit btn-default btn-lg\">\n" +
                        "\t\t\t<a href=\"index.html\">return</a>\n" +
                        "\t\t</button>");
            } else {
                out.print("something went wrong");
            }


            out.println("<script\n" +
                    "        src=\"https://code.jquery.com/jquery-3.4.1.js\"\n" +
                    "        integrity=\"sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU=\"\n" +
                    "        crossorigin=\"anonymous\"></script>\n" +
                    "<script src=\"bootstrap.js\"></script>");
            out.println("</body>");
            out.println("</html>");

        }
    }


    public void printNav(PrintWriter out) {
        out.println(
                "<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<head>\n" +
                        "    <title>register form</title>\n" +
                        "    <link rel=\"stylesheet\" type=\"text/css\" href=\"bootstrap.css\">\n" +
                        "    <meta charset=\"utf-8\">\n" +
                        "    <style type=\"text/css\">\n" +
                        "        #collapseExample {\n" +
                        "            background: #2b542c;\n" +
                        "        }\n" +
                        "\n" +
                        "        .jumbotron {\n" +
                        "            color: rgb(41, 187, 48);\n" +
                        "            text-shadow: rgba(252, 117, 0, 0) 0px 0px 10px;\n" +
                        "\n" +
                        "\n" +
                        "            background-image: url(\"https://images.unsplash.com/photo-1519389950473-47ba0277781c?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1350&q=80\");\n" +
                        "            background-size: 100%;\n" +
                        "        }\n" +
                        "\n" +
                        "        .navbar-brand {\n" +
                        "            background: #216732;\n" +
                        "        }\n" +
                        "\n" +
                        "        body {\n" +
                        "            background-color: rgb(41, 187, 105);\n" +
                        "            text-align: center;\n" +
                        "        }\n" +
                        "\n" +
                        "        .btn-default {\n" +
                        "            background-color: orange;\n" +
                        "        }\n" +
                        "\n" +
                        "\n" +
                        "        h1 {\n" +
                        "            margin-bottom: 10px;\n" +
                        "        }\n" +
                        "    </style>\n" +
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
