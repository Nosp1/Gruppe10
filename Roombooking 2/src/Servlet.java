import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

@WebServlet(name = "Servlet", urlPatterns = {"/Servlet"})
public class Servlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try(PrintWriter out = response.getWriter()) {
            printNav(out);
            String firstname = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String day = request.getParameter("day");
            String month = request.getParameter("month");
            String year = request.getParameter("year");
            String action = request.getParameter("action");
            String dob = day + "/" + month + "/" + year;


            System.out.println(firstname + lastName + email + day + action + dob);

            if (action.contains("register")) {
                DbTool dbtool = new DbTool();
               Connection connection = dbtool.dbLogIn(out);
               DbFunctionality dbFunctionality = new DbFunctionality();
               dbFunctionality.addUser(firstname,lastName,email,dob, out, connection);
                out.println(firstname + "has been added to db");

            }
            else {
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


    public void printNav(PrintWriter out){
        out.println(
                "<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<head>\n" +
                        "  <title>register form</title>\n" +
                        "  <link rel=\"stylesheet\" type=\"text/css\" href=\"bootstrap.css\">\n" +
                        "  <style type=\"text/css\">\n" +
                        "    .navbar-brand {\n" +
                        "      background: #216732;\n" +
                        "    }\n" +
                        "    body {\n" +
                        "      background-color: rgb(83, 140, 19);\n" +
                        "    }\n" +
                        "    .btn-default {\n" +
                        "      background-color: orange;\n" +
                        "    }\n" +
                        "  </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<nav class=\"navbar navbar-default\">\n" +
                        "  <div class=\"container-fluid\">\n" +
                        "    <!-- Brand and toggle get grouped for better mobile display -->\n" +
                        "    <div class=\"navbar-header\">\n" +
                        "      <button type=\"button\" class=\"navbar-toggle collapsed\" data-toggle=\"collapse\" data-target=\"#bs-example-navbar-collapse-1\" aria-expanded=\"false\">\n" +
                        "        <span class=\"sr-only\">Toggle navigation</span>\n" +
                        "        <span class=\"icon-bar\"></span>\n" +
                        "        <span class=\"icon-bar\"></span>\n" +
                        "        <span class=\"icon-bar\"></span>\n" +
                        "      </button>\n" +
                        "      <a class=\"navbar-brand\" href=\"index.html\">Roombooking</a>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <!-- Collect the nav links, forms, and other content for toggling -->\n" +
                        "    <div class=\"collapse navbar-collapse\" id=\"bs-example-navbar-collapse-1\">\n" +
                        "      <ul class=\"nav navbar-nav\">\n" +
                        "        <li class=\"active\"><a href=\"#\">Link <span class=\"sr-only\">(current)</span></a></li>\n" +
                        "        <li><a href=\"#\">Link</a></li>\n" +
                        "        <li class=\"dropdown\">\n" +
                        "          <a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\" role=\"button\" aria-haspopup=\"true\" aria-expanded=\"false\">Dropdown <span class=\"caret\"></span></a>\n" +
                        "          <ul class=\"dropdown-menu\">\n" +
                        "            <li><a href=\"#\">Action</a></li>\n" +
                        "            <li><a href=\"#\">Another action</a></li>\n" +
                        "            <li><a href=\"#\">Something else here</a></li>\n" +
                        "            <li role=\"separator\" class=\"divider\"></li>\n" +
                        "            <li><a href=\"#\">Separated link</a></li>\n" +
                        "            <li role=\"separator\" class=\"divider\"></li>\n" +
                        "            <li><a href=\"#\">One more separated link</a></li>\n" +
                        "          </ul>\n" +
                        "        </li>\n" +
                        "      </ul>\n" +
                        "      <form class=\"navbar-form navbar-left\">\n" +
                        "        <div class=\"form-group\">\n" +
                        "          <input type=\"text\" class=\"form-control\" placeholder=\"Search\">\n" +
                        "        </div>\n" +
                        "        <button type=\"submit\" class=\"btn btn-default\">Submit</button>\n" +
                        "      </form>\n" +
                        "      <ul class=\"nav navbar-nav navbar-right\">\n" +
                        "        <li><a href=\"#\">Link</a></li>\n" +
                        "        <li class=\"dropdown\">\n" +
                        "          <a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\" role=\"button\" aria-haspopup=\"true\" aria-expanded=\"false\">Dropdown <span class=\"caret\"></span></a>\n" +
                        "          <ul class=\"dropdown-menu\">\n" +
                        "            <li><a href=\"#\">Action</a></li>\n" +
                        "            <li><a href=\"#\">Another action</a></li>\n" +
                        "            <li><a href=\"#\">Something else here</a></li>\n" +
                        "            <li role=\"separator\" class=\"divider\"></li>\n" +
                        "            <li><a href=\"#\">Separated link</a></li>\n" +
                        "          </ul>\n" +
                        "        </li>\n" +
                        "      </ul>\n" +
                        "    </div><!-- /.navbar-collapse -->\n" +
                        "  </div><!-- /.container-fluid -->\n" +
                        "</nav>"
        );
    }

}
