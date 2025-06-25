@echo off
echo ===============================================
echo   Coffee Shop Management System - Setup
echo ===============================================
echo.

REM Check if Docker is installed
docker --version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Docker is not installed or not in PATH
    echo Please install Docker Desktop
    echo.
    pause
    exit /b 1
)

REM Check if Docker Compose is available
docker-compose --version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Docker Compose is not available
    echo Please ensure Docker Desktop is running
    echo.
    pause
    exit /b 1
)

echo Starting database containers...
docker-compose up -d

REM Wait a moment for containers to start
echo Waiting for database to initialize...
timeout /t 10 /nobreak >nul

REM Check if containers are running
echo Checking container status...
docker-compose ps

echo.
echo ===============================================
echo Setup complete! 
echo.
echo Database Access:
echo - phpMyAdmin: http://localhost:8080
echo - MySQL: localhost:3306
echo - Username: coffeeuser
echo - Password: coffee123
echo.
echo Now run: start.bat
echo ===============================================
echo.
pause
