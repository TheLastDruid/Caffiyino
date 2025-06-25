package com.coffeeshop.dao;

import com.coffeeshop.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * DAO interface for User entity
 */
public interface UserDAO extends GenericDAO<User, Long> {
    
    /**
     * Find user by username
     * @param username Username to search for
     * @return Optional containing user if found
     * @throws SQLException if database error occurs
     */
    Optional<User> findByUsername(String username) throws SQLException;
    
    /**
     * Find users by role
     * @param role User role
     * @return List of users with specified role
     * @throws SQLException if database error occurs
     */
    List<User> findByRole(User.Role role) throws SQLException;
    
    /**
     * Find active users
     * @return List of active users
     * @throws SQLException if database error occurs
     */
    List<User> findActiveUsers() throws SQLException;
    
    /**
     * Update user password
     * @param userId User ID
     * @param newPassword New password (should be hashed)
     * @throws SQLException if database error occurs
     */
    void updatePassword(Long userId, String newPassword) throws SQLException;
    
    /**
     * Activate or deactivate user
     * @param userId User ID
     * @param active Active status
     * @throws SQLException if database error occurs
     */
    void updateActiveStatus(Long userId, boolean active) throws SQLException;
}
