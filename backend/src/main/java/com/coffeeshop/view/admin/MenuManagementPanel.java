package com.coffeeshop.view.admin;

import com.coffeeshop.dao.MenuItemDAO;
import com.coffeeshop.dao.impl.MenuItemDAOImpl;
import com.coffeeshop.model.Category;
import com.coffeeshop.model.MenuItem;
import com.coffeeshop.service.MenuService;
import com.coffeeshop.util.UIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Menu Management Panel for admin users
 */
public class MenuManagementPanel extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(MenuManagementPanel.class);
    
    private MenuService menuService;
    
    // UI Components
    private JTable menuTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JButton clearSearchButton;
    private JTextField searchField;
    private JComboBox<Category> categoryFilter;
    
    // Form components
    private JTextField nameField;
    private JComboBox<Category> categoryCombo;
    private JTextArea descriptionArea;
    private JTextField priceField;
    private JCheckBox availableCheckbox;
    private JTextField preparationTimeField;
    private JTextField imagePathField;
    private JButton imageSelectButton;
    private JLabel imagePreviewLabel;
    
    public MenuManagementPanel() {
        this.menuService = new MenuService();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
        loadMenuItems();
        
        logger.info("Menu Management Panel initialized");
    }
    
    private void initializeComponents() {
        // Table setup
        String[] columnNames = {"ID", "Name", "Category", "Description", "Price", "Available", "Prep Time"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        menuTable = new JTable(tableModel);
        menuTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menuTable.getColumnModel().getColumn(0).setMaxWidth(50);
        menuTable.getColumnModel().getColumn(4).setMaxWidth(80);
        menuTable.getColumnModel().getColumn(5).setMaxWidth(80);
        menuTable.getColumnModel().getColumn(6).setMaxWidth(80);
        
        // Buttons
        addButton = UIUtils.createSuccessButton("Add Item");
        editButton = UIUtils.createPrimaryButton("Edit Item");
        deleteButton = UIUtils.createDangerButton("Delete Item");
        refreshButton = UIUtils.createPrimaryButton("Refresh");
        clearSearchButton = UIUtils.createSecondaryButton("Clear");
        
        // Search and filter components
        searchField = UIUtils.createStyledTextField();
        searchField.setColumns(20);
        searchField.setToolTipText("Type to search menu items by name");
        categoryFilter = UIUtils.createStyledComboBox();
        categoryFilter.setToolTipText("Filter by category");
        
        // Form components
        nameField = UIUtils.createStyledTextField();
        categoryCombo = UIUtils.createStyledComboBox();
        descriptionArea = UIUtils.createStyledTextArea(3, 20);
        priceField = UIUtils.createStyledTextField();
        availableCheckbox = new JCheckBox("Available");
        preparationTimeField = UIUtils.createStyledTextField();
        
        // Image components
        imagePathField = UIUtils.createStyledTextField();
        imagePathField.setEditable(false);
        imageSelectButton = UIUtils.createImageSelectButton();
        imagePreviewLabel = UIUtils.createPlaceholderImageLabel(80, 80);
        
        // Image selection event
        imageSelectButton.addActionListener(e -> {
            String imagePath = UIUtils.selectImageFile(this);
            if (imagePath != null) {
                imagePathField.setText(imagePath);
                updateImagePreview(imagePath);
            }
        });
        
        // Load categories
        loadCategories();
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Top panel with search and filters
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(clearSearchButton);
        topPanel.add(new JLabel("Category:"));
        topPanel.add(categoryFilter);
        topPanel.add(refreshButton);
        
        // Center panel with table
        JScrollPane scrollPane = new JScrollPane(menuTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Menu Items"));
        
        // Bottom panel with buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        addButton.addActionListener(e -> showAddDialog());
        editButton.addActionListener(e -> showEditDialog());
        deleteButton.addActionListener(e -> deleteSelectedItem());
        refreshButton.addActionListener(e -> loadMenuItems());
        
        // Clear search functionality
        clearSearchButton.addActionListener(e -> clearSearch());
        
        // Search functionality
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                filterMenuItems();
            }
        });
        
        // Category filter functionality
        categoryFilter.addActionListener(e -> filterMenuItems());
        
        // Enable/disable edit and delete buttons based on selection
        menuTable.getSelectionModel().addListSelectionListener(e -> {
            boolean hasSelection = menuTable.getSelectedRow() >= 0;
            editButton.setEnabled(hasSelection);
            deleteButton.setEnabled(hasSelection);
        });
        
        // Initial state
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }
    
    private void setupFrame() {
        setTitle("Menu Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        UIUtils.setResponsiveSize(this, 1000, 700);
        setResizable(true);
        UIUtils.centerOnScreen(this);
    }
    
    private void loadCategories() {
        try {
            List<Category> categories = menuService.getAllCategories();
            categoryFilter.removeAllItems();
            categoryCombo.removeAllItems();
            
            categoryFilter.addItem(null); // For "All Categories"
            
            for (Category category : categories) {
                categoryFilter.addItem(category);
                categoryCombo.addItem(category);
            }
        } catch (Exception e) {
            logger.error("Error loading categories", e);
            UIUtils.showError(this, "Error loading categories: " + e.getMessage());
        }
    }
    
    private void loadMenuItems() {
        try {
            List<MenuItem> menuItems = menuService.getAllMenuItems();
            tableModel.setRowCount(0);
            
            for (MenuItem item : menuItems) {
                Object[] row = {
                    item.getId(),
                    item.getName(),
                    item.getCategoryName(),
                    item.getDescription(),
                    "$" + item.getPrice(),
                    item.isAvailable() ? "Yes" : "No",
                    item.getPreparationTime() + " min"
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            logger.error("Error loading menu items", e);
            UIUtils.showError(this, "Error loading menu items: " + e.getMessage());
        }
    }
    
    private void showAddDialog() {
        clearForm();
        int result = showItemDialog("Add Menu Item");
        if (result == JOptionPane.OK_OPTION) {
            try {
                MenuItem item = createMenuItemFromForm();
                menuService.createMenuItem(item);
                loadMenuItems();
                UIUtils.showSuccess(this, "Menu item added successfully!");
            } catch (Exception e) {
                logger.error("Error adding menu item", e);
                UIUtils.showError(this, "Error adding menu item: " + e.getMessage());
            }
        }
    }
    
    private void showEditDialog() {
        int selectedRow = menuTable.getSelectedRow();
        if (selectedRow < 0) return;
        
        try {
            Long itemId = (Long) tableModel.getValueAt(selectedRow, 0);
            MenuItem item = menuService.getMenuItemById(itemId);
            populateForm(item);
            
            int result = showItemDialog("Edit Menu Item");
            if (result == JOptionPane.OK_OPTION) {
                MenuItem updatedItem = createMenuItemFromForm();
                updatedItem.setId(itemId);
                menuService.updateMenuItem(updatedItem);
                loadMenuItems();
                UIUtils.showSuccess(this, "Menu item updated successfully!");
            }
        } catch (Exception e) {
            logger.error("Error editing menu item", e);
            UIUtils.showError(this, "Error editing menu item: " + e.getMessage());
        }
    }
    
    private void deleteSelectedItem() {
        int selectedRow = menuTable.getSelectedRow();
        if (selectedRow < 0) return;
        
        String itemName = (String) tableModel.getValueAt(selectedRow, 1);
        if (UIUtils.showConfirmation(this, 
                "Are you sure you want to delete '" + itemName + "'?", 
                "Confirm Delete")) {
            try {
                Long itemId = (Long) tableModel.getValueAt(selectedRow, 0);
                menuService.deleteMenuItem(itemId);
                loadMenuItems();
                UIUtils.showSuccess(this, "Menu item deleted successfully!");
            } catch (Exception e) {
                logger.error("Error deleting menu item", e);
                UIUtils.showError(this, "Error deleting menu item: " + e.getMessage());
            }
        }
    }
    
    private int showItemDialog(String title) {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(nameField, gbc);
        
        // Category
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(categoryCombo, gbc);
        
        // Description
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(new JScrollPane(descriptionArea), gbc);
        
        // Price
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(priceField, gbc);
        
        // Preparation Time
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Prep Time (min):"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(preparationTimeField, gbc);
        
        // Available
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        formPanel.add(availableCheckbox, gbc);
        
        // Image
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Image:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JPanel imagePanel = new JPanel(new BorderLayout(5, 5));
        imagePanel.add(imagePathField, BorderLayout.CENTER);
        imagePanel.add(imageSelectButton, BorderLayout.EAST);
        formPanel.add(imagePanel, gbc);
        
        // Image preview
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(imagePreviewLabel, gbc);
        
        return JOptionPane.showConfirmDialog(this, formPanel, title, 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    }
    
    private void clearForm() {
        nameField.setText("");
        categoryCombo.setSelectedIndex(0);
        descriptionArea.setText("");
        priceField.setText("");
        preparationTimeField.setText("0");
        availableCheckbox.setSelected(true);
        imagePathField.setText("");
        imagePreviewLabel.setIcon(null);
        imagePreviewLabel.setText("📷");
        imagePreviewLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
    }
    
    private void populateForm(MenuItem item) {
        nameField.setText(item.getName());
        
        // Find and select category
        for (int i = 0; i < categoryCombo.getItemCount(); i++) {
            Category category = categoryCombo.getItemAt(i);
            if (category != null && category.getId().equals(item.getCategoryId())) {
                categoryCombo.setSelectedIndex(i);
                break;
            }
        }
        
        descriptionArea.setText(item.getDescription());
        priceField.setText(item.getPrice().toString());
        preparationTimeField.setText(String.valueOf(item.getPreparationTime()));
        availableCheckbox.setSelected(item.isAvailable());
        
        // Update image
        String imagePath = item.getImagePath();
        if (imagePath != null && !imagePath.isEmpty()) {
            imagePathField.setText(imagePath);
            updateImagePreview(imagePath);
        } else {
            imagePathField.setText("");
            imagePreviewLabel.setIcon(null);
            imagePreviewLabel.setText("📷");
        }
    }
    
    private MenuItem createMenuItemFromForm() throws IllegalArgumentException {
        try {
            Category selectedCategory = (Category) categoryCombo.getSelectedItem();
            if (selectedCategory == null) {
                throw new IllegalArgumentException("Please select a category");
            }
            
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Name is required");
            }
            
            String description = descriptionArea.getText().trim();
            
            BigDecimal price = new BigDecimal(priceField.getText().trim());
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Price must be greater than 0");
            }
            
            int prepTime = Integer.parseInt(preparationTimeField.getText().trim());
            if (prepTime < 0) {
                throw new IllegalArgumentException("Preparation time cannot be negative");
            }
            
            MenuItem item = new MenuItem(name, selectedCategory.getId(), description, price);
            item.setPreparationTime(prepTime);
            item.setAvailable(availableCheckbox.isSelected());
            
            // Set image path if provided
            String imagePath = imagePathField.getText().trim();
            if (!imagePath.isEmpty()) {
                item.setImagePath(imagePath);
            }
            
            return item;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format in price or preparation time");
        }
    }
    
    /**
     * Update the image preview label with the selected image
     */
    private void updateImagePreview(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            ImageIcon icon = UIUtils.loadImageIcon(imagePath, 80, 80);
            if (icon != null) {
                imagePreviewLabel.setIcon(icon);
                imagePreviewLabel.setText("");
            } else {
                imagePreviewLabel.setIcon(null);
                imagePreviewLabel.setText("❌");
                imagePreviewLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
            }
        } else {
            imagePreviewLabel.setIcon(null);
            imagePreviewLabel.setText("📷");
            imagePreviewLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        }
    }
    
    private void filterMenuItems() {
        try {
            String searchText = searchField.getText().trim();
            Category selectedCategory = (Category) categoryFilter.getSelectedItem();
            
            List<MenuItem> menuItems;
            
            // If search text is provided, use search functionality
            if (!searchText.isEmpty()) {
                menuItems = menuService.searchMenuItems(searchText);
            } else {
                menuItems = menuService.getAllMenuItems();
            }
            
            // Filter by category if selected
            if (selectedCategory != null) {
                menuItems = menuItems.stream()
                    .filter(item -> selectedCategory.getId().equals(item.getCategoryId()))
                    .collect(java.util.stream.Collectors.toList());
            }
            
            // Update table
            tableModel.setRowCount(0);
            for (MenuItem item : menuItems) {
                Object[] row = {
                    item.getId(),
                    item.getName(),
                    item.getCategoryName(),
                    item.getDescription(),
                    "$" + item.getPrice(),
                    item.isAvailable() ? "Yes" : "No",
                    item.getPreparationTime() + " min"
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            logger.error("Error filtering menu items", e);
            UIUtils.showError(this, "Error filtering menu items: " + e.getMessage());
        }
    }
    
    private void clearSearch() {
        searchField.setText("");
        categoryFilter.setSelectedIndex(0); // Select "All Categories"
        loadMenuItems(); // Reload all items
    }
}
