package com.coffeeshop.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Order entity representing customer orders
 */
public class Order {
    public enum Status {
        NEW, IN_PROGRESS, READY, COMPLETED, CANCELLED
    }
    
    private Long id;
    private String orderNumber;
    private Long tableId;
    private String tableName; // For display purposes
    private String customerName;
    private Long waiterId;
    private String waiterName; // For display purposes
    private Status status;
    private BigDecimal totalAmount;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItem> orderItems;
    
    // Constructors
    public Order() {
        this.orderItems = new ArrayList<>();
        this.totalAmount = BigDecimal.ZERO;
        this.status = Status.NEW;
    }
    
    public Order(String orderNumber, Long tableId, String customerName, Long waiterId) {
        this();
        this.orderNumber = orderNumber;
        this.tableId = tableId;
        this.customerName = customerName;
        this.waiterId = waiterId;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    
    public Long getTableId() { return tableId; }
    public void setTableId(Long tableId) { this.tableId = tableId; }
    
    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public Long getWaiterId() { return waiterId; }
    public void setWaiterId(Long waiterId) { this.waiterId = waiterId; }
    
    public String getWaiterName() { return waiterName; }
    public void setWaiterName(String waiterName) { this.waiterName = waiterName; }
    
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public List<OrderItem> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }
    
    // Utility methods
    public void addOrderItem(OrderItem item) {
        this.orderItems.add(item);
        calculateTotal();
    }
    
    public void removeOrderItem(OrderItem item) {
        this.orderItems.remove(item);
        calculateTotal();
    }
    
    public void calculateTotal() {
        this.totalAmount = orderItems.stream()
            .map(OrderItem::getTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public int getTotalItems() {
        return orderItems.stream()
            .mapToInt(OrderItem::getQuantity)
            .sum();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(orderNumber, order.orderNumber);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, orderNumber);
    }
    
    @Override
    public String toString() {
        return orderNumber + " - " + customerName + " ($" + totalAmount + ")";
    }
}
