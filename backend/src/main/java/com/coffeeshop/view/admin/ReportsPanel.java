package com.coffeeshop.view.admin;

import com.coffeeshop.util.UIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

/**
 * Reports Panel for admin users
 */
public class ReportsPanel extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(ReportsPanel.class);
    
    // UI Components
    private JButton dailySalesButton;
    private JButton monthlySalesButton;
    private JButton menuPerformanceButton;
    private JButton userActivityButton;
    private JButton exportButton;
    
    public ReportsPanel() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
        
        logger.info("Reports Panel initialized");
    }
    
    private void initializeComponents() {
        dailySalesButton = UIUtils.createPrimaryButton("Daily Sales Report");
        dailySalesButton.setPreferredSize(new Dimension(200, 60));
        
        monthlySalesButton = UIUtils.createPrimaryButton("Monthly Sales Report");
        monthlySalesButton.setPreferredSize(new Dimension(200, 60));
        
        menuPerformanceButton = UIUtils.createPrimaryButton("Menu Performance");
        menuPerformanceButton.setPreferredSize(new Dimension(200, 60));
        
        userActivityButton = UIUtils.createPrimaryButton("User Activity Report");
        userActivityButton.setPreferredSize(new Dimension(200, 60));
        
        exportButton = UIUtils.createSuccessButton("Export to CSV");
        exportButton.setPreferredSize(new Dimension(200, 60));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Title
        JLabel titleLabel = UIUtils.createTitleLabel("Reports & Analytics");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Content panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        UIUtils.styleAsCard(contentPanel);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        
        // Row 1
        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(dailySalesButton, gbc);
        
        gbc.gridx = 1;
        contentPanel.add(monthlySalesButton, gbc);
        
        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        contentPanel.add(menuPerformanceButton, gbc);
        
        gbc.gridx = 1;
        contentPanel.add(userActivityButton, gbc);
        
        // Row 3
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(exportButton, gbc);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIUtils.LIGHT_GRAY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void setupEventHandlers() {
        dailySalesButton.addActionListener(e -> showDailySalesReport());
        monthlySalesButton.addActionListener(e -> showMonthlySalesReport());
        menuPerformanceButton.addActionListener(e -> showMenuPerformanceReport());
        userActivityButton.addActionListener(e -> showUserActivityReport());
        exportButton.addActionListener(e -> exportReports());
    }
    
    private void setupFrame() {
        setTitle("Reports & Analytics");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        UIUtils.setResponsiveSize(this, 600, 500);
        setResizable(true);
        UIUtils.centerOnScreen(this);
    }
    
    private void showDailySalesReport() {
        UIUtils.showSuccess(this, "Daily Sales Report will be implemented in the next phase!");
    }
    
    private void showMonthlySalesReport() {
        UIUtils.showSuccess(this, "Monthly Sales Report will be implemented in the next phase!");
    }
    
    private void showMenuPerformanceReport() {
        UIUtils.showSuccess(this, "Menu Performance Report will be implemented in the next phase!");
    }
    
    private void showUserActivityReport() {
        UIUtils.showSuccess(this, "User Activity Report will be implemented in the next phase!");
    }
    
    private void exportReports() {
        UIUtils.showSuccess(this, "Export functionality will be implemented in the next phase!");
    }
}
