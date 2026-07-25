package com.preethi.preethimart.dao;

import com.preethi.preethimart.exception.DatabaseException;
import com.preethi.preethimart.model.Review;
import com.preethi.preethimart.util.DBConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAOImpl implements ReviewDAO {

    @Override
    public Review create(Review review) {
        String sql = "INSERT INTO reviews (product_id, buyer_id, order_id, rating, comment) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, review.getProductId());
            ps.setLong(2, review.getBuyerId());
            if (review.getOrderId() != null) {
                ps.setLong(3, review.getOrderId());
            } else {
                ps.setNull(3, java.sql.Types.BIGINT);
            }
            ps.setInt(4, review.getRating());
            ps.setString(5, review.getComment());
            ps.executeUpdate();

            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) {
                    review.setId(gk.getLong(1));
                }
            }
            return review;
        } catch (SQLException e) {
            throw new DatabaseException("Error creating review: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Review> findByProductId(Long productId) {
        String sql = "SELECT r.id, r.product_id, r.buyer_id, r.order_id, r.rating, r.comment, r.created_at, " +
                     "       u.name AS buyer_name, p.name AS product_name " +
                     "FROM reviews r " +
                     "JOIN users u ON r.buyer_id = u.id " +
                     "JOIN products p ON r.product_id = p.id " +
                     "WHERE r.product_id = ? " +
                     "ORDER BY r.created_at DESC";
        List<Review> list = new ArrayList<>();
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Review r = mapRowToReview(rs);
                    list.add(r);
                }
            }
            return list;
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving reviews for product: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Review> findByBuyerId(Long buyerId) {
        String sql = "SELECT r.id, r.product_id, r.buyer_id, r.order_id, r.rating, r.comment, r.created_at, " +
                     "       u.name AS buyer_name, p.name AS product_name " +
                     "FROM reviews r " +
                     "JOIN users u ON r.buyer_id = u.id " +
                     "JOIN products p ON r.product_id = p.id " +
                     "WHERE r.buyer_id = ? " +
                     "ORDER BY r.created_at DESC";
        List<Review> list = new ArrayList<>();
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, buyerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Review r = mapRowToReview(rs);
                    list.add(r);
                }
            }
            return list;
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving reviews for buyer: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean hasBuyerPurchasedProduct(Long buyerId, Long productId) {
        String sql = "SELECT COUNT(*) FROM orders o " +
                     "JOIN order_items oi ON o.id = oi.order_id " +
                     "WHERE o.buyer_id = ? AND oi.product_id = ? AND o.status IN ('CONFIRMED', 'SHIPPED', 'DELIVERED')";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, buyerId);
            ps.setLong(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new DatabaseException("Error checking purchase history: " + e.getMessage(), e);
        }
    }

    private Review mapRowToReview(ResultSet rs) throws SQLException {
        Review r = new Review();
        r.setId(rs.getLong("id"));
        r.setProductId(rs.getLong("product_id"));
        r.setBuyerId(rs.getLong("buyer_id"));
        r.setOrderId(rs.getLong("order_id"));
        r.setRating(rs.getInt("rating"));
        r.setComment(rs.getString("comment"));
        r.setCreatedAt(rs.getTimestamp("created_at"));
        r.setBuyerName(rs.getString("buyer_name"));
        r.setProductName(rs.getString("product_name"));
        return r;
    }
}
