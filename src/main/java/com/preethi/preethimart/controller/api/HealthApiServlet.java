package com.preethi.preethimart.controller.api;

import com.preethi.preethimart.util.DBConnectionManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

@WebServlet(name = "HealthApiServlet", urlPatterns = {"/api/v1/health"})
public class HealthApiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String dbStatus = "DOWN";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             Statement st = conn.createStatement()) {
            st.execute("SELECT 1");
            dbStatus = "UP";
        } catch (Exception ignored) {}

        if ("UP".equals(dbStatus)) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"status\":\"UP\",\"db\":\"UP\"}");
        } else {
            resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            resp.getWriter().write("{\"status\":\"DEGRADED\",\"db\":\"DOWN\"}");
        }
    }
}
