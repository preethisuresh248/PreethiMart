package com.preethi.preethimart.dao;

import com.preethi.preethimart.model.Product;
import com.preethi.preethimart.util.DBConnectionManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ProductDAOTest {

    private static ProductDAO productDAO;

    @BeforeAll
    public static void setUp() {
        DBConnectionManager.getInstance();
        productDAO = new ProductDAOImpl();
    }

    @Test
    @DisplayName("Find Seed Products and Filter by Category")
    public void testFindAllAndFilterCategory() {
        List<Product> electronics = productDAO.findAll("Electronics", null, 10, 0);
        assertFalse(electronics.isEmpty(), "Should retrieve seeded electronics products");
        for (Product p : electronics) {
            assertEquals("Electronics", p.getCategory());
        }
    }

    @Test
    @DisplayName("Search Products by Keyword")
    public void testSearchKeyword() {
        List<Product> results = productDAO.findAll(null, "Headphones", 10, 0);
        assertFalse(results.isEmpty(), "Should find product matching keyword 'Headphones'");
        assertTrue(results.get(0).getName().contains("Headphones"));
    }

    @Test
    @DisplayName("Create New Product and Retrieve by ID")
    public void testCreateProduct() {
        Product p = new Product();
        p.setSellerId(2L); // seller1
        p.setName("Test Smart Thermostat " + System.currentTimeMillis());
        p.setDescription("IoT Wi-Fi smart room temperature control thermostat");
        p.setCategory("Electronics");
        p.setPrice(new BigDecimal("1999.00"));
        p.setStockQuantity(25);
        p.setImageUrl("https://example.com/thermostat.jpg");
        p.setStatus("ACTIVE");

        Product created = productDAO.create(p);
        assertNotNull(created.getId());

        Optional<Product> found = productDAO.findById(created.getId());
        assertTrue(found.isPresent());
        assertEquals(25, found.get().getStockQuantity());
    }
}
