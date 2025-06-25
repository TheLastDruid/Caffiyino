@echo off
echo Starting Coffee Shop Management System...

echo.
echo 1. Starting Docker containers...
docker-compose up -d

echo.
echo 2. Waiting for database to be ready...
timeout /t 10

echo.
echo 3. Building application...
cd backend
mvn clean compile

echo.
echo 4. Starting application...
mvn exec:java -Dexec.mainClass="com.coffeeshop.CoffeeShopApplication"

pause
