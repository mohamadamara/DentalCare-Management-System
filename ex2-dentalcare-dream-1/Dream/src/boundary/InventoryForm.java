package boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import control.InventoryManager;
import entity.InventoryItem;
import entity.consts;

public class InventoryForm extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField itemNameField, descriptionField, categoryIDField, quantityField, supplierIDField;
    private JTextField expirationDateField, serialNumberField, searchField;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;
    private JLabel pageLabel;

    private int currentPage = 1;
    private int pageSize = 10;

    public InventoryForm() {
        setTitle("Inventory Management – DentalCare");
        setSize(1100, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // === TOP NAVIGATION BAR ===
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton inventoryBtn = new JButton("Inventory");
        JButton supplierBtn = new JButton("Suppliers");
        JButton xmlBtn = new JButton("XML Import");
        navPanel.add(inventoryBtn);
        navPanel.add(supplierBtn);
        navPanel.add(xmlBtn);
        add(navPanel, BorderLayout.NORTH);
        supplierBtn.addActionListener(e -> {
            new SupplierForm().setVisible(true);
            dispose(); // Close current window
        });
        xmlBtn.addActionListener(e -> {
            new UploadXMLForm().setVisible(true);
            dispose(); // Close current window
        });


        // === CENTER PANEL ===
        JPanel centerPanel = new JPanel(new BorderLayout());

        // === Search Panel ===
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search & Filter"));
        searchPanel.add(new JLabel("Search:"));
        searchField = new JTextField(15);
        searchPanel.add(searchField);
        JButton searchBtn = new JButton("Search");
        JButton refreshBtn = new JButton("Refresh");
        searchPanel.add(searchBtn);
        searchPanel.add(refreshBtn);
        centerPanel.add(searchPanel, BorderLayout.NORTH);

        // === Form Panel ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Item Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 1 - Item Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Item Name:"), gbc);
        gbc.gridx = 1;
        itemNameField = new JTextField(20);
        formPanel.add(itemNameField, gbc);

        // Row 2 - Description
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        descriptionField = new JTextField(20);
        formPanel.add(descriptionField, gbc);

        // Row 3 - Category ID
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Category ID:"), gbc);
        gbc.gridx = 1;
        categoryIDField = new JTextField(20);
        formPanel.add(categoryIDField, gbc);

        // Row 4 - Quantity
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1;
        quantityField = new JTextField(20);
        formPanel.add(quantityField, gbc);

        // Row 5 - Supplier ID
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Supplier ID:"), gbc);
        gbc.gridx = 1;
        supplierIDField = new JTextField(20);
        formPanel.add(supplierIDField, gbc);

        // Row 6 - Expiration Date
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Expiration Date:"), gbc);
        gbc.gridx = 1;
        expirationDateField = new JTextField(20);
        formPanel.add(expirationDateField, gbc);

        // Row 7 - Serial Number
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Serial Number:"), gbc);
        gbc.gridx = 1;
        serialNumberField = new JTextField(20);
        formPanel.add(serialNumberField, gbc);

        centerPanel.add(formPanel, BorderLayout.WEST);


        // === Table ===
        tableModel = new DefaultTableModel(new String[]{
                "ItemID", "ItemName", "Description", "CategoryID", "Quantity", "Supplier", "ExpirationDate", "SerialNumber"
            }, 0) {
                private static final long serialVersionUID = 1L;
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createTitledBorder("Inventory Table"));
            centerPanel.add(scrollPane, BorderLayout.CENTER);
            add(centerPanel, BorderLayout.CENTER);

        // === Bottom Panel ===
        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton clearBtn = new JButton("Clear");
        actionsPanel.add(addBtn);
        actionsPanel.add(updateBtn);
        actionsPanel.add(deleteBtn);
        actionsPanel.add(clearBtn);

        JPanel navPanel2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton firstBtn = new JButton("First");
        JButton prevBtn = new JButton("Previous");
        pageLabel = new JLabel("Page: 1");
        JButton nextBtn = new JButton("Next");
        JButton lastBtn = new JButton("Last");
        navPanel2.add(firstBtn);
        navPanel2.add(prevBtn);
        navPanel2.add(pageLabel);
        navPanel2.add(nextBtn);
        navPanel2.add(lastBtn);

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusLabel = new JLabel("Ready");
        statusPanel.add(statusLabel, BorderLayout.WEST);

        bottomPanel.add(actionsPanel, BorderLayout.WEST);
        bottomPanel.add(navPanel2, BorderLayout.EAST);
        bottomPanel.add(statusPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // === Table Click Event ===
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                itemNameField.setText(getValueAsString(row, 1));
                descriptionField.setText(getValueAsString(row, 2));
                categoryIDField.setText(getValueAsString(row, 3));
                quantityField.setText(getValueAsString(row, 4));
                supplierIDField.setText(getValueAsString(row, 5));
                expirationDateField.setText(getValueAsString(row, 6));
                serialNumberField.setText(getValueAsString(row, 7));
            }
        });

        // === Button Actions ===
       
        searchBtn.addActionListener(e -> {
            String keyword = searchField.getText().trim().toLowerCase();
            tableModel.setRowCount(0); // Clear existing rows

            List<InventoryItem> allItems = new InventoryManager().getAllInventory();
            for (InventoryItem item : allItems) {
                String itemIdStr = String.valueOf(item.getItemID());
                String serial = item.getSerialNumber(); // זה עלול להיות null

                // נוודא ש-serial לא null לפני שמבצעים toLowerCase
                if (itemIdStr.equals(keyword) ||
                    (serial != null && serial.toLowerCase().contains(keyword))) {

                    tableModel.addRow(new Object[]{
                        item.getItemID(),
                        item.getItemName(),
                        item.getDescription(),
                        item.getCategoryID(),
                        item.getQuantity(),
                        item.getSupplierID(),
                        item.getExpirationDate(),
                        item.getSerialNumber()
                    });
                }
            }

            pageLabel.setText("Search Results");
            statusLabel.setText("Filtered by ID or Serial Number: " + keyword);
        });

        // === Refresh Button Action ===
        refreshBtn.addActionListener(e -> {
            searchField.setText(""); // Clear search box
            currentPage = 1;
            loadInventoryData(); // Reload full list
            statusLabel.setText("Inventory refreshed.");
        });


        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                InventoryItem item = getFormData();
                item.setItemID(Integer.parseInt(tableModel.getValueAt(row, 0).toString()));
                boolean success = new InventoryManager().updateInventory(item);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Item updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadInventoryData();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update item.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        addBtn.addActionListener(e -> {
            InventoryItem item = getFormData();

            // ❗ Step 1: Check for duplicate serial number
            if (isSerialNumberDuplicate(item.getSerialNumber())) {
                JOptionPane.showMessageDialog(this,
                    "⚠️ This serial number already exists in the table. Please enter a unique one.",
                    "Duplicate Serial Number",
                    JOptionPane.WARNING_MESSAGE);
                return; // ✅ THIS LINE PREVENTS INSERT
            }

            // ❗ Step 2: Check date format
            if (!isValidDateFormat(item.getExpirationDate())) {
                JOptionPane.showMessageDialog(this,
                    "❌ Invalid date format! Please use YYYY-MM-DD (e.g. 2025-12-31).",
                    "Invalid Date",
                    JOptionPane.ERROR_MESSAGE);
                return; // ✅ Prevents insert on invalid date
            }

            // ✅ Step 3: Only insert if everything is valid
            boolean success = new InventoryManager().addItem(item);
            if (success) {
                JOptionPane.showMessageDialog(this, "Item added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadInventoryData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add item.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        
        

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int itemId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                boolean success = new InventoryManager().deleteInventory(itemId);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Item deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadInventoryData();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete item.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        
        
        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                InventoryItem item = getFormData();
                item.setItemID(Integer.parseInt(tableModel.getValueAt(row, 0).toString()));

                // Check for duplicate serial number excluding current item
                if (isSerialNumberDuplicate(item.getSerialNumber(), item.getItemID())) {
                    JOptionPane.showMessageDialog(this,
                        "⚠️ This serial number already exists for another item.",
                        "Duplicate Serial Number",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                boolean success = new InventoryManager().updateInventory(item);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Item updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadInventoryData();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update item.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


     
        

        clearBtn.addActionListener(e -> clearForm());

        firstBtn.addActionListener(e -> {
            currentPage = 1;
            loadInventoryData();
        });

        prevBtn.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                loadInventoryData();
            }
        });

        nextBtn.addActionListener(e -> {
            List<InventoryItem> allItems = new InventoryManager().getAllInventory();
            int totalPages = (int) Math.ceil((double) allItems.size() / pageSize);
            if (currentPage < totalPages) {
                currentPage++;
                loadInventoryData();
            }
        });

        lastBtn.addActionListener(e -> {
            List<InventoryItem> allItems = new InventoryManager().getAllInventory();
            int totalPages = (int) Math.ceil((double) allItems.size() / pageSize);
            currentPage = totalPages;
            loadInventoryData();
        });

        loadInventoryData();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadInventoryData() {
        tableModel.setRowCount(0);
        List<InventoryItem> allItems = new InventoryManager().getAllInventory();
        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, allItems.size());
        for (int i = start; i < end; i++) {
            InventoryItem item = allItems.get(i);
            tableModel.addRow(new Object[]{
                item.getItemID(),
                item.getItemName(),
                item.getDescription(),
                item.getCategoryID(),
                item.getQuantity(),
                item.getSupplierID(),
                item.getExpirationDate(),
                item.getSerialNumber()
            });
        }
        int totalPages = (int) Math.ceil((double) allItems.size() / pageSize);
        pageLabel.setText("Page: " + currentPage + " / " + totalPages);
    }

    private void clearForm() {
        itemNameField.setText("");
        descriptionField.setText("");
        categoryIDField.setText("");
        quantityField.setText("");
        supplierIDField.setText("");
        expirationDateField.setText("");
        serialNumberField.setText("");
    }

    private InventoryItem getFormData() {
        return new InventoryItem(
            0,
            itemNameField.getText(),
            descriptionField.getText(),
            Integer.parseInt(categoryIDField.getText()),
            Integer.parseInt(quantityField.getText()),
            Integer.parseInt(supplierIDField.getText()),  
            expirationDateField.getText(),
            serialNumberField.getText()
        );
    }
    private boolean isSerialNumberDuplicate(String serialNumber, int currentItemId) {
        String sql = "SELECT COUNT(*) FROM Inventory WHERE SerialNumber = ? AND ItemID <> ?";
        try (Connection conn = DriverManager.getConnection(consts.CONN_STR);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, serialNumber);
            stmt.setInt(2, currentItemId); // exclude the current record from the check

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Error checking serial number: " + e.getMessage());
        }
        return false;
    }

    private boolean isSerialNumberDuplicate(String serialNumber) {
        String sql = "SELECT COUNT(*) FROM Inventory WHERE SerialNumber = ?";
        try (Connection conn = DriverManager.getConnection(entity.consts.CONN_STR);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, serialNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "❌ Error checking serial number: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    private boolean isValidDateFormat(String dateStr) {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false); // strict format
            sdf.parse(dateStr);
            return true;
        } catch (java.text.ParseException e) {
            return false;
        }
    }
    
    private String getValueAsString(int row, int col) {
        Object value = tableModel.getValueAt(row, col);
        return value != null ? value.toString() : "";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InventoryForm().setVisible(true));
    }
}
