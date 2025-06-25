package com.coffeeshop.service;

import com.coffeeshop.model.User;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Service for user authentication and management
 */
public class AuthenticationService {
    private static AuthenticationService instance;
    private User currentUser;
    
    private AuthenticationService() {}
    
    public static synchronized AuthenticationService getInstance() {
        if (instance == null) {
            instance = new AuthenticationService();
        }
        return instance;
    }
    
    /**
     * Hash a password using BCrypt
     * @param password Plain text password
     * @return Hashed password
     */
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
    
    /**
     * Verify a password against its hash
     * @param password Plain text password
     * @param hashedPassword Hashed password
     * @return true if password matches
     */
    public boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
    
    /**
     * Set current authenticated user
     * @param user Current user
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    /**
     * Get current authenticated user
     * @return Current user
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Check if user is authenticated
     * @return true if user is logged in
     */
    public boolean isAuthenticated() {
        return currentUser != null;
    }
    
    /**
     * Check if current user has specific role
     * @param role Role to check
     * @return true if user has role
     */
    public boolean hasRole(User.Role role) {
        return isAuthenticated() && currentUser.getRole() == role;
    }
    
    /**
     * Check if current user is admin
     * @return true if user is admin
     */
    public boolean isAdmin() {
        return hasRole(User.Role.ADMIN);
    }
    
    /**
     * Check if current user is waiter
     * @return true if user is waiter
     */
    public boolean isWaiter() {
        return hasRole(User.Role.WAITER);
    }
    
    /**
     * Check if current user is kitchen staff
     * @return true if user is kitchen staff
     */
    public boolean isKitchen() {
        return hasRole(User.Role.KITCHEN);
    }
    
    /**
     * Logout current user
     */
    public void logout() {
        this.currentUser = null;
    }
}
