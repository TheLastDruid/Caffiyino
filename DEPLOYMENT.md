# Deployment Guide

## Quick Start

### Option 1: Automated Setup (Windows)
```bash
# Run the automated setup script
setup.bat

# Then run the application
run.bat
```

### Option 2: Manual Setup

#### 1. Prerequisites
- Java 11+
- Maven 3.6+
- Docker & Docker Compose

#### 2. Database Setup
```bash
# Start MySQL container
docker-compose up -d

# Verify containers are running
docker-compose ps
```

#### 3. Build & Run
```bash
# Navigate to backend directory
cd backend

# Build the project
mvn clean compile

# Run the application
mvn exec:java -Dexec.mainClass="com.coffeeshop.CoffeeShopApplication"
```

## Access Information

### Application Login
- **Admin:** admin / admin123
- **Waiter:** waiter1 / admin123  
- **Kitchen:** kitchen1 / admin123

### Database Access
- **phpMyAdmin:** http://localhost:8080
- **MySQL Direct:** localhost:3306
- **Username:** coffeeuser
- **Password:** coffee123

## Building JAR File

```bash
cd backend
mvn clean package
java -jar target/coffee-shop-management-1.0.0.jar
```

## Troubleshooting

### Common Issues
1. **Port conflicts:** Ensure ports 3306 and 8080 are available
2. **Java version:** Use Java 11 or higher
3. **Docker issues:** Make sure Docker Desktop is running

### Clean Restart
```bash
# Stop containers
docker-compose down

# Remove volumes (reset database)
docker-compose down -v

# Restart fresh
docker-compose up -d
```
