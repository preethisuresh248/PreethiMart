package com.preethi.preethimart.dao;

import com.preethi.preethimart.exception.DatabaseException;
import com.preethi.preethimart.model.CartItem;
import com.preethi.preethimart.util.DBConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartDAOImpl implements CartDAO {

    @Override
    public List<CartItem> findByUserId(Long userId) {
        String sql = "SELECT c.id, c.user_id, c.product_id, c.quantity, c.created_at, " +
                     "       p.name AS product_name, p.price AS product_price, p.image_url AS product_image, " +
                     "       p.stock_quantity AS available_stock, p.category AS product_category " +
                     "FROM cart_items c " +
                     "JOIN products p ON c.product_id = p.id " +
                     "WHERE c.user_id = ? " +
                     "ORDER BY c.created_at DESC";
        List<CartItem> list = new ArrayList<>();
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CartItem item = new CartItem();
                    item.setId(rs.getLong("id"));
                    item.setUserId(rs.getLong("user_id"));
                    item.setProductId(rs.getLong("product_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setCreatedAt(rs.getTimestamp("created_at"));
                    item.setProductName(rs.getString("product_name"));
                    item.setProductPrice(rs.getBigDecimal("product_price"));
                    item.setProductImageUrl(rs.getString("product_image"));
                    item.setAvailableStock(rs.getInt("available_stock"));
                    item.setCategory(rs.getString("product_category"));
                    list.add(item);
                }
            }
            return list;
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving cart items: " + e.getMessage(), e);
        }
    }

    @Override
    public void addItem(Long userId, Long productId, int quantity) {
        String upsert = "MERGE INTO cart_items (user_id, product_id, quantity) KEY(user_id, product_id) " +
                        "VALUES (?, ?, COALESCE((SELECT quantity FROM cart_items WHERE user_id = ? AND product_id = ?), 0) + ?)";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(upsert)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            ps.setLong(3, userId);
            ps.setLong(4, productId);
            ps.setInt(5, quantity);
            ps.executeUpdate();
        } catch (SQLException e) {
            // Fallback standard insert/update if MERGE has dialect differences
            fallbackUpsert(userId, productId, quantity);
        }
    }

    private void fallbackUpsert(Long userId, Long productId, int quantity) {
        String checkSql = "SELECT id, quantity FROM cart_items WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement psCheck = conn.prepareStatement(checkSql)) {
            psCheck.setLong(1, userId);
            psCheck.setLong(2, productId);
            try (ResultSet rs = psCheck.executeQuery()) {
                if (rs.next()) {
                    int existingQty = rs.getInt("quantity");
                    String updateSql = "UPDATE cart_items SET quantity = ? WHERE user_id = ? AND product_id = ?";
                    try (PreparedStatement psUpdate = conn.prepareStatement(updateSql)) {
                        psUpdate.setInt(1, existingQty + quantity);
                        psUpdate.setLong(2, userId);
                        psUpdate.setLong(3, productId);
                        psUpdate.executeUpdate();
                    }
                } else {
                    String insertSql = "INSERT INTO cart_items (user_id, product_id, quantity) VALUES (?, ?, ?)";
                    try (PreparedStatement psInsert = conn.prepareStatement(insertSql)) {
                        psInsert.setLong(1, userId);
                        psInsert.setLong(2, productId);
                        psInsert.setInt(3, quantity);
                        psInsert.executeUpdate();
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DatabaseException("Error adding item to cart: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void updateQuantity(Long userId, Long productId, int quantity) {
        if (quantity <= 0) {
            removeItem(userId, productId);
            return;
        }
        String sql = "UPDATE cart_items SET quantity = ? WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setLong(2, userId);
            ps.setLong(3, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating cart quantity: " + e.getMessage(), e);
        }
    }

    @Override
    public void removeItem(Long userId, Long productId) {
        String sql = "DELETE FROM cart_items WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error removing cart item: " + e.getMessage(), e);
        }
    }

    @Override
    public void clearCart(Long userId, Connection conn) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE user_id = ?";
        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, userId);
                ps.executeUpdate();
            }
        } else {
            try (Connection newConn = DBConnectionManager.getInstance().getConnection();
                 PreparedStatement ps = newConn.prepareStatement(sql)) {
                ps.setLong(1, userId);
                ps.executeUpdate();
            }
        }
    }

    @Override
    public int getCartItemCount(Long userId) {
        String sql = "SELECT COALESCE(SUM(quantity), 0) FROM cart_items WHERE user_id = ?";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error getting cart count: " + e.getMessage(), e);
        }
    }
}
