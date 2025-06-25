package com.coffeeshop.view.waiter;

import com.coffeeshop.util.UIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

/**
 * New Order Panel for waiter users
 */
public class NewOrderPanel extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(NewOrderPanel.class);
    
    public NewOrderPanel() {
        initializeComponents();
        setupLayout();
        setupFrame();
        
        logger.info("New Order Panel initialized");
    }
    
    private void initializeComponents() {
        // This will be implemented in the next phase
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JLabel messageLabel = UIUtils.createTitleLabel("New Order Panel");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel infoLabel = new JLabel("<html><center>Order creation functionality<br/>will be implemented in the next phase!</center></html>");
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        infoLabel.setFont(UIUtils.NORMAL_FONT);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        centerPanel.add(messageLabel, BorderLayout.NORTH);
        centerPanel.add(infoLabel, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private void setupFrame() {
        setTitle("New Order");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setResizable(true);
        UIUtils.centerOnScreen(this);
    }
}
