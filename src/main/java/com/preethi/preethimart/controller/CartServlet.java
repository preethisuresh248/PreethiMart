package com.preethi.preethimart.controller;

import com.preethi.preethimart.dto.CartDTO;
import com.preethi.preethimart.exception.ValidationException;
import com.preethi.preethimart.model.User;
import com.preethi.preethimart.service.CartService;
import com.preethi.preethimart.service.CartServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "CartServlet", urlPatterns = {"/cart", "/cart/*"})
public class CartServlet extends HttpServlet {

    private final CartService cartService = new CartServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = getCurrentUser(req);
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login?redirect=" + req.getRequestURI());
            return;
        }

        CartDTO cart = cartService.getCart(currentUser.getId());
        req.setAttribute("cart", cart);
        req.setAttribute("cartItemCount", cart.getTotalItems());
        req.getRequestDispatcher("/WEB-INF/views/cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = getCurrentUser(req);
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        String path = req.getPathInfo();
        if (path == null) path = "/";

        try {
            switch (path) {
                case "/add":
                    handleAdd(req, resp, currentUser);
                    break;
                case "/update":
                    handleUpdate(req, resp, currentUser);
                    break;
                case "/remove":
                    handleRemove(req, resp, currentUser);
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/cart");
            }
        } catch (ValidationException e) {
            req.getSession().setAttribute("cartError", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/cart");
        }
    }

    private void handleAdd(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException {
        Long productId = Long.parseLong(req.getParameter("productId"));
        int quantity = 1;
        try {
            String q = req.getParameter("quantity");
            if (q != null) quantity = Integer.parseInt(q);
        } catch (NumberFormatException ignored) {}

        cartService.addToCart(user.getId(), productId, quantity);
        String redirect = req.getParameter("redirect");
        if (redirect != null && !redirect.trim().isEmpty()) {
            resp.sendRedirect(redirect + (redirect.contains("?") ? "&" : "?") + "added=true");
        } else {
            resp.sendRedirect(req.getContextPath() + "/cart");
        }
    }

    private void handleUpdate(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException {
        Long productId = Long.parseLong(req.getParameter("productId"));
        int quantity = Integer.parseInt(req.getParameter("quantity"));
        cartService.updateQuantity(user.getId(), productId, quantity);
        resp.sendRedirect(req.getContextPath() + "/cart");
    }

    private void handleRemove(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException {
        Long productId = Long.parseLong(req.getParameter("productId"));
        cartService.removeFromCart(user.getId(), productId);
        resp.sendRedirect(req.getContextPath() + "/cart");
    }

    private User getCurrentUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return (session != null) ? (User) session.getAttribute("currentUser") : null;
    }
}
