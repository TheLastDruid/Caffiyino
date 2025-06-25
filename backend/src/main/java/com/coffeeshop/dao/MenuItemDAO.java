package com.coffeeshop.dao;

import com.coffeeshop.model.MenuItem;

import java.sql.SQLException;
import java.util.List;

/**
 * DAO interface for MenuItem entity
 */
public interface MenuItemDAO extends GenericDAO<MenuItem, Long> {
    
    /**
     * Find menu items by category
     * @param categoryId Category ID
     * @return List of menu items in category
     * @throws SQLException if database error occurs
     */
    List<MenuItem> findByCategory(Long categoryId) throws SQLException;
    
    /**
     * Find available menu items
     * @return List of available menu items
     * @throws SQLException if database error occurs
     */
    List<MenuItem> findAvailableItems() throws SQLException;
    
    /**
     * Find menu items by name (partial match)
     * @param name Name to search for
     * @return List of matching menu items
     * @throws SQLException if database error occurs
     */
    List<MenuItem> findByNameContaining(String name) throws SQLException;
    
    /**
     * Update availability status of menu item
     * @param itemId Menu item ID
     * @param available Availability status
     * @throws SQLException if database error occurs
     */
    void updateAvailability(Long itemId, boolean available) throws SQLException;
    
    /**
     * Find menu items with category information
     * @return List of menu items with category names
     * @throws SQLException if database error occurs
     */
    List<MenuItem> findAllWithCategory() throws SQLException;
}
