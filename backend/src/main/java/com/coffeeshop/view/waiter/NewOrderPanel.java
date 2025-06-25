package com.coffeeshop.view.waiter;

import com.coffeeshop.model.*;
import com.coffeeshop.service.*;
import com.coffeeshop.util.UIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * New Order Panel for waiter users
 */
public class NewOrderPanel extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(NewOrderPanel.class);
    
    private final OrderService orderService;
    private final MenuService menuService;
    private final TableService tableService;
    private final AuthenticationService authService;
    
    // UI Components
    private JComboBox<Table> tableComboBox;
    private JTextField customerNameField;
    private JComboBox<Category> categoryComboBox;
    private JList<com.coffeeshop.model.MenuItem> menuItemsList;
    private JSpinner quantitySpinner;
    private JTextField specialInstructionsField;
    private JTable orderItemsTable;
    private DefaultTableModel orderItemsModel;
    private JLabel totalLabel;
    private JTextArea notesArea;
    private JButton addItemButton;
    private JButton removeItemButton;
    private JButton saveOrderButton;
    private JButton cancelButton;
    
    private List<OrderItem> orderItems;
    private BigDecimal totalAmount;
    
    public NewOrderPanel() {
        this.orderService = new OrderService();
        this.menuService = new MenuService();
        this.tableService = new TableService();
        this.authService = AuthenticationService.getInstance();
        this.orderItems = new ArrayList<>();
        this.totalAmount = BigDecimal.ZERO;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
        loadData();
        
        logger.info("New Order Panel initialized");
    }
    
    private void initializeComponents() {
        // Table selection
        tableComboBox = new JComboBox<>();
        tableComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Table) {
                    Table table = (Table) value;
                    setText(table.getTableNumber() + " (" + table.getCapacity() + " seats)");
                }
                return this;
            }
        });
        
        // Customer info
        customerNameField = new JTextField(20);
        
        // Menu selection
        categoryComboBox = new JComboBox<>();
        categoryComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Category) {
                    setText(((Category) value).getName());
                }
                return this;
            }
        });
        
        menuItemsList = new JList<>();
        menuItemsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menuItemsList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof com.coffeeshop.model.MenuItem) {
                    com.coffeeshop.model.MenuItem item = (com.coffeeshop.model.MenuItem) value;
                    setText(item.getName() + " - $" + item.getPrice());
                }
                return this;
            }
        });
        
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        specialInstructionsField = new JTextField(30);
        
        // Order items table
        orderItemsModel = new DefaultTableModel(
            new String[]{"Item", "Quantity", "Unit Price", "Total", "Instructions"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderItemsTable = new JTable(orderItemsModel);
        orderItemsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Total and notes
        totalLabel = UIUtils.createTitleLabel("Total: $0.00");
        notesArea = new JTextArea(3, 30);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        
        // Buttons
        addItemButton = UIUtils.createSuccessButton("Add Item");
        removeItemButton = UIUtils.createDangerButton("Remove Item");
        saveOrderButton = UIUtils.createPrimaryButton("Save Order");
        cancelButton = UIUtils.createSecondaryButton("Cancel");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Main panel with tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Order Info Tab
        JPanel orderInfoPanel = createOrderInfoPanel();
        tabbedPane.addTab("Order Info", orderInfoPanel);
        
        // Menu Selection Tab
        JPanel menuPanel = createMenuPanel();
        tabbedPane.addTab("Add Items", menuPanel);
        
        // Order Items Tab
        JPanel itemsPanel = createOrderItemsPanel();
        tabbedPane.addTab("Order Items", itemsPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Bottom panel with buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.add(cancelButton);
        bottomPanel.add(saveOrderButton);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createOrderInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Table selection
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Table:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(tableComboBox, gbc);
        
        // Customer name
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Customer Name:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(customerNameField, gbc);
        
        // Order notes
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Notes:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 1.0;
        panel.add(new JScrollPane(notesArea), gbc);
        
        return panel;
    }
    
    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Category filter
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Category:"));
        topPanel.add(categoryComboBox);
        panel.add(topPanel, BorderLayout.NORTH);
        
        // Menu items list
        JScrollPane scrollPane = new JScrollPane(menuItemsList);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add item panel
        JPanel addPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        addPanel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1;
        addPanel.add(quantitySpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        addPanel.add(new JLabel("Special Instructions:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        addPanel.add(specialInstructionsField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        addPanel.add(addItemButton, gbc);
        
        panel.add(addPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createOrderItemsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Order items table
        JScrollPane scrollPane = new JScrollPane(orderItemsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Bottom panel with total and remove button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.add(totalLabel);
        bottomPanel.add(totalPanel, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(removeItemButton);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void setupEventHandlers() {
        // Category selection changes menu items
        categoryComboBox.addActionListener(e -> loadMenuItems());
        
        // Add item to order
        addItemButton.addActionListener(e -> addSelectedItem());
        
        // Remove item from order
        removeItemButton.addActionListener(e -> removeSelectedItem());
        
        // Save order
        saveOrderButton.addActionListener(e -> saveOrder());
        
        // Cancel
        cancelButton.addActionListener(e -> dispose());
    }
    
    private void setupFrame() {
        setTitle("New Order");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        UIUtils.setResponsiveSize(this, 900, 700);
        setResizable(true);
        UIUtils.centerOnScreen(this);
    }
    
    private void loadData() {
        try {
            // Load available tables
            List<Table> tables = tableService.getAvailableTables();
            tableComboBox.removeAllItems();
            for (Table table : tables) {
                tableComboBox.addItem(table);
            }
            
            // Load categories
            List<Category> categories = menuService.getAllCategories();
            categoryComboBox.removeAllItems();
            categoryComboBox.addItem(null); // "All categories" option
            for (Category category : categories) {
                categoryComboBox.addItem(category);
            }
            
            // Load initial menu items
            loadMenuItems();
            
        } catch (SQLException e) {
            logger.error("Error loading data", e);
            UIUtils.showError(this, "Error loading data: " + e.getMessage());
        }
    }
    
    private void loadMenuItems() {
        try {
            Category selectedCategory = (Category) categoryComboBox.getSelectedItem();
            List<com.coffeeshop.model.MenuItem> menuItems;
            
            if (selectedCategory == null) {
                menuItems = menuService.getAllMenuItems();
            } else {
                menuItems = menuService.getMenuItemsByCategory(selectedCategory.getId());
            }
            
            DefaultListModel<com.coffeeshop.model.MenuItem> model = new DefaultListModel<>();
            for (com.coffeeshop.model.MenuItem item : menuItems) {
                if (item.isAvailable()) {
                    model.addElement(item);
                }
            }
            menuItemsList.setModel(model);
            
        } catch (SQLException e) {
            logger.error("Error loading menu items", e);
            UIUtils.showError(this, "Error loading menu items: " + e.getMessage());
        }
    }
    
    private void addSelectedItem() {
        com.coffeeshop.model.MenuItem selectedItem = menuItemsList.getSelectedValue();
        if (selectedItem == null) {
            UIUtils.showWarning(this, "Please select a menu item first.");
            return;
        }
        
        int quantity = (Integer) quantitySpinner.getValue();
        String instructions = specialInstructionsField.getText().trim();
        
        // Create order item
        OrderItem orderItem = new OrderItem();
        orderItem.setMenuItemId(selectedItem.getId());
        orderItem.setMenuItemName(selectedItem.getName());
        orderItem.setQuantity(quantity);
        orderItem.setUnitPrice(selectedItem.getPrice());
        orderItem.setTotalPrice(selectedItem.getPrice().multiply(BigDecimal.valueOf(quantity)));
        orderItem.setSpecialInstructions(instructions.isEmpty() ? null : instructions);
        
        // Add to order items list
        orderItems.add(orderItem);
        
        // Update table
        updateOrderItemsTable();
        
        // Clear inputs
        quantitySpinner.setValue(1);
        specialInstructionsField.setText("");
        menuItemsList.clearSelection();
        
        logger.info("Added item to order: {} x{}", selectedItem.getName(), quantity);
    }
    
    private void removeSelectedItem() {
        int selectedRow = orderItemsTable.getSelectedRow();
        if (selectedRow == -1) {
            UIUtils.showWarning(this, "Please select an item to remove.");
            return;
        }
        
        orderItems.remove(selectedRow);
        updateOrderItemsTable();
        
        logger.info("Removed item from order at index: {}", selectedRow);
    }
    
    private void updateOrderItemsTable() {
        // Clear existing rows
        orderItemsModel.setRowCount(0);
        
        // Add order items
        totalAmount = BigDecimal.ZERO;
        for (OrderItem item : orderItems) {
            orderItemsModel.addRow(new Object[]{
                item.getMenuItemName(),
                item.getQuantity(),
                "$" + item.getUnitPrice(),
                "$" + item.getTotalPrice(),
                item.getSpecialInstructions() != null ? item.getSpecialInstructions() : ""
            });
            totalAmount = totalAmount.add(item.getTotalPrice());
        }
        
        // Update total label
        totalLabel.setText("Total: $" + totalAmount);
    }
    
    private void saveOrder() {
        // Validate inputs
        if (tableComboBox.getSelectedItem() == null) {
            UIUtils.showWarning(this, "Please select a table.");
            return;
        }
        
        String customerName = customerNameField.getText().trim();
        if (customerName.isEmpty()) {
            UIUtils.showWarning(this, "Please enter customer name.");
            return;
        }
        
        if (orderItems.isEmpty()) {
            UIUtils.showWarning(this, "Please add at least one item to the order.");
            return;
        }
        
        try {
            // Create order
            Table selectedTable = (Table) tableComboBox.getSelectedItem();
            User currentUser = authService.getCurrentUser();
            
            Order order = new Order();
            order.setTableId(selectedTable.getId());
            order.setCustomerName(customerName);
            order.setWaiterId(currentUser.getId());
            order.setStatus(Order.Status.NEW);
            order.setTotalAmount(totalAmount);
            order.setNotes(notesArea.getText().trim().isEmpty() ? null : notesArea.getText().trim());
            order.setOrderItems(orderItems);
            
            // Save order
            Order savedOrder = orderService.createOrder(order);
            
            UIUtils.showSuccess(this, "Order created successfully! Order #" + savedOrder.getOrderNumber());
            logger.info("Order created successfully: {}", savedOrder.getOrderNumber());
            
            // Close dialog
            dispose();
            
        } catch (SQLException e) {
            logger.error("Error creating order", e);
            UIUtils.showError(this, "Error creating order: " + e.getMessage());
        }
    }
}
