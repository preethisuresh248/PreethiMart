package com.preethi.preethimart.controller;

import com.preethi.preethimart.dto.CartDTO;
import com.preethi.preethimart.dto.OrderDTO;
import com.preethi.preethimart.exception.ValidationException;
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

@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout", "/checkout/*"})
public class CheckoutServlet extends HttpServlet {

    private final CartService cartService = new CartServiceImpl();
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getCurrentUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login?redirect=" + req.getRequestURI());
            return;
        }

        String path = req.getPathInfo();
        if (path != null && path.startsWith("/success")) {
            showSuccess(req, resp);
            return;
        }

        CartDTO cart = cartService.getCart(user.getId());
        if (cart.getItems().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        req.setAttribute("cart", cart);
        req.setAttribute("cartItemCount", cart.getTotalItems());
        req.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getCurrentUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        String address = req.getParameter("shippingAddress");
        String paymentMethod = req.getParameter("paymentMethod");

        try {
            OrderDTO placedOrder = orderService.placeOrder(user.getId(), address, paymentMethod);
            resp.sendRedirect(req.getContextPath() + "/checkout/success?orderId=" + placedOrder.getId());
        } catch (ValidationException e) {
            CartDTO cart = cartService.getCart(user.getId());
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("shippingAddress", address);
            req.setAttribute("paymentMethod", paymentMethod);
            req.setAttribute("cart", cart);
            req.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(req, resp);
        }
    }

    private void showSuccess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String orderIdStr = req.getParameter("orderId");
        if (orderIdStr == null) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        try {
            Long orderId = Long.parseLong(orderIdStr);
            OrderDTO order = orderService.getOrderById(orderId);
            req.setAttribute("order", order);
            req.getRequestDispatcher("/WEB-INF/views/buyer/order-success.jsp").forward(req, resp);
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/orders");
        }
    }

    private User getCurrentUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return (session != null) ? (User) session.getAttribute("currentUser") : null;
    }
}
