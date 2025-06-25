package com.coffeeshop.dao;

import com.coffeeshop.model.Category;

import java.sql.SQLException;
import java.util.List;

/**
 * DAO interface for Category entity
 */
public interface CategoryDAO extends GenericDAO<Category, Long> {
    
    /**
     * Find active categories
     * @return List of active categories
     * @throws SQLException if database error occurs
     */
    List<Category> findActiveCategories() throws SQLException;
    
    /**
     * Find category by name
     * @param name Category name
     * @return Category if found
     * @throws SQLException if database error occurs
     */
    Category findByName(String name) throws SQLException;
    
    /**
     * Update category active status
     * @param categoryId Category ID
     * @param active Active status
     * @throws SQLException if database error occurs
     */
    void updateActiveStatus(Long categoryId, boolean active) throws SQLException;
}
