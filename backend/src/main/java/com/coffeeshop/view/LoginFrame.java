package com.coffeeshop.view;

import com.coffeeshop.model.User;
import com.coffeeshop.service.UserService;
import com.coffeeshop.util.UIUtils;
import com.coffeeshop.view.admin.AdminDashboard;
import com.coffeeshop.view.waiter.WaiterDashboard;
import com.coffeeshop.view.kitchen.KitchenDashboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Login frame for user authentication
 */
public class LoginFrame extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(LoginFrame.class);
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;
    private UserService userService;
    
    public LoginFrame() {
        this.userService = new UserService();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
    }
    
    private void initializeComponents() {
        // Create components
        usernameField = UIUtils.createStyledTextField();
        usernameField.setColumns(20);
        
        passwordField = UIUtils.createStyledPasswordField();
        passwordField.setColumns(20);
        
        loginButton = UIUtils.createPrimaryButton("Login");
        statusLabel = new JLabel(" ");
        statusLabel.setFont(UIUtils.SMALL_FONT);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIUtils.LIGHT_GRAY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(UIUtils.LIGHT_GRAY);
        JLabel titleLabel = UIUtils.createTitleLabel("Coffee Shop Management");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(titleLabel);
        
        // Login form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        UIUtils.styleAsCard(formPanel);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Form title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel formTitle = UIUtils.createHeaderLabel("Login");
        formPanel.add(formTitle, gbc);
        
        // Username
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(UIUtils.createStyledLabel("Username:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(usernameField, gbc);
        
        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(UIUtils.createStyledLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(passwordField, gbc);
        
        // Login button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(loginButton, gbc);
        
        // Status label
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(statusLabel, gbc);
        
        // Instructions panel
        JPanel instructionsPanel = new JPanel();
        instructionsPanel.setBackground(UIUtils.LIGHT_GRAY);
        JLabel instructionsLabel = new JLabel("<html><center>Default Login Credentials:<br/>" +
            "Admin: admin / admin123<br/>" +
            "Waiter: waiter1 / admin123<br/>" +
            "Kitchen: kitchen1 / admin123</center></html>");
        instructionsLabel.setFont(UIUtils.SMALL_FONT);
        instructionsLabel.setForeground(Color.GRAY);
        instructionsPanel.add(instructionsLabel);
        
        // Add panels to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(instructionsPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        // Login button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
        
        // Enter key listeners
        KeyListener enterKeyListener = new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {}
            
            @Override
            public void keyTyped(KeyEvent e) {}
        };
        
        usernameField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);
    }
    
    private void setupFrame() {
        setTitle("Coffee Shop Management - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setResizable(true);
        setMinimumSize(new Dimension(400, 500));
        UIUtils.centerOnScreen(this);
        
        // Add fullscreen toggle with F11 key
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0), "fullscreen");
        getRootPane().getActionMap().put("fullscreen", new AbstractAction() {
            private Rectangle normalBounds;
            private boolean isFullscreen = false;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isFullscreen) {
                    normalBounds = getBounds();
                    setExtendedState(Frame.MAXIMIZED_BOTH);
                    isFullscreen = true;
                } else {
                    setExtendedState(Frame.NORMAL);
                    setBounds(normalBounds);
                    isFullscreen = false;
                }
            }
        });
        
        // Set focus to username field
        SwingUtilities.invokeLater(() -> usernameField.requestFocus());
    }
    
    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            setStatus("Please enter username and password", Color.RED);
            return;
        }
        
        // Disable login button during authentication
        loginButton.setEnabled(false);
        setStatus("Authenticating...", Color.BLUE);
        
        // Perform authentication in background thread
        SwingWorker<User, Void> worker = new SwingWorker<User, Void>() {
            @Override
            protected User doInBackground() throws Exception {
                return userService.authenticate(username, password);
            }
            
            @Override
            protected void done() {
                try {
                    User user = get();
                    onLoginSuccess(user);
                } catch (Exception e) {
                    onLoginFailure(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
                } finally {
                    loginButton.setEnabled(true);
                }
            }
        };
        
        worker.execute();
    }
    
    private void onLoginSuccess(User user) {
        logger.info("Login successful for user: {} with role: {}", user.getUsername(), user.getRole());
        setStatus("Login successful!", UIUtils.SUCCESS_COLOR);
        
        // Hide login frame
        setVisible(false);
        
        // Open appropriate dashboard based on user role
        SwingUtilities.invokeLater(() -> {
            try {
                switch (user.getRole()) {
                    case ADMIN:
                        new AdminDashboard().setVisible(true);
                        break;
                    case WAITER:
                        new WaiterDashboard().setVisible(true);
                        break;
                    case KITCHEN:
                        new KitchenDashboard().setVisible(true);
                        break;
                }
                
            } catch (Exception e) {
                logger.error("Error opening dashboard", e);
                UIUtils.showError(this, "Error opening dashboard: " + e.getMessage());
                setVisible(true); // Show login frame again
            }
        });
    }
    
    private void onLoginFailure(String errorMessage) {
        logger.warn("Login failed: {}", errorMessage);
        setStatus(errorMessage, Color.RED);
        passwordField.setText("");
        passwordField.requestFocus();
    }
    
    private void setStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }
}
