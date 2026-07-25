package com.preethi.preethimart.service;

import com.preethi.preethimart.dao.CartDAO;
import com.preethi.preethimart.dao.CartDAOImpl;
import com.preethi.preethimart.dao.OrderDAO;
import com.preethi.preethimart.dao.OrderDAOImpl;
import com.preethi.preethimart.dao.ProductDAO;
import com.preethi.preethimart.dao.ProductDAOImpl;
import com.preethi.preethimart.dto.CartDTO;
import com.preethi.preethimart.dto.OrderDTO;
import com.preethi.preethimart.dto.OrderItemDTO;
import com.preethi.preethimart.exception.ResourceNotFoundException;
import com.preethi.preethimart.exception.ValidationException;
import com.preethi.preethimart.model.CartItem;
import com.preethi.preethimart.model.Order;
import com.preethi.preethimart.model.OrderItem;
import com.preethi.preethimart.model.Product;
import com.preethi.preethimart.util.ValidationUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OrderService {

    private static final List<String> VALID_STATUSES = Arrays.asList(
            "PENDING", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED"
    );

    private final OrderDAO orderDAO;
    private final CartDAO cartDAO;
    private final ProductDAO productDAO;
    private final CartService cartService;

    public OrderService() {
        this(new OrderDAOImpl(), new CartDAOImpl(), new ProductDAOImpl(), new CartServiceImpl());
    }

    public OrderService(OrderDAO orderDAO, CartDAO cartDAO, ProductDAO productDAO, CartService cartService) {
        this.orderDAO = orderDAO;
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
        this.cartService = cartService;
    }

    public OrderDTO placeOrder(Long buyerId, String shippingAddress, String paymentMethod) {
        if (buyerId == null || buyerId <= 0) {
            throw new ValidationException("Valid buyer ID is required.");
        }
        ValidationUtil.validateNotBlank(shippingAddress, "Shipping Address");

        CartDTO cartDTO = cartService.getCart(buyerId);
        if (cartDTO.getItems().isEmpty()) {
            throw new ValidationException("Cannot place order: your shopping cart is empty.");
        }

        List<CartItem> cartItems = cartDAO.findByUserId(buyerId);
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem ci : cartItems) {
            Product p = productDAO.findById(ci.getProductId())
                    .orElseThrow(() -> new ValidationException("Product no longer exists."));

            if (p.getStockQuantity() < ci.getQuantity()) {
                throw new ValidationException("Insufficient stock for '" + p.getName() + "'. Available: " + p.getStockQuantity());
            }

            OrderItem oi = new OrderItem();
            oi.setProductId(p.getId());
            oi.setSellerId(p.getSellerId());
            oi.setQuantity(ci.getQuantity());
            oi.setPriceAtPurchase(p.getPrice());
            orderItems.add(oi);
        }

        Order order = new Order();
        order.setBuyerId(buyerId);
        order.setTotalAmount(cartDTO.getGrandTotal());
        order.setStatus("CONFIRMED"); // Auto-confirmed on successful mock payment
        order.setShippingAddress(shippingAddress.trim());
        order.setPaymentMethod(paymentMethod != null && !paymentMethod.trim().isEmpty() ? paymentMethod.trim() : "MOCK_CARD");

        Order created = orderDAO.createOrder(order, orderItems);
        return getOrderById(created.getId());
    }

    public OrderDTO getOrderById(Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new ValidationException("Valid order ID is required.");
        }
        Order order = orderDAO.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        return toDTO(order);
    }

    public List<OrderDTO> getOrdersForBuyer(Long buyerId) {
        if (buyerId == null || buyerId <= 0) {
            throw new ValidationException("Valid buyer ID is required.");
        }
        return orderDAO.findByBuyerId(buyerId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getOrdersForSeller(Long sellerId) {
        if (sellerId == null || sellerId <= 0) {
            throw new ValidationException("Valid seller ID is required.");
        }
        return orderDAO.findBySellerId(sellerId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getAllOrders() {
        return orderDAO.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public boolean updateOrderStatus(Long orderId, String newStatus) {
        if (orderId == null || orderId <= 0) {
            throw new ValidationException("Valid order ID is required.");
        }
        ValidationUtil.validateNotBlank(newStatus, "Order Status");
        String status = newStatus.trim().toUpperCase();

        if (!VALID_STATUSES.contains(status)) {
            throw new ValidationException("Invalid order status: " + newStatus + ". Allowed: " + VALID_STATUSES);
        }

        return orderDAO.updateStatus(orderId, status);
    }

    public OrderDTO toDTO(Order order) {
        if (order == null) return null;
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setBuyerId(order.getBuyerId());
        dto.setBuyerName(order.getBuyerName());
        dto.setBuyerEmail(order.getBuyerEmail());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setCreatedAt(order.getCreatedAt());

        if (order.getItems() != null) {
            List<OrderItemDTO> itemDTOs = order.getItems().stream().map(item -> {
                OrderItemDTO idto = new OrderItemDTO();
                idto.setId(item.getId());
                idto.setProductId(item.getProductId());
                idto.setProductName(item.getProductName());
                idto.setProductImageUrl(item.getProductImageUrl());
                idto.setSellerId(item.getSellerId());
                idto.setSellerName(item.getSellerName());
                idto.setQuantity(item.getQuantity());
                idto.setPriceAtPurchase(item.getPriceAtPurchase());
                idto.setSubtotal(item.getSubtotal());
                return idto;
            }).collect(Collectors.toList());
            dto.setItems(itemDTOs);
        }
        return dto;
    }
}
