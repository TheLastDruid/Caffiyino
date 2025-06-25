package com.coffeeshop.view.kitchen;

import com.coffeeshop.model.Order;
import com.coffeeshop.model.User;
import com.coffeeshop.service.AuthenticationService;
import com.coffeeshop.service.OrderService;
import com.coffeeshop.util.UIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Kitchen Order Management Panel
 */
public class KitchenOrderPanel extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(KitchenOrderPanel.class);
    
    private final OrderService orderService;
    private final AuthenticationService authService;
    private final Order.Status filterStatus;
    
    // UI Components
    private JTable ordersTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton updateStatusButton;
    private JComboBox<Order.Status> statusComboBox;
    private JLabel titleLabel;
    private JLabel countLabel;
    private Timer refreshTimer;
    
    public KitchenOrderPanel(Order.Status status) {
        this.orderService = new OrderService();
        this.authService = AuthenticationService.getInstance();
        this.filterStatus = status;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
        loadOrders();
        startAutoRefresh();
        
        logger.info("Kitchen Order Panel initialized for status: {}", status);
    }
    
    private void initializeComponents() {
        // Title
        String statusTitle = getStatusTitle(filterStatus);
        titleLabel = UIUtils.createTitleLabel(statusTitle);
        
        // Count label
        countLabel = new JLabel("Orders: 0");
        
        // Buttons
        refreshButton = UIUtils.createSecondaryButton("Refresh");
        updateStatusButton = UIUtils.createPrimaryButton("Update Status");
        
        // Status combo for updates
        statusComboBox = new JComboBox<>(Order.Status.values());
        statusComboBox.setSelectedItem(getNextStatus(filterStatus));
        
        // Orders table
        tableModel = new DefaultTableModel(
            new String[]{"Order #", "Customer", "Table", "Items", "Time", "Duration", "Notes"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ordersTable = new JTable(tableModel);
        ordersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ordersTable.getTableHeader().setReorderingAllowed(false);
        
        // Set row height for better readability
        ordersTable.setRowHeight(25);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Top panel with title and controls
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createHorizontalStrut(20));
        titlePanel.add(countLabel);
        
        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlPanel.add(new JLabel("Update to:"));
        controlPanel.add(statusComboBox);
        controlPanel.add(updateStatusButton);
        controlPanel.add(Box.createHorizontalStrut(10));
        controlPanel.add(refreshButton);
        
        topPanel.add(titlePanel, BorderLayout.WEST);
        topPanel.add(controlPanel, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Center panel with table
        JScrollPane scrollPane = new JScrollPane(ordersTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Orders"));
        add(scrollPane, BorderLayout.CENTER);
        
        // Bottom panel with info
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        bottomPanel.add(new JLabel("Select an order and click 'Update Status' to move it to the next stage."));
        
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        // Refresh button
        refreshButton.addActionListener(e -> loadOrders());
        
        // Update status button
        updateStatusButton.addActionListener(e -> updateOrderStatus());
        
        // Double-click on table row to update status
        ordersTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    updateOrderStatus();
                }
            }
        });
    }
    
    private void setupFrame() {
        setTitle("Kitchen - " + getStatusTitle(filterStatus));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        UIUtils.setResponsiveSize(this, 1000, 600);
        setResizable(true);
        UIUtils.centerOnScreen(this);
    }
    
    private void startAutoRefresh() {
        // Auto-refresh every 30 seconds for real-time updates
        refreshTimer = new Timer(30000, e -> loadOrders());
        refreshTimer.start();
        
        // Stop timer when window is closed
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (refreshTimer != null) {
                    refreshTimer.stop();
                }
            }
        });
    }
    
    private void loadOrders() {
        try {
            List<Order> orders = orderService.getOrdersByStatus(filterStatus);
            updateTable(orders);
        } catch (SQLException e) {
            logger.error("Error loading orders for status: {}", filterStatus, e);
            UIUtils.showError(this, "Error loading orders: " + e.getMessage());
        }
    }
    
    private void updateTable(List<Order> orders) {
        tableModel.setRowCount(0);
        
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        
        for (Order order : orders) {
            // Calculate duration since order was created
            long minutes = java.time.Duration.between(order.getCreatedAt(), java.time.LocalDateTime.now()).toMinutes();
            String duration = minutes + " min";
            
            // Count items in order
            int itemCount = order.getOrderItems() != null ? order.getOrderItems().size() : 0;
            
            tableModel.addRow(new Object[]{
                order.getOrderNumber(),
                order.getCustomerName(),
                order.getTableName() != null ? order.getTableName() : "N/A",
                itemCount + " items",
                order.getCreatedAt().format(timeFormatter),
                duration,
                order.getNotes() != null ? order.getNotes() : ""
            });
        }
        
        countLabel.setText("Orders: " + orders.size());
        
        // Highlight urgent orders (older than 30 minutes)
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            long minutes = java.time.Duration.between(order.getCreatedAt(), java.time.LocalDateTime.now()).toMinutes();
            if (minutes > 30) {
                // Would highlight row in red if we had custom renderer
                // For now, just log it
                logger.warn("Order {} is taking longer than 30 minutes", order.getOrderNumber());
            }
        }
    }
    
    private void updateOrderStatus() {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow == -1) {
            UIUtils.showWarning(this, "Please select an order to update.");
            return;
        }
        
        String orderNumber = (String) tableModel.getValueAt(selectedRow, 0);
        Order.Status newStatus = (Order.Status) statusComboBox.getSelectedItem();
        
        if (newStatus == null) {
            UIUtils.showWarning(this, "Please select a status to update to.");
            return;
        }
        
        try {
            Order order = orderService.getOrderByNumber(orderNumber);
            if (order != null) {
                User currentUser = authService.getCurrentUser();
                orderService.updateOrderStatus(order.getId(), newStatus, currentUser.getId());
                
                UIUtils.showSuccess(this, String.format("Order %s updated to %s", orderNumber, newStatus));
                logger.info("Order {} status updated to {} by {}", orderNumber, newStatus, currentUser.getUsername());
                
                // Refresh the table
                loadOrders();
                
            } else {
                UIUtils.showError(this, "Order not found: " + orderNumber);
            }
        } catch (SQLException e) {
            logger.error("Error updating order status", e);
            UIUtils.showError(this, "Error updating order status: " + e.getMessage());
        }
    }
    
    private String getStatusTitle(Order.Status status) {
        switch (status) {
            case NEW:
                return "New Orders";
            case IN_PROGRESS:
                return "In Progress Orders";
            case READY:
                return "Ready Orders";
            case COMPLETED:
                return "Completed Orders";
            case CANCELLED:
                return "Cancelled Orders";
            default:
                return "Orders";
        }
    }
    
    private Order.Status getNextStatus(Order.Status currentStatus) {
        switch (currentStatus) {
            case NEW:
                return Order.Status.IN_PROGRESS;
            case IN_PROGRESS:
                return Order.Status.READY;
            case READY:
                return Order.Status.COMPLETED;
            default:
                return Order.Status.COMPLETED;
        }
    }
}
