package com.preethi.preethimart.service;

import com.preethi.preethimart.dao.ReviewDAO;
import com.preethi.preethimart.dao.ReviewDAOImpl;
import com.preethi.preethimart.dto.ReviewDTO;
import com.preethi.preethimart.exception.ValidationException;
import com.preethi.preethimart.model.Review;
import com.preethi.preethimart.util.ValidationUtil;

import java.util.List;
import java.util.stream.Collectors;

public class ReviewService {

    private final ReviewDAO reviewDAO;

    public ReviewService() {
        this(new ReviewDAOImpl());
    }

    public ReviewService(ReviewDAO reviewDAO) {
        this.reviewDAO = reviewDAO;
    }

    public ReviewDTO submitReview(Long buyerId, Long productId, Long orderId, int rating, String comment) {
        if (buyerId == null || buyerId <= 0) {
            throw new ValidationException("Valid buyer ID is required.");
        }
        if (productId == null || productId <= 0) {
            throw new ValidationException("Valid product ID is required.");
        }

        ValidationUtil.validateRating(rating);
        ValidationUtil.validateNotBlank(comment, "Review Comment");

        // F8 rule: reviews and star ratings on completed/purchased orders
        if (!reviewDAO.hasBuyerPurchasedProduct(buyerId, productId)) {
            throw new ValidationException("Only verified buyers who have purchased this item can leave a review.");
        }

        Review review = new Review();
        review.setBuyerId(buyerId);
        review.setProductId(productId);
        review.setOrderId(orderId);
        review.setRating(rating);
        review.setComment(comment.trim());

        Review created = reviewDAO.create(review);
        return toDTO(created);
    }

    public List<ReviewDTO> getReviewsForProduct(Long productId) {
        if (productId == null || productId <= 0) {
            throw new ValidationException("Valid product ID is required.");
        }
        return reviewDAO.findByProductId(productId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ReviewDTO toDTO(Review r) {
        if (r == null) return null;
        ReviewDTO dto = new ReviewDTO();
        dto.setId(r.getId());
        dto.setProductId(r.getProductId());
        dto.setProductName(r.getProductName());
        dto.setBuyerId(r.getBuyerId());
        dto.setBuyerName(r.getBuyerName());
        dto.setOrderId(r.getOrderId());
        dto.setRating(r.getRating());
        dto.setComment(r.getComment());
        dto.setCreatedAt(r.getCreatedAt());
        return dto;
    }
}
