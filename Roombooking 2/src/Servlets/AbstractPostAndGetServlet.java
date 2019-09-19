package Servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractPostAndGetServlet extends AbstractServlet {

    protected abstract void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

    protected abstract void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
