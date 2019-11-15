package Servlets;

import Tools.DbFunctionality;
import Tools.DbTool;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;

@WebServlet(name = "ServletMostPopularRooms", urlPatterns = {"/Servlets.ServletMostPopularRooms"})
public class ServletMostPopularRooms extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connection = null;
        try (PrintWriter out = response.getWriter()) {
            DbTool dbTool = new DbTool();
            connection = dbTool.dbLogIn(out);
            DbFunctionality dbFunctionality = new DbFunctionality();
            ResultSet rooms = dbFunctionality.getMostBookedRoom(1, connection);
            out.print("[");
            int i = 0;
            while(rooms.next()) {
                if (i > 0) {
                    out.print(",");
                }
                out.print("{");
                int roomId = rooms.getInt("room_id");
                int amount = rooms.getInt("amount");
                String name = rooms.getString("room_name");
                out.print("\"roomId\":\"" + Integer.toString(roomId) + "\"");
                out.print(",");
                out.print("\"roomName\":\"" + name + "\"");
                out.print(",");
                out.print("\"amount\":\"" + Integer.toString(amount) + "\"");
                out.print("}");
                i++;
            }
            out.println("]");
            // out.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}