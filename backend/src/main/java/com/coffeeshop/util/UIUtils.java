package com.coffeeshop.util;

import javax.swing.*;
import java.awt.*;

/**
 * Utility class for common UI operations and styling
 */
public class UIUtils {
    
    // Color scheme
    public static final Color PRIMARY_COLOR = new Color(70, 130, 180);
    public static final Color SECONDARY_COLOR = new Color(176, 196, 222);
    public static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    public static final Color WARNING_COLOR = new Color(255, 193, 7);
    public static final Color DANGER_COLOR = new Color(220, 53, 69);
    public static final Color LIGHT_GRAY = new Color(248, 249, 250);
    public static final Color DARK_GRAY = new Color(52, 58, 64);
    
    // Fonts
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    
    /**
     * Create a styled button with primary color
     */
    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(NORMAL_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR.darker());
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
        
        return button;
    }
    
    /**
     * Create a styled button with success color
     */
    public static JButton createSuccessButton(String text) {
        JButton button = createPrimaryButton(text);
        button.setBackground(SUCCESS_COLOR);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(SUCCESS_COLOR.darker());
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(SUCCESS_COLOR);
            }
        });
        
        return button;
    }
    
    /**
     * Create a styled button with danger color
     */
    public static JButton createDangerButton(String text) {
        JButton button = createPrimaryButton(text);
        button.setBackground(DANGER_COLOR);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(DANGER_COLOR.darker());
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(DANGER_COLOR);
            }
        });
        
        return button;
    }
    
    /**
     * Create a styled text field
     */
    public static JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setFont(NORMAL_FONT);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return textField;
    }
    
    /**
     * Create a styled password field
     */
    public static JPasswordField createStyledPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(NORMAL_FONT);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return passwordField;
    }
    
    /**
     * Create a styled text area
     */
    public static JTextArea createStyledTextArea(int rows, int cols) {
        JTextArea textArea = new JTextArea(rows, cols);
        textArea.setFont(NORMAL_FONT);
        textArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        return textArea;
    }
    
    /**
     * Create a styled combo box
     */
    public static <T> JComboBox<T> createStyledComboBox() {
        JComboBox<T> comboBox = new JComboBox<>();
        comboBox.setFont(NORMAL_FONT);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        return comboBox;
    }
    
    /**
     * Create a styled label
     */
    public static JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(NORMAL_FONT);
        return label;
    }
    
    /**
     * Create a title label
     */
    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(TITLE_FONT);
        label.setForeground(DARK_GRAY);
        return label;
    }
    
    /**
     * Create a header label
     */
    public static JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(HEADER_FONT);
        label.setForeground(DARK_GRAY);
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
     * Apply card-like styling to panel
     */
    public static void styleAsCard(JPanel panel) {
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        panel.setBackground(Color.WHITE);
    }
}
