package com.preethi.preethimart.dao;

import com.preethi.preethimart.model.Review;

import java.util.List;

public interface ReviewDAO {
    Review create(Review review);
    List<Review> findByProductId(Long productId);
    List<Review> findByBuyerId(Long buyerId);
    boolean hasBuyerPurchasedProduct(Long buyerId, Long productId);
}
