package com.coffeeshop.view.admin;

import com.coffeeshop.model.User;
import com.coffeeshop.service.AuthenticationService;
import com.coffeeshop.util.UIUtils;
import com.coffeeshop.view.LoginFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

/**
 * Admin Dashboard - Main interface for admin users
 */
public class AdminDashboard extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(AdminDashboard.class);
    
    private AuthenticationService authService;
    private User currentUser;
    
    // UI Components
    private JPanel mainPanel;
    private JPanel contentPanel;
    private JLabel welcomeLabel;
    private JButton menuManagementButton;
    private JButton userManagementButton;
    private JButton reportsButton;
    private JButton settingsButton;
    private JButton logoutButton;
    
    public AdminDashboard() {
        this.authService = AuthenticationService.getInstance();
        this.currentUser = authService.getCurrentUser();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
        
        logger.info("Admin Dashboard initialized for user: {}", currentUser.getUsername());
    }
    
    private void initializeComponents() {
        // Create main components
        mainPanel = new JPanel(new BorderLayout());
        contentPanel = new JPanel(new GridBagLayout());
        
        // Welcome label
        welcomeLabel = UIUtils.createTitleLabel("Welcome, " + currentUser.getFullName());
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Menu buttons
        menuManagementButton = UIUtils.createPrimaryButton("Menu Management");
        menuManagementButton.setPreferredSize(new Dimension(200, 80));
        
        userManagementButton = UIUtils.createPrimaryButton("User Management");
        userManagementButton.setPreferredSize(new Dimension(200, 80));
        
        reportsButton = UIUtils.createPrimaryButton("Reports & Analytics");
        reportsButton.setPreferredSize(new Dimension(200, 80));
        
        settingsButton = UIUtils.createPrimaryButton("System Settings");
        settingsButton.setPreferredSize(new Dimension(200, 80));
        
        logoutButton = UIUtils.createDangerButton("Logout");
        logoutButton.setPreferredSize(new Dimension(100, 40));
    }
    
    private void setupLayout() {
        // Main panel setup
        mainPanel.setBackground(UIUtils.LIGHT_GRAY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIUtils.LIGHT_GRAY);
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        // Content panel with grid layout
        UIUtils.styleAsCard(contentPanel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        
        // Row 1
        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(menuManagementButton, gbc);
        
        gbc.gridx = 1;
        contentPanel.add(userManagementButton, gbc);
        
        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        contentPanel.add(reportsButton, gbc);
        
        gbc.gridx = 1;
        contentPanel.add(settingsButton, gbc);
        
        // Add to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void setupEventHandlers() {
        menuManagementButton.addActionListener(e -> openMenuManagement());
        userManagementButton.addActionListener(e -> openUserManagement());
        reportsButton.addActionListener(e -> openReports());
        settingsButton.addActionListener(e -> openSettings());
        logoutButton.addActionListener(e -> performLogout());
    }
    
    private void setupFrame() {
        setTitle("Coffee Shop Management - Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UIUtils.setResponsiveSize(this, 800, 600);
        setResizable(true);
        UIUtils.centerOnScreen(this);
    }
    
    private void openMenuManagement() {
        try {
            MenuManagementPanel menuPanel = new MenuManagementPanel();
            menuPanel.setVisible(true);
            logger.info("Menu Management panel opened");
        } catch (Exception e) {
            logger.error("Error opening Menu Management panel", e);
            UIUtils.showError(this, "Error opening Menu Management: " + e.getMessage());
        }
    }
    
    private void openUserManagement() {
        try {
            UserManagementPanel userPanel = new UserManagementPanel();
            userPanel.setVisible(true);
            logger.info("User Management panel opened");
        } catch (Exception e) {
            logger.error("Error opening User Management panel", e);
            UIUtils.showError(this, "Error opening User Management: " + e.getMessage());
        }
    }
    
    private void openReports() {
        try {
            ReportsPanel reportsPanel = new ReportsPanel();
            reportsPanel.setVisible(true);
            logger.info("Reports panel opened");
        } catch (Exception e) {
            logger.error("Error opening Reports panel", e);
            UIUtils.showError(this, "Error opening Reports: " + e.getMessage());
        }
    }
    
    private void openSettings() {
        UIUtils.showSuccess(this, "System Settings will be implemented in the next phase!");
    }
    
    private void performLogout() {
        if (UIUtils.showConfirmation(this, "Are you sure you want to logout?", "Confirm Logout")) {
            authService.logout();
            logger.info("User logged out: {}", currentUser.getUsername());
            
            // Close this window and show login
            dispose();
            SwingUtilities.invokeLater(() -> {
                new LoginFrame().setVisible(true);
            });
        }
    }
}
