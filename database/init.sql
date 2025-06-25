-- Coffee Shop Management System Database Schema
-- Created: June 25, 2025

USE coffeeshop;

-- Create users table for admin authentication
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'WAITER', 'KITCHEN') NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create categories table
CREATE TABLE IF NOT EXISTS categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create menu_items table
CREATE TABLE IF NOT EXISTS menu_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category_id INT NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    image_path VARCHAR(255),
    preparation_time INT DEFAULT 0, -- in minutes
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);

-- Create tables table for restaurant tables
CREATE TABLE IF NOT EXISTS tables (
    id INT AUTO_INCREMENT PRIMARY KEY,
    table_number VARCHAR(10) UNIQUE NOT NULL,
    capacity INT NOT NULL DEFAULT 4,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create orders table
CREATE TABLE IF NOT EXISTS orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(20) UNIQUE NOT NULL,
    table_id INT,
    customer_name VARCHAR(100),
    waiter_id INT,
    status ENUM('NEW', 'IN_PROGRESS', 'READY', 'COMPLETED', 'CANCELLED') DEFAULT 'NEW',
    total_amount DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (table_id) REFERENCES tables(id) ON DELETE SET NULL,
    FOREIGN KEY (waiter_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Create order_items table
CREATE TABLE IF NOT EXISTS order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    menu_item_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    unit_price DECIMAL(10, 2) NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    special_instructions TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (menu_item_id) REFERENCES menu_items(id) ON DELETE CASCADE
);

-- Create order_status_history table for tracking status changes
CREATE TABLE IF NOT EXISTS order_status_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    previous_status ENUM('NEW', 'IN_PROGRESS', 'READY', 'COMPLETED', 'CANCELLED'),
    new_status ENUM('NEW', 'IN_PROGRESS', 'READY', 'COMPLETED', 'CANCELLED') NOT NULL,
    changed_by INT,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (changed_by) REFERENCES users(id) ON DELETE SET NULL
);

-- Insert default categories
INSERT INTO categories (name, description) VALUES 
('Hot Drinks', 'Coffee, tea, and other hot beverages'),
('Cold Drinks', 'Iced coffee, smoothies, and cold beverages'),
('Sweets', 'Pastries, cakes, and desserts'),
('Snacks', 'Light snacks and sandwiches');

-- Insert default admin user (password: admin123)
INSERT INTO users (username, password, role, full_name, email) VALUES 
('admin', '$2a$10$MMLJ8xgactum9zWb4S.t2OljQzHfeXZXqlNQgt54wKlnKJyXgeVo2', 'ADMIN', 'System Administrator', 'admin@coffeeshop.com'),
('waiter1', '$2a$10$MMLJ8xgactum9zWb4S.t2OljQzHfeXZXqlNQgt54wKlnKJyXgeVo2', 'WAITER', 'John Waiter', 'waiter1@coffeeshop.com'),
('kitchen1', '$2a$10$MMLJ8xgactum9zWb4S.t2OljQzHfeXZXqlNQgt54wKlnKJyXgeVo2', 'KITCHEN', 'Kitchen Staff', 'kitchen1@coffeeshop.com');

-- Insert sample menu items
INSERT INTO menu_items (name, category_id, description, price, preparation_time) VALUES 
-- Hot Drinks
('Espresso', 1, 'Strong Italian coffee shot', 2.50, 2),
('Americano', 1, 'Espresso with hot water', 3.00, 3),
('Cappuccino', 1, 'Espresso with steamed milk and foam', 4.50, 4),
('Latte', 1, 'Espresso with steamed milk', 4.75, 4),
('Mocha', 1, 'Chocolate espresso with steamed milk', 5.25, 5),

-- Cold Drinks  
('Iced Coffee', 2, 'Cold brew coffee over ice', 3.75, 2),
('Frappuccino', 2, 'Blended iced coffee drink', 5.50, 3),
('Smoothie', 2, 'Fresh fruit smoothie', 4.25, 3),

-- Sweets
('Chocolate Cake', 3, 'Rich chocolate layer cake', 6.50, 1),
('Cheesecake', 3, 'Classic New York cheesecake', 5.75, 1),
('Croissant', 3, 'Fresh buttery croissant', 3.25, 2),
('Muffin', 3, 'Blueberry or chocolate chip muffin', 2.75, 1),

-- Snacks
('Club Sandwich', 4, 'Triple-decker sandwich with chicken', 8.50, 8),
('Panini', 4, 'Grilled sandwich with your choice of filling', 7.25, 6),
('Bagel', 4, 'Fresh bagel with cream cheese', 4.50, 3);

-- Insert sample tables
INSERT INTO tables (table_number, capacity) VALUES 
('T01', 2), ('T02', 4), ('T03', 4), ('T04', 6), ('T05', 2),
('T06', 4), ('T07', 4), ('T08', 8), ('T09', 2), ('T10', 4);

-- Create indexes for better performance
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_created_at ON orders(created_at);
CREATE INDEX idx_menu_items_category ON menu_items(category_id);
CREATE INDEX idx_menu_items_available ON menu_items(is_available);
CREATE INDEX idx_order_items_order_id ON order_items(order_id);

-- Create views for common queries
CREATE VIEW order_summary AS
SELECT 
    o.id,
    o.order_number,
    o.customer_name,
    t.table_number,
    u.full_name as waiter_name,
    o.status,
    o.total_amount,
    o.created_at,
    COUNT(oi.id) as item_count
FROM orders o
LEFT JOIN tables t ON o.table_id = t.id
LEFT JOIN users u ON o.waiter_id = u.id
LEFT JOIN order_items oi ON o.id = oi.order_id
GROUP BY o.id;

CREATE VIEW menu_with_category AS
SELECT 
    mi.id,
    mi.name,
    c.name as category_name,
    mi.description,
    mi.price,
    mi.is_available,
    mi.preparation_time
FROM menu_items mi
JOIN categories c ON mi.category_id = c.id;

-- Insert some sample orders for testing
INSERT INTO orders (order_number, table_id, customer_name, waiter_id, status, total_amount) VALUES 
('ORD-001', 1, 'John Smith', 2, 'NEW', 12.50),
('ORD-002', 3, 'Jane Doe', 2, 'IN_PROGRESS', 18.75),
('ORD-003', 5, 'Bob Johnson', 2, 'READY', 8.25);

-- Insert order items for sample orders
INSERT INTO order_items (order_id, menu_item_id, quantity, unit_price, total_price) VALUES 
(1, 3, 2, 4.50, 9.00), -- 2 Cappuccinos
(1, 11, 1, 3.25, 3.25), -- 1 Croissant
(2, 1, 1, 2.50, 2.50),  -- 1 Espresso  
(2, 13, 1, 8.50, 8.50), -- 1 Club Sandwich
(2, 9, 1, 6.50, 6.50),  -- 1 Chocolate Cake
(3, 4, 1, 4.75, 4.75),  -- 1 Latte
(3, 12, 1, 2.75, 2.75); -- 1 Muffin
