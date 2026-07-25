package com.preethi.preethimart.controller;

import com.preethi.preethimart.dto.OrderDTO;
import com.preethi.preethimart.dto.UserResponseDTO;
import com.preethi.preethimart.model.Product;
import com.preethi.preethimart.model.User;
import com.preethi.preethimart.service.OrderService;
import com.preethi.preethimart.service.ProductService;
import com.preethi.preethimart.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "AdminServlet", urlPatterns = {"/admin", "/admin/*"})
public class AdminServlet extends HttpServlet {

    private final UserService userService = new UserService();
    private final ProductService productService = new ProductService();
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getCurrentAdmin(req, resp);
        if (user == null) return;

        String path = req.getPathInfo();
        if (path == null) path = "/dashboard";

        switch (path) {
            case "/dashboard":
                showDashboard(req, resp);
                break;
            case "/users":
                showUsers(req, resp);
                break;
            case "/orders":
                showOrders(req, resp);
                break;
            case "/listings":
                showListings(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getCurrentAdmin(req, resp);
        if (user == null) return;

        String path = req.getPathInfo();
        if (path == null) path = "/dashboard";

        switch (path) {
            case "/users/delete":
                handleDeleteUser(req, resp);
                break;
            case "/listings/moderate":
                handleModerateListing(req, resp);
                break;
            case "/orders/status":
                handleAdminUpdateStatus(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
        }
    }

    private void showDashboard(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<UserResponseDTO> users = userService.getAllUsers();
        List<OrderDTO> orders = orderService.getAllOrders();
        List<Product> products = productService.listProducts(null, null, 1, 100);

        BigDecimal gmv = orders.stream()
                .map(OrderDTO::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        req.setAttribute("totalUsers", users.size());
        req.setAttribute("totalOrders", orders.size());
        req.setAttribute("totalProducts", products.size());
        req.setAttribute("gmv", gmv);
        req.setAttribute("recentOrders", orders.stream().limit(5).toList());

        req.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(req, resp);
    }

    private void showUsers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<UserResponseDTO> users = userService.getAllUsers();
        req.setAttribute("users", users);
        req.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(req, resp);
    }

    private void showOrders(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<OrderDTO> orders = orderService.getAllOrders();
        req.setAttribute("orders", orders);
        req.getRequestDispatcher("/WEB-INF/views/admin/orders.jsp").forward(req, resp);
    }

    private void showListings(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Product> products = productService.listProducts(null, null, 1, 100);
        req.setAttribute("products", products);
        req.getRequestDispatcher("/WEB-INF/views/admin/listings.jsp").forward(req, resp);
    }

    private void handleDeleteUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long userId = Long.parseLong(req.getParameter("id"));
        userService.deleteUser(userId);
        req.getSession().setAttribute("adminSuccess", "User #" + userId + " deleted successfully.");
        resp.sendRedirect(req.getContextPath() + "/admin/users");
    }

    private void handleModerateListing(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long productId = Long.parseLong(req.getParameter("id"));
        String newStatus = req.getParameter("status"); // MODERATED or ACTIVE
        productService.updateStatus(productId, newStatus);
        req.getSession().setAttribute("adminSuccess", "Listing #" + productId + " status updated to " + newStatus);
        resp.sendRedirect(req.getContextPath() + "/admin/listings");
    }

    private void handleAdminUpdateStatus(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long orderId = Long.parseLong(req.getParameter("orderId"));
        String status = req.getParameter("status");
        orderService.updateOrderStatus(orderId, status);
        req.getSession().setAttribute("adminSuccess", "Order #" + orderId + " status updated to " + status);
        resp.sendRedirect(req.getContextPath() + "/admin/orders");
    }

    private User getCurrentAdmin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login?redirect=" + req.getRequestURI());
            return null;
        }
        if (!user.isAdmin()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Administrator access required.");
            return null;
        }
        return user;
    }
}
