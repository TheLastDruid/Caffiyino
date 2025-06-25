package com.coffeeshop.view.kitchen;

import com.coffeeshop.model.User;
import com.coffeeshop.service.AuthenticationService;
import com.coffeeshop.util.UIUtils;
import com.coffeeshop.view.LoginFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

/**
 * Kitchen Dashboard - Main interface for kitchen users
 */
public class KitchenDashboard extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(KitchenDashboard.class);
    
    private AuthenticationService authService;
    private User currentUser;
    
    // UI Components
    private JPanel mainPanel;
    private JPanel contentPanel;
    private JLabel welcomeLabel;
    private JButton newOrdersButton;
    private JButton inProgressButton;
    private JButton readyOrdersButton;
    private JButton completedButton;
    private JButton logoutButton;
    
    public KitchenDashboard() {
        this.authService = AuthenticationService.getInstance();
        this.currentUser = authService.getCurrentUser();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
        
        logger.info("Kitchen Dashboard initialized for user: {}", currentUser.getUsername());
    }
    
    private void initializeComponents() {
        // Create main components
        mainPanel = new JPanel(new BorderLayout());
        contentPanel = new JPanel(new GridBagLayout());
        
        // Welcome label
        welcomeLabel = UIUtils.createTitleLabel("Kitchen Dashboard - " + currentUser.getFullName());
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Menu buttons
        newOrdersButton = UIUtils.createDangerButton("New Orders");
        newOrdersButton.setPreferredSize(new Dimension(200, 80));
        
        inProgressButton = UIUtils.createPrimaryButton("In Progress");
        inProgressButton.setPreferredSize(new Dimension(200, 80));
        
        readyOrdersButton = UIUtils.createSuccessButton("Ready Orders");
        readyOrdersButton.setPreferredSize(new Dimension(200, 80));
        
        completedButton = UIUtils.createPrimaryButton("Completed Orders");
        completedButton.setPreferredSize(new Dimension(200, 80));
        
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
        contentPanel.add(newOrdersButton, gbc);
        
        gbc.gridx = 1;
        contentPanel.add(inProgressButton, gbc);
        
        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        contentPanel.add(readyOrdersButton, gbc);
        
        gbc.gridx = 1;
        contentPanel.add(completedButton, gbc);
        
        // Add to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void setupEventHandlers() {
        newOrdersButton.addActionListener(e -> viewNewOrders());
        inProgressButton.addActionListener(e -> viewInProgressOrders());
        readyOrdersButton.addActionListener(e -> viewReadyOrders());
        completedButton.addActionListener(e -> viewCompletedOrders());
        logoutButton.addActionListener(e -> performLogout());
    }
    
    private void setupFrame() {
        setTitle("Coffee Shop Management - Kitchen Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UIUtils.setResponsiveSize(this, 800, 600);
        setResizable(true);
        UIUtils.centerOnScreen(this);
    }
    
    private void viewNewOrders() {
        try {
            KitchenOrderPanel orderPanel = new KitchenOrderPanel(com.coffeeshop.model.Order.Status.NEW);
            orderPanel.setVisible(true);
            logger.info("New orders panel opened");
        } catch (Exception e) {
            logger.error("Error opening new orders panel", e);
            UIUtils.showError(this, "Error opening new orders: " + e.getMessage());
        }
    }
    
    private void viewInProgressOrders() {
        try {
            KitchenOrderPanel orderPanel = new KitchenOrderPanel(com.coffeeshop.model.Order.Status.IN_PROGRESS);
            orderPanel.setVisible(true);
            logger.info("In progress orders panel opened");
        } catch (Exception e) {
            logger.error("Error opening in progress orders panel", e);
            UIUtils.showError(this, "Error opening in progress orders: " + e.getMessage());
        }
    }
    
    private void viewReadyOrders() {
        try {
            KitchenOrderPanel orderPanel = new KitchenOrderPanel(com.coffeeshop.model.Order.Status.READY);
            orderPanel.setVisible(true);
            logger.info("Ready orders panel opened");
        } catch (Exception e) {
            logger.error("Error opening ready orders panel", e);
            UIUtils.showError(this, "Error opening ready orders: " + e.getMessage());
        }
    }
    
    private void viewCompletedOrders() {
        try {
            KitchenOrderPanel orderPanel = new KitchenOrderPanel(com.coffeeshop.model.Order.Status.COMPLETED);
            orderPanel.setVisible(true);
            logger.info("Completed orders panel opened");
        } catch (Exception e) {
            logger.error("Error opening completed orders panel", e);
            UIUtils.showError(this, "Error opening completed orders: " + e.getMessage());
        }
    }
    
    private void performLogout() {
        if (UIUtils.showConfirmation(this, "Are you sure you want to logout?", "Confirm Logout")) {
            authService.logout();
            logger.info("User logged out: {}", currentUser.getUsername());
            
            // Close this window and show login
            dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        }
    }
}
