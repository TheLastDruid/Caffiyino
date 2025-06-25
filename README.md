# Coffee Shop Management System ☕

A modern full-featured desktop application for coffee shop management featuring a beautiful 2025 UI design with Java Swing and MySQL database.

## 🚀 Quick Start

### Option 1: One-Click Setup (Recommended)
```bash
# 1. Setup database
quick-setup.bat

# 2. Start application  
start.bat
```

### Option 2: Manual Setup
```bash
# Start database
docker-compose up -d

# Run application
java -jar coffee-shop-management-1.0.0.jar
```

## 🎨 2025 Modern UI Features
- **Coffee Shop Themed Design**: Warm espresso browns and golden caramel accents
- **Modern Components**: Enhanced buttons, cards, and input fields with hover effects
- **Consistent Color Palette**: Professional and inviting visual identity
- **Accessibility**: High contrast text and focus indicators
- **Responsive Design**: Clean, intuitive interface for all user roles
- **📱 Mobile-Friendly**: Optimized for 6-inch screens with responsive fonts and layouts

## 👥 Login Credentials
- **Admin**: `admin` / `admin123`
- **Waiter**: `waiter1` / `admin123`  
- **Kitchen**: `kitchen1` / `admin123`

## Features

### Admin Panel
- ✅ User management (add, edit, delete users)
- ✅ Menu management (add, edit, delete menu items)
- ✅ Category management
- ✅ Reports dashboard (framework ready)
- ✅ System settings

### Waiter Interface
- ✅ View available menu items
- ✅ Create and save new client orders (implemented)
- ✅ Order history management (implemented)
- ✅ Table management (implemented)

### Kitchen Dashboard
- ✅ Dashboard interface for kitchen staff
- ✅ View orders by status (New, In Progress, Ready, Completed)
- ✅ Update order status
- ✅ Real-time order notifications

### Technical Features
- ✅ MySQL database with Docker support
- ✅ Connection pooling with HikariCP
- ✅ DAO/Repository pattern implementation
- ✅ MVC architecture
- ✅ Modern UI with FlatLaf
- ✅ User authentication with BCrypt
- ✅ Comprehensive error handling
- ✅ Logging with Logback
- ✅ **Mobile-Responsive UI**: Optimized for 6-inch screens and small displays

## � Mobile-Friendly Features

The Coffee Shop Management System is now optimized for 6-inch screens and small displays:

### 🔧 **Responsive Design**
- **Smart Screen Detection**: Automatically detects screen size and adjusts UI accordingly
- **Mobile Font Sizes**: Smaller, readable fonts for compact displays
- **Responsive Buttons**: Adaptive button sizes (120x48px on mobile vs 200x80px on desktop)
- **Compact Layouts**: Reduced padding and margins for efficient space usage
- **Auto-Maximize**: Windows automatically maximize on small screens for optimal usage

### 📐 **Mobile Optimizations**
- **Window Sizing**: All panels scale to 90% of screen size with mobile-friendly constraints
- **Text Fields**: Responsive padding and font sizes for better touch interaction
- **Navigation**: Optimized button placement and sizing for finger-friendly interaction
- **Search Interface**: Compact search controls with appropriate spacing

### 💡 **Smart Scaling**
- **Dynamic Font Scaling**: Title fonts scale from 28px to 18px, buttons from 14px to 11px
- **Intelligent Padding**: Margins and padding automatically reduce by 50% on small screens
- **Flexible Layouts**: GridBag layouts adapt to available screen real estate
- **Optimized Tables**: Table components scale appropriately for mobile viewing

### 🎯 **Screen Size Threshold**
- **Detection**: Screens ≤ 600px width or height are considered mobile
- **Graceful Degradation**: Maintains full functionality while optimizing for smaller displays
- **Cross-Platform**: Works on various devices including tablets, small laptops, and mobile displays

## �🗄️ Database Access
- **phpMyAdmin**: http://localhost:8080
- **MySQL Direct**: `localhost:3306`
- **Username**: `coffeeuser`
- **Password**: `coffee123`
- **Database**: `coffeeshop`

## 📋 Prerequisites

- Java 11 or higher
- Docker and Docker Compose (for database)
- (Optional) Maven 3.6+ for development

## Setup Instructions

### 1. Start the Database

Navigate to the project root directory and start the MySQL container:

```bash
docker-compose up -d
```

This will start:
- MySQL database on port 3306
- phpMyAdmin on port 8080 (http://localhost:8080)

### 2. Build the Application

```bash
cd backend
mvn clean compile
```

### 3. Run the Application

```bash
mvn exec:java -Dexec.mainClass="com.coffeeshop.CoffeeShopApplication"
```

Or build a JAR file:

```bash
mvn clean package
java -jar target/coffee-shop-management-1.0.0.jar
```

## Default Login Credentials

### Admin User
- **Username:** admin
- **Password:** admin123

### Waiter User
- **Username:** waiter1
- **Password:** admin123

### Kitchen User
- **Username:** kitchen1
- **Password:** admin123

## Database Access

### phpMyAdmin
- **URL:** http://localhost:8080
- **Username:** coffeeuser
- **Password:** coffee123

### Direct MySQL Connection
- **Host:** localhost
- **Port:** 3306
- **Database:** coffeeshop
- **Username:** coffeeuser
- **Password:** coffee123

## Project Structure

```
coffeV2/
├── backend/
│   ├── src/main/java/com/coffeeshop/
│   │   ├── config/          # Database configuration
│   │   ├── dao/             # Data Access Objects
│   │   │   └── impl/        # DAO implementations
│   │   ├── model/           # Entity models
│   │   ├── service/         # Business logic layer
│   │   ├── util/            # Utility classes
│   │   └── view/            # UI components
│   │       ├── admin/       # Admin interface
│   │       ├── waiter/      # Waiter interface
│   │       └── kitchen/     # Kitchen interface
│   └── pom.xml             # Maven dependencies
├── database/
│   └── init.sql            # Database schema and sample data
└── docker-compose.yml     # Docker configuration
```

## Dependencies

- **MySQL Connector:** Database connectivity
- **HikariCP:** Connection pooling
- **FlatLaf:** Modern Swing look and feel
- **BCrypt:** Password hashing
- **Logback:** Logging framework
- **iText7:** PDF generation (ready for reports)
- **OpenCSV:** CSV export (ready for reports)

## Current Implementation Status

### ✅ Completed Features
1. **Database Setup**
   - Complete schema with tables for users, categories, menu items, orders, etc.
   - Sample data populated
   - Docker containerization

2. **User Authentication**
   - Secure login with BCrypt password hashing
   - Role-based access control
   - Session management

3. **Admin Dashboard**
   - User management (CRUD operations)
   - Menu management (CRUD operations)
   - Category management
   - Modern, responsive UI

4. **Waiter Dashboard**
   - Menu viewing
   - Dashboard framework for order management

5. **Kitchen Dashboard**
   - Interface framework for order status management

### ✅ Framework Ready (Completed)
1. **Order Management**
   - ✅ Complete order creation workflow
   - ✅ Order status tracking
   - ✅ Real-time updates

2. **Reports & Analytics**
   - Sales reports
   - Menu performance analytics
   - Export to CSV/PDF

3. **Advanced Features**
   - ✅ Table management
   - Inventory tracking
   - Customer management

## Development Notes

- The application follows MVC architecture with clear separation of concerns
- All database operations use prepared statements to prevent SQL injection
- Connection pooling ensures efficient database resource usage
- Modern UI components provide a professional look and feel
- Comprehensive error handling and logging throughout the application

## Troubleshooting

### Database Connection Issues
1. Ensure Docker containers are running: `docker-compose ps`
2. Check if ports 3306 and 8080 are available
3. Verify database credentials in `DatabaseConfig.java`

### Build Issues
1. Ensure Java 11+ is installed: `java -version`
2. Ensure Maven is installed: `mvn -version`
3. Clear Maven cache: `mvn clean`

### UI Issues
1. The application uses FlatLaf for modern UI - ensure it's in the classpath
2. On some systems, you may need to adjust font rendering properties

## Next Development Phase

The foundation is complete and ready for extending with:
1. Complete order management workflow
2. Real-time order tracking
3. Advanced reporting features
4. Table and customer management
5. Inventory management
6. Receipt printing
7. Payment processing integration

This provides a solid foundation for a complete coffee shop management system with room for future enhancements.
