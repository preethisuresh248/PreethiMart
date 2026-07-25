package com.preethi.preethimart.dao;

import com.preethi.preethimart.model.WishlistItem;

import java.util.List;

public interface WishlistDAO {
    void addItem(Long userId, Long productId);
    void removeItem(Long userId, Long productId);
    List<WishlistItem> findByUserId(Long userId);
    boolean isInWishlist(Long userId, Long productId);
}
