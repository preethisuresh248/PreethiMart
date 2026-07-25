package com.preethi.preethimart.filter;

import com.preethi.preethimart.model.User;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "AuthFilter", urlPatterns = {"/*"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI();
        String contextPath = request.getContextPath();
        String relativePath = path.substring(contextPath.length());

        // Always allow public routes and assets
        if (isPublicPath(relativePath)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;

        // 1. Check if route requires logged-in user
        if (requiresAuthentication(relativePath)) {
            if (currentUser == null) {
                if (relativePath.startsWith("/api/")) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"success\":false,\"error\":\"Authentication required.\"}");
                } else {
                    response.sendRedirect(contextPath + "/auth/login?redirect=" + request.getRequestURI());
                }
                return;
            }

            // 2. Role-based checks
            if (relativePath.startsWith("/admin/")) {
                if (!currentUser.isAdmin()) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Administrator role required.");
                    return;
                }
            } else if (relativePath.startsWith("/seller/")) {
                if (!currentUser.isSeller() && !currentUser.isAdmin()) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Seller account required.");
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) {
        if (path.equals("/") || path.isEmpty()) return true;
        if (path.startsWith("/assets/") || path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/images/")) return true;
        if (path.startsWith("/auth/")) return true;
        if (path.startsWith("/products")) return true;
        if (path.startsWith("/api/v1/health") || path.startsWith("/api/chat") || path.startsWith("/api/v1/chat") || path.startsWith("/api/v1/products")) return true;
        return false;
    }

    private boolean requiresAuthentication(String path) {
        return path.startsWith("/cart")
                || path.startsWith("/checkout")
                || path.startsWith("/orders")
                || path.startsWith("/wishlist")
                || path.startsWith("/reviews")
                || path.startsWith("/seller")
                || path.startsWith("/admin");
    }

    @Override
    public void destroy() {}
}
