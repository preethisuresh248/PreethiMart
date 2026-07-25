package com.preethi.preethimart.dao;

import com.preethi.preethimart.model.Product;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProductDAO {
    Product create(Product product);
    Optional<Product> findById(Long id);
    List<Product> findAll(String category, String keyword, int limit, int offset);
    int countAll(String category, String keyword);
    List<Product> findBySellerId(Long sellerId);
    List<String> findAllCategories();
    boolean update(Product product);
    boolean updateStock(Long id, int newStock, Connection conn) throws SQLException;
    boolean updateStatus(Long id, String status);
    boolean delete(Long id);
}
