package com.coffeeshop.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * MenuItem entity representing items available on the menu
 */
public class MenuItem {
    private Long id;
    private String name;
    private Long categoryId;
    private String categoryName; // For display purposes
    private String description;
    private BigDecimal price;
    private boolean isAvailable;
    private String imagePath;
    private int preparationTime; // in minutes
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public MenuItem() {}
    
    public MenuItem(String name, Long categoryId, String description, BigDecimal price) {
        this.name = name;
        this.categoryId = categoryId;
        this.description = description;
        this.price = price;
        this.isAvailable = true;
        this.preparationTime = 0;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    
    public int getPreparationTime() { return preparationTime; }
    public void setPreparationTime(int preparationTime) { this.preparationTime = preparationTime; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem menuItem = (MenuItem) o;
        return Objects.equals(id, menuItem.id) && Objects.equals(name, menuItem.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
    
    @Override
    public String toString() {
        return name + " - $" + price;
    }
}
