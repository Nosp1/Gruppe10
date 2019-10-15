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
import java.sql.SQLException;

@WebServlet(name = "ServletSearch", urlPatterns = {"/Servlets.ServletSearch"})
public class ServletSearch extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int roomId = Integer.parseInt(request.getParameter("roomId"));
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            DbTool dbTool = new DbTool();
            Connection connection = dbTool.dbLogIn(out);
            DbFunctionality dbFunctionality = new DbFunctionality();
            try {
                if (!dbFunctionality.checkRoom(roomId, connection)) {
                    out.print("{\"error\": \"Room not found\"}");
                    return;
                }
                dbFunctionality.getTodayOrders(roomId, out, connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            out.close();
        }
    }
}
