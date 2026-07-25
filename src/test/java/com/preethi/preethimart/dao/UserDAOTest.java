package com.preethi.preethimart.dao;

import com.preethi.preethimart.model.User;
import com.preethi.preethimart.util.DBConnectionManager;
import com.preethi.preethimart.util.PasswordUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {

    private static UserDAO userDAO;

    @BeforeAll
    public static void setUp() {
        DBConnectionManager.getInstance();
        userDAO = new UserDAOImpl();
    }

    @Test
    @DisplayName("Create User and Find by Email")
    public void testCreateAndFindByEmail() {
        String testEmail = "testuser_" + System.currentTimeMillis() + "@preethimart.com";
        User user = new User();
        user.setName("Test Student");
        user.setEmail(testEmail);
        user.setPasswordHash(PasswordUtil.hash("Test@12345"));
        user.setRole("BUYER");

        User created = userDAO.create(user);
        assertNotNull(created.getId(), "User ID should be generated upon insertion");

        Optional<User> found = userDAO.findByEmail(testEmail);
        assertTrue(found.isPresent(), "User should be found by email");
        assertEquals("Test Student", found.get().getName());
        assertTrue(PasswordUtil.verify("Test@12345", found.get().getPasswordHash()));
    }

    @Test
    @DisplayName("Find Seed Administrator Account")
    public void testFindSeedAdmin() {
        Optional<User> admin = userDAO.findByEmail("admin@preethimart.com");
        assertTrue(admin.isPresent(), "Seeded admin account must exist");
        assertEquals("ADMIN", admin.get().getRole());
        assertTrue(admin.get().isAdmin());
    }
}
