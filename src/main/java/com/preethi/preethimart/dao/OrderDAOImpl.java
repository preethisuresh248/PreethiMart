package com.preethi.preethimart.dao;

import com.preethi.preethimart.exception.DatabaseException;
import com.preethi.preethimart.exception.ValidationException;
import com.preethi.preethimart.model.Order;
import com.preethi.preethimart.model.OrderItem;
import com.preethi.preethimart.util.DBConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDAOImpl implements OrderDAO {

    @Override
    public Order createOrder(Order order, List<OrderItem> items) {
        String insertOrderSql = "INSERT INTO orders (buyer_id, total_amount, status, shipping_address, payment_method) VALUES (?, ?, ?, ?, ?)";
        String insertItemSql = "INSERT INTO order_items (order_id, product_id, seller_id, quantity, price_at_purchase) VALUES (?, ?, ?, ?, ?)";
        String checkStockSql = "SELECT stock_quantity, name FROM products WHERE id = ? FOR UPDATE";
        String updateStockSql = "UPDATE products SET stock_quantity = stock_quantity - ? WHERE id = ?";
        String clearCartSql = "DELETE FROM cart_items WHERE user_id = ?";

        Connection conn = null;
        try {
            conn = DBConnectionManager.getInstance().getConnection();
            conn.setAutoCommit(false);

            // 1. Verify stock availability for all items
            for (OrderItem item : items) {
                try (PreparedStatement psStock = conn.prepareStatement(checkStockSql)) {
                    psStock.setLong(1, item.getProductId());
                    try (ResultSet rs = psStock.executeQuery()) {
                        if (rs.next()) {
                            int currentStock = rs.getInt("stock_quantity");
                            String productName = rs.getString("name");
                            if (currentStock < item.getQuantity()) {
                                throw new ValidationException("Insufficient stock for product '" + productName + "'. Available: " + currentStock + ", Requested: " + item.getQuantity());
                            }
                        } else {
                            throw new ValidationException("Product ID " + item.getProductId() + " not found.");
                        }
                    }
                }
            }

            // 2. Insert Order Header
            try (PreparedStatement psOrder = conn.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {
                psOrder.setLong(1, order.getBuyerId());
                psOrder.setBigDecimal(2, order.getTotalAmount());
                psOrder.setString(3, order.getStatus() != null ? order.getStatus() : "PENDING");
                psOrder.setString(4, order.getShippingAddress());
                psOrder.setString(5, order.getPaymentMethod() != null ? order.getPaymentMethod() : "MOCK_PAYMENT");
                psOrder.executeUpdate();

                try (ResultSet gk = psOrder.getGeneratedKeys()) {
                    if (gk.next()) {
                        order.setId(gk.getLong(1));
                    }
                }
            }

            // 3. Insert Order Items & Decrement Inventory
            for (OrderItem item : items) {
                item.setOrderId(order.getId());
                try (PreparedStatement psItem = conn.prepareStatement(insertItemSql, Statement.RETURN_GENERATED_KEYS)) {
                    psItem.setLong(1, item.getOrderId());
                    psItem.setLong(2, item.getProductId());
                    psItem.setLong(3, item.getSellerId());
                    psItem.setInt(4, item.getQuantity());
                    psItem.setBigDecimal(5, item.getPriceAtPurchase());
                    psItem.executeUpdate();

                    try (ResultSet gk = psItem.getGeneratedKeys()) {
                        if (gk.next()) {
                            item.setId(gk.getLong(1));
                        }
                    }
                }

                try (PreparedStatement psUpdateStock = conn.prepareStatement(updateStockSql)) {
                    psUpdateStock.setInt(1, item.getQuantity());
                    psUpdateStock.setLong(2, item.getProductId());
                    psUpdateStock.executeUpdate();
                }
            }

            // 4. Clear Cart for Buyer
            try (PreparedStatement psCart = conn.prepareStatement(clearCartSql)) {
                psCart.setLong(1, order.getBuyerId());
                psCart.executeUpdate();
            }

            conn.commit();
            order.setItems(items);
            return order;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    // ignore rollback exception
                }
            }
            throw new DatabaseException("Failed to process order transaction: " + e.getMessage(), e);
        } catch (ValidationException ve) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    // ignore
                }
            }
            throw ve;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    // ignore close exception
                }
            }
        }
    }

    @Override
    public Optional<Order> findById(Long id) {
        String sql = "SELECT o.id, o.buyer_id, o.total_amount, o.status, o.shipping_address, o.payment_method, o.created_at, " +
                     "       u.name AS buyer_name, u.email AS buyer_email " +
                     "FROM orders o " +
                     "JOIN users u ON o.buyer_id = u.id " +
                     "WHERE o.id = ?";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order order = mapRowToOrder(rs);
                    order.setItems(loadItemsForOrder(conn, order.getId()));
                    return Optional.of(order);
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException("Error finding order by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Order> findByBuyerId(Long buyerId) {
        String sql = "SELECT o.id, o.buyer_id, o.total_amount, o.status, o.shipping_address, o.payment_method, o.created_at, " +
                     "       u.name AS buyer_name, u.email AS buyer_email " +
                     "FROM orders o " +
                     "JOIN users u ON o.buyer_id = u.id " +
                     "WHERE o.buyer_id = ? " +
                     "ORDER BY o.created_at DESC";
        List<Order> list = new ArrayList<>();
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, buyerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order o = mapRowToOrder(rs);
                    o.setItems(loadItemsForOrder(conn, o.getId()));
                    list.add(o);
                }
            }
            return list;
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving orders for buyer: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Order> findBySellerId(Long sellerId) {
        String sql = "SELECT DISTINCT o.id, o.buyer_id, o.total_amount, o.status, o.shipping_address, o.payment_method, o.created_at, " +
                     "       u.name AS buyer_name, u.email AS buyer_email " +
                     "FROM orders o " +
                     "JOIN users u ON o.buyer_id = u.id " +
                     "JOIN order_items oi ON o.id = oi.order_id " +
                     "WHERE oi.seller_id = ? " +
                     "ORDER BY o.created_at DESC";
        List<Order> list = new ArrayList<>();
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order o = mapRowToOrder(rs);
                    o.setItems(loadItemsForSellerOrder(conn, o.getId(), sellerId));
                    list.add(o);
                }
            }
            return list;
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving incoming orders for seller: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Order> findAll() {
        String sql = "SELECT o.id, o.buyer_id, o.total_amount, o.status, o.shipping_address, o.payment_method, o.created_at, " +
                     "       u.name AS buyer_name, u.email AS buyer_email " +
                     "FROM orders o " +
                     "JOIN users u ON o.buyer_id = u.id " +
                     "ORDER BY o.created_at DESC";
        List<Order> list = new ArrayList<>();
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Order o = mapRowToOrder(rs);
                o.setItems(loadItemsForOrder(conn, o.getId()));
                list.add(o);
            }
            return list;
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving all orders: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updateStatus(Long orderId, String status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.toUpperCase());
            ps.setLong(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating order status: " + e.getMessage(), e);
        }
    }

    private List<OrderItem> loadItemsForOrder(Connection conn, Long orderId) throws SQLException {
        String sql = "SELECT oi.id, oi.order_id, oi.product_id, oi.seller_id, oi.quantity, oi.price_at_purchase, oi.created_at, " +
                     "       p.name AS product_name, p.image_url AS product_image, u.name AS seller_name " +
                     "FROM order_items oi " +
                     "JOIN products p ON oi.product_id = p.id " +
                     "JOIN users u ON oi.seller_id = u.id " +
                     "WHERE oi.order_id = ?";
        List<OrderItem> items = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setId(rs.getLong("id"));
                    item.setOrderId(rs.getLong("order_id"));
                    item.setProductId(rs.getLong("product_id"));
                    item.setSellerId(rs.getLong("seller_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setPriceAtPurchase(rs.getBigDecimal("price_at_purchase"));
                    item.setCreatedAt(rs.getTimestamp("created_at"));
                    item.setProductName(rs.getString("product_name"));
                    item.setProductImageUrl(rs.getString("product_image"));
                    item.setSellerName(rs.getString("seller_name"));
                    items.add(item);
                }
            }
        }
        return items;
    }

    private List<OrderItem> loadItemsForSellerOrder(Connection conn, Long orderId, Long sellerId) throws SQLException {
        String sql = "SELECT oi.id, oi.order_id, oi.product_id, oi.seller_id, oi.quantity, oi.price_at_purchase, oi.created_at, " +
                     "       p.name AS product_name, p.image_url AS product_image, u.name AS seller_name " +
                     "FROM order_items oi " +
                     "JOIN products p ON oi.product_id = p.id " +
                     "JOIN users u ON oi.seller_id = u.id " +
                     "WHERE oi.order_id = ? AND oi.seller_id = ?";
        List<OrderItem> items = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            ps.setLong(2, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setId(rs.getLong("id"));
                    item.setOrderId(rs.getLong("order_id"));
                    item.setProductId(rs.getLong("product_id"));
                    item.setSellerId(rs.getLong("seller_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setPriceAtPurchase(rs.getBigDecimal("price_at_purchase"));
                    item.setCreatedAt(rs.getTimestamp("created_at"));
                    item.setProductName(rs.getString("product_name"));
                    item.setProductImageUrl(rs.getString("product_image"));
                    item.setSellerName(rs.getString("seller_name"));
                    items.add(item);
                }
            }
        }
        return items;
    }

    private Order mapRowToOrder(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setId(rs.getLong("id"));
        o.setBuyerId(rs.getLong("buyer_id"));
        o.setTotalAmount(rs.getBigDecimal("total_amount"));
        o.setStatus(rs.getString("status"));
        o.setShippingAddress(rs.getString("shipping_address"));
        o.setPaymentMethod(rs.getString("payment_method"));
        o.setCreatedAt(rs.getTimestamp("created_at"));
        o.setBuyerName(rs.getString("buyer_name"));
        o.setBuyerEmail(rs.getString("buyer_email"));
        return o;
    }
}
