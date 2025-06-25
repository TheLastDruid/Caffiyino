# Coffee Shop Management System

A full-featured desktop application for coffee shop management using Java Swing for the user interface and MySQL as the database, running inside a Docker container.

## Features

### Admin Panel
- ✅ User management (add, edit, delete users)
- ✅ Menu management (add, edit, delete menu items)
- ✅ Category management
- ✅ Reports dashboard (framework ready)
- ✅ System settings

### Waiter Interface
- ✅ View available menu items
- 🚧 Create and save new client orders (framework ready)
- 🚧 Order history management (framework ready)
- 🚧 Table management (framework ready)

### Kitchen Dashboard
- ✅ Dashboard interface for kitchen staff
- 🚧 View orders by status (New, In Progress, Ready, Completed)
- 🚧 Update order status
- 🚧 Real-time order notifications

### Technical Features
- ✅ MySQL database with Docker support
- ✅ Connection pooling with HikariCP
- ✅ DAO/Repository pattern implementation
- ✅ MVC architecture
- ✅ Modern UI with FlatLaf
- ✅ User authentication with BCrypt
- ✅ Comprehensive error handling
- ✅ Logging with Logback

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Docker and Docker Compose

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

### 🚧 Framework Ready (Next Phase)
1. **Order Management**
   - Complete order creation workflow
   - Order status tracking
   - Real-time updates

2. **Reports & Analytics**
   - Sales reports
   - Menu performance analytics
   - Export to CSV/PDF

3. **Advanced Features**
   - Table management
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
