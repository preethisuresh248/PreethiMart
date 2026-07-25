package com.preethi.preethimart.controller.api;

import com.preethi.preethimart.dto.ApiResponse;
import com.preethi.preethimart.model.Product;
import com.preethi.preethimart.service.ProductService;
import com.preethi.preethimart.util.JsonUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductApiServlet", urlPatterns = {"/api/v1/products"})
public class ProductApiServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String category = req.getParameter("category");
        String query = req.getParameter("q");

        try {
            List<Product> products = productService.listProducts(category, query, 1, 50);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(JsonUtil.toJson(ApiResponse.ok(products)));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(JsonUtil.toJson(ApiResponse.error(e.getMessage())));
        }
    }
}
