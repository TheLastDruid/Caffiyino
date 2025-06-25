@echo off
echo ===============================================
echo     Coffee Shop Management System - 2025
echo ===============================================
echo Starting application...
echo.

REM Check if Java is installed
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 11 or higher
    echo.
    pause
    exit /b 1
)

REM Check if JAR file exists
if not exist "coffee-shop-management-1.0.0.jar" (
    echo ERROR: coffee-shop-management-1.0.0.jar not found
    echo Please ensure the JAR file is in the same directory as this script
    echo.
    pause
    exit /b 1
)

REM Check if Docker containers are running (optional check)
echo Checking database connection...
docker-compose ps >nul 2>&1
if errorlevel 1 (
    echo WARNING: Docker Compose not available or containers not running
    echo Make sure to start the database with: docker-compose up -d
    echo.
)

echo Starting Coffee Shop Management System...
echo.
echo ===============================================
echo  Login Credentials:
echo  - Admin: admin / admin123
echo  - Waiter: waiter1 / admin123
echo  - Kitchen: kitchen1 / admin123
echo ===============================================
echo.

REM Start the application
java -jar coffee-shop-management-1.0.0.jar

REM Pause to see any error messages
if errorlevel 1 (
    echo.
    echo Application exited with an error.
    echo Please check the console output above.
    echo.
    pause
)
