package com.coffeeshop.dao.impl;

import com.coffeeshop.config.DatabaseConfig;
import com.coffeeshop.dao.CategoryDAO;
import com.coffeeshop.model.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of CategoryDAO interface
 */
public class CategoryDAOImpl implements CategoryDAO {
    private static final Logger logger = LoggerFactory.getLogger(CategoryDAOImpl.class);
    
    private static final String INSERT_CATEGORY = 
        "INSERT INTO categories (name, description, is_active) VALUES (?, ?, ?)";
    
    private static final String SELECT_CATEGORY_BY_ID = 
        "SELECT * FROM categories WHERE id = ?";
    
    private static final String SELECT_ALL_CATEGORIES = 
        "SELECT * FROM categories ORDER BY name";
    
    private static final String SELECT_ACTIVE_CATEGORIES = 
        "SELECT * FROM categories WHERE is_active = TRUE ORDER BY name";
    
    private static final String SELECT_CATEGORY_BY_NAME = 
        "SELECT * FROM categories WHERE name = ?";
    
    private static final String UPDATE_CATEGORY = 
        "UPDATE categories SET name = ?, description = ?, is_active = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
    
    private static final String UPDATE_ACTIVE_STATUS = 
        "UPDATE categories SET is_active = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
    
    private static final String DELETE_CATEGORY = 
        "DELETE FROM categories WHERE id = ?";
    
    private static final String COUNT_CATEGORIES = 
        "SELECT COUNT(*) FROM categories";
    
    private static final String EXISTS_CATEGORY = 
        "SELECT COUNT(*) FROM categories WHERE id = ?";
    
    @Override
    public Category save(Category category) throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_CATEGORY, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setBoolean(3, category.isActive());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating category failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    category.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating category failed, no ID obtained.");
                }
            }
            
            logger.info("Category created successfully: {}", category.getName());
            return category;
        }
    }
    
    @Override
    public Optional<Category> findById(Long id) throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_CATEGORY_BY_ID)) {
            
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCategory(rs));
                }
            }
        }
        return Optional.empty();
    }
    
    @Override
    public List<Category> findAll() throws SQLException {
        List<Category> categories = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_CATEGORIES);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                categories.add(mapResultSetToCategory(rs));
            }
        }
        return categories;
    }
    
    @Override
    public List<Category> findActiveCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ACTIVE_CATEGORIES);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                categories.add(mapResultSetToCategory(rs));
            }
        }
        return categories;
    }
    
    @Override
    public Category findByName(String name) throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_CATEGORY_BY_NAME)) {
            
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCategory(rs);
                }
            }
        }
        return null;
    }
    
    @Override
    public void update(Category category) throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_CATEGORY)) {
            
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setBoolean(3, category.isActive());
            stmt.setLong(4, category.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating category failed, no rows affected.");
            }
            
            logger.info("Category updated successfully: {}", category.getName());
        }
    }
    
    @Override
    public void updateActiveStatus(Long categoryId, boolean active) throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_ACTIVE_STATUS)) {
            
            stmt.setBoolean(1, active);
            stmt.setLong(2, categoryId);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating category active status failed, no rows affected.");
            }
            
            logger.info("Category active status updated for ID: {} to {}", categoryId, active);
        }
    }
    
    @Override
    public void deleteById(Long id) throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_CATEGORY)) {
            
            stmt.setLong(1, id);
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Deleting category failed, no rows affected.");
            }
            
            logger.info("Category deleted successfully with ID: {}", id);
        }
    }
    
    @Override
    public boolean existsById(Long id) throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(EXISTS_CATEGORY)) {
            
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
             PreparedStatement stmt = conn.prepareStatement(COUNT_CATEGORIES);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        return 0;
    }
    
    private Category mapResultSetToCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setId(rs.getLong("id"));
        category.setName(rs.getString("name"));
        category.setDescription(rs.getString("description"));
        category.setActive(rs.getBoolean("is_active"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            category.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            category.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return category;
    }
}
