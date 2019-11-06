package Servlets;


import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author trym
 */

@WebServlet (name = "Servlets.ServletEditProfile", urlPatterns = {"/Servlets.ServletEditProfile"})
public class ServletEditProfile extends AbstractServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter() ) {
            printLoggedInNav(out);
            HttpSession session = request.getSession();
            String userName = (String) session.getAttribute("userEmail");
            ServletContext servletContext = getServletContext();
            session.setAttribute("userEmail", userName);
            servletContext.getRequestDispatcher("/editProfile.html").forward(request, response);
            out.println(userName);
            addHomeLoggedInButton(out);
            addBootStrapFunctionality(out);
            out.println("</body>");
            out.println("</html>");


        }

    }
}
