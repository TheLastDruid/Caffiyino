package com.coffeeshop.dao.impl;

import com.coffeeshop.config.DatabaseConfig;
import com.coffeeshop.dao.OrderDAO;
import com.coffeeshop.model.Order;
import com.coffeeshop.model.OrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of OrderDAO for database operations
 */
public class OrderDAOImpl implements OrderDAO {
    private static final Logger logger = LoggerFactory.getLogger(OrderDAOImpl.class);
    
    private static final String INSERT_ORDER = 
        "INSERT INTO orders (order_number, table_id, customer_name, waiter_id, status, total_amount, notes, created_at, updated_at) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String INSERT_ORDER_ITEM = 
        "INSERT INTO order_items (order_id, menu_item_id, quantity, unit_price, total_price, special_instructions) " +
        "VALUES (?, ?, ?, ?, ?, ?)";
    
    private static final String SELECT_ORDER_BY_ID = 
        "SELECT o.*, u.full_name as waiter_name, t.table_number as table_name " +
        "FROM orders o " +
        "LEFT JOIN users u ON o.waiter_id = u.id " +
        "LEFT JOIN tables t ON o.table_id = t.id " +
        "WHERE o.id = ?";
    
    private static final String SELECT_ORDERS_BY_STATUS = 
        "SELECT o.*, u.full_name as waiter_name, t.table_number as table_name " +
        "FROM orders o " +
        "LEFT JOIN users u ON o.waiter_id = u.id " +
        "LEFT JOIN tables t ON o.table_id = t.id " +
        "WHERE o.status = ? ORDER BY o.created_at ASC";
    
    private static final String SELECT_ORDERS_BY_WAITER = 
        "SELECT o.*, u.full_name as waiter_name, t.table_number as table_name " +
        "FROM orders o " +
        "LEFT JOIN users u ON o.waiter_id = u.id " +
        "LEFT JOIN tables t ON o.table_id = t.id " +
        "WHERE o.waiter_id = ? ORDER BY o.created_at DESC";
    
    private static final String SELECT_ORDERS_BY_TABLE = 
        "SELECT o.*, u.full_name as waiter_name, t.table_number as table_name " +
        "FROM orders o " +
        "LEFT JOIN users u ON o.waiter_id = u.id " +
        "LEFT JOIN tables t ON o.table_id = t.id " +
        "WHERE o.table_id = ? ORDER BY o.created_at DESC";
    
    private static final String SELECT_ALL_ORDERS = 
        "SELECT o.*, u.full_name as waiter_name, t.table_number as table_name " +
        "FROM orders o " +
        "LEFT JOIN users u ON o.waiter_id = u.id " +
        "LEFT JOIN tables t ON o.table_id = t.id " +
        "ORDER BY o.created_at DESC";
    
    private static final String SELECT_ORDER_ITEMS = 
        "SELECT oi.*, mi.name as menu_item_name " +
        "FROM order_items oi " +
        "JOIN menu_items mi ON oi.menu_item_id = mi.id " +
        "WHERE oi.order_id = ?";
    
    private static final String UPDATE_ORDER_STATUS = 
        "UPDATE orders SET status = ?, updated_at = ? WHERE id = ?";
    
    private static final String UPDATE_ORDER = 
        "UPDATE orders SET table_id = ?, customer_name = ?, status = ?, total_amount = ?, notes = ?, updated_at = ? WHERE id = ?";
    
    private static final String DELETE_ORDER = "DELETE FROM orders WHERE id = ?";
    private static final String DELETE_ORDER_ITEMS = "DELETE FROM order_items WHERE order_id = ?";
    private static final String COUNT_ORDERS = "SELECT COUNT(*) FROM orders";
    private static final String EXISTS_ORDER = "SELECT 1 FROM orders WHERE id = ? LIMIT 1";
    private static final String SELECT_ORDER_BY_NUMBER = "SELECT o.*, u.full_name as waiter_name, t.table_number as table_name FROM orders o LEFT JOIN users u ON o.waiter_id = u.id LEFT JOIN tables t ON o.table_id = t.id WHERE o.order_number = ?";
    private static final String SELECT_TODAYS_ORDERS = "SELECT o.*, u.full_name as waiter_name, t.table_number as table_name FROM orders o LEFT JOIN users u ON o.waiter_id = u.id LEFT JOIN tables t ON o.table_id = t.id WHERE DATE(o.created_at) = CURDATE() ORDER BY o.created_at DESC";
    private static final String SELECT_ORDERS_BY_DATE_RANGE = "SELECT o.*, u.full_name as waiter_name, t.table_number as table_name FROM orders o LEFT JOIN users u ON o.waiter_id = u.id LEFT JOIN tables t ON o.table_id = t.id WHERE o.created_at BETWEEN ? AND ? ORDER BY o.created_at DESC";
    
    @Override
    public Order save(Order order) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConfig.getInstance().getConnection();
            conn.setAutoCommit(false);
            
            // Generate order number if not set
            if (order.getOrderNumber() == null || order.getOrderNumber().isEmpty()) {
                order.setOrderNumber(generateOrderNumber());
            }
            
            // Insert order
            stmt = conn.prepareStatement(INSERT_ORDER, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, order.getOrderNumber());
            stmt.setLong(2, order.getTableId());
            stmt.setString(3, order.getCustomerName());
            stmt.setLong(4, order.getWaiterId());
            stmt.setString(5, order.getStatus().name());
            stmt.setBigDecimal(6, order.getTotalAmount());
            stmt.setString(7, order.getNotes());
            stmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }
            
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                Long orderId = rs.getLong(1);
                order.setId(orderId);
                
                // Insert order items
                saveOrderItems(conn, orderId, order.getOrderItems());
                
                conn.commit();
                logger.info("Order saved successfully with ID: {}", orderId);
                return order;
            } else {
                throw new SQLException("Creating order failed, no ID obtained.");
            }
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    logger.error("Error rolling back transaction", ex);
                }
            }
            logger.error("Error saving order", e);
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }
    
    private void saveOrderItems(Connection conn, Long orderId, List<OrderItem> orderItems) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(INSERT_ORDER_ITEM);
            for (OrderItem item : orderItems) {
                stmt.setLong(1, orderId);
                stmt.setLong(2, item.getMenuItemId());
                stmt.setInt(3, item.getQuantity());
                stmt.setBigDecimal(4, item.getUnitPrice());
                stmt.setBigDecimal(5, item.getTotalPrice());
                stmt.setString(6, item.getSpecialInstructions());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } finally {
            if (stmt != null) stmt.close();
        }
    }
    
    @Override
    public Optional<Order> findById(Long id) throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ORDER_BY_ID)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                order.setOrderItems(getOrderItems(conn, id));
                return Optional.of(order);
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error finding order by ID: {}", id, e);
            throw e;
        }
    }
    
    @Override
    public List<Order> findByStatus(Order.Status status) throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ORDERS_BY_STATUS)) {
            
            stmt.setString(1, status.name());
            ResultSet rs = stmt.executeQuery();
            
            List<Order> orders = new ArrayList<>();
            while (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                order.setOrderItems(getOrderItems(conn, order.getId()));
                orders.add(order);
            }
            
            return orders;
            
        } catch (SQLException e) {
            logger.error("Error finding orders by status: {}", status, e);
            throw e;
        }
    }
    
    @Override
    public List<Order> findByWaiter(Long waiterId) throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ORDERS_BY_WAITER)) {
            
            stmt.setLong(1, waiterId);
            ResultSet rs = stmt.executeQuery();
            
            List<Order> orders = new ArrayList<>();
            while (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                order.setOrderItems(getOrderItems(conn, order.getId()));
                orders.add(order);
            }
            
            return orders;
            
        } catch (SQLException e) {
            logger.error("Error finding orders by waiter ID: {}", waiterId, e);
            throw e;
        }
    }
    
    @Override
    public List<Order> findByTable(Long tableId) throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ORDERS_BY_TABLE)) {
            
            stmt.setLong(1, tableId);
            ResultSet rs = stmt.executeQuery();
            
            List<Order> orders = new ArrayList<>();
            while (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                order.setOrderItems(getOrderItems(conn, order.getId()));
                orders.add(order);
            }
            
            return orders;
            
        } catch (SQLException e) {
            logger.error("Error finding orders by table ID: {}", tableId, e);
            throw e;
        }
    }
    
    @Override
    public List<Order> findAll() throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_ORDERS)) {
            
            ResultSet rs = stmt.executeQuery();
            
            List<Order> orders = new ArrayList<>();
            while (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                order.setOrderItems(getOrderItems(conn, order.getId()));
                orders.add(order);
            }
            
            return orders;
            
        } catch (SQLException e) {
            logger.error("Error finding all orders", e);
            throw e;
        }
    }
    
    @Override
    public List<Order> findAllWithDetails() throws SQLException {
        return findAll(); // Already includes details
    }
    
    @Override
    public List<Order> findTodaysOrders() throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_TODAYS_ORDERS)) {
            
            ResultSet rs = stmt.executeQuery();
            
            List<Order> orders = new ArrayList<>();
            while (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                order.setOrderItems(getOrderItems(conn, order.getId()));
                orders.add(order);
            }
            
            return orders;
            
        } catch (SQLException e) {
            logger.error("Error finding today's orders", e);
            throw e;
        }
    }
    
    @Override
    public List<Order> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ORDERS_BY_DATE_RANGE)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(startDate));
            stmt.setTimestamp(2, Timestamp.valueOf(endDate));
            ResultSet rs = stmt.executeQuery();
            
            List<Order> orders = new ArrayList<>();
            while (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                order.setOrderItems(getOrderItems(conn, order.getId()));
                orders.add(order);
            }
            
            return orders;
            
        } catch (SQLException e) {
            logger.error("Error finding orders by date range", e);
            throw e;
        }
    }
    
    @Override
    public Order findByOrderNumber(String orderNumber) throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ORDER_BY_NUMBER)) {
            
            stmt.setString(1, orderNumber);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                order.setOrderItems(getOrderItems(conn, order.getId()));
                return order;
            }
            
            return null;
            
        } catch (SQLException e) {
            logger.error("Error finding order by number: {}", orderNumber, e);
            throw e;
        }
    }
    
    @Override
    public void updateStatus(Long orderId, Order.Status status, Long changedBy) throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_ORDER_STATUS)) {
            
            stmt.setString(1, status.name());
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setLong(3, orderId);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Order status updated successfully for ID: {} by user: {}", orderId, changedBy);
            } else {
                logger.warn("No order found with ID: {}", orderId);
            }
            
        } catch (SQLException e) {
            logger.error("Error updating order status for ID: {}", orderId, e);
            throw e;
        }
    }
    
    // Overloaded method for backward compatibility
    public void updateStatus(Long orderId, Order.Status status) throws SQLException {
        updateStatus(orderId, status, null);
    }
    
    @Override
    public void update(Order order) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConfig.getInstance().getConnection();
            conn.setAutoCommit(false);
            
            // Update order
            PreparedStatement stmt = conn.prepareStatement(UPDATE_ORDER);
            stmt.setLong(1, order.getTableId());
            stmt.setString(2, order.getCustomerName());
            stmt.setString(3, order.getStatus().name());
            stmt.setBigDecimal(4, order.getTotalAmount());
            stmt.setString(5, order.getNotes());
            stmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setLong(7, order.getId());
            
            stmt.executeUpdate();
            stmt.close();
            
            // Delete existing order items and insert new ones
            stmt = conn.prepareStatement(DELETE_ORDER_ITEMS);
            stmt.setLong(1, order.getId());
            stmt.executeUpdate();
            stmt.close();
            
            saveOrderItems(conn, order.getId(), order.getOrderItems());
            
            conn.commit();
            logger.info("Order updated successfully: {}", order.getId());
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    logger.error("Error rolling back transaction", ex);
                }
            }
            logger.error("Error updating order", e);
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error("Error closing connection", e);
                }
            }
        }
    }
    
    @Override
    public void deleteById(Long id) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConfig.getInstance().getConnection();
            conn.setAutoCommit(false);
            
            // Delete order items first
            PreparedStatement stmt = conn.prepareStatement(DELETE_ORDER_ITEMS);
            stmt.setLong(1, id);
            stmt.executeUpdate();
            stmt.close();
            
            // Delete order
            stmt = conn.prepareStatement(DELETE_ORDER);
            stmt.setLong(1, id);
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                conn.commit();
                logger.info("Order deleted successfully: {}", id);
            } else {
                conn.rollback();
                logger.warn("No order found with ID: {}", id);
            }
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    logger.error("Error rolling back transaction", ex);
                }
            }
            logger.error("Error deleting order", e);
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error("Error closing connection", e);
                }
            }
        }
    }
    
    @Override
    public boolean existsById(Long id) throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(EXISTS_ORDER)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
            
        } catch (SQLException e) {
            logger.error("Error checking if order exists: {}", id, e);
            throw e;
        }
    }
    
    @Override
    public long count() throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(COUNT_ORDERS)) {
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0;
            
        } catch (SQLException e) {
            logger.error("Error counting orders", e);
            throw e;
        }
    }
    
    @Override
    public String generateOrderNumber() throws SQLException {
        // Simple order number generation - can be enhanced with business rules
        String timestamp = String.valueOf(System.currentTimeMillis());
        return "ORD-" + timestamp.substring(timestamp.length() - 8);
    }
    
    private List<OrderItem> getOrderItems(Connection conn, Long orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_ORDER_ITEMS)) {
            stmt.setLong(1, orderId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                OrderItem item = new OrderItem();
                item.setId(rs.getLong("id"));
                item.setOrderId(orderId);
                item.setMenuItemId(rs.getLong("menu_item_id"));
                item.setMenuItemName(rs.getString("menu_item_name"));
                item.setQuantity(rs.getInt("quantity"));
                item.setUnitPrice(rs.getBigDecimal("unit_price"));
                item.setTotalPrice(rs.getBigDecimal("total_price"));
                item.setSpecialInstructions(rs.getString("special_instructions"));
                items.add(item);
            }
        }
        
        return items;
    }
    
    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setOrderNumber(rs.getString("order_number"));
        order.setTableId(rs.getLong("table_id"));
        order.setTableName(rs.getString("table_name"));
        order.setCustomerName(rs.getString("customer_name"));
        order.setWaiterId(rs.getLong("waiter_id"));
        order.setWaiterName(rs.getString("waiter_name"));
        order.setStatus(Order.Status.valueOf(rs.getString("status")));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setNotes(rs.getString("notes"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            order.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            order.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return order;
    }
    
    @Override
    public List<Order> findActiveOrders() throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT o.id, o.order_number, o.table_id, t.table_number as table_name, " +
                 "o.customer_name, o.waiter_id, u.username as waiter_name, o.status, " +
                 "o.total_amount, o.notes, o.created_at, o.updated_at " +
                 "FROM orders o " +
                 "LEFT JOIN tables t ON o.table_id = t.id " +
                 "LEFT JOIN users u ON o.waiter_id = u.id " +
                 "WHERE o.status IN ('NEW', 'IN_PROGRESS') " +
                 "ORDER BY o.created_at ASC")) {
            
            List<Order> orders = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSetToOrder(rs));
                }
            }
            return orders;
        } catch (SQLException e) {
            logger.error("Error finding active orders", e);
            throw e;
        }
    }
    
    @Override
    public List<Order> findCompletedOrders() throws SQLException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT o.id, o.order_number, o.table_id, t.table_number as table_name, " +
                 "o.customer_name, o.waiter_id, u.username as waiter_name, o.status, " +
                 "o.total_amount, o.notes, o.created_at, o.updated_at " +
                 "FROM orders o " +
                 "LEFT JOIN tables t ON o.table_id = t.id " +
                 "LEFT JOIN users u ON o.waiter_id = u.id " +
                 "WHERE o.status IN ('READY', 'COMPLETED') " +
                 "ORDER BY o.updated_at DESC")) {
            
            List<Order> orders = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSetToOrder(rs));
                }
            }
            return orders;
        } catch (SQLException e) {
            logger.error("Error finding completed orders", e);
            throw e;
        }
    }
    
    @Override
    public List<Order> searchOrders(String searchTerm) throws SQLException {
        String sql = "SELECT o.id, o.order_number, o.table_id, t.table_number as table_name, " +
                    "o.customer_name, o.waiter_id, u.username as waiter_name, o.status, " +
                    "o.total_amount, o.notes, o.created_at, o.updated_at " +
                    "FROM orders o " +
                    "LEFT JOIN tables t ON o.table_id = t.id " +
                    "LEFT JOIN users u ON o.waiter_id = u.id " +
                    "WHERE o.order_number LIKE ? OR o.customer_name LIKE ? OR t.table_number LIKE ? " +
                    "ORDER BY o.created_at DESC";
        
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            
            List<Order> orders = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSetToOrder(rs));
                }
            }
            return orders;
        } catch (SQLException e) {
            logger.error("Error searching orders with term: {}", searchTerm, e);
            throw e;
        }
    }
}
