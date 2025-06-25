package com.coffeeshop.view.waiter;

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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Order History Panel for waiter users
 */
public class OrderHistoryPanel extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(OrderHistoryPanel.class);
    
    private final OrderService orderService;
    private final AuthenticationService authService;
    
    // UI Components
    private JTable ordersTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton viewActiveButton;
    private JButton viewAllButton;
    private JLabel orderCountLabel;
    
    public OrderHistoryPanel() {
        this.orderService = new OrderService();
        this.authService = AuthenticationService.getInstance();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
        loadOrders();
        
        logger.info("Order History Panel initialized");
    }
    
    private void initializeComponents() {
        // Buttons
        refreshButton = UIUtils.createSecondaryButton("Refresh");
        viewActiveButton = UIUtils.createSuccessButton("Active Orders");
        viewAllButton = UIUtils.createPrimaryButton("All Orders");
        
        // Order count label
        orderCountLabel = UIUtils.createSecondaryLabel("Loading...");
        
        // Orders table
        String[] columnNames = {"Order #", "Table", "Status", "Items", "Total", "Created", "Updated"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ordersTable = new JTable(tableModel);
        ordersTable.setFont(UIUtils.getResponsiveNormalFont());
        ordersTable.setRowHeight(UIUtils.isMobileScreen() ? 25 : 30);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Top panel with controls
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(UIUtils.createResponsiveBorder());
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.add(refreshButton);
        buttonsPanel.add(viewActiveButton);
        buttonsPanel.add(viewAllButton);
        
        // Status panel
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statusPanel.add(orderCountLabel);
        
        topPanel.add(buttonsPanel, BorderLayout.WEST);
        topPanel.add(statusPanel, BorderLayout.EAST);
        
        // Center panel with table
        JScrollPane scrollPane = new JScrollPane(ordersTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Order History"));
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        refreshButton.addActionListener(e -> loadOrders());
        viewActiveButton.addActionListener(e -> loadActiveOrders());
        viewAllButton.addActionListener(e -> loadOrders());
    }
    
    private void setupFrame() {
        setTitle("Order History");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        UIUtils.setResponsiveSize(this, 1000, 600);
        setResizable(true);
        UIUtils.centerOnScreen(this);
    }
    
    private void loadOrders() {
        try {
            User currentUser = authService.getCurrentUser();
            List<Order> orders = orderService.getAllOrders();
            
            updateTable(orders);
            orderCountLabel.setText("Total Orders: " + orders.size());
            
            logger.info("Loaded {} orders", orders.size());
        } catch (Exception e) {
            logger.error("Error loading orders", e);
            UIUtils.showError(this, "Error loading orders: " + e.getMessage());
        }
    }
    
    private void loadActiveOrders() {
        try {
            User currentUser = authService.getCurrentUser();
            List<Order> activeOrders = orderService.getActiveOrders();
            
            updateTable(activeOrders);
            orderCountLabel.setText("Active Orders: " + activeOrders.size());
            
            logger.info("Loaded {} active orders", activeOrders.size());
        } catch (Exception e) {
            logger.error("Error loading active orders", e);
            UIUtils.showError(this, "Error loading active orders: " + e.getMessage());
        }
    }
    
    private void updateTable(List<Order> orders) {
        tableModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm");
        
        for (Order order : orders) {
            Object[] row = {
                order.getOrderNumber(),
                "Table " + (order.getTableId() != null ? order.getTableId() : "N/A"),
                order.getStatus().toString(),
                order.getOrderItems() != null ? order.getOrderItems().size() + " items" : "0 items",
                "$" + (order.getTotalAmount() != null ? order.getTotalAmount() : "0.00"),
                order.getCreatedAt() != null ? order.getCreatedAt().format(formatter) : "N/A",
                order.getUpdatedAt() != null ? order.getUpdatedAt().format(formatter) : "N/A"
            };
            tableModel.addRow(row);
        }
    }
}
