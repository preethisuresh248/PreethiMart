package com.preethi.preethimart.dao;

import com.preethi.preethimart.exception.DatabaseException;
import com.preethi.preethimart.model.WishlistItem;
import com.preethi.preethimart.util.DBConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WishlistDAOImpl implements WishlistDAO {

    @Override
    public void addItem(Long userId, Long productId) {
        String sql = "MERGE INTO wishlist_items (user_id, product_id) KEY(user_id, product_id) VALUES (?, ?)";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            // Fallback insert ignore
            String insertSql = "INSERT INTO wishlist_items (user_id, product_id) VALUES (?, ?)";
            try (Connection conn = DBConnectionManager.getInstance().getConnection();
                 PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setLong(1, userId);
                ps.setLong(2, productId);
                ps.executeUpdate();
            } catch (SQLException ignored) {}
        }
    }

    @Override
    public void removeItem(Long userId, Long productId) {
        String sql = "DELETE FROM wishlist_items WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error removing wishlist item: " + e.getMessage(), e);
        }
    }

    @Override
    public List<WishlistItem> findByUserId(Long userId) {
        String sql = "SELECT w.id, w.user_id, w.product_id, w.created_at, " +
                     "       p.name AS product_name, p.price AS product_price, p.image_url AS product_image, " +
                     "       p.category AS product_category, p.stock_quantity AS stock_quantity " +
                     "FROM wishlist_items w " +
                     "JOIN products p ON w.product_id = p.id " +
                     "WHERE w.user_id = ? " +
                     "ORDER BY w.created_at DESC";
        List<WishlistItem> list = new ArrayList<>();
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    WishlistItem item = new WishlistItem();
                    item.setId(rs.getLong("id"));
                    item.setUserId(rs.getLong("user_id"));
                    item.setProductId(rs.getLong("product_id"));
                    item.setCreatedAt(rs.getTimestamp("created_at"));
                    item.setProductName(rs.getString("product_name"));
                    item.setProductPrice(rs.getBigDecimal("product_price"));
                    item.setProductImageUrl(rs.getString("product_image"));
                    item.setCategory(rs.getString("product_category"));
                    item.setStockQuantity(rs.getInt("stock_quantity"));
                    list.add(item);
                }
            }
            return list;
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching wishlist items: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isInWishlist(Long userId, Long productId) {
        String sql = "SELECT COUNT(*) FROM wishlist_items WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new DatabaseException("Error checking wishlist status: " + e.getMessage(), e);
        }
    }
}
