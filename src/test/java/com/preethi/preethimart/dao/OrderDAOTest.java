package com.preethi.preethimart.dao;

import com.preethi.preethimart.model.Order;
import com.preethi.preethimart.model.OrderItem;
import com.preethi.preethimart.model.Product;
import com.preethi.preethimart.util.DBConnectionManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class OrderDAOTest {

    private static OrderDAO orderDAO;
    private static ProductDAO productDAO;

    @BeforeAll
    public static void setUp() {
        DBConnectionManager.getInstance();
        orderDAO = new OrderDAOImpl();
        productDAO = new ProductDAOImpl();
    }

    @Test
    @DisplayName("Create Order Transactionally and Decrement Stock")
    public void testCreateOrderAndDecrementStock() {
        Long buyerId = 5L; // buyer2
        Long productId = 4L; // 4K Monitor

        Optional<Product> initialProduct = productDAO.findById(productId);
        assertTrue(initialProduct.isPresent());
        int initialStock = initialProduct.get().getStockQuantity();

        Order order = new Order();
        order.setBuyerId(buyerId);
        order.setTotalAmount(new BigDecimal("21999.00"));
        order.setStatus("CONFIRMED");
        order.setShippingAddress("Anna University Hostels, Guindy, Chennai 600025");
        order.setPaymentMethod("MOCK_UPI");

        List<OrderItem> items = new ArrayList<>();
        OrderItem item = new OrderItem();
        item.setProductId(productId);
        item.setSellerId(initialProduct.get().getSellerId());
        item.setQuantity(1);
        item.setPriceAtPurchase(initialProduct.get().getPrice());
        items.add(item);

        Order created = orderDAO.createOrder(order, items);
        assertNotNull(created.getId());

        // Verify stock decremented
        Optional<Product> updatedProduct = productDAO.findById(productId);
        assertTrue(updatedProduct.isPresent());
        assertEquals(initialStock - 1, updatedProduct.get().getStockQuantity(), "Stock must decrement by purchase quantity");

        // Verify status update
        boolean statusUpdated = orderDAO.updateStatus(created.getId(), "SHIPPED");
        assertTrue(statusUpdated);

        Optional<Order> fetched = orderDAO.findById(created.getId());
        assertTrue(fetched.isPresent());
        assertEquals("SHIPPED", fetched.get().getStatus());
    }
}
