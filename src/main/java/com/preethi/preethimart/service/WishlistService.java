package com.preethi.preethimart.service;

import com.preethi.preethimart.dao.WishlistDAO;
import com.preethi.preethimart.dao.WishlistDAOImpl;
import com.preethi.preethimart.exception.ValidationException;
import com.preethi.preethimart.model.WishlistItem;

import java.util.List;

public class WishlistService {

    private final WishlistDAO wishlistDAO;

    public WishlistService() {
        this(new WishlistDAOImpl());
    }

    public WishlistService(WishlistDAO wishlistDAO) {
        this.wishlistDAO = wishlistDAO;
    }

    public boolean toggleWishlist(Long userId, Long productId) {
        if (userId == null || userId <= 0) {
            throw new ValidationException("Valid user ID is required.");
        }
        if (productId == null || productId <= 0) {
            throw new ValidationException("Valid product ID is required.");
        }

        if (wishlistDAO.isInWishlist(userId, productId)) {
            wishlistDAO.removeItem(userId, productId);
            return false; // Removed
        } else {
            wishlistDAO.addItem(userId, productId);
            return true; // Added
        }
    }

    public List<WishlistItem> getWishlist(Long userId) {
        if (userId == null || userId <= 0) {
            throw new ValidationException("Valid user ID is required.");
        }
        return wishlistDAO.findByUserId(userId);
    }

    public boolean isInWishlist(Long userId, Long productId) {
        if (userId == null || productId == null) return false;
        return wishlistDAO.isInWishlist(userId, productId);
    }
}
