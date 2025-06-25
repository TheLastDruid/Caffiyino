package com.coffeeshop.view.waiter;

import com.coffeeshop.model.MenuItem;
import com.coffeeshop.service.MenuService;
import com.coffeeshop.util.UIUtils;
import com.coffeeshop.view.components.MenuItemCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Modern Menu View Panel with card-based layout for waiter users
 */
public class ModernMenuViewPanel extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(ModernMenuViewPanel.class);
    
    private MenuService menuService;
    
    // UI Components
    private JPanel menuCardsPanel;
    private JScrollPane scrollPane;
    private JButton refreshButton;
    private JTextField searchField;
    private JComboBox<String> categoryFilter;
    private JLabel itemCountLabel;
    
    public ModernMenuViewPanel() {
        this.menuService = new MenuService();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
        loadMenuItems();
        
        logger.info("Modern Menu View Panel initialized");
    }
    
    private void initializeComponents() {
        // Top controls
        refreshButton = UIUtils.createPrimaryButton("🔄 Refresh");
        searchField = UIUtils.createStyledTextField();
        searchField.setColumns(20);
        categoryFilter = UIUtils.createStyledComboBox();
        categoryFilter.addItem("All Categories");
        
        itemCountLabel = UIUtils.createSecondaryLabel("Loading...");
        
        // Menu cards panel with modern 2025 styling
        menuCardsPanel = new JPanel();
        menuCardsPanel.setBackground(UIUtils.BACKGROUND_PRIMARY);
        updateMenuLayout();
        
        // Scroll pane
        scrollPane = new JScrollPane(menuCardsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Top panel with modern 2025 styling
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIUtils.SURFACE_PRIMARY);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Left side of top panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        searchPanel.setBackground(UIUtils.SURFACE_PRIMARY);
        searchPanel.add(UIUtils.createStyledLabel("🔍 Search:"));
        searchPanel.add(searchField);
        searchPanel.add(UIUtils.createStyledLabel("📂 Category:"));
        searchPanel.add(categoryFilter);
        
        // Right side of top panel
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        controlsPanel.setBackground(UIUtils.SURFACE_PRIMARY);
        controlsPanel.add(itemCountLabel);
        controlsPanel.add(refreshButton);
        
        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(controlsPanel, BorderLayout.EAST);
        
        // Header with coffee shop branding
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIUtils.PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        
        JLabel titleLabel = UIUtils.createTitleLabel("☕ Coffee Shop Menu");
        titleLabel.setForeground(UIUtils.TEXT_INVERSE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Create a combined top panel
        JPanel combinedTopPanel = new JPanel(new BorderLayout());
        combinedTopPanel.add(headerPanel, BorderLayout.NORTH);
        combinedTopPanel.add(topPanel, BorderLayout.SOUTH);
        
        add(combinedTopPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        refreshButton.addActionListener(this::refreshMenuItems);
        
        // Search functionality
        searchField.addActionListener(this::performSearch);
        categoryFilter.addActionListener(this::filterByCategory);
    }
    
    private void setupFrame() {
        setTitle("☕ Coffee Shop Menu");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setResizable(true);
        UIUtils.centerOnScreen(this);
    }
    
    private void loadMenuItems() {
        SwingUtilities.invokeLater(() -> {
            try {
                List<MenuItem> menuItems = menuService.getAvailableMenuItems();
                updateMenuCards(menuItems);
                updateItemCount(menuItems.size());
                logger.info("Loaded {} menu items", menuItems.size());
            } catch (Exception e) {
                logger.error("Error loading menu items", e);
                UIUtils.showError(this, "Error loading menu items: " + e.getMessage());
            }
        });
    }
    
    private void updateMenuCards(List<MenuItem> menuItems) {
        menuCardsPanel.removeAll();
        
        if (menuItems.isEmpty()) {
            JLabel emptyLabel = UIUtils.createHeaderLabel("No menu items available");
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            menuCardsPanel.add(emptyLabel);
        } else {
            for (MenuItem item : menuItems) {
                MenuItemCard card = new MenuItemCard(item);
                
                // Add action listener for ordering
                card.addActionListener(e -> handleAddToOrder(item));
                
                menuCardsPanel.add(card);
            }
        }
        
        updateMenuLayout();
        menuCardsPanel.revalidate();
        menuCardsPanel.repaint();
    }
    
    private void updateMenuLayout() {
        // Calculate number of columns based on window width
        int cardWidth = 340; // Width of each card including margins (updated for larger cards)
        int panelWidth = scrollPane.getViewport().getWidth();
        if (panelWidth == 0) panelWidth = 1200; // Default width
        
        int columns = Math.max(1, panelWidth / cardWidth);
        
        menuCardsPanel.setLayout(new GridLayout(0, columns, 20, 20));
        menuCardsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }
    
    private void updateItemCount(int count) {
        itemCountLabel.setText(count + " items");
    }
    
    private void refreshMenuItems(ActionEvent e) {
        refreshButton.setEnabled(false);
        refreshButton.setText("🔄 Loading...");
        
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Thread.sleep(500); // Small delay for better UX
                loadMenuItems();
                return null;
            }
            
            @Override
            protected void done() {
                refreshButton.setEnabled(true);
                refreshButton.setText("🔄 Refresh");
            }
        };
        worker.execute();
    }
    
    private void performSearch(ActionEvent e) {
        String searchTerm = searchField.getText().trim().toLowerCase();
        
        try {
            List<MenuItem> allItems = menuService.getAvailableMenuItems();
            List<MenuItem> filteredItems = allItems.stream()
                .filter(item -> item.getName().toLowerCase().contains(searchTerm) ||
                               item.getDescription().toLowerCase().contains(searchTerm))
                .toList();
            
            updateMenuCards(filteredItems);
            updateItemCount(filteredItems.size());
        } catch (Exception ex) {
            logger.error("Error searching menu items", ex);
            UIUtils.showError(this, "Error searching menu items: " + ex.getMessage());
        }
    }
    
    private void filterByCategory(ActionEvent e) {
        String selectedCategory = (String) categoryFilter.getSelectedItem();
        
        if ("All Categories".equals(selectedCategory)) {
            loadMenuItems();
            return;
        }
        
        try {
            List<MenuItem> allItems = menuService.getAvailableMenuItems();
            List<MenuItem> filteredItems = allItems.stream()
                .filter(item -> selectedCategory.equals(item.getCategoryName()))
                .toList();
            
            updateMenuCards(filteredItems);
            updateItemCount(filteredItems.size());
        } catch (Exception ex) {
            logger.error("Error filtering menu items", ex);
            UIUtils.showError(this, "Error filtering menu items: " + ex.getMessage());
        }
    }
    
    private void handleAddToOrder(MenuItem item) {
        // Show a modern dialog for adding to order
        String message = String.format(
            "Add %s to order?\\n\\nPrice: $%s\\nPrep Time: %d minutes",
            item.getName(), item.getPrice(), item.getPreparationTime()
        );
        
        if (UIUtils.showConfirmation(this, message, "Add to Order")) {
            UIUtils.showSuccess(this, item.getName() + " added to order!");
            logger.info("Added {} to order", item.getName());
        }
    }
    
    // Method to update layout when window is resized
    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        if (menuCardsPanel != null) {
            SwingUtilities.invokeLater(this::updateMenuLayout);
        }
    }
}
