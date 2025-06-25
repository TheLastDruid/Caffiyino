package com.coffeeshop.service;

import com.coffeeshop.dao.MenuItemDAO;
import com.coffeeshop.dao.impl.MenuItemDAOImpl;
import com.coffeeshop.dao.impl.CategoryDAOImpl;
import com.coffeeshop.model.Category;
import com.coffeeshop.model.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * Service class for Menu operations
 */
public class MenuService {
    private static final Logger logger = LoggerFactory.getLogger(MenuService.class);
    private final MenuItemDAO menuItemDAO;
    private final CategoryDAOImpl categoryDAO;
    
    public MenuService() {
        this.menuItemDAO = new MenuItemDAOImpl();
        this.categoryDAO = new CategoryDAOImpl();
    }
    
    /**
     * Get all menu items with category information
     */
    public List<MenuItem> getAllMenuItems() throws SQLException {
        return menuItemDAO.findAllWithCategory();
    }
    
    /**
     * Get available menu items
     */
    public List<MenuItem> getAvailableMenuItems() throws SQLException {
        return menuItemDAO.findAvailableItems();
    }
    
    /**
     * Get menu items by category
     */
    public List<MenuItem> getMenuItemsByCategory(Long categoryId) throws SQLException {
        return menuItemDAO.findByCategory(categoryId);
    }
    
    /**
     * Get menu item by ID
     */
    public MenuItem getMenuItemById(Long id) throws SQLException {
        return menuItemDAO.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Menu item not found with ID: " + id));
    }
    
    /**
     * Create new menu item
     */
    public MenuItem createMenuItem(MenuItem menuItem) throws SQLException {
        validateMenuItem(menuItem);
        MenuItem created = menuItemDAO.save(menuItem);
        logger.info("Menu item created: {}", created.getName());
        return created;
    }
    
    /**
     * Update existing menu item
     */
    public void updateMenuItem(MenuItem menuItem) throws SQLException {
        validateMenuItem(menuItem);
        menuItemDAO.update(menuItem);
        logger.info("Menu item updated: {}", menuItem.getName());
    }
    
    /**
     * Delete menu item
     */
    public void deleteMenuItem(Long id) throws SQLException {
        menuItemDAO.deleteById(id);
        logger.info("Menu item deleted with ID: {}", id);
    }
    
    /**
     * Update menu item availability
     */
    public void updateMenuItemAvailability(Long id, boolean available) throws SQLException {
        menuItemDAO.updateAvailability(id, available);
        logger.info("Menu item availability updated for ID: {} to {}", id, available);
    }
    
    /**
     * Search menu items by name
     */
    public List<MenuItem> searchMenuItems(String name) throws SQLException {
        return menuItemDAO.findByNameContaining(name);
    }
    
    /**
     * Get all categories
     */
    public List<Category> getAllCategories() throws SQLException {
        return categoryDAO.findAll();
    }
    
    /**
     * Get active categories
     */
    public List<Category> getActiveCategories() throws SQLException {
        return categoryDAO.findActiveCategories();
    }
    
    /**
     * Create new category
     */
    public Category createCategory(Category category) throws SQLException {
        validateCategory(category);
        Category created = categoryDAO.save(category);
        logger.info("Category created: {}", created.getName());
        return created;
    }
    
    /**
     * Update existing category
     */
    public void updateCategory(Category category) throws SQLException {
        validateCategory(category);
        categoryDAO.update(category);
        logger.info("Category updated: {}", category.getName());
    }
    
    /**
     * Delete category
     */
    public void deleteCategory(Long id) throws SQLException {
        // Check if category has menu items
        List<MenuItem> items = menuItemDAO.findByCategory(id);
        if (!items.isEmpty()) {
            throw new IllegalArgumentException("Cannot delete category with existing menu items");
        }
        
        categoryDAO.deleteById(id);
        logger.info("Category deleted with ID: {}", id);
    }
    
    private void validateMenuItem(MenuItem menuItem) {
        if (menuItem.getName() == null || menuItem.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Menu item name is required");
        }
        
        if (menuItem.getCategoryId() == null) {
            throw new IllegalArgumentException("Category is required");
        }
        
        if (menuItem.getPrice() == null || menuItem.getPrice().doubleValue() <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        
        if (menuItem.getPreparationTime() < 0) {
            throw new IllegalArgumentException("Preparation time cannot be negative");
        }
    }
    
    private void validateCategory(Category category) {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category name is required");
        }
    }
}
