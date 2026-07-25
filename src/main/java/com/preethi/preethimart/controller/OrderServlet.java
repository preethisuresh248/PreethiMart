package com.preethi.preethimart.controller;

import com.preethi.preethimart.dto.OrderDTO;
import com.preethi.preethimart.model.User;
import com.preethi.preethimart.service.CartService;
import com.preethi.preethimart.service.CartServiceImpl;
import com.preethi.preethimart.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "OrderServlet", urlPatterns = {"/orders", "/orders/*"})
public class OrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();
    private final CartService cartService = new CartServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = getCurrentUser(req);
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login?redirect=" + req.getRequestURI());
            return;
        }

        String path = req.getPathInfo();
        if (path != null && path.startsWith("/detail")) {
            showDetail(req, resp, currentUser);
            return;
        }

        List<OrderDTO> orders = orderService.getOrdersForBuyer(currentUser.getId());
        req.setAttribute("orders", orders);
        req.setAttribute("cartItemCount", cartService.getCartCount(currentUser.getId()));
        req.getRequestDispatcher("/WEB-INF/views/buyer/orders.jsp").forward(req, resp);
    }

    private void showDetail(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        try {
            Long orderId = Long.parseLong(idStr);
            OrderDTO order = orderService.getOrderById(orderId);
            if (!order.getBuyerId().equals(user.getId()) && !user.isAdmin()) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized view.");
                return;
            }
            req.setAttribute("order", order);
            req.getRequestDispatcher("/WEB-INF/views/buyer/order-detail.jsp").forward(req, resp);
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/orders");
        }
    }

    private User getCurrentUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return (session != null) ? (User) session.getAttribute("currentUser") : null;
    }
}
