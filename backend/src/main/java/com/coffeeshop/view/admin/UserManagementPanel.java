package com.coffeeshop.view.admin;

import com.coffeeshop.model.User;
import com.coffeeshop.service.UserService;
import com.coffeeshop.util.UIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * User Management Panel for admin users
 */
public class UserManagementPanel extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(UserManagementPanel.class);
    
    private UserService userService;
    
    // UI Components
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JButton toggleStatusButton;
    
    // Form components
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JComboBox<User.Role> roleCombo;
    private JCheckBox activeCheckbox;
    
    public UserManagementPanel() {
        this.userService = new UserService();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
        loadUsers();
        
        logger.info("User Management Panel initialized");
    }
    
    private void initializeComponents() {
        // Table setup
        String[] columnNames = {"ID", "Username", "Full Name", "Email", "Role", "Active", "Created"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.getColumnModel().getColumn(0).setMaxWidth(50);
        userTable.getColumnModel().getColumn(4).setMaxWidth(80);
        userTable.getColumnModel().getColumn(5).setMaxWidth(60);
        
        // Buttons
        addButton = UIUtils.createSuccessButton("Add User");
        editButton = UIUtils.createPrimaryButton("Edit User");
        deleteButton = UIUtils.createDangerButton("Delete User");
        refreshButton = UIUtils.createPrimaryButton("Refresh");
        toggleStatusButton = UIUtils.createPrimaryButton("Toggle Status");
        
        // Form components
        usernameField = UIUtils.createStyledTextField();
        passwordField = UIUtils.createStyledPasswordField();
        fullNameField = UIUtils.createStyledTextField();
        emailField = UIUtils.createStyledTextField();
        roleCombo = UIUtils.createStyledComboBox();
        activeCheckbox = new JCheckBox("Active");
        
        // Populate role combo
        for (User.Role role : User.Role.values()) {
            roleCombo.addItem(role);
        }
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Top panel with refresh button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(refreshButton);
        
        // Center panel with table
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("System Users"));
        
        // Bottom panel with buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(toggleStatusButton);
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        addButton.addActionListener(e -> showAddDialog());
        editButton.addActionListener(e -> showEditDialog());
        deleteButton.addActionListener(e -> deleteSelectedUser());
        refreshButton.addActionListener(e -> loadUsers());
        toggleStatusButton.addActionListener(e -> toggleUserStatus());
        
        // Enable/disable buttons based on selection
        userTable.getSelectionModel().addListSelectionListener(e -> {
            boolean hasSelection = userTable.getSelectedRow() >= 0;
            editButton.setEnabled(hasSelection);
            deleteButton.setEnabled(hasSelection);
            toggleStatusButton.setEnabled(hasSelection);
        });
        
        // Initial state
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        toggleStatusButton.setEnabled(false);
    }
    
    private void setupFrame() {
        setTitle("User Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        UIUtils.setResponsiveSize(this, 1000, 600);
        setResizable(true);
        UIUtils.centerOnScreen(this);
    }
    
    private void loadUsers() {
        try {
            List<User> users = userService.getAllUsers();
            tableModel.setRowCount(0);
            
            for (User user : users) {
                Object[] row = {
                    user.getId(),
                    user.getUsername(),
                    user.getFullName(),
                    user.getEmail(),
                    user.getRole(),
                    user.isActive() ? "Yes" : "No",
                    user.getCreatedAt() != null ? user.getCreatedAt().toLocalDate() : ""
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            logger.error("Error loading users", e);
            UIUtils.showError(this, "Error loading users: " + e.getMessage());
        }
    }
    
    private void showAddDialog() {
        clearForm();
        int result = showUserDialog("Add User");
        if (result == JOptionPane.OK_OPTION) {
            try {
                User user = createUserFromForm();
                userService.createUser(user);
                loadUsers();
                UIUtils.showSuccess(this, "User added successfully!");
            } catch (Exception e) {
                logger.error("Error adding user", e);
                UIUtils.showError(this, "Error adding user: " + e.getMessage());
            }
        }
    }
    
    private void showEditDialog() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow < 0) return;
        
        try {
            Long userId = (Long) tableModel.getValueAt(selectedRow, 0);
            User user = userService.getUserById(userId);
            populateForm(user);
            
            int result = showUserDialog("Edit User");
            if (result == JOptionPane.OK_OPTION) {
                User updatedUser = createUserFromForm();
                updatedUser.setId(userId);
                userService.updateUser(updatedUser);
                loadUsers();
                UIUtils.showSuccess(this, "User updated successfully!");
            }
        } catch (Exception e) {
            logger.error("Error editing user", e);
            UIUtils.showError(this, "Error editing user: " + e.getMessage());
        }
    }
    
    private void deleteSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow < 0) return;
        
        String username = (String) tableModel.getValueAt(selectedRow, 1);
        if (UIUtils.showConfirmation(this, 
                "Are you sure you want to delete user '" + username + "'?", 
                "Confirm Delete")) {
            try {
                Long userId = (Long) tableModel.getValueAt(selectedRow, 0);
                userService.deleteUser(userId);
                loadUsers();
                UIUtils.showSuccess(this, "User deleted successfully!");
            } catch (Exception e) {
                logger.error("Error deleting user", e);
                UIUtils.showError(this, "Error deleting user: " + e.getMessage());
            }
        }
    }
    
    private void toggleUserStatus() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow < 0) return;
        
        try {
            Long userId = (Long) tableModel.getValueAt(selectedRow, 0);
            String currentStatus = (String) tableModel.getValueAt(selectedRow, 5);
            boolean newStatus = !"Yes".equals(currentStatus);
            
            userService.updateUserStatus(userId, newStatus);
            loadUsers();
            UIUtils.showSuccess(this, "User status updated successfully!");
        } catch (Exception e) {
            logger.error("Error updating user status", e);
            UIUtils.showError(this, "Error updating user status: " + e.getMessage());
        }
    }
    
    private int showUserDialog(String title) {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(usernameField, gbc);
        
        // Password (only show for new users)
        if (title.contains("Add")) {
            gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
            formPanel.add(new JLabel("Password:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
            formPanel.add(passwordField, gbc);
        }
        
        // Full Name
        gbc.gridx = 0; gbc.gridy = title.contains("Add") ? 2 : 1; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(fullNameField, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = title.contains("Add") ? 3 : 2; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(emailField, gbc);
        
        // Role
        gbc.gridx = 0; gbc.gridy = title.contains("Add") ? 4 : 3; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(roleCombo, gbc);
        
        // Active
        gbc.gridx = 0; gbc.gridy = title.contains("Add") ? 5 : 4; gbc.gridwidth = 2;
        formPanel.add(activeCheckbox, gbc);
        
        return JOptionPane.showConfirmDialog(this, formPanel, title, 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    }
    
    private void clearForm() {
        usernameField.setText("");
        passwordField.setText("");
        fullNameField.setText("");
        emailField.setText("");
        roleCombo.setSelectedIndex(0);
        activeCheckbox.setSelected(true);
    }
    
    private void populateForm(User user) {
        usernameField.setText(user.getUsername());
        passwordField.setText(""); // Don't populate password
        fullNameField.setText(user.getFullName());
        emailField.setText(user.getEmail());
        roleCombo.setSelectedItem(user.getRole());
        activeCheckbox.setSelected(user.isActive());
    }
    
    private User createUserFromForm() throws IllegalArgumentException {
        String username = usernameField.getText().trim();
        if (username.isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        
        String password = new String(passwordField.getPassword());
        String fullName = fullNameField.getText().trim();
        if (fullName.isEmpty()) {
            throw new IllegalArgumentException("Full name is required");
        }
        
        String email = emailField.getText().trim();
        User.Role role = (User.Role) roleCombo.getSelectedItem();
        
        User user = new User(username, password, role, fullName);
        user.setEmail(email);
        user.setActive(activeCheckbox.isSelected());
        
        return user;
    }
}
