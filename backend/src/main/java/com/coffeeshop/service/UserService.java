package com.coffeeshop.service;

import com.coffeeshop.dao.UserDAO;
import com.coffeeshop.dao.impl.UserDAOImpl;
import com.coffeeshop.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Service class for User operations
 */
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserDAO userDAO;
    private final AuthenticationService authService;
    
    public UserService() {
        this.userDAO = new UserDAOImpl();
        this.authService = AuthenticationService.getInstance();
    }
    
    /**
     * Authenticate user with username and password
     * @param username Username
     * @param password Plain text password
     * @return User if authentication successful
     * @throws SQLException if database error occurs
     * @throws IllegalArgumentException if authentication fails
     */
    public User authenticate(String username, String password) throws SQLException {
        Optional<User> userOpt = userDAO.findByUsername(username);
        
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        
        User user = userOpt.get();
        
        if (!user.isActive()) {
            throw new IllegalArgumentException("User account is disabled");
        }
        
        if (!authService.verifyPassword(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        
        authService.setCurrentUser(user);
        logger.info("User authenticated successfully: {}", username);
        return user;
    }
    
    /**
     * Create new user
     * @param user User to create
     * @return Created user
     * @throws SQLException if database error occurs
     * @throws IllegalArgumentException if validation fails
     */
    public User createUser(User user) throws SQLException {
        validateUser(user);
        
        // Check if username already exists
        if (userDAO.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        // Hash password
        user.setPassword(authService.hashPassword(user.getPassword()));
        
        User createdUser = userDAO.save(user);
        logger.info("User created successfully: {}", user.getUsername());
        return createdUser;
    }
    
    /**
     * Update existing user
     * @param user User to update
     * @throws SQLException if database error occurs
     * @throws IllegalArgumentException if validation fails
     */
    public void updateUser(User user) throws SQLException {
        validateUser(user);
        
        // Check if username is taken by another user
        Optional<User> existingUser = userDAO.findByUsername(user.getUsername());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        userDAO.update(user);
        logger.info("User updated successfully: {}", user.getUsername());
    }
    
    /**
     * Change user password
     * @param userId User ID
     * @param oldPassword Current password
     * @param newPassword New password
     * @throws SQLException if database error occurs
     * @throws IllegalArgumentException if old password is incorrect
     */
    public void changePassword(Long userId, String oldPassword, String newPassword) throws SQLException {
        Optional<User> userOpt = userDAO.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        
        User user = userOpt.get();
        if (!authService.verifyPassword(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        
        String hashedNewPassword = authService.hashPassword(newPassword);
        userDAO.updatePassword(userId, hashedNewPassword);
        logger.info("Password changed for user ID: {}", userId);
    }
    
    /**
     * Get all users
     * @return List of all users
     * @throws SQLException if database error occurs
     */
    public List<User> getAllUsers() throws SQLException {
        return userDAO.findAll();
    }
    
    /**
     * Get users by role
     * @param role User role
     * @return List of users with specified role
     * @throws SQLException if database error occurs
     */
    public List<User> getUsersByRole(User.Role role) throws SQLException {
        return userDAO.findByRole(role);
    }
    
    /**
     * Get active users
     * @return List of active users
     * @throws SQLException if database error occurs
     */
    public List<User> getActiveUsers() throws SQLException {
        return userDAO.findActiveUsers();
    }
    
    /**
     * Get user by ID
     * @param id User ID
     * @return User if found
     * @throws SQLException if database error occurs
     * @throws IllegalArgumentException if user not found
     */
    public User getUserById(Long id) throws SQLException {
        return userDAO.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
    }
    
    /**
     * Delete user
     * @param id User ID
     * @throws SQLException if database error occurs
     */
    public void deleteUser(Long id) throws SQLException {
        userDAO.deleteById(id);
        logger.info("User deleted with ID: {}", id);
    }
    
    /**
     * Activate or deactivate user
     * @param userId User ID
     * @param active Active status
     * @throws SQLException if database error occurs
     */
    public void updateUserStatus(Long userId, boolean active) throws SQLException {
        userDAO.updateActiveStatus(userId, active);
        logger.info("User status updated for ID: {} to {}", userId, active);
    }
    
    private void validateUser(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        
        if (user.getUsername().length() < 3) {
            throw new IllegalArgumentException("Username must be at least 3 characters");
        }
        
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Full name is required");
        }
        
        if (user.getRole() == null) {
            throw new IllegalArgumentException("Role is required");
        }
        
        if (user.getPassword() != null && user.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
        
        if (user.getEmail() != null && !user.getEmail().trim().isEmpty()) {
            if (!isValidEmail(user.getEmail())) {
                throw new IllegalArgumentException("Invalid email format");
            }
        }
    }
    
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}
