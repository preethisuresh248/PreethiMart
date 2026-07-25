package com.preethi.preethimart.service;

import com.preethi.preethimart.dao.UserDAO;
import com.preethi.preethimart.dao.UserDAOImpl;
import com.preethi.preethimart.dto.UserLoginDTO;
import com.preethi.preethimart.dto.UserRegistrationDTO;
import com.preethi.preethimart.dto.UserResponseDTO;
import com.preethi.preethimart.exception.AuthenticationException;
import com.preethi.preethimart.exception.ResourceNotFoundException;
import com.preethi.preethimart.exception.ValidationException;
import com.preethi.preethimart.model.User;
import com.preethi.preethimart.util.PasswordUtil;
import com.preethi.preethimart.util.ValidationUtil;

import java.util.List;
import java.util.stream.Collectors;

public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this(new UserDAOImpl());
    }

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserResponseDTO register(UserRegistrationDTO dto) {
        if (dto == null) {
            throw new ValidationException("Registration payload cannot be null.");
        }
        ValidationUtil.validateNotBlank(dto.getName(), "Full Name");
        ValidationUtil.validateEmail(dto.getEmail());
        ValidationUtil.validatePassword(dto.getPassword());
        ValidationUtil.validateRole(dto.getRole());

        if (userDAO.findByEmail(dto.getEmail()).isPresent()) {
            throw new ValidationException("An account with email " + dto.getEmail() + " already exists.");
        }

        String hashedPassword = PasswordUtil.hash(dto.getPassword());
        User user = new User();
        user.setName(dto.getName().trim());
        user.setEmail(dto.getEmail().toLowerCase().trim());
        user.setPasswordHash(hashedPassword);
        user.setRole(dto.getRole().trim().toUpperCase());

        User created = userDAO.create(user);
        return toResponseDTO(created);
    }

    public User login(UserLoginDTO dto) {
        if (dto == null) {
            throw new ValidationException("Login payload cannot be null.");
        }
        ValidationUtil.validateEmail(dto.getEmail());
        ValidationUtil.validateNotBlank(dto.getPassword(), "Password");

        User user = userDAO.findByEmail(dto.getEmail())
                .orElseThrow(() -> new AuthenticationException("Invalid email or password."));

        if (!PasswordUtil.verify(dto.getPassword(), user.getPasswordHash())) {
            throw new AuthenticationException("Invalid email or password.");
        }

        return user;
    }

    public UserResponseDTO getUserById(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException("Valid user ID is required.");
        }
        User user = userDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        return toResponseDTO(user);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userDAO.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public boolean deleteUser(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException("Valid user ID is required.");
        }
        return userDAO.delete(id);
    }

    public UserResponseDTO toResponseDTO(User user) {
        if (user == null) return null;
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}
