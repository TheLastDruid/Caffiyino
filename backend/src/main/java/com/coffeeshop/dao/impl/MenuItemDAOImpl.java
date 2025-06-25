package com.coffeeshop.dao.impl;

import com.coffeeshop.config.DatabaseConfig;
import com.coffeeshop.dao.MenuItemDAO;
import com.coffeeshop.model.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of MenuItemDAO interface
 */
public class MenuItemDAOImpl implements MenuItemDAO {
    private static final Logger logger = LoggerFactory.getLogger(MenuItemDAOImpl.class);
    
    private static final String INSERT_MENU_ITEM = 
        "INSERT INTO menu_items (name, category_id, description, price, is_available, preparation_time) VALUES (?, ?, ?, ?, ?, ?)";
    
    private static final String SELECT_MENU_ITEM_BY_ID = 
        "SELECT * FROM menu_items WHERE id = ?";
    
    private static final String SELECT_ALL_MENU_ITEMS = 
        "SELECT * FROM menu_items ORDER BY name";
    
    private static final String SELECT_MENU_ITEMS_WITH_CATEGORY = 
        "SELECT mi.*, c.name as category_name FROM menu_items mi " +
        "JOIN categories c ON mi.category_id = c.id ORDER BY mi.name";
    
    private static final String SELECT_MENU_ITEMS_BY_CATEGORY = 
        "SELECT * FROM menu_items WHERE category_id = ? ORDER BY name";
    
    private static final String SELECT_AVAILABLE_MENU_ITEMS = 
        "SELECT * FROM menu_items WHERE is_available = TRUE ORDER BY name";
    
    private static final String SELECT_MENU_ITEMS_BY_NAME = 
        "SELECT * FROM menu_items WHERE name LIKE ? ORDER BY name";
    
    private static final String UPDATE_MENU_ITEM = 
        "UPDATE menu_items SET name = ?, category_id = ?, description = ?, price = ?, " +
        "is_available = ?, preparation_time = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
    
    private static final String UPDATE_AVAILABILITY = 
        "UPDATE menu_items SET is_available = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
    
    private static final String DELETE_MENU_ITEM = 
        "DELETE FROM menu_items WHERE id = ?";
    
    private static final String COUNT_MENU_ITEMS = 
        "SELECT COUNT(*) FROM menu_items";
    
    private static final String EXISTS_MENU_ITEM = 
        "SELECT COUNT(*) FROM menu_items WHERE id = ?";
    
    @Override
    public MenuItem save(MenuItem menuItem) throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_MENU_ITEM, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, menuItem.getName());
            stmt.setLong(2, menuItem.getCategoryId());
            stmt.setString(3, menuItem.getDescription());
            stmt.setBigDecimal(4, menuItem.getPrice());
            stmt.setBoolean(5, menuItem.isAvailable());
            stmt.setInt(6, menuItem.getPreparationTime());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating menu item failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    menuItem.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating menu item failed, no ID obtained.");
                }
            }
            
            logger.info("Menu item created successfully: {}", menuItem.getName());
            return menuItem;
        }
    }
    
    @Override
    public Optional<MenuItem> findById(Long id) throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_MENU_ITEM_BY_ID)) {
            
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToMenuItem(rs));
                }
            }
        }
        return Optional.empty();
    }
    
    @Override
    public List<MenuItem> findAll() throws SQLException {
        List<MenuItem> menuItems = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_MENU_ITEMS);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                menuItems.add(mapResultSetToMenuItem(rs));
            }
        }
        return menuItems;
    }
    
    @Override
    public List<MenuItem> findAllWithCategory() throws SQLException {
        List<MenuItem> menuItems = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_MENU_ITEMS_WITH_CATEGORY);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                MenuItem item = mapResultSetToMenuItem(rs);
                item.setCategoryName(rs.getString("category_name"));
                menuItems.add(item);
            }
        }
        return menuItems;
    }
    
    @Override
    public List<MenuItem> findByCategory(Long categoryId) throws SQLException {
        List<MenuItem> menuItems = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_MENU_ITEMS_BY_CATEGORY)) {
            
            stmt.setLong(1, categoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    menuItems.add(mapResultSetToMenuItem(rs));
                }
            }
        }
        return menuItems;
    }
    
    @Override
    public List<MenuItem> findAvailableItems() throws SQLException {
        List<MenuItem> menuItems = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_AVAILABLE_MENU_ITEMS);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                menuItems.add(mapResultSetToMenuItem(rs));
            }
        }
        return menuItems;
    }
    
    @Override
    public List<MenuItem> findByNameContaining(String name) throws SQLException {
        List<MenuItem> menuItems = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_MENU_ITEMS_BY_NAME)) {
            
            stmt.setString(1, "%" + name + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    menuItems.add(mapResultSetToMenuItem(rs));
                }
            }
        }
        return menuItems;
    }
    
    @Override
    public void update(MenuItem menuItem) throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_MENU_ITEM)) {
            
            stmt.setString(1, menuItem.getName());
            stmt.setLong(2, menuItem.getCategoryId());
            stmt.setString(3, menuItem.getDescription());
            stmt.setBigDecimal(4, menuItem.getPrice());
            stmt.setBoolean(5, menuItem.isAvailable());
            stmt.setInt(6, menuItem.getPreparationTime());
            stmt.setLong(7, menuItem.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating menu item failed, no rows affected.");
            }
            
            logger.info("Menu item updated successfully: {}", menuItem.getName());
        }
    }
    
    @Override
    public void updateAvailability(Long itemId, boolean available) throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_AVAILABILITY)) {
            
            stmt.setBoolean(1, available);
            stmt.setLong(2, itemId);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating availability failed, no rows affected.");
            }
            
            logger.info("Menu item availability updated for ID: {} to {}", itemId, available);
        }
    }
    
    @Override
    public void deleteById(Long id) throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_MENU_ITEM)) {
            
            stmt.setLong(1, id);
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Deleting menu item failed, no rows affected.");
            }
            
            logger.info("Menu item deleted successfully with ID: {}", id);
        }
    }
    
    @Override
    public boolean existsById(Long id) throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(EXISTS_MENU_ITEM)) {
            
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
             PreparedStatement stmt = conn.prepareStatement(COUNT_MENU_ITEMS);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        return 0;
    }
    
    private MenuItem mapResultSetToMenuItem(ResultSet rs) throws SQLException {
        MenuItem item = new MenuItem();
        item.setId(rs.getLong("id"));
        item.setName(rs.getString("name"));
        item.setCategoryId(rs.getLong("category_id"));
        item.setDescription(rs.getString("description"));
        item.setPrice(rs.getBigDecimal("price"));
        item.setAvailable(rs.getBoolean("is_available"));
        item.setImagePath(rs.getString("image_path"));
        item.setPreparationTime(rs.getInt("preparation_time"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            item.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            item.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return item;
    }
}
