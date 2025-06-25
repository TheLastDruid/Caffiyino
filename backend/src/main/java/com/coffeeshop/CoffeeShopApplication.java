package com.coffeeshop;

import com.coffeeshop.config.DatabaseConfig;
import com.coffeeshop.view.LoginFrame;
import com.formdev.flatlaf.FlatDarkLaf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

/**
 * Main application entry point for Coffee Shop Management System
 * 
 * @author Coffee Shop Team
 * @version 1.0.0
 */
public class CoffeeShopApplication {
    private static final Logger logger = LoggerFactory.getLogger(CoffeeShopApplication.class);
    
    public static void main(String[] args) {
        logger.info("Starting Coffee Shop Management System...");
        
        // Set system properties for better UI rendering
        System.setProperty("sun.java2d.opengl", "true");
        System.setProperty("swing.aatext", "true");
        System.setProperty("awt.useSystemAAFontSettings", "on");
        
        // Set look and feel
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            
            // Customize UI defaults
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
            UIManager.put("ProgressBar.arc", 10);
            UIManager.put("TextComponent.arc", 10);
            UIManager.put("Button.margin", new Insets(8, 12, 8, 12));
            
        } catch (Exception ex) {
            logger.error("Failed to set look and feel", ex);
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                logger.error("Failed to set system look and feel", e);
            }
        }
        
        // Initialize database
        try {
            DatabaseConfig.getInstance().initializeDatabase();
            logger.info("Database initialized successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize database", e);
            JOptionPane.showMessageDialog(null, 
                "Failed to connect to database. Please ensure MySQL is running.\n" + e.getMessage(),
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        
        // Start the application
        SwingUtilities.invokeLater(() -> {
            try {
                new LoginFrame().setVisible(true);
                logger.info("Application started successfully");
            } catch (Exception e) {
                logger.error("Failed to start application", e);
                JOptionPane.showMessageDialog(null, 
                    "Failed to start application: " + e.getMessage(),
                    "Application Error", 
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}
