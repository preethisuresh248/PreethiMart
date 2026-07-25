package com.preethi.preethimart.dao;

import com.preethi.preethimart.model.CartItem;
import com.preethi.preethimart.util.DBConnectionManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CartDAOTest {

    private static CartDAO cartDAO;

    @BeforeAll
    public static void setUp() {
        DBConnectionManager.getInstance();
        cartDAO = new CartDAOImpl();
    }

    @Test
    @DisplayName("Add Item to Cart, Update, and Remove")
    public void testCartOperations() {
        Long testUserId = 4L; // buyer1
        Long testProductId = 3L; // keyboard

        // Remove first if already present
        cartDAO.removeItem(testUserId, testProductId);

        // Add 2 items
        cartDAO.addItem(testUserId, testProductId, 2);
        List<CartItem> items = cartDAO.findByUserId(testUserId);
        assertTrue(items.stream().anyMatch(i -> i.getProductId().equals(testProductId)));

        // Update quantity to 4
        cartDAO.updateQuantity(testUserId, testProductId, 4);
        items = cartDAO.findByUserId(testUserId);
        CartItem item = items.stream().filter(i -> i.getProductId().equals(testProductId)).findFirst().orElse(null);
        assertNotNull(item);
        assertEquals(4, item.getQuantity());

        // Remove item
        cartDAO.removeItem(testUserId, testProductId);
        items = cartDAO.findByUserId(testUserId);
        assertFalse(items.stream().anyMatch(i -> i.getProductId().equals(testProductId)));
    }
}
