package Servlets;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author trym, hedda
 */
@WebServlet(name = "Servlets.ServletLogOut", urlPatterns = {"/Servlets.ServletLogOut"})
public class ServletLogOut extends AbstractPostServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {

        String action = request.getParameter("action").toLowerCase();

        if(action.contains("log out")) {
            Cookie[] cookies = request.getCookies();
            for(Cookie c : cookies) {
                if(c.getName().contains("user_type")) {
                    // Set the max age of the Cookie to clear it
                    c.setMaxAge(0);
                    // "Adding" the cookie back to the response, to override the old one
                    response.addCookie(c);
                }
            }

            HttpSession session = request.getSession(false);
            if(session != null) {
                session.invalidate();
            }

            ServletContext servletContext = getServletContext();
            servletContext.getRequestDispatcher("/index.html").forward(request, response);
        }
    }
}
