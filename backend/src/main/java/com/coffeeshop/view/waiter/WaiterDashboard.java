package com.coffeeshop.view.waiter;

import com.coffeeshop.model.User;
import com.coffeeshop.service.AuthenticationService;
import com.coffeeshop.util.UIUtils;
import com.coffeeshop.view.LoginFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

/**
 * Waiter Dashboard - Main interface for waiter users
 */
public class WaiterDashboard extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(WaiterDashboard.class);
    
    private AuthenticationService authService;
    private User currentUser;
    
    // UI Components
    private JPanel mainPanel;
    private JPanel contentPanel;
    private JLabel welcomeLabel;
    private JButton viewMenuButton;
    private JButton newOrderButton;
    private JButton myOrdersButton;
    private JButton tablesButton;
    private JButton logoutButton;
    
    public WaiterDashboard() {
        this.authService = AuthenticationService.getInstance();
        this.currentUser = authService.getCurrentUser();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
        
        logger.info("Waiter Dashboard initialized for user: {}", currentUser.getUsername());
    }
    
    private void initializeComponents() {
        // Create main components
        mainPanel = new JPanel(new BorderLayout());
        contentPanel = new JPanel(new GridBagLayout());
        
        // Welcome label
        welcomeLabel = UIUtils.createTitleLabel("Welcome, " + currentUser.getFullName());
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Menu buttons
        viewMenuButton = UIUtils.createPrimaryButton("View Menu");
        viewMenuButton.setPreferredSize(new Dimension(200, 80));
        
        newOrderButton = UIUtils.createSuccessButton("New Order");
        newOrderButton.setPreferredSize(new Dimension(200, 80));
        
        myOrdersButton = UIUtils.createPrimaryButton("My Orders");
        myOrdersButton.setPreferredSize(new Dimension(200, 80));
        
        tablesButton = UIUtils.createPrimaryButton("Table Management");
        tablesButton.setPreferredSize(new Dimension(200, 80));
        
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
        contentPanel.add(viewMenuButton, gbc);
        
        gbc.gridx = 1;
        contentPanel.add(newOrderButton, gbc);
        
        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        contentPanel.add(myOrdersButton, gbc);
        
        gbc.gridx = 1;
        contentPanel.add(tablesButton, gbc);
        
        // Add to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void setupEventHandlers() {
        viewMenuButton.addActionListener(e -> viewMenu());
        newOrderButton.addActionListener(e -> createNewOrder());
        myOrdersButton.addActionListener(e -> viewMyOrders());
        tablesButton.addActionListener(e -> manageTable());
        logoutButton.addActionListener(e -> performLogout());
    }
    
    private void setupFrame() {
        setTitle("Coffee Shop Management - Waiter Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setResizable(true);
        UIUtils.centerOnScreen(this);
    }
    
    private void viewMenu() {
        try {
            MenuViewPanel menuPanel = new MenuViewPanel();
            menuPanel.setVisible(true);
            logger.info("Menu view panel opened");
        } catch (Exception e) {
            logger.error("Error opening Menu view panel", e);
            UIUtils.showError(this, "Error opening Menu view: " + e.getMessage());
        }
    }
    
    private void createNewOrder() {
        try {
            NewOrderPanel orderPanel = new NewOrderPanel();
            orderPanel.setVisible(true);
            logger.info("New order panel opened");
        } catch (Exception e) {
            logger.error("Error opening New order panel", e);
            UIUtils.showError(this, "Error opening New order: " + e.getMessage());
        }
    }
    
    private void viewMyOrders() {
        try {
            OrderHistoryPanel historyPanel = new OrderHistoryPanel();
            historyPanel.setVisible(true);
            logger.info("Order history panel opened");
        } catch (Exception e) {
            logger.error("Error opening Order history panel", e);
            UIUtils.showError(this, "Error opening Order history: " + e.getMessage());
        }
    }
    
    private void manageTable() {
        UIUtils.showSuccess(this, "Table Management will be implemented in the next phase!");
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
