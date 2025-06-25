package com.coffeeshop.service;

import com.coffeeshop.dao.TableDAO;
import com.coffeeshop.dao.impl.TableDAOImpl;
import com.coffeeshop.model.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Service class for table-related business operations
 */
public class TableService {
    private static final Logger logger = LoggerFactory.getLogger(TableService.class);
    private final TableDAO tableDAO;
    
    public TableService() {
        this.tableDAO = new TableDAOImpl();
    }
    
    /**
     * Create a new table
     */
    public Table createTable(Table table) throws SQLException {
        try {
            // Validate table number uniqueness
            Optional<Table> existing = tableDAO.findByTableNumber(table.getTableNumber());
            if (existing.isPresent()) {
                throw new SQLException("Table number already exists: " + table.getTableNumber());
            }
            
            Table savedTable = tableDAO.save(table);
            logger.info("Created new table: {}", savedTable.getTableNumber());
            return savedTable;
            
        } catch (SQLException e) {
            logger.error("Error creating table", e);
            throw e;
        }
    }
    
    /**
     * Update an existing table
     */
    public Table updateTable(Table table) throws SQLException {
        try {
            // Check if table exists
            if (!tableDAO.existsById(table.getId())) {
                throw new SQLException("Table not found with ID: " + table.getId());
            }
            
            // Check if table number is unique (excluding current table)
            Optional<Table> existing = tableDAO.findByTableNumber(table.getTableNumber());
            if (existing.isPresent() && !existing.get().getId().equals(table.getId())) {
                throw new SQLException("Table number already exists: " + table.getTableNumber());
            }
            
            tableDAO.update(table);
            logger.info("Updated table: {}", table.getTableNumber());
            return table;
            
        } catch (SQLException e) {
            logger.error("Error updating table", e);
            throw e;
        }
    }
    
    /**
     * Get table by ID
     */
    public Optional<Table> getTableById(Long id) throws SQLException {
        try {
            return tableDAO.findById(id);
        } catch (SQLException e) {
            logger.error("Error finding table by ID: {}", id, e);
            throw e;
        }
    }
    
    /**
     * Get table by table number
     */
    public Optional<Table> getTableByNumber(String tableNumber) throws SQLException {
        try {
            return tableDAO.findByTableNumber(tableNumber);
        } catch (SQLException e) {
            logger.error("Error finding table by number: {}", tableNumber, e);
            throw e;
        }
    }
    
    /**
     * Get all tables
     */
    public List<Table> getAllTables() throws SQLException {
        try {
            return tableDAO.findAll();
        } catch (SQLException e) {
            logger.error("Error retrieving all tables", e);
            throw e;
        }
    }
    
    /**
     * Get all active tables
     */
    public List<Table> getActiveTables() throws SQLException {
        try {
            return tableDAO.findAllActive();
        } catch (SQLException e) {
            logger.error("Error retrieving active tables", e);
            throw e;
        }
    }
    
    /**
     * Get available tables (not occupied)
     */
    public List<Table> getAvailableTables() throws SQLException {
        try {
            return tableDAO.findAvailableTables();
        } catch (SQLException e) {
            logger.error("Error retrieving available tables", e);
            throw e;
        }
    }
    
    /**
     * Get tables by minimum capacity
     */
    public List<Table> getTablesByCapacity(int minCapacity) throws SQLException {
        try {
            return tableDAO.findByCapacity(minCapacity);
        } catch (SQLException e) {
            logger.error("Error finding tables by capacity: {}", minCapacity, e);
            throw e;
        }
    }
    
    /**
     * Activate table
     */
    public void activateTable(Long tableId) throws SQLException {
        try {
            tableDAO.updateStatus(tableId, true);
            logger.info("Activated table with ID: {}", tableId);
        } catch (SQLException e) {
            logger.error("Error activating table", e);
            throw e;
        }
    }
    
    /**
     * Deactivate table
     */
    public void deactivateTable(Long tableId) throws SQLException {
        try {
            tableDAO.updateStatus(tableId, false);
            logger.info("Deactivated table with ID: {}", tableId);
        } catch (SQLException e) {
            logger.error("Error deactivating table", e);
            throw e;
        }
    }
    
    /**
     * Delete table
     */
    public void deleteTable(Long tableId) throws SQLException {
        try {
            // Check if table exists
            if (!tableDAO.existsById(tableId)) {
                throw new SQLException("Table not found with ID: " + tableId);
            }
            
            tableDAO.deleteById(tableId);
            logger.info("Deleted table with ID: {}", tableId);
        } catch (SQLException e) {
            logger.error("Error deleting table", e);
            throw e;
        }
    }
    
    /**
     * Get total table count
     */
    public long getTableCount() throws SQLException {
        try {
            return tableDAO.count();
        } catch (SQLException e) {
            logger.error("Error counting tables", e);
            throw e;
        }
    }
    
    /**
     * Check if table number exists
     */
    public boolean tableNumberExists(String tableNumber) throws SQLException {
        try {
            return tableDAO.findByTableNumber(tableNumber).isPresent();
        } catch (SQLException e) {
            logger.error("Error checking table number existence: {}", tableNumber, e);
            throw e;
        }
    }
}
