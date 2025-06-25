package com.coffeeshop.service;

import com.coffeeshop.dao.OrderDAO;
import com.coffeeshop.dao.impl.OrderDAOImpl;
import com.coffeeshop.model.Order;
import com.coffeeshop.model.OrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class for order-related business operations
 */
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final OrderDAO orderDAO;
    
    public OrderService() {
        this.orderDAO = new OrderDAOImpl();
    }
    
    /**
     * Create a new order
     */
    public Order createOrder(Order order) throws SQLException {
        try {
            // Set order timestamp
            order.setCreatedAt(LocalDateTime.now());
            order.setUpdatedAt(LocalDateTime.now());
            
            // Generate order number if not set
            if (order.getOrderNumber() == null || order.getOrderNumber().isEmpty()) {
                String orderNumber = orderDAO.generateOrderNumber();
                order.setOrderNumber(orderNumber);
            }
            
            // Set default status if not set
            if (order.getStatus() == null) {
                order.setStatus(Order.Status.NEW);
            }
            
            // Calculate total amount
            calculateOrderTotal(order);
            
            Order savedOrder = orderDAO.save(order);
            logger.info("Created new order: {}", savedOrder.getOrderNumber());
            return savedOrder;
            
        } catch (SQLException e) {
            logger.error("Error creating order", e);
            throw e;
        }
    }
    
    /**
     * Update order status
     */
    public void updateOrderStatus(Long orderId, Order.Status status, Long changedBy) throws SQLException {
        try {
            orderDAO.updateStatus(orderId, status, changedBy);
            logger.info("Updated order {} status to {}", orderId, status);
        } catch (SQLException e) {
            logger.error("Error updating order status", e);
            throw e;
        }
    }
    
    /**
     * Get order by ID
     */
    public Optional<Order> getOrderById(Long id) throws SQLException {
        try {
            return orderDAO.findById(id);
        } catch (SQLException e) {
            logger.error("Error finding order by ID: {}", id, e);
            throw e;
        }
    }
    
    /**
     * Get order by order number
     */
    public Order getOrderByNumber(String orderNumber) throws SQLException {
        try {
            return orderDAO.findByOrderNumber(orderNumber);
        } catch (SQLException e) {
            logger.error("Error finding order by number: {}", orderNumber, e);
            throw e;
        }
    }
    
    /**
     * Get all orders
     */
    public List<Order> getAllOrders() throws SQLException {
        try {
            return orderDAO.findAll();
        } catch (SQLException e) {
            logger.error("Error retrieving all orders", e);
            throw e;
        }
    }
    
    /**
     * Get orders by status
     */
    public List<Order> getOrdersByStatus(Order.Status status) throws SQLException {
        try {
            return orderDAO.findByStatus(status);
        } catch (SQLException e) {
            logger.error("Error finding orders by status: {}", status, e);
            throw e;
        }
    }
    
    /**
     * Get orders by waiter
     */
    public List<Order> getOrdersByWaiter(Long waiterId) throws SQLException {
        try {
            return orderDAO.findByWaiter(waiterId);
        } catch (SQLException e) {
            logger.error("Error finding orders by waiter: {}", waiterId, e);
            throw e;
        }
    }
    
    /**
     * Get orders by table
     */
    public List<Order> getOrdersByTable(Long tableId) throws SQLException {
        try {
            return orderDAO.findByTable(tableId);
        } catch (SQLException e) {
            logger.error("Error finding orders by table: {}", tableId, e);
            throw e;
        }
    }
    
    /**
     * Get orders by date range
     */
    public List<Order> getOrdersByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        try {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
            return orderDAO.findByDateRange(startDateTime, endDateTime);
        } catch (SQLException e) {
            logger.error("Error finding orders by date range: {} to {}", startDate, endDate, e);
            throw e;
        }
    }
    
    /**
     * Get today's orders
     */
    public List<Order> getTodaysOrders() throws SQLException {
        LocalDate today = LocalDate.now();
        return getOrdersByDateRange(today, today);
    }
    
    /**
     * Get active orders (NEW, IN_PROGRESS)
     */
    public List<Order> getActiveOrders() throws SQLException {
        try {
            return orderDAO.findActiveOrders();
        } catch (SQLException e) {
            logger.error("Error finding active orders", e);
            throw e;
        }
    }
    
    /**
     * Get completed orders
     */
    public List<Order> getCompletedOrders() throws SQLException {
        try {
            return orderDAO.findCompletedOrders();
        } catch (SQLException e) {
            logger.error("Error finding completed orders", e);
            throw e;
        }
    }
    
    /**
     * Add item to order
     */
    public void addItemToOrder(Long orderId, OrderItem item) throws SQLException {
        try {
            Optional<Order> orderOpt = orderDAO.findById(orderId);
            if (orderOpt.isPresent()) {
                Order order = orderOpt.get();
                order.getOrderItems().add(item);
                item.setOrderId(orderId);
                
                // Recalculate total
                calculateOrderTotal(order);
                
                orderDAO.update(order);
                logger.info("Added item to order {}: {}", orderId, item.getMenuItemName());
            } else {
                throw new SQLException("Order not found with ID: " + orderId);
            }
        } catch (SQLException e) {
            logger.error("Error adding item to order", e);
            throw e;
        }
    }
    
    /**
     * Remove item from order
     */
    public void removeItemFromOrder(Long orderId, Long itemId) throws SQLException {
        try {
            Optional<Order> orderOpt = orderDAO.findById(orderId);
            if (orderOpt.isPresent()) {
                Order order = orderOpt.get();
                order.getOrderItems().removeIf(item -> item.getId().equals(itemId));
                
                // Recalculate total
                calculateOrderTotal(order);
                
                orderDAO.update(order);
                logger.info("Removed item from order {}: {}", orderId, itemId);
            } else {
                throw new SQLException("Order not found with ID: " + orderId);
            }
        } catch (SQLException e) {
            logger.error("Error removing item from order", e);
            throw e;
        }
    }
    
    /**
     * Calculate and set the total amount for an order
     */
    private void calculateOrderTotal(Order order) {
        if (order.getOrderItems() != null) {
            BigDecimal total = order.getOrderItems().stream()
                    .map(OrderItem::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            order.setTotalAmount(total);
        } else {
            order.setTotalAmount(BigDecimal.ZERO);
        }
    }
    
    /**
     * Delete order
     */
    public void deleteOrder(Long orderId) throws SQLException {
        try {
            orderDAO.deleteById(orderId);
            logger.info("Deleted order: {}", orderId);
        } catch (SQLException e) {
            logger.error("Error deleting order", e);
            throw e;
        }
    }
    
    /**
     * Search orders
     */
    public List<Order> searchOrders(String searchTerm) throws SQLException {
        try {
            return orderDAO.searchOrders(searchTerm);
        } catch (SQLException e) {
            logger.error("Error searching orders with term: {}", searchTerm, e);
            throw e;
        }
    }
}
