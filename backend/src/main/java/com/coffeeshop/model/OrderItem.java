package com.coffeeshop.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * OrderItem entity representing individual items within an order
 */
public class OrderItem {
    private Long id;
    private Long orderId;
    private Long menuItemId;
    private String menuItemName; // For display purposes
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private String specialInstructions;
    private LocalDateTime createdAt;
    
    // Constructors
    public OrderItem() {}
    
    public OrderItem(Long orderId, Long menuItemId, int quantity, BigDecimal unitPrice) {
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    
    public Long getMenuItemId() { return menuItemId; }
    public void setMenuItemId(Long menuItemId) { this.menuItemId = menuItemId; }
    
    public String getMenuItemName() { return menuItemName; }
    public void setMenuItemName(String menuItemName) { this.menuItemName = menuItemName; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { 
        this.quantity = quantity;
        calculateTotalPrice();
    }
    
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { 
        this.unitPrice = unitPrice;
        calculateTotalPrice();
    }
    
    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    
    public String getSpecialInstructions() { return specialInstructions; }
    public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    // Utility methods
    private void calculateTotalPrice() {
        if (unitPrice != null && quantity > 0) {
            this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id) && 
               Objects.equals(orderId, orderItem.orderId) && 
               Objects.equals(menuItemId, orderItem.menuItemId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, orderId, menuItemId);
    }
    
    @Override
    public String toString() {
        return quantity + "x " + menuItemName + " ($" + totalPrice + ")";
    }
}
