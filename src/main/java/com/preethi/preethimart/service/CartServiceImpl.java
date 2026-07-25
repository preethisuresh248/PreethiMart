package com.preethi.preethimart.service;

import com.preethi.preethimart.dao.CartDAO;
import com.preethi.preethimart.dao.CartDAOImpl;
import com.preethi.preethimart.dao.ProductDAO;
import com.preethi.preethimart.dao.ProductDAOImpl;
import com.preethi.preethimart.dto.CartDTO;
import com.preethi.preethimart.dto.CartItemDTO;
import com.preethi.preethimart.exception.ResourceNotFoundException;
import com.preethi.preethimart.exception.ValidationException;
import com.preethi.preethimart.model.CartItem;
import com.preethi.preethimart.model.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class CartServiceImpl implements CartService {

    private final CartDAO cartDAO;
    private final ProductDAO productDAO;

    public CartServiceImpl() {
        this(new CartDAOImpl(), new ProductDAOImpl());
    }

    public CartServiceImpl(CartDAO cartDAO, ProductDAO productDAO) {
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
    }

    @Override
    public CartDTO getCart(Long userId) {
        if (userId == null || userId <= 0) {
            throw new ValidationException("Valid user ID is required.");
        }

        List<CartItem> items = cartDAO.findByUserId(userId);
        CartDTO cart = new CartDTO();
        List<CartItemDTO> dtoList = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;
        int totalItems = 0;

        for (CartItem item : items) {
            CartItemDTO dto = new CartItemDTO();
            dto.setId(item.getId());
            dto.setProductId(item.getProductId());
            dto.setProductName(item.getProductName());
            dto.setProductPrice(item.getProductPrice());
            dto.setProductImageUrl(item.getProductImageUrl());
            dto.setQuantity(item.getQuantity());
            dto.setAvailableStock(item.getAvailableStock());
            dto.setSubtotal(item.getSubtotal());

            dtoList.add(dto);
            subtotal = subtotal.add(item.getSubtotal());
            totalItems += item.getQuantity();
        }

        cart.setItems(dtoList);
        cart.setTotalItems(totalItems);
        cart.setSubtotal(subtotal);

        // 5% Estimated GST / Tax
        BigDecimal tax = subtotal.multiply(new BigDecimal("0.05")).setScale(2, RoundingMode.HALF_UP);
        cart.setEstimatedTax(tax);

        // Free shipping if subtotal >= 500, else 49.00
        BigDecimal shipping = (subtotal.compareTo(new BigDecimal("500.00")) >= 0 || subtotal.compareTo(BigDecimal.ZERO) == 0)
                ? BigDecimal.ZERO
                : new BigDecimal("49.00");
        cart.setShippingFee(shipping);

        cart.setGrandTotal(subtotal.add(tax).add(shipping).setScale(2, RoundingMode.HALF_UP));

        return cart;
    }

    @Override
    public void addToCart(Long userId, Long productId, int quantity) {
        if (userId == null || userId <= 0) {
            throw new ValidationException("Valid user ID is required.");
        }
        if (productId == null || productId <= 0) {
            throw new ValidationException("Valid product ID is required.");
        }
        if (quantity <= 0) {
            throw new ValidationException("Quantity to add must be at least 1.");
        }

        Product product = productDAO.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        if (!product.isInStock()) {
            throw new ValidationException("Sorry, '" + product.getName() + "' is currently out of stock.");
        }

        if (product.getStockQuantity() < quantity) {
            throw new ValidationException("Only " + product.getStockQuantity() + " units available in stock.");
        }

        cartDAO.addItem(userId, productId, quantity);
    }

    @Override
    public void updateQuantity(Long userId, Long productId, int quantity) {
        if (userId == null || userId <= 0) {
            throw new ValidationException("Valid user ID is required.");
        }
        if (productId == null || productId <= 0) {
            throw new ValidationException("Valid product ID is required.");
        }
        if (quantity <= 0) {
            cartDAO.removeItem(userId, productId);
            return;
        }

        Product product = productDAO.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        if (product.getStockQuantity() < quantity) {
            throw new ValidationException("Cannot update quantity: only " + product.getStockQuantity() + " units in stock.");
        }

        cartDAO.updateQuantity(userId, productId, quantity);
    }

    @Override
    public void removeFromCart(Long userId, Long productId) {
        if (userId == null || userId <= 0) {
            throw new ValidationException("Valid user ID is required.");
        }
        if (productId == null || productId <= 0) {
            throw new ValidationException("Valid product ID is required.");
        }
        cartDAO.removeItem(userId, productId);
    }

    @Override
    public int getCartCount(Long userId) {
        if (userId == null || userId <= 0) return 0;
        return cartDAO.getCartItemCount(userId);
    }
}
