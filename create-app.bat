@echo off
echo Creating Windows application package for Coffee Shop Management System...
echo.

cd /d "C:\Users\Spooky\Desktop\Dev\Caffiyino\backend\target"

echo Cleaning up any previous builds...
if exist "CoffeeShopManagement" rmdir /s /q "CoffeeShopManagement"

echo.
echo Creating Windows application package with jpackage...
jpackage ^
  --input . ^
  --name "CoffeeShopManagement" ^
  --main-jar coffee-shop-management-1.0.0.jar ^
  --main-class com.coffeeshop.CoffeeShopApplication ^
  --type app-image ^
  --dest . ^
  --app-version "1.0.0" ^
  --description "Coffee Shop Management System" ^
  --vendor "CoffeeShop Inc." ^
  --copyright "Copyright 2024 CoffeeShop Inc."

echo.
if exist "CoffeeShopManagement" (
    echo SUCCESS: CoffeeShopManagement application package created successfully!
    echo Location: %CD%\CoffeeShopManagement\
    echo.
    echo Creating convenient launchers...
    
    REM Create launcher for this directory
    echo @echo off > CoffeeShopManagement-Launcher.bat
    echo echo Starting Coffee Shop Management System... >> CoffeeShopManagement-Launcher.bat
    echo cd /d "%~dp0" >> CoffeeShopManagement-Launcher.bat
    echo start "" "CoffeeShopManagement\CoffeeShopManagement.exe" >> CoffeeShopManagement-Launcher.bat
    
    REM Copy executable to a convenient location
    echo Copying executable to main project directory...
    if not exist "..\..\CoffeeShopManagement" mkdir "..\..\CoffeeShopManagement"
    xcopy "CoffeeShopManagement\*" "..\..\CoffeeShopManagement\" /E /Y /Q
    
    REM Create launcher in main project directory
    echo @echo off > "..\..\Start-CoffeeShop.bat"
    echo echo ======================================== >> "..\..\Start-CoffeeShop.bat"
    echo echo    Coffee Shop Management System >> "..\..\Start-CoffeeShop.bat"
    echo echo ======================================== >> "..\..\Start-CoffeeShop.bat"
    echo echo Starting application... >> "..\..\Start-CoffeeShop.bat"
    echo cd /d "%~dp0" >> "..\..\Start-CoffeeShop.bat"
    echo start "" "CoffeeShopManagement\CoffeeShopManagement.exe" >> "..\..\Start-CoffeeShop.bat"
    
    echo.
    echo READY TO USE:
    echo 1. Main executable: CoffeeShopManagement\CoffeeShopManagement.exe
    echo 2. Convenient launcher: Start-CoffeeShop.bat (in main project folder)
    echo.
    echo Double-click Start-CoffeeShop.bat to run the application!
) else (
    echo ERROR: Failed to create application package. Check the output above for errors.
)

echo.
pause
