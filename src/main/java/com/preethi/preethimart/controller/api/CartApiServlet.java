package com.preethi.preethimart.controller.api;

import com.preethi.preethimart.dto.ApiResponse;
import com.preethi.preethimart.dto.CartDTO;
import com.preethi.preethimart.model.User;
import com.preethi.preethimart.service.CartService;
import com.preethi.preethimart.service.CartServiceImpl;
import com.preethi.preethimart.util.JsonUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "CartApiServlet", urlPatterns = {"/api/v1/cart"})
public class CartApiServlet extends HttpServlet {

    private final CartService cartService = new CartServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write(JsonUtil.toJson(ApiResponse.error("Unauthorized")));
            return;
        }

        try {
            CartDTO cart = cartService.getCart(user.getId());
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(JsonUtil.toJson(ApiResponse.ok(cart)));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(JsonUtil.toJson(ApiResponse.error(e.getMessage())));
        }
    }
}
