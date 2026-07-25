package com.preethi.preethimart.service;

import com.preethi.preethimart.dto.CartDTO;

public interface CartService {
    CartDTO getCart(Long userId);
    void addToCart(Long userId, Long productId, int quantity);
    void updateQuantity(Long userId, Long productId, int quantity);
    void removeFromCart(Long userId, Long productId);
    int getCartCount(Long userId);
}
