package com.coffeeshop.dao;

import com.coffeeshop.model.Order;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DAO interface for Order entity
 */
public interface OrderDAO extends GenericDAO<Order, Long> {
    
    /**
     * Find orders by status
     * @param status Order status
     * @return List of orders with specified status
     * @throws SQLException if database error occurs
     */
    List<Order> findByStatus(Order.Status status) throws SQLException;
    
    /**
     * Find orders by waiter
     * @param waiterId Waiter ID
     * @return List of orders for specified waiter
     * @throws SQLException if database error occurs
     */
    List<Order> findByWaiter(Long waiterId) throws SQLException;
    
    /**
     * Find orders by table
     * @param tableId Table ID
     * @return List of orders for specified table
     * @throws SQLException if database error occurs
     */
    List<Order> findByTable(Long tableId) throws SQLException;
    
    /**
     * Find orders created between dates
     * @param startDate Start date
     * @param endDate End date
     * @return List of orders in date range
     * @throws SQLException if database error occurs
     */
    List<Order> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws SQLException;
    
    /**
     * Find today's orders
     * @return List of today's orders
     * @throws SQLException if database error occurs
     */
    List<Order> findTodaysOrders() throws SQLException;
    
    /**
     * Find orders with all related data (table, waiter, items)
     * @return List of complete orders
     * @throws SQLException if database error occurs
     */
    List<Order> findAllWithDetails() throws SQLException;
    
    /**
     * Find order by order number
     * @param orderNumber Order number
     * @return Order if found
     * @throws SQLException if database error occurs
     */
    Order findByOrderNumber(String orderNumber) throws SQLException;
    
    /**
     * Update order status
     * @param orderId Order ID
     * @param status New status
     * @param changedBy User who changed status
     * @throws SQLException if database error occurs
     */
    void updateStatus(Long orderId, Order.Status status, Long changedBy) throws SQLException;
    
    /**
     * Generate next order number
     * @return Next order number
     * @throws SQLException if database error occurs
     */
    String generateOrderNumber() throws SQLException;
    
    /**
     * Find active orders (NEW, IN_PROGRESS)
     * @return List of active orders
     * @throws SQLException if database error occurs
     */
    List<Order> findActiveOrders() throws SQLException;
    
    /**
     * Find completed orders (READY, COMPLETED)
     * @return List of completed orders
     * @throws SQLException if database error occurs
     */
    List<Order> findCompletedOrders() throws SQLException;
    
    /**
     * Search orders by various criteria
     * @param searchTerm Search term
     * @return List of matching orders
     * @throws SQLException if database error occurs
     */
    List<Order> searchOrders(String searchTerm) throws SQLException;
}
