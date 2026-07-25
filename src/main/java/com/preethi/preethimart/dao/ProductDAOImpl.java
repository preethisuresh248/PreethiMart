package com.preethi.preethimart.dao;

import com.preethi.preethimart.exception.DatabaseException;
import com.preethi.preethimart.model.Product;
import com.preethi.preethimart.util.DBConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDAOImpl implements ProductDAO {

    @Override
    public Product create(Product product) {
        String sql = "INSERT INTO products (seller_id, name, description, category, price, stock_quantity, image_url, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, product.getSellerId());
            ps.setString(2, product.getName());
            ps.setString(3, product.getDescription());
            ps.setString(4, product.getCategory());
            ps.setBigDecimal(5, product.getPrice());
            ps.setInt(6, product.getStockQuantity());
            ps.setString(7, product.getImageUrl());
            ps.setString(8, product.getStatus() != null ? product.getStatus() : "ACTIVE");

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Failed to insert product.");
            }

            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) {
                    product.setId(gk.getLong(1));
                }
            }
            return product;
        } catch (SQLException e) {
            throw new DatabaseException("Error creating product: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Product> findById(Long id) {
        String sql = "SELECT p.id, p.seller_id, p.name, p.description, p.category, p.price, p.stock_quantity, p.image_url, p.status, p.created_at, " +
                     "       u.name AS seller_name, " +
                     "       COALESCE(AVG(r.rating), 0.0) AS avg_rating, " +
                     "       COUNT(r.id) AS review_count " +
                     "FROM products p " +
                     "JOIN users u ON p.seller_id = u.id " +
                     "LEFT JOIN reviews r ON p.id = r.product_id " +
                     "WHERE p.id = ? " +
                     "GROUP BY p.id, u.name";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToProduct(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException("Error finding product by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Product> findAll(String category, String keyword, int limit, int offset) {
        StringBuilder sql = new StringBuilder(
                "SELECT p.id, p.seller_id, p.name, p.description, p.category, p.price, p.stock_quantity, p.image_url, p.status, p.created_at, " +
                "       u.name AS seller_name, " +
                "       COALESCE(AVG(r.rating), 0.0) AS avg_rating, " +
                "       COUNT(r.id) AS review_count " +
                "FROM products p " +
                "JOIN users u ON p.seller_id = u.id " +
                "LEFT JOIN reviews r ON p.id = r.product_id " +
                "WHERE p.status = 'ACTIVE' "
        );

        List<Object> params = new ArrayList<>();
        if (category != null && !category.trim().isEmpty() && !"ALL".equalsIgnoreCase(category)) {
            sql.append("AND LOWER(p.category) = LOWER(?) ");
            params.add(category.trim());
        }
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (LOWER(p.name) LIKE ? OR LOWER(p.description) LIKE ?) ");
            String pattern = "%" + keyword.trim().toLowerCase() + "%";
            params.add(pattern);
            params.add(pattern);
        }

        sql.append("GROUP BY p.id, u.name ORDER BY p.created_at DESC LIMIT ? OFFSET ?");
        params.add(limit > 0 ? limit : 20);
        params.add(offset >= 0 ? offset : 0);

        List<Product> products = new ArrayList<>();
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapRowToProduct(rs));
                }
            }
            return products;
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching products: " + e.getMessage(), e);
        }
    }

    @Override
    public int countAll(String category, String keyword) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(DISTINCT p.id) FROM products p WHERE p.status = 'ACTIVE' ");
        List<Object> params = new ArrayList<>();
        if (category != null && !category.trim().isEmpty() && !"ALL".equalsIgnoreCase(category)) {
            sql.append("AND LOWER(p.category) = LOWER(?) ");
            params.add(category.trim());
        }
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (LOWER(p.name) LIKE ? OR LOWER(p.description) LIKE ?) ");
            String pattern = "%" + keyword.trim().toLowerCase() + "%";
            params.add(pattern);
            params.add(pattern);
        }

        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error counting products: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Product> findBySellerId(Long sellerId) {
        String sql = "SELECT p.id, p.seller_id, p.name, p.description, p.category, p.price, p.stock_quantity, p.image_url, p.status, p.created_at, " +
                     "       u.name AS seller_name, " +
                     "       COALESCE(AVG(r.rating), 0.0) AS avg_rating, " +
                     "       COUNT(r.id) AS review_count " +
                     "FROM products p " +
                     "JOIN users u ON p.seller_id = u.id " +
                     "LEFT JOIN reviews r ON p.id = r.product_id " +
                     "WHERE p.seller_id = ? " +
                     "GROUP BY p.id, u.name " +
                     "ORDER BY p.created_at DESC";
        List<Product> products = new ArrayList<>();
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapRowToProduct(rs));
                }
            }
            return products;
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching products by seller ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<String> findAllCategories() {
        String sql = "SELECT DISTINCT category FROM products WHERE status = 'ACTIVE' ORDER BY category ASC";
        List<String> categories = new ArrayList<>();
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
            return categories;
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching categories: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(Product product) {
        String sql = "UPDATE products SET name = ?, description = ?, category = ?, price = ?, stock_quantity = ?, image_url = ?, status = ? " +
                     "WHERE id = ? AND seller_id = ?";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setString(3, product.getCategory());
            ps.setBigDecimal(4, product.getPrice());
            ps.setInt(5, product.getStockQuantity());
            ps.setString(6, product.getImageUrl());
            ps.setString(7, product.getStatus());
            ps.setLong(8, product.getId());
            ps.setLong(9, product.getSellerId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating product: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updateStock(Long id, int newStock, Connection conn) throws SQLException {
        String sql = "UPDATE products SET stock_quantity = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newStock);
            ps.setLong(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateStatus(Long id, String status) {
        String sql = "UPDATE products SET status = ? WHERE id = ?";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setLong(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating product status: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting product: " + e.getMessage(), e);
        }
    }

    private Product mapRowToProduct(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getLong("id"));
        p.setSellerId(rs.getLong("seller_id"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setCategory(rs.getString("category"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setStockQuantity(rs.getInt("stock_quantity"));
        p.setImageUrl(rs.getString("image_url"));
        p.setStatus(rs.getString("status"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setSellerName(rs.getString("seller_name"));
        p.setAverageRating(rs.getDouble("avg_rating"));
        p.setReviewCount(rs.getInt("review_count"));
        return p;
    }
}
