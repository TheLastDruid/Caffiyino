package com.coffeeshop.dao.impl;

import com.coffeeshop.config.DatabaseConfig;
import com.coffeeshop.dao.UserDAO;
import com.coffeeshop.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of UserDAO interface
 */
public class UserDAOImpl implements UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);
    
    private static final String INSERT_USER = 
        "INSERT INTO users (username, password, role, full_name, email, is_active) VALUES (?, ?, ?, ?, ?, ?)";
    
    private static final String SELECT_USER_BY_ID = 
        "SELECT * FROM users WHERE id = ?";
    
    private static final String SELECT_ALL_USERS = 
        "SELECT * FROM users ORDER BY full_name";
    
    private static final String SELECT_USER_BY_USERNAME = 
        "SELECT * FROM users WHERE username = ?";
    
    private static final String SELECT_USERS_BY_ROLE = 
        "SELECT * FROM users WHERE role = ? ORDER BY full_name";
    
    private static final String SELECT_ACTIVE_USERS = 
        "SELECT * FROM users WHERE is_active = TRUE ORDER BY full_name";
    
    private static final String UPDATE_USER = 
        "UPDATE users SET username = ?, role = ?, full_name = ?, email = ?, is_active = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
    
    private static final String UPDATE_PASSWORD = 
        "UPDATE users SET password = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
    
    private static final String UPDATE_ACTIVE_STATUS = 
        "UPDATE users SET is_active = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
    
    private static final String DELETE_USER = 
        "DELETE FROM users WHERE id = ?";
    
    private static final String COUNT_USERS = 
        "SELECT COUNT(*) FROM users";
    
    private static final String EXISTS_USER = 
        "SELECT COUNT(*) FROM users WHERE id = ?";
    
    @Override
    public User save(User user) throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole().name());
            stmt.setString(4, user.getFullName());
            stmt.setString(5, user.getEmail());
            stmt.setBoolean(6, user.isActive());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
            
            logger.info("User created successfully: {}", user.getUsername());
            return user;
        }
    }
    
    @Override
    public Optional<User> findById(Long id) throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_USER_BY_ID)) {
            
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
        }
        return Optional.empty();
    }
    
    @Override
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_USERS);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        }
        return users;
    }
    
    @Override
    public Optional<User> findByUsername(String username) throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_USER_BY_USERNAME)) {
            
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
        }
        return Optional.empty();
    }
    
    @Override
    public List<User> findByRole(User.Role role) throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_USERS_BY_ROLE)) {
            
            stmt.setString(1, role.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(mapResultSetToUser(rs));
                }
            }
        }
        return users;
    }
    
    @Override
    public List<User> findActiveUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ACTIVE_USERS);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        }
        return users;
    }
    
    @Override
    public void update(User user) throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_USER)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getRole().name());
            stmt.setString(3, user.getFullName());
            stmt.setString(4, user.getEmail());
            stmt.setBoolean(5, user.isActive());
            stmt.setLong(6, user.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating user failed, no rows affected.");
            }
            
            logger.info("User updated successfully: {}", user.getUsername());
        }
    }
    
    @Override
    public void updatePassword(Long userId, String newPassword) throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_PASSWORD)) {
            
            stmt.setString(1, newPassword);
            stmt.setLong(2, userId);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating password failed, no rows affected.");
            }
            
            logger.info("Password updated for user ID: {}", userId);
        }
    }
    
    @Override
    public void updateActiveStatus(Long userId, boolean active) throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_ACTIVE_STATUS)) {
            
            stmt.setBoolean(1, active);
            stmt.setLong(2, userId);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating active status failed, no rows affected.");
            }
            
            logger.info("Active status updated for user ID: {} to {}", userId, active);
        }
    }
    
    @Override
    public void deleteById(Long id) throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_USER)) {
            
            stmt.setLong(1, id);
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Deleting user failed, no rows affected.");
            }
            
            logger.info("User deleted successfully with ID: {}", id);
        }
    }
    
    @Override
    public boolean existsById(Long id) throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(EXISTS_USER)) {
            
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    @Override
    public long count() throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(COUNT_USERS);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        return 0;
    }
    
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setRole(User.Role.valueOf(rs.getString("role")));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setActive(rs.getBoolean("is_active"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            user.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return user;
    }
}
