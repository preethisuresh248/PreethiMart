package com.preethi.preethimart.util;

import com.preethi.preethimart.exception.ValidationException;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public final class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private ValidationUtil() {}

    public static void validateNotBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " is required and cannot be empty.");
        }
    }

    public static void validateEmail(String email) {
        validateNotBlank(email, "Email");
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new ValidationException("Invalid email format: " + email);
        }
    }

    public static void validatePassword(String password) {
        validateNotBlank(password, "Password");
        if (password.length() < 6) {
            throw new ValidationException("Password must be at least 6 characters long.");
        }
    }

    public static void validateRole(String role) {
        validateNotBlank(role, "Role");
        String r = role.trim().toUpperCase();
        if (!"BUYER".equals(r) && !"SELLER".equals(r) && !"ADMIN".equals(r)) {
            throw new ValidationException("Invalid role specified: " + role + ". Allowed roles: BUYER, SELLER.");
        }
    }

    public static void validatePositivePrice(BigDecimal price, String fieldName) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(fieldName + " must be a positive value greater than zero.");
        }
    }

    public static void validateNonNegativeInteger(Integer val, String fieldName) {
        if (val == null || val < 0) {
            throw new ValidationException(fieldName + " must be zero or a positive integer.");
        }
    }

    public static void validateRating(Integer rating) {
        if (rating == null || rating < 1 || rating > 5) {
            throw new ValidationException("Rating must be an integer between 1 and 5.");
        }
    }
}
