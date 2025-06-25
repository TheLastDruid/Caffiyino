@echo off
echo Coffee Shop Management System - Setup

echo.
echo Checking prerequisites...

echo.
echo Checking Java...
java -version
if errorlevel 1 (
    echo ERROR: Java is not installed or not in PATH
    pause
    exit /b 1
)

echo.
echo Checking Maven...
mvn -version
if errorlevel 1 (
    echo ERROR: Maven is not installed or not in PATH
    pause
    exit /b 1
)

echo.
echo Checking Docker...
docker --version
if errorlevel 1 (
    echo ERROR: Docker is not installed or not in PATH
    pause
    exit /b 1
)

echo.
echo All prerequisites met!
echo.
echo Starting setup...

echo.
echo 1. Building application...
cd backend
mvn clean compile

if errorlevel 1 (
    echo ERROR: Build failed
    pause
    exit /b 1
)

echo.
echo 2. Starting Docker containers...
cd ..
docker-compose up -d

echo.
echo Setup complete!
echo.
echo You can now run the application using run.bat
echo Or manually with: cd backend && mvn exec:java -Dexec.mainClass="com.coffeeshop.CoffeeShopApplication"
echo.
echo Default credentials:
echo Admin: admin / admin123
echo Waiter: waiter1 / admin123
echo Kitchen: kitchen1 / admin123
echo.
echo Database access:
echo phpMyAdmin: http://localhost:8080
echo Username: coffeeuser, Password: coffee123

pause
