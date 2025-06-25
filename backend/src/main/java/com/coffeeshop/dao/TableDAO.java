package com.coffeeshop.dao;

import com.coffeeshop.model.Table;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * DAO interface for Table entity operations
 */
public interface TableDAO extends GenericDAO<Table, Long> {
    
    /**
     * Find table by table number
     * @param tableNumber Table number to search for
     * @return Optional containing table if found
     * @throws SQLException if database error occurs
     */
    Optional<Table> findByTableNumber(String tableNumber) throws SQLException;
    
    /**
     * Find all active tables
     * @return List of active tables
     * @throws SQLException if database error occurs
     */
    List<Table> findAllActive() throws SQLException;
    
    /**
     * Find tables by capacity
     * @param capacity Minimum capacity required
     * @return List of tables with at least the specified capacity
     * @throws SQLException if database error occurs
     */
    List<Table> findByCapacity(int capacity) throws SQLException;
    
    /**
     * Get all available tables (not occupied by active orders)
     * @return List of available tables
     * @throws SQLException if database error occurs
     */
    List<Table> findAvailableTables() throws SQLException;
    
    /**
     * Update table status (active/inactive)
     * @param id Table ID
     * @param isActive New status
     * @throws SQLException if database error occurs
     */
    void updateStatus(Long id, boolean isActive) throws SQLException;
}
