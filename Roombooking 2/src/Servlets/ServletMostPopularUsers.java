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

@WebServlet(name = "ServletMostPopularUsers", urlPatterns = {"/Servlets.ServletMostPopularUsers"})
public class ServletMostPopularUsers extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connection = null;
        try (PrintWriter out = response.getWriter()) {
            DbTool dbTool = new DbTool();
            connection = dbTool.dbLogIn(out);
            DbFunctionality dbFunctionality = new DbFunctionality();
            ResultSet users = dbFunctionality.getMostActiveUsers(1, connection);
            out.print("[");
            int i = 0;
            while(users.next()) {
                if (i > 0) {
                    out.print(",");
                }
                out.print("{");
                int userId = users.getInt("user_id");
                int amount = users.getInt("amount");
                String firstName = users.getString("User_firstName");
                String lastName = users.getString("User_lastName");
                out.print("\"userId\":\"" + Integer.toString(userId) + "\"");
                out.print(",");
                out.print("\"userName\":\"" + firstName + " " + lastName + "\"");
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