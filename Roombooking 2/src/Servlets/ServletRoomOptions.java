package Servlets;

import Tools.DbFunctionality;
import Tools.DbTool;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

@WebServlet(name = "Servlets.ServletRoomOptions", urlPatterns = {"/Servlets.ServletRoomOptions"})
public class ServletRoomOptions extends AbstractPostServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {

            printNav(out);
            String action = request.getParameter("action").toLowerCase();
            // TODO: Lag HTML side med action som legger til et rom
            if(action.contains("add")) {
                DbTool dbTool = new DbTool();
                Connection connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();
                /*
                Rest of the code ---
                 */


                addHomeButton(out);

            // TODO: Lag HTML side med action som fjerner et rom
            } else if(action.contains("delete")) {
                DbTool dbTool = new DbTool();
                Connection connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();
                /*
                Rest of the code ---
                 */


                addHomeButton(out);
            }

            scriptBootstrap(out);
            out.print("</body>");
            out.print("</html>");
        }
    }
}
