package com.preethi.preethimart.service;

import com.preethi.preethimart.dao.ProductDAO;
import com.preethi.preethimart.dao.ProductDAOImpl;
import com.preethi.preethimart.dto.ProductDTO;
import com.preethi.preethimart.exception.ResourceNotFoundException;
import com.preethi.preethimart.exception.ValidationException;
import com.preethi.preethimart.model.Product;
import com.preethi.preethimart.util.ValidationUtil;

import java.util.List;

public class ProductService {

    private final ProductDAO productDAO;

    public ProductService() {
        this(new ProductDAOImpl());
    }

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public Product createProduct(Long sellerId, ProductDTO dto) {
        if (sellerId == null || sellerId <= 0) {
            throw new ValidationException("Valid seller ID is required.");
        }
        if (dto == null) {
            throw new ValidationException("Product data cannot be null.");
        }

        ValidationUtil.validateNotBlank(dto.getName(), "Product Name");
        ValidationUtil.validateNotBlank(dto.getCategory(), "Category");
        ValidationUtil.validatePositivePrice(dto.getPrice(), "Price");
        ValidationUtil.validateNonNegativeInteger(dto.getStockQuantity(), "Stock Quantity");

        Product product = new Product();
        product.setSellerId(sellerId);
        product.setName(dto.getName().trim());
        product.setDescription(dto.getDescription() != null ? dto.getDescription().trim() : "");
        product.setCategory(dto.getCategory().trim());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setImageUrl(dto.getImageUrl() != null && !dto.getImageUrl().trim().isEmpty()
                ? dto.getImageUrl().trim()
                : "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500");
        product.setStatus("ACTIVE");

        return productDAO.create(product);
    }

    public Product getProductById(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException("Valid product ID is required.");
        }
        return productDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
    }

    public List<Product> listProducts(String category, String keyword, int page, int pageSize) {
        int limit = pageSize > 0 ? pageSize : 12;
        int offset = page > 0 ? (page - 1) * limit : 0;
        return productDAO.findAll(category, keyword, limit, offset);
    }

    public int countProducts(String category, String keyword) {
        return productDAO.countAll(category, keyword);
    }

    public List<Product> getProductsBySeller(Long sellerId) {
        if (sellerId == null || sellerId <= 0) {
            throw new ValidationException("Valid seller ID is required.");
        }
        return productDAO.findBySellerId(sellerId);
    }

    public List<String> getCategories() {
        return productDAO.findAllCategories();
    }

    public boolean updateProduct(Long sellerId, ProductDTO dto) {
        if (sellerId == null || sellerId <= 0) {
            throw new ValidationException("Valid seller ID is required.");
        }
        if (dto == null || dto.getId() == null) {
            throw new ValidationException("Product ID is required for update.");
        }
        ValidationUtil.validateNotBlank(dto.getName(), "Product Name");
        ValidationUtil.validateNotBlank(dto.getCategory(), "Category");
        ValidationUtil.validatePositivePrice(dto.getPrice(), "Price");
        ValidationUtil.validateNonNegativeInteger(dto.getStockQuantity(), "Stock Quantity");

        Product product = getProductById(dto.getId());
        if (!product.getSellerId().equals(sellerId)) {
            throw new ValidationException("Unauthorized: You can only edit your own listings.");
        }

        product.setName(dto.getName().trim());
        product.setDescription(dto.getDescription() != null ? dto.getDescription().trim() : "");
        product.setCategory(dto.getCategory().trim());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        if (dto.getImageUrl() != null && !dto.getImageUrl().trim().isEmpty()) {
            product.setImageUrl(dto.getImageUrl().trim());
        }
        if (dto.getStatus() != null) {
            product.setStatus(dto.getStatus().trim().toUpperCase());
        }

        return productDAO.update(product);
    }

    public boolean updateStatus(Long productId, String status) {
        if (productId == null || productId <= 0) {
            throw new ValidationException("Valid product ID is required.");
        }
        ValidationUtil.validateNotBlank(status, "Status");
        return productDAO.updateStatus(productId, status.trim().toUpperCase());
    }

    public boolean deleteProduct(Long productId) {
        if (productId == null || productId <= 0) {
            throw new ValidationException("Valid product ID is required.");
        }
        return productDAO.delete(productId);
    }
}
