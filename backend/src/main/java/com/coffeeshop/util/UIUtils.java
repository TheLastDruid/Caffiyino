package com.coffeeshop.util;

import javax.swing.*;
import java.awt.*;

/**
 * Utility class for common UI operations and styling
 */
public class UIUtils {
    
    private static final String FONT_FAMILY = "Segoe UI";
    
    // Private constructor to prevent instantiation
    private UIUtils() {
        throw new IllegalStateException("Utility class");
    }
    
    // 2025 Modern Coffee Shop Color Palette - Consistent & Contemporary
    // Primary Brand Colors (Refined Coffee Shop Inspired)
    public static final Color PRIMARY_COLOR = new Color(92, 51, 23);          // Rich Espresso Brown
    public static final Color PRIMARY_DARK = new Color(59, 32, 14);           // Dark Roast
    public static final Color PRIMARY_LIGHT = new Color(141, 98, 57);         // Light Roast
    public static final Color PRIMARY_HOVER = new Color(112, 67, 33);         // Hover state
    
    // Secondary & Accent Colors (Harmonious Palette)
    public static final Color SECONDARY_COLOR = new Color(203, 156, 99);      // Warm Latte
    public static final Color ACCENT_COLOR = new Color(191, 144, 72);         // Golden Caramel
    public static final Color ACCENT_LIGHT = new Color(228, 188, 140);        // Light Caramel
    
    // Semantic Colors (Coffee Shop Themed)
    public static final Color SUCCESS_COLOR = new Color(34, 139, 34);         // Forest Green (natural)
    public static final Color WARNING_COLOR = new Color(218, 165, 32);        // Goldenrod (warm)
    public static final Color DANGER_COLOR = new Color(205, 92, 92);          // Indian Red (warm)
    public static final Color INFO_COLOR = new Color(70, 130, 180);           // Steel Blue (professional)
    
    // Neutral Colors (2025 Design System)
    public static final Color BACKGROUND_PRIMARY = new Color(250, 248, 246);   // Warm off-white
    public static final Color BACKGROUND_SECONDARY = new Color(245, 242, 238); // Warm light gray
    public static final Color SURFACE_PRIMARY = new Color(255, 253, 250);      // Warm white surfaces
    public static final Color SURFACE_SECONDARY = new Color(248, 246, 243);    // Subtle warm gray
    public static final Color SURFACE_ELEVATED = Color.WHITE;                  // Pure white for cards
    
    // Border & Divider Colors (Warm Tones)
    public static final Color BORDER_LIGHT = new Color(230, 225, 218);        // Light warm borders
    public static final Color BORDER_MEDIUM = new Color(211, 203, 193);       // Medium warm borders
    public static final Color BORDER_DARK = new Color(169, 157, 143);         // Dark warm borders
    public static final Color BORDER_FOCUS = new Color(141, 98, 57);          // Focus state border
    
    // Text Colors (High Contrast for Accessibility)
    public static final Color TEXT_PRIMARY = new Color(41, 37, 36);           // Warm near-black
    public static final Color TEXT_SECONDARY = new Color(87, 83, 78);         // Warm dark gray
    public static final Color TEXT_TERTIARY = new Color(120, 113, 108);       // Warm medium gray
    public static final Color TEXT_INVERSE = Color.WHITE;                     // White text
    public static final Color TEXT_MUTED = new Color(156, 163, 175);          // Muted text
    
    // Legacy aliases for backward compatibility (Updated to new palette)
    public static final Color LIGHT_GRAY = BACKGROUND_SECONDARY;
    public static final Color MEDIUM_GRAY = BORDER_MEDIUM;
    public static final Color DARK_GRAY = TEXT_SECONDARY;
    public static final Color CARD_BACKGROUND = SURFACE_ELEVATED;
    
    // Additional Utility Colors for 2025 Design
    public static final Color HOVER_OVERLAY = new Color(0, 0, 0, 10);         // Subtle hover overlay
    public static final Color SHADOW_LIGHT = new Color(0, 0, 0, 5);           // Light shadow
    public static final Color SHADOW_MEDIUM = new Color(0, 0, 0, 15);         // Medium shadow
    public static final Color FOCUS_RING = new Color(141, 98, 57, 40);        // Focus ring color
    
    // Modern Fonts
    public static final Font TITLE_FONT = new Font(FONT_FAMILY, Font.BOLD, 28);
    public static final Font HEADER_FONT = new Font(FONT_FAMILY, Font.BOLD, 20);
    public static final Font NORMAL_FONT = new Font(FONT_FAMILY, Font.PLAIN, 14);
    public static final Font SMALL_FONT = new Font(FONT_FAMILY, Font.PLAIN, 12);
    public static final Font LARGE_FONT = new Font(FONT_FAMILY, Font.PLAIN, 16);
    public static final Font BUTTON_FONT = new Font(FONT_FAMILY, Font.BOLD, 14);
    
    /**
     * Create a modern 2025 styled button with primary color
     */
    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(TEXT_INVERSE);
        button.setBackground(PRIMARY_COLOR);
        button.setBorder(BorderFactory.createEmptyBorder(14, 28, 14, 28));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        
        // Modern 2025 shadow effect with new colors
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_MEDIUM, 1),
            BorderFactory.createEmptyBorder(14, 28, 14, 28)
        ));
        
        // Smooth hover transition with updated palette
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_HOVER);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_DARK, 1),
                    BorderFactory.createEmptyBorder(14, 28, 14, 28)
                ));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_MEDIUM, 1),
                    BorderFactory.createEmptyBorder(14, 28, 14, 28)
                ));
            }
        });
        
        return button;
    }
    
    /**
     * Create a modern 2025 styled button with success color
     */
    public static JButton createSuccessButton(String text) {
        JButton button = createPrimaryButton(text);
        button.setBackground(SUCCESS_COLOR);
        button.setForeground(TEXT_INVERSE);
        
        // Updated success button hover effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(22, 101, 22)); // Darker success
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(22, 101, 22), 1),
                    BorderFactory.createEmptyBorder(14, 28, 14, 28)
                ));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(SUCCESS_COLOR);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_MEDIUM, 1),
                    BorderFactory.createEmptyBorder(14, 28, 14, 28)
                ));
            }
        });
        
        return button;
    }
    
    /**
     * Create a modern 2025 styled button with danger color
     */
    public static JButton createDangerButton(String text) {
        JButton button = createPrimaryButton(text);
        button.setBackground(DANGER_COLOR);
        button.setForeground(TEXT_INVERSE);
        
        // Updated danger button hover effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(165, 55, 55)); // Darker danger
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(165, 55, 55), 1),
                    BorderFactory.createEmptyBorder(14, 28, 14, 28)
                ));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(DANGER_COLOR);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_MEDIUM, 1),
                    BorderFactory.createEmptyBorder(14, 28, 14, 28)
                ));
            }
        });
        
        return button;
    }
    
    /**
     * Create a modern 2025 styled button with warning color
     */
    public static JButton createWarningButton(String text) {
        JButton button = createPrimaryButton(text);
        button.setBackground(WARNING_COLOR);
        button.setForeground(TEXT_PRIMARY); // Dark text for better contrast
        
        // Updated warning button hover effects  
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(184, 134, 11)); // Darker warning
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(184, 134, 11), 1),
                    BorderFactory.createEmptyBorder(14, 28, 14, 28)
                ));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(WARNING_COLOR);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_MEDIUM, 1),
                    BorderFactory.createEmptyBorder(14, 28, 14, 28)
                ));
            }
        });
        
        return button;
    }
    
    /**
     * Create a modern 2025 secondary button (outline style)
     */
    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(PRIMARY_COLOR);
        button.setBackground(SURFACE_PRIMARY);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            BorderFactory.createEmptyBorder(12, 26, 12, 26)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
                button.setForeground(TEXT_INVERSE);
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(SURFACE_PRIMARY);
                button.setForeground(PRIMARY_COLOR);
            }
        });
        
        return button;
    }
    
    /**
     * Create a modern 2025 styled text field
     */
    public static JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setFont(NORMAL_FONT);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_MEDIUM, 1),
            BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));
        textField.setBackground(SURFACE_PRIMARY);
        textField.setForeground(TEXT_PRIMARY);
        
        // Modern focus effect with 2025 styling
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_FOCUS, 2),
                    BorderFactory.createEmptyBorder(13, 15, 13, 15)
                ));
                textField.setBackground(SURFACE_ELEVATED);
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_MEDIUM, 1),
                    BorderFactory.createEmptyBorder(14, 16, 14, 16)
                ));
                textField.setBackground(SURFACE_PRIMARY);
            }
        });
        
        return textField;
    }
    
    /**
     * Create a modern 2025 styled password field
     */
    public static JPasswordField createStyledPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(NORMAL_FONT);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_MEDIUM, 1),
            BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));
        passwordField.setBackground(SURFACE_PRIMARY);
        passwordField.setForeground(TEXT_PRIMARY);
        
        // Modern focus effect with 2025 styling
        passwordField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                passwordField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_FOCUS, 2),
                    BorderFactory.createEmptyBorder(13, 15, 13, 15)
                ));
                passwordField.setBackground(SURFACE_ELEVATED);
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                passwordField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_MEDIUM, 1),
                    BorderFactory.createEmptyBorder(14, 16, 14, 16)
                ));
                passwordField.setBackground(SURFACE_PRIMARY);
            }
        });
        
        return passwordField;
    }
    
    /**
     * Create a modern 2025 styled text area with focus effects
     */
    public static JTextArea createStyledTextArea(int rows, int cols) {
        JTextArea textArea = new JTextArea(rows, cols);
        textArea.setFont(NORMAL_FONT);
        textArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_MEDIUM, 1),
            BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(SURFACE_PRIMARY);
        textArea.setForeground(TEXT_PRIMARY);
        
        // Add focus effects like other inputs
        textArea.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                textArea.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_FOCUS, 2),
                    BorderFactory.createEmptyBorder(13, 15, 13, 15)
                ));
                textArea.setBackground(SURFACE_ELEVATED);
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                textArea.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_MEDIUM, 1),
                    BorderFactory.createEmptyBorder(14, 16, 14, 16)
                ));
                textArea.setBackground(SURFACE_PRIMARY);
            }
        });
        
        return textArea;
    }
    
    /**
     * Create a modern 2025 styled combo box with enhanced appearance
     */
    public static <T> JComboBox<T> createStyledComboBox() {
        JComboBox<T> comboBox = new JComboBox<>();
        comboBox.setFont(NORMAL_FONT);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_MEDIUM, 1),
            BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));
        comboBox.setBackground(SURFACE_PRIMARY);
        comboBox.setForeground(TEXT_PRIMARY);
        return comboBox;
    }
    
    /**
     * Create a modern styled label
     */
    public static JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(NORMAL_FONT);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }
    
    /**
     * Create a modern title label
     */
    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(TITLE_FONT);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }
    
    /**
     * Create a modern header label
     */
    public static JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(HEADER_FONT);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }
    
    /**
     * Create a secondary text label
     */
    public static JLabel createSecondaryLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(NORMAL_FONT);
        label.setForeground(TEXT_SECONDARY);
        return label;
    }
    
    /**
     * Show error message dialog
     */
    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Show success message dialog
     */
    public static void showSuccess(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Show warning message dialog
     */
    public static void showWarning(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }
    
    /**
     * Show confirmation dialog
     */
    public static boolean showConfirmation(Component parent, String message, String title) {
        int result = JOptionPane.showConfirmDialog(
            parent, 
            message, 
            title, 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        return result == JOptionPane.YES_OPTION;
    }
    
    /**
     * Center component on screen
     */
    public static void centerOnScreen(Component component) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension componentSize = component.getSize();
        int x = (screenSize.width - componentSize.width) / 2;
        int y = (screenSize.height - componentSize.height) / 2;
        component.setLocation(x, y);
    }
    
    /**
     * Apply modern 2025 card styling with enhanced visual hierarchy
     */
    public static void styleAsCard(JPanel panel) {
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_LIGHT, 1),
            BorderFactory.createEmptyBorder(24, 24, 24, 24)
        ));
        panel.setBackground(SURFACE_ELEVATED);
    }
    
    /**
     * Apply elevated card styling for prominent components
     */
    public static void styleAsElevatedCard(JPanel panel) {
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_LIGHT, 1),
                BorderFactory.createLineBorder(SHADOW_LIGHT, 1)
            ),
            BorderFactory.createEmptyBorder(24, 24, 24, 24)
        ));
        panel.setBackground(SURFACE_ELEVATED);
    }
    
    /**
     * Create a modern 2025 menu item card with enhanced coffee shop branding
     */
    public static JPanel createMenuItemCard(String name, String description, String price, String imagePath) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(SURFACE_ELEVATED);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_LIGHT, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setPreferredSize(new Dimension(320, 220));
        
        // Image panel with warm styling
        JPanel imagePanel = new JPanel();
        imagePanel.setBackground(BACKGROUND_SECONDARY);
        imagePanel.setPreferredSize(new Dimension(100, 100));
        imagePanel.setBorder(BorderFactory.createLineBorder(BORDER_MEDIUM, 1));
        
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                ImageIcon imageIcon = loadImageIcon(imagePath, 100, 100);
                if (imageIcon != null) {
                    JLabel imageLabel = new JLabel(imageIcon);
                    imagePanel.add(imageLabel);
                } else {
                    imagePanel.add(createPlaceholderImageLabel(100, 100));
                }
            } catch (Exception e) {
                imagePanel.add(createPlaceholderImageLabel(100, 100));
            }
        } else {
            imagePanel.add(createPlaceholderImageLabel(100, 100));
        }
        
        // Content panel with enhanced spacing and colors
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(SURFACE_ELEVATED);
        
        JLabel nameLabel = createHeaderLabel(name);
        nameLabel.setForeground(PRIMARY_COLOR); // Rich espresso brown for names
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel descLabel = createSecondaryLabel("<html>" + (description.length() > 60 ? 
            description.substring(0, 60) + "..." : description) + "</html>");
        descLabel.setForeground(TEXT_SECONDARY); // Consistent secondary text
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel priceLabel = createStyledLabel(price);
        priceLabel.setFont(LARGE_FONT.deriveFont(Font.BOLD));
        priceLabel.setForeground(ACCENT_COLOR); // Golden caramel for prices
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        contentPanel.add(nameLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(descLabel);
        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(priceLabel);
        
        // Main layout
        // Main panel layout with consistent background
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(SURFACE_ELEVATED);
        mainContent.add(imagePanel, BorderLayout.WEST);
        mainContent.add(Box.createHorizontalStrut(20), BorderLayout.CENTER);
        mainContent.add(contentPanel, BorderLayout.CENTER);
        
        card.add(mainContent, BorderLayout.CENTER);
        
        return card;
    }
    
    /**
     * Load and resize an image icon
     */
    public static ImageIcon loadImageIcon(String imagePath, int width, int height) {
        try {
            java.io.File imageFile = new java.io.File(imagePath);
            if (imageFile.exists()) {
                ImageIcon originalIcon = new ImageIcon(imagePath);
                Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            }
        } catch (Exception e) {
            // Return null if image cannot be loaded
        }
        return null;
    }
    
    /**
     * Create a modern 2025 placeholder with coffee shop theming
     */
    public static JLabel createPlaceholderImageLabel(int width, int height) {
        JLabel placeholder = new JLabel("☕");
        placeholder.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        placeholder.setHorizontalAlignment(SwingConstants.CENTER);
        placeholder.setVerticalAlignment(SwingConstants.CENTER);
        placeholder.setPreferredSize(new Dimension(width, height));
        placeholder.setOpaque(true);
        placeholder.setBackground(BACKGROUND_SECONDARY);
        placeholder.setForeground(ACCENT_COLOR); // Golden coffee emoji
        placeholder.setBorder(BorderFactory.createLineBorder(BORDER_MEDIUM, 1));
        return placeholder;
    }
    
    /**
     * Create a modern 2025 image selection button with enhanced styling
     */
    public static JButton createImageSelectButton() {
        JButton button = new JButton("📷 Select Image");
        button.setFont(NORMAL_FONT);
        button.setBackground(SURFACE_SECONDARY);
        button.setForeground(TEXT_PRIMARY);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_MEDIUM, 1),
            BorderFactory.createEmptyBorder(10, 16, 10, 16)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Enhanced hover effect with coffee shop theming
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_LIGHT);
                button.setForeground(TEXT_INVERSE);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                    BorderFactory.createEmptyBorder(10, 16, 10, 16)
                ));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(SURFACE_SECONDARY);
                button.setForeground(TEXT_PRIMARY);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_MEDIUM, 1),
                    BorderFactory.createEmptyBorder(10, 16, 10, 16)
                ));
            }
        });
        
        return button;
    }
    
    /**
     * Open file chooser for image selection
     */
    public static String selectImageFile(Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Image Files", "jpg", "jpeg", "png", "gif", "bmp"));
        
        int result = fileChooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }
}
