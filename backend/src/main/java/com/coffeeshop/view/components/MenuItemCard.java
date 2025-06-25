package com.coffeeshop.view.components;

import com.coffeeshop.model.MenuItem;
import com.coffeeshop.util.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Modern card component for displaying menu items with images
 */
public class MenuItemCard extends JPanel {
    private MenuItem menuItem;
    private JLabel imageLabel;
    private JLabel nameLabel;
    private JLabel descriptionLabel;
    private JLabel priceLabel;
    private JLabel categoryLabel;
    private JButton actionButton;
    
    public MenuItemCard(MenuItem item) {
        this.menuItem = item;
        initializeComponents();
        setupLayout();
        updateContent();
    }
    
    private void initializeComponents() {
        setBackground(UIUtils.SURFACE_PRIMARY);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtils.BORDER_LIGHT, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        setPreferredSize(new Dimension(320, 240));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Image placeholder with coffee shop styling
        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(110, 110));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setOpaque(true);
        imageLabel.setBackground(UIUtils.BACKGROUND_SECONDARY);
        imageLabel.setBorder(BorderFactory.createLineBorder(UIUtils.BORDER_LIGHT, 1));
        
        // Text components with consistent styling
        nameLabel = UIUtils.createHeaderLabel("");
        nameLabel.setForeground(UIUtils.PRIMARY_COLOR); // Coffee brown for names
        
        descriptionLabel = UIUtils.createSecondaryLabel("");
        
        priceLabel = UIUtils.createStyledLabel("");
        priceLabel.setFont(UIUtils.LARGE_FONT);
        priceLabel.setForeground(UIUtils.ACCENT_COLOR); // Golden amber for prices
        
        categoryLabel = UIUtils.createSecondaryLabel("");
        categoryLabel.setFont(UIUtils.SMALL_FONT);
        categoryLabel.setForeground(UIUtils.TEXT_TERTIARY);
        
        // Modern action button
        actionButton = UIUtils.createSuccessButton("Add to Order");
        actionButton.setPreferredSize(new Dimension(130, 36));
        
        // Subtle hover effects
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UIUtils.PRIMARY_LIGHT, 2),
                    BorderFactory.createEmptyBorder(19, 19, 19, 19)
                ));
                setBackground(UIUtils.SURFACE_SECONDARY);
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UIUtils.BORDER_LIGHT, 1),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
                ));
                setBackground(UIUtils.SURFACE_PRIMARY);
            }
        });
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(16, 16));
        
        // Left side - Image with modern styling
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(UIUtils.SURFACE_PRIMARY);
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        
        // Right side - Content with better spacing
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(UIUtils.SURFACE_PRIMARY);
        
        // Header section
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIUtils.SURFACE_PRIMARY);
        headerPanel.add(nameLabel, BorderLayout.NORTH);
        headerPanel.add(categoryLabel, BorderLayout.SOUTH);
        
        // Description section
        JPanel descPanel = new JPanel(new BorderLayout());
        descPanel.setBackground(UIUtils.SURFACE_PRIMARY);
        descPanel.add(descriptionLabel, BorderLayout.CENTER);
        
        // Footer section with price and button
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(UIUtils.SURFACE_PRIMARY);
        footerPanel.add(priceLabel, BorderLayout.WEST);
        footerPanel.add(actionButton, BorderLayout.EAST);
        
        // Assemble content panel
        contentPanel.add(headerPanel);
        contentPanel.add(Box.createVerticalStrut(12));
        contentPanel.add(descPanel);
        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(footerPanel);
        
        add(imagePanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    private void updateContent() {
        // Update image
        if (menuItem.getImagePath() != null && !menuItem.getImagePath().isEmpty()) {
            ImageIcon icon = UIUtils.loadImageIcon(menuItem.getImagePath(), 100, 100);
            if (icon != null) {
                imageLabel.setIcon(icon);
                imageLabel.setText("");
            } else {
                imageLabel.setIcon(null);
                imageLabel.setText("📷");
                imageLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
                imageLabel.setForeground(UIUtils.TEXT_SECONDARY);
            }
        } else {
            imageLabel.setIcon(null);
            imageLabel.setText("📷");
            imageLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
            imageLabel.setForeground(UIUtils.TEXT_SECONDARY);
        }
        
        // Update text
        nameLabel.setText(menuItem.getName());
        categoryLabel.setText(menuItem.getCategoryName());
        
        String desc = menuItem.getDescription();
        if (desc != null && desc.length() > 60) {
            desc = desc.substring(0, 60) + "...";
        }
        descriptionLabel.setText("<html>" + desc + "</html>");
        
        priceLabel.setText("$" + menuItem.getPrice());
        
        // Update availability with modern styling
        if (!menuItem.isAvailable()) {
            setBackground(UIUtils.BACKGROUND_SECONDARY);
            actionButton.setText("Unavailable");
            actionButton.setEnabled(false);
            actionButton.setBackground(UIUtils.BORDER_MEDIUM);
            actionButton.setForeground(UIUtils.TEXT_TERTIARY);
            
            // Dim the entire card for unavailable items
            nameLabel.setForeground(UIUtils.TEXT_TERTIARY);
            priceLabel.setForeground(UIUtils.TEXT_TERTIARY);
        } else {
            nameLabel.setForeground(UIUtils.PRIMARY_COLOR);
            priceLabel.setForeground(UIUtils.ACCENT_COLOR);
        }
    }
    
    public MenuItem getMenuItem() {
        return menuItem;
    }
    
    public void setMenuItem(MenuItem item) {
        this.menuItem = item;
        updateContent();
    }
    
    public void addActionListener(ActionListener listener) {
        actionButton.addActionListener(listener);
    }
    
    public JButton getActionButton() {
        return actionButton;
    }
}
