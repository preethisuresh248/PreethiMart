package com.preethi.preethimart.controller;

import com.preethi.preethimart.dto.OrderDTO;
import com.preethi.preethimart.dto.ProductDTO;
import com.preethi.preethimart.exception.ValidationException;
import com.preethi.preethimart.model.Product;
import com.preethi.preethimart.model.User;
import com.preethi.preethimart.service.OrderService;
import com.preethi.preethimart.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "SellerServlet", urlPatterns = {"/seller", "/seller/*"})
public class SellerServlet extends HttpServlet {

    private final ProductService productService = new ProductService();
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getCurrentSeller(req, resp);
        if (user == null) return;

        String path = req.getPathInfo();
        if (path == null) path = "/dashboard";

        switch (path) {
            case "/dashboard":
            case "/products":
                showDashboard(req, resp, user);
                break;
            case "/orders":
                showIncomingOrders(req, resp, user);
                break;
            case "/product/edit":
                showEditForm(req, resp, user);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/seller/dashboard");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getCurrentSeller(req, resp);
        if (user == null) return;

        String path = req.getPathInfo();
        if (path == null) path = "/dashboard";

        try {
            switch (path) {
                case "/product/create":
                    handleCreateProduct(req, resp, user);
                    break;
                case "/product/update":
                    handleUpdateProduct(req, resp, user);
                    break;
                case "/product/delete":
                    handleDeleteProduct(req, resp, user);
                    break;
                case "/orders/status":
                    handleUpdateOrderStatus(req, resp);
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/seller/dashboard");
            }
        } catch (ValidationException e) {
            req.getSession().setAttribute("sellerError", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/seller/dashboard");
        }
    }

    private void showDashboard(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        List<Product> products = productService.getProductsBySeller(user.getId());
        List<String> categories = productService.getCategories();
        req.setAttribute("products", products);
        req.setAttribute("categories", categories);
        req.getRequestDispatcher("/WEB-INF/views/seller/dashboard.jsp").forward(req, resp);
    }

    private void showIncomingOrders(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        List<OrderDTO> orders = orderService.getOrdersForSeller(user.getId());
        req.setAttribute("orders", orders);
        req.getRequestDispatcher("/WEB-INF/views/seller/orders.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        Long productId = Long.parseLong(req.getParameter("id"));
        Product product = productService.getProductById(productId);
        if (!product.getSellerId().equals(user.getId()) && !user.isAdmin()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        req.setAttribute("product", product);
        req.setAttribute("categories", productService.getCategories());
        req.getRequestDispatcher("/WEB-INF/views/seller/product-edit.jsp").forward(req, resp);
    }

    private void handleCreateProduct(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException {
        ProductDTO dto = new ProductDTO();
        dto.setName(req.getParameter("name"));
        dto.setDescription(req.getParameter("description"));
        dto.setCategory(req.getParameter("category"));
        dto.setPrice(new BigDecimal(req.getParameter("price")));
        dto.setStockQuantity(Integer.parseInt(req.getParameter("stockQuantity")));
        dto.setImageUrl(req.getParameter("imageUrl"));

        productService.createProduct(user.getId(), dto);
        req.getSession().setAttribute("sellerSuccess", "Product listing created successfully!");
        resp.sendRedirect(req.getContextPath() + "/seller/dashboard");
    }

    private void handleUpdateProduct(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException {
        ProductDTO dto = new ProductDTO();
        dto.setId(Long.parseLong(req.getParameter("id")));
        dto.setName(req.getParameter("name"));
        dto.setDescription(req.getParameter("description"));
        dto.setCategory(req.getParameter("category"));
        dto.setPrice(new BigDecimal(req.getParameter("price")));
        dto.setStockQuantity(Integer.parseInt(req.getParameter("stockQuantity")));
        dto.setImageUrl(req.getParameter("imageUrl"));
        dto.setStatus(req.getParameter("status"));

        productService.updateProduct(user.getId(), dto);
        req.getSession().setAttribute("sellerSuccess", "Product listing updated successfully!");
        resp.sendRedirect(req.getContextPath() + "/seller/dashboard");
    }

    private void handleDeleteProduct(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException {
        Long productId = Long.parseLong(req.getParameter("id"));
        Product p = productService.getProductById(productId);
        if (p.getSellerId().equals(user.getId()) || user.isAdmin()) {
            productService.deleteProduct(productId);
            req.getSession().setAttribute("sellerSuccess", "Product listing removed.");
        }
        resp.sendRedirect(req.getContextPath() + "/seller/dashboard");
    }

    private void handleUpdateOrderStatus(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long orderId = Long.parseLong(req.getParameter("orderId"));
        String newStatus = req.getParameter("status");
        orderService.updateOrderStatus(orderId, newStatus);
        req.getSession().setAttribute("sellerSuccess", "Order #" + orderId + " status updated to " + newStatus);
        resp.sendRedirect(req.getContextPath() + "/seller/orders");
    }

    private User getCurrentSeller(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login?redirect=" + req.getRequestURI());
            return null;
        }
        if (!user.isSeller() && !user.isAdmin()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Seller access required.");
            return null;
        }
        return user;
    }
}
