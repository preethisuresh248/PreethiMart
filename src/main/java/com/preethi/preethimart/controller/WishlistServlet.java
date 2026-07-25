package com.preethi.preethimart.controller;

import com.preethi.preethimart.model.User;
import com.preethi.preethimart.model.WishlistItem;
import com.preethi.preethimart.service.CartService;
import com.preethi.preethimart.service.CartServiceImpl;
import com.preethi.preethimart.service.WishlistService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "WishlistServlet", urlPatterns = {"/wishlist", "/wishlist/*"})
public class WishlistServlet extends HttpServlet {

    private final WishlistService wishlistService = new WishlistService();
    private final CartService cartService = new CartServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login?redirect=" + req.getRequestURI());
            return;
        }

        List<WishlistItem> wishlist = wishlistService.getWishlist(currentUser.getId());
        req.setAttribute("wishlist", wishlist);
        req.setAttribute("cartItemCount", cartService.getCartCount(currentUser.getId()));
        req.getRequestDispatcher("/WEB-INF/views/buyer/wishlist.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        Long productId = Long.parseLong(req.getParameter("productId"));
        boolean added = wishlistService.toggleWishlist(currentUser.getId(), productId);

        String redirect = req.getParameter("redirect");
        if (redirect != null && !redirect.trim().isEmpty()) {
            resp.sendRedirect(redirect + (redirect.contains("?") ? "&" : "?") + (added ? "wishlistAdded=true" : "wishlistRemoved=true"));
        } else {
            resp.sendRedirect(req.getContextPath() + "/wishlist");
        }
    }
}
