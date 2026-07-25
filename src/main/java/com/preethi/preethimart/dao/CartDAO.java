package com.preethi.preethimart.dao;

import com.preethi.preethimart.model.CartItem;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface CartDAO {
    List<CartItem> findByUserId(Long userId);
    void addItem(Long userId, Long productId, int quantity);
    void updateQuantity(Long userId, Long productId, int quantity);
    void removeItem(Long userId, Long productId);
    void clearCart(Long userId, Connection conn) throws SQLException;
    int getCartItemCount(Long userId);
}
