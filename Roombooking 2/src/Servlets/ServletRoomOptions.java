package Servlets;

import Classes.AbstractRoom;
import Classes.Grouproom;
import Classes.Order;
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
import java.sql.Timestamp;

@WebServlet(name = "Servlets.ServletRoomOptions", urlPatterns = {"/Servlets.ServletRoomOptions"})
public class ServletRoomOptions extends AbstractPostServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {

            printNav(out);

            String action = request.getParameter("action").toLowerCase();

            if(action.contains("add")) {
                int roomID = Integer.parseInt(request.getParameter("Add_roomID"));
                String roomName = request.getParameter("Add_roomName");
                String roomBuilding = request.getParameter("Add_roomBuilding");
                int maxCapacity = Integer.parseInt(request.getParameter("maxCapacity"));

                DbTool dbTool = new DbTool();
                Connection connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();

                AbstractRoom room = new Grouproom(roomID, roomName, roomBuilding, maxCapacity);
                // TODO: Bruker kun grupperom typen for nå
                dbFunctionality.addRoom(room, connection);
                //TODO: legg til annen knapp for å forbli logget inn.
                addHomeButton(out);

            // TODO: Lag HTML side med action som fjerner et rom
            } else if(action.contains("delete")) {
                int roomID = Integer.parseInt(request.getParameter("Delete_roomID"));
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

            } else if(action.contains("reserve")) {
                int roomId = Integer.parseInt(request.getParameter("Reserve_RoomId"));
                Timestamp timestampStart = Timestamp.valueOf(request.getParameter("Reserve_startTime"));
                Timestamp timeestampEnd = Timestamp.valueOf(request.getParameter("Reserve_endTime"));

                DbTool dbTool = new DbTool();
                Connection connection = dbTool.dbLogIn(out);
                DbFunctionality dbFunctionality = new DbFunctionality();

                // TODO ADD AUTOMATIC ORDERID AND USERID
                Order order = new Order(1,1, roomId, timestampStart, timeestampEnd);
                dbFunctionality.addOrder(order, connection);
            }

            scriptBootstrap(out);
            out.print("</body>");
            out.print("</html>");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
