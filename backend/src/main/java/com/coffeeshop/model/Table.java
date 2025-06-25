package com.coffeeshop.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Table entity representing restaurant tables
 */
public class Table {
    private Long id;
    private String tableNumber;
    private int capacity;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public Table() {}
    
    public Table(String tableNumber, int capacity) {
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.isActive = true;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTableNumber() { return tableNumber; }
    public void setTableNumber(String tableNumber) { this.tableNumber = tableNumber; }
    
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return Objects.equals(id, table.id) && Objects.equals(tableNumber, table.tableNumber);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, tableNumber);
    }
    
    @Override
    public String toString() {
        return tableNumber + " (" + capacity + " seats)";
    }
}
