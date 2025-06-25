package com.coffeeshop.view.waiter;

import com.coffeeshop.model.MenuItem;
import com.coffeeshop.service.MenuService;
import com.coffeeshop.util.UIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Menu View Panel for waiter users
 */
public class MenuViewPanel extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(MenuViewPanel.class);
    
    private MenuService menuService;
    
    // UI Components
    private JTable menuTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    
    public MenuViewPanel() {
        this.menuService = new MenuService();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
        loadMenuItems();
        
        logger.info("Menu View Panel initialized");
    }
    
    private void initializeComponents() {
        // Table setup
        String[] columnNames = {"Name", "Category", "Description", "Price", "Available", "Prep Time"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        menuTable = new JTable(tableModel);
        menuTable.getColumnModel().getColumn(3).setMaxWidth(80);
        menuTable.getColumnModel().getColumn(4).setMaxWidth(80);
        menuTable.getColumnModel().getColumn(5).setMaxWidth(80);
        
        refreshButton = UIUtils.createPrimaryButton("Refresh");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Top panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(refreshButton);
        
        // Center panel with table
        JScrollPane scrollPane = new JScrollPane(menuTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Available Menu Items"));
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        refreshButton.addActionListener(e -> loadMenuItems());
    }
    
    private void setupFrame() {
        setTitle("Menu View");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        UIUtils.setResponsiveSize(this, 800, 600);
        setResizable(true);
        UIUtils.centerOnScreen(this);
    }
    
    private void loadMenuItems() {
        try {
            List<MenuItem> menuItems = menuService.getAvailableMenuItems();
            tableModel.setRowCount(0);
            
            for (MenuItem item : menuItems) {
                Object[] row = {
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
}
