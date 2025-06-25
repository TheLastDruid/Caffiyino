package com.coffeeshop.view.waiter;

import com.coffeeshop.model.Table;
import com.coffeeshop.service.TableService;
import com.coffeeshop.util.UIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Table Management Panel for waiter users
 */
public class TableManagementPanel extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(TableManagementPanel.class);
    
    private final TableService tableService;
    
    // UI Components
    private JTable tablesTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton viewAvailableButton;
    private JButton viewAllButton;
    private JLabel totalTablesLabel;
    private JLabel availableTablesLabel;
    
    public TableManagementPanel() {
        this.tableService = new TableService();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
        loadTables();
        
        logger.info("Table Management Panel initialized");
    }
    
    private void initializeComponents() {
        // Buttons
        refreshButton = UIUtils.createSecondaryButton("Refresh");
        viewAvailableButton = UIUtils.createSuccessButton("Available Tables");
        viewAllButton = UIUtils.createPrimaryButton("All Tables");
        
        // Tables table
        tableModel = new DefaultTableModel(
            new String[]{"Table #", "Capacity", "Status", "Availability"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablesTable = new JTable(tableModel);
        tablesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablesTable.getTableHeader().setReorderingAllowed(false);
        
        // Labels
        totalTablesLabel = new JLabel("Total Tables: 0");
        availableTablesLabel = new JLabel("Available: 0");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Top panel with filters and controls
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.add(viewAllButton);
        controlPanel.add(viewAvailableButton);
        controlPanel.add(refreshButton);
        
        // Stats panel
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statsPanel.add(totalTablesLabel);
        statsPanel.add(Box.createHorizontalStrut(20));
        statsPanel.add(availableTablesLabel);
        
        topPanel.add(controlPanel, BorderLayout.WEST);
        topPanel.add(statsPanel, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Center panel with table
        JScrollPane scrollPane = new JScrollPane(tablesTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Tables"));
        add(scrollPane, BorderLayout.CENTER);
        
        // Bottom panel with info
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.add(new JLabel("Table management allows you to view table status and availability."));
        
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        // Refresh button
        refreshButton.addActionListener(e -> loadTables());
        
        // View available tables
        viewAvailableButton.addActionListener(e -> loadAvailableTables());
        
        // View all tables
        viewAllButton.addActionListener(e -> loadTables());
    }
    
    private void setupFrame() {
        setTitle("Table Management");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setResizable(true);
        UIUtils.centerOnScreen(this);
    }
    
    private void loadTables() {
        try {
            List<Table> allTables = tableService.getAllTables();
            List<Table> availableTables = tableService.getAvailableTables();
            
            updateTable(allTables, availableTables);
            
        } catch (SQLException e) {
            logger.error("Error loading tables", e);
            UIUtils.showError(this, "Error loading tables: " + e.getMessage());
        }
    }
    
    private void loadAvailableTables() {
        try {
            List<Table> availableTables = tableService.getAvailableTables();
            
            updateTable(availableTables, availableTables);
            
        } catch (SQLException e) {
            logger.error("Error loading available tables", e);
            UIUtils.showError(this, "Error loading available tables: " + e.getMessage());
        }
    }
    
    private void updateTable(List<Table> tables, List<Table> availableTables) {
        tableModel.setRowCount(0);
        
        for (Table table : tables) {
            boolean isAvailable = availableTables.contains(table);
            tableModel.addRow(new Object[]{
                table.getTableNumber(),
                table.getCapacity() + " seats",
                table.isActive() ? "Active" : "Inactive",
                isAvailable ? "Available" : "Occupied"
            });
        }
        
        // Update stats labels
        try {
            List<Table> allTables = tableService.getAllTables();
            List<Table> allAvailable = tableService.getAvailableTables();
            
            totalTablesLabel.setText("Total Tables: " + allTables.size());
            availableTablesLabel.setText("Available: " + allAvailable.size());
            
        } catch (SQLException e) {
            logger.error("Error updating stats", e);
        }
    }
}
