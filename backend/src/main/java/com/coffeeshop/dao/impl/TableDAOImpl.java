package com.coffeeshop.dao.impl;

import com.coffeeshop.config.DatabaseConfig;
import com.coffeeshop.dao.TableDAO;
import com.coffeeshop.model.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of TableDAO for MySQL database operations
 */
public class TableDAOImpl implements TableDAO {
    private static final Logger logger = LoggerFactory.getLogger(TableDAOImpl.class);
    private final DatabaseConfig databaseConfig;
    
    public TableDAOImpl() {
        this.databaseConfig = DatabaseConfig.getInstance();
    }
    
    @Override
    public Table save(Table table) throws SQLException {
        if (table.getId() == null) {
            return create(table);
        } else {
            update(table);
            return table;
        }
    }
    
    private Table create(Table table) throws SQLException {
        String sql = "INSERT INTO tables (table_number, capacity, is_active, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            LocalDateTime now = LocalDateTime.now();
            table.setCreatedAt(now);
            table.setUpdatedAt(now);
            
            stmt.setString(1, table.getTableNumber());
            stmt.setInt(2, table.getCapacity());
            stmt.setBoolean(3, table.isActive());
            stmt.setTimestamp(4, Timestamp.valueOf(table.getCreatedAt()));
            stmt.setTimestamp(5, Timestamp.valueOf(table.getUpdatedAt()));
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating table failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    table.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating table failed, no ID obtained.");
                }
            }
            
            logger.info("Created new table: {}", table.getTableNumber());
            return table;
        }
    }
    
    @Override
    public void update(Table table) throws SQLException {
        String sql = "UPDATE tables SET table_number = ?, capacity = ?, is_active = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            table.setUpdatedAt(LocalDateTime.now());
            
            stmt.setString(1, table.getTableNumber());
            stmt.setInt(2, table.getCapacity());
            stmt.setBoolean(3, table.isActive());
            stmt.setTimestamp(4, Timestamp.valueOf(table.getUpdatedAt()));
            stmt.setLong(5, table.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating table failed, no rows affected.");
            }
            
            logger.info("Updated table: {}", table.getTableNumber());
        }
    }
    
    @Override
    public Optional<Table> findById(Long id) throws SQLException {
        String sql = "SELECT id, table_number, capacity, is_active, created_at, updated_at FROM tables WHERE id = ?";
        
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToTable(rs));
                }
            }
        }
        
        return Optional.empty();
    }
    
    @Override
    public Optional<Table> findByTableNumber(String tableNumber) throws SQLException {
        String sql = "SELECT id, table_number, capacity, is_active, created_at, updated_at FROM tables WHERE table_number = ?";
        
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, tableNumber);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToTable(rs));
                }
            }
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<Table> findAll() throws SQLException {
        String sql = "SELECT id, table_number, capacity, is_active, created_at, updated_at FROM tables ORDER BY table_number";
        List<Table> tables = new ArrayList<>();
        
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                tables.add(mapResultSetToTable(rs));
            }
        }
        
        return tables;
    }
    
    @Override
    public List<Table> findAllActive() throws SQLException {
        String sql = "SELECT id, table_number, capacity, is_active, created_at, updated_at FROM tables WHERE is_active = true ORDER BY table_number";
        List<Table> tables = new ArrayList<>();
        
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                tables.add(mapResultSetToTable(rs));
            }
        }
        
        return tables;
    }
    
    @Override
    public List<Table> findByCapacity(int capacity) throws SQLException {
        String sql = "SELECT id, table_number, capacity, is_active, created_at, updated_at FROM tables WHERE capacity >= ? AND is_active = true ORDER BY capacity, table_number";
        List<Table> tables = new ArrayList<>();
        
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, capacity);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tables.add(mapResultSetToTable(rs));
                }
            }
        }
        
        return tables;
    }
    
    @Override
    public List<Table> findAvailableTables() throws SQLException {
        String sql = "SELECT t.id, t.table_number, t.capacity, t.is_active, t.created_at, t.updated_at FROM tables t " +
                    "WHERE t.is_active = true " +
                    "AND t.id NOT IN (" +
                        "SELECT DISTINCT o.table_id " +
                        "FROM orders o " +
                        "WHERE o.table_id IS NOT NULL " +
                        "AND o.status IN ('NEW', 'IN_PROGRESS')" +
                    ") " +
                    "ORDER BY t.table_number";
        
        List<Table> tables = new ArrayList<>();
        
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                tables.add(mapResultSetToTable(rs));
            }
        }
        
        return tables;
    }
    
    @Override
    public void updateStatus(Long id, boolean isActive) throws SQLException {
        String sql = "UPDATE tables SET is_active = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBoolean(1, isActive);
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setLong(3, id);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating table status failed, no rows affected.");
            }
            
            logger.info("Updated table status for ID {}: {}", id, isActive);
        }
    }
    
    @Override
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM tables WHERE id = ?";
        
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting table failed, no rows affected.");
            }
            
            logger.info("Deleted table with ID: {}", id);
        }
    }
    
    @Override
    public boolean existsById(Long id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM tables WHERE id = ?";
        
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
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
        String sql = "SELECT COUNT(*) FROM tables";
        
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        
        return 0;
    }
    
    private Table mapResultSetToTable(ResultSet rs) throws SQLException {
        Table table = new Table();
        table.setId(rs.getLong("id"));
        table.setTableNumber(rs.getString("table_number"));
        table.setCapacity(rs.getInt("capacity"));
        table.setActive(rs.getBoolean("is_active"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            table.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            table.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return table;
    }
}
