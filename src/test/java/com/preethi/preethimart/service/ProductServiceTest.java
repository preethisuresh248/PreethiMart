package com.preethi.preethimart.service;

import com.preethi.preethimart.dao.ProductDAO;
import com.preethi.preethimart.dto.ProductDTO;
import com.preethi.preethimart.exception.ValidationException;
import com.preethi.preethimart.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductDAO productDAO;

    private ProductService productService;

    @BeforeEach
    public void setUp() {
        productService = new ProductService(productDAO);
    }

    @Test
    @DisplayName("Create Product - Negative Price Should Throw ValidationException")
    public void testCreateProductNegativePrice() {
        ProductDTO dto = new ProductDTO();
        dto.setName("Wireless Mouse");
        dto.setCategory("Electronics");
        dto.setPrice(new BigDecimal("-10.00"));
        dto.setStockQuantity(10);

        assertThrows(ValidationException.class, () -> productService.createProduct(2L, dto));
    }

    @Test
    @DisplayName("Create Product - Success Flow")
    public void testCreateProductSuccess() {
        ProductDTO dto = new ProductDTO();
        dto.setName("Wireless Mouse");
        dto.setCategory("Electronics");
        dto.setPrice(new BigDecimal("799.00"));
        dto.setStockQuantity(15);
        dto.setDescription("High precision optical mouse");

        when(productDAO.create(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            p.setId(50L);
            return p;
        });

        Product created = productService.createProduct(2L, dto);
        assertNotNull(created);
        assertEquals(50L, created.getId());
        assertEquals("Wireless Mouse", created.getName());
    }

    @Test
    @DisplayName("Update Product - Foreign Seller Cannot Edit Another Seller's Listing")
    public void testUnauthorizedSellerEdit() {
        Product existing = new Product();
        existing.setId(10L);
        existing.setSellerId(2L); // owned by seller 2

        when(productDAO.findById(10L)).thenReturn(Optional.of(existing));

        ProductDTO updateDTO = new ProductDTO();
        updateDTO.setId(10L);
        updateDTO.setName("Hacked Listing");
        updateDTO.setCategory("Electronics");
        updateDTO.setPrice(new BigDecimal("100.00"));
        updateDTO.setStockQuantity(5);

        // seller 3 attempts to update
        ValidationException ex = assertThrows(ValidationException.class,
                () -> productService.updateProduct(3L, updateDTO));
        assertTrue(ex.getMessage().contains("Unauthorized"));
    }
}
