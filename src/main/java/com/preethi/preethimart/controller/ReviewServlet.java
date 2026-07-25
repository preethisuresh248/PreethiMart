package com.preethi.preethimart.controller;

import com.preethi.preethimart.exception.ValidationException;
import com.preethi.preethimart.model.User;
import com.preethi.preethimart.service.ReviewService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "ReviewServlet", urlPatterns = {"/reviews/*"})
public class ReviewServlet extends HttpServlet {

    private final ReviewService reviewService = new ReviewService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        String productIdStr = req.getParameter("productId");
        String orderIdStr = req.getParameter("orderId");
        String ratingStr = req.getParameter("rating");
        String comment = req.getParameter("comment");

        try {
            Long productId = Long.parseLong(productIdStr);
            Long orderId = (orderIdStr != null && !orderIdStr.isEmpty()) ? Long.parseLong(orderIdStr) : null;
            int rating = Integer.parseInt(ratingStr);

            reviewService.submitReview(currentUser.getId(), productId, orderId, rating, comment);
            session.setAttribute("reviewSuccess", "Thank you! Your verified review has been posted.");
            resp.sendRedirect(req.getContextPath() + "/products/detail?id=" + productId);
        } catch (ValidationException e) {
            session.setAttribute("reviewError", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/products/detail?id=" + productIdStr);
        }
    }
}
