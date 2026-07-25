package com.preethi.preethimart.service;

import com.preethi.preethimart.dao.CartDAO;
import com.preethi.preethimart.dao.OrderDAO;
import com.preethi.preethimart.dao.ProductDAO;
import com.preethi.preethimart.dto.CartDTO;
import com.preethi.preethimart.dto.OrderDTO;
import com.preethi.preethimart.exception.ValidationException;
import com.preethi.preethimart.model.CartItem;
import com.preethi.preethimart.model.Order;
import com.preethi.preethimart.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderDAO orderDAO;
    @Mock
    private CartDAO cartDAO;
    @Mock
    private ProductDAO productDAO;
    @Mock
    private CartService cartService;

    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        orderService = new OrderService(orderDAO, cartDAO, productDAO, cartService);
    }

    @Test
    @DisplayName("Place Order - Empty Cart Should Throw ValidationException")
    public void testPlaceOrderEmptyCart() {
        CartDTO emptyCart = new CartDTO(); // no items
        when(cartService.getCart(4L)).thenReturn(emptyCart);

        ValidationException ex = assertThrows(ValidationException.class,
                () -> orderService.placeOrder(4L, "123 Test Street", "MOCK_CARD"));
        assertTrue(ex.getMessage().contains("cart is empty"));
    }

    @Test
    @DisplayName("Place Order - Insufficient Product Stock Should Reject")
    public void testPlaceOrderInsufficientStock() {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setTotalItems(1);
        cartDTO.getItems().add(new com.preethi.preethimart.dto.CartItemDTO());
        when(cartService.getCart(4L)).thenReturn(cartDTO);

        CartItem cartItem = new CartItem();
        cartItem.setProductId(1L);
        cartItem.setQuantity(10);
        when(cartDAO.findByUserId(4L)).thenReturn(List.of(cartItem));

        Product product = new Product();
        product.setId(1L);
        product.setName("Limited Item");
        product.setStockQuantity(2); // only 2 left, buyer wants 10
        when(productDAO.findById(1L)).thenReturn(Optional.of(product));

        ValidationException ex = assertThrows(ValidationException.class,
                () -> orderService.placeOrder(4L, "123 Test Street", "MOCK_CARD"));
        assertTrue(ex.getMessage().contains("Insufficient stock"));
    }

    @Test
    @DisplayName("Update Order Status - Valid Workflow Transition")
    public void testUpdateStatusValid() {
        when(orderDAO.updateStatus(10L, "SHIPPED")).thenReturn(true);
        boolean updated = orderService.updateOrderStatus(10L, "SHIPPED");
        assertTrue(updated);
    }

    @Test
    @DisplayName("Update Order Status - Invalid Status String Should Reject")
    public void testUpdateStatusInvalid() {
        assertThrows(ValidationException.class,
                () -> orderService.updateOrderStatus(10L, "INVALID_STATE"));
    }
}
