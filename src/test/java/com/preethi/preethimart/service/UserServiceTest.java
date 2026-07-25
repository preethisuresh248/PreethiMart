package com.preethi.preethimart.service;

import com.preethi.preethimart.dao.UserDAO;
import com.preethi.preethimart.dto.UserLoginDTO;
import com.preethi.preethimart.dto.UserRegistrationDTO;
import com.preethi.preethimart.dto.UserResponseDTO;
import com.preethi.preethimart.exception.AuthenticationException;
import com.preethi.preethimart.exception.ValidationException;
import com.preethi.preethimart.model.User;
import com.preethi.preethimart.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserService(userDAO);
    }

    @Test
    @DisplayName("Register User - Valid Details Should Succeed")
    public void testRegisterSuccess() {
        UserRegistrationDTO dto = new UserRegistrationDTO("Preethi", "preethi@test.com", "Password@123", "BUYER");

        when(userDAO.findByEmail("preethi@test.com")).thenReturn(Optional.empty());
        when(userDAO.create(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(101L);
            return u;
        });

        UserResponseDTO result = userService.register(dto);
        assertNotNull(result);
        assertEquals(101L, result.getId());
        assertEquals("Preethi", result.getName());
        assertEquals("BUYER", result.getRole());
    }

    @Test
    @DisplayName("Register User - Short Password Should Throw ValidationException")
    public void testRegisterShortPassword() {
        UserRegistrationDTO dto = new UserRegistrationDTO("Preethi", "preethi@test.com", "123", "BUYER");

        ValidationException ex = assertThrows(ValidationException.class, () -> userService.register(dto));
        assertTrue(ex.getMessage().contains("at least 6 characters"));
    }

    @Test
    @DisplayName("Register User - Invalid Email Should Throw ValidationException")
    public void testRegisterInvalidEmail() {
        UserRegistrationDTO dto = new UserRegistrationDTO("Preethi", "not-an-email", "Password@123", "BUYER");

        assertThrows(ValidationException.class, () -> userService.register(dto));
    }

    @Test
    @DisplayName("Login - Invalid Password Should Throw AuthenticationException")
    public void testLoginInvalidPassword() {
        User user = new User();
        user.setEmail("user@test.com");
        user.setPasswordHash(PasswordUtil.hash("CorrectPassword"));

        when(userDAO.findByEmail("user@test.com")).thenReturn(Optional.of(user));

        UserLoginDTO dto = new UserLoginDTO("user@test.com", "WrongPassword");
        assertThrows(AuthenticationException.class, () -> userService.login(dto));
    }
}
