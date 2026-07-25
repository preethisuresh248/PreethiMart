package com.preethi.preethimart.controller;

import com.preethi.preethimart.dto.ReviewDTO;
import com.preethi.preethimart.model.Product;
import com.preethi.preethimart.model.User;
import com.preethi.preethimart.service.CartService;
import com.preethi.preethimart.service.CartServiceImpl;
import com.preethi.preethimart.service.ProductService;
import com.preethi.preethimart.service.ReviewService;
import com.preethi.preethimart.service.WishlistService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductServlet", urlPatterns = {"", "/products", "/products/*"})
public class ProductServlet extends HttpServlet {

    private final ProductService productService = new ProductService();
    private final ReviewService reviewService = new ReviewService();
    private final CartService cartService = new CartServiceImpl();
    private final WishlistService wishlistService = new WishlistService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();

        if (path != null && path.startsWith("/detail")) {
            showProductDetail(req, resp);
            return;
        }

        // Default catalog browsing
        showCatalog(req, resp);
    }

    private void showCatalog(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String category = req.getParameter("category");
        String keyword = req.getParameter("q");
        String pageStr = req.getParameter("page");

        int page = 1;
        int pageSize = 9;
        try {
            if (pageStr != null) page = Math.max(1, Integer.parseInt(pageStr));
        } catch (NumberFormatException ignored) {}

        List<Product> products = productService.listProducts(category, keyword, page, pageSize);
        int totalProducts = productService.countProducts(category, keyword);
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
        List<String> categories = productService.getCategories();

        // Cart and wishlist badges for current user
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if (currentUser != null) {
            req.setAttribute("cartItemCount", cartService.getCartCount(currentUser.getId()));
        }

        req.setAttribute("products", products);
        req.setAttribute("categories", categories);
        req.setAttribute("selectedCategory", category != null ? category : "ALL");
        req.setAttribute("searchQuery", keyword != null ? keyword : "");
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalProducts", totalProducts);

        req.getRequestDispatcher("/WEB-INF/views/catalog.jsp").forward(req, resp);
    }

    private void showProductDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/products");
            return;
        }

        try {
            Long productId = Long.parseLong(idStr);
            Product product = productService.getProductById(productId);
            List<ReviewDTO> reviews = reviewService.getReviewsForProduct(productId);

            HttpSession session = req.getSession(false);
            User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
            boolean inWishlist = false;
            if (currentUser != null) {
                inWishlist = wishlistService.isInWishlist(currentUser.getId(), productId);
                req.setAttribute("cartItemCount", cartService.getCartCount(currentUser.getId()));
            }

            req.setAttribute("product", product);
            req.setAttribute("reviews", reviews);
            req.setAttribute("inWishlist", inWishlist);

            req.getRequestDispatcher("/WEB-INF/views/product-detail.jsp").forward(req, resp);
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/products");
        }
    }
}
