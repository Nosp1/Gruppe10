package Servlets;

import Classes.AbstractRoom;
import Classes.Grouproom;
import Tools.DbFunctionality;
import Tools.DbTool;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "Servlets.ServletRoomOptions", urlPatterns = {"/Servlets.ServletRoomOptions"})
public class ServletRoomOptions extends AbstractPostServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {

            printNav(out);
                                        // TODO: Gjør dette andre steder også?
            String action = request.getParameter("action").toLowerCase();

            // TODO: Lag HTML side med action som legger til et rom
            if(action.contains("add")) {
                String roomID = request.getParameter("Add_roomID");
                String roomFloor = request.getParameter("Add_roomFloor");
                String maxCapacity = request.getParameter("maxCapacity");

                DbTool dbTool = new DbTool();
                Connection connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();

                int parsedMaxCapacity = Integer.parseInt(maxCapacity);
                AbstractRoom room = new Grouproom(roomID, roomFloor, parsedMaxCapacity);
                // TODO: Bruker kun grupperom typen for nå
                dbFunctionality.addRoom(room, connection);

                addHomeButton(out);

            // TODO: Lag HTML side med action som fjerner et rom
            } else if(action.contains("delete")) {
                String roomID = request.getParameter("Delete_roomID");

                DbTool dbTool = new DbTool();
                Connection connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();

                dbFunctionality.deleteRoom(roomID, connection);

                addHomeButton(out);

            } else if(action.contains("show")) {
                DbTool dbTool = new DbTool();
                Connection connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();
                dbFunctionality.printRooms(out, connection);
            }

            scriptBootstrap(out);
            out.print("</body>");
            out.print("</html>");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
