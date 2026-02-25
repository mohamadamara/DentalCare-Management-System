package boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

import control.InventoryManager;
import control.SupplierManager;
import entity.consts;
import entity.Supplier;

public class SupplierForm extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField nameField, contactField, phoneField, emailField, addressField;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;

    public SupplierForm() {
        setTitle("Supplier Management – DentalCare");
        setSize(1100, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // === TOP NAV BAR ===
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] tabs = { "Inventory", "Suppliers", "Import/Export"};
        for (String tab : tabs) {
            JButton btn = new JButton(tab);
            navPanel.add(btn);

            // Navigation functionality
            if (tab.equals("Inventory")) {
                btn.addActionListener(e -> {
                    dispose();
                    new InventoryForm();
                });
            } else if (tab.equals("Import/Export")) {
                btn.addActionListener(e -> {
                    dispose();
                    new UploadXMLForm();
                });
            }
        }
        add(navPanel, BorderLayout.NORTH);

        // === CENTER PANEL ===
        JPanel centerPanel = new JPanel(new BorderLayout());

        // === Search Panel ===
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search & Filter"));
        searchPanel.add(new JLabel("Search:"));
        JTextField searchField = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        JButton refreshBtn = new JButton("Refresh");
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        searchPanel.add(refreshBtn);
        centerPanel.add(searchPanel, BorderLayout.NORTH);

        searchBtn.addActionListener(e -> {
            String keyword = searchField.getText().trim().toLowerCase();
            tableModel.setRowCount(0); // Clear the table
            boolean found = false;

            try (Connection conn = DriverManager.getConnection(consts.CONN_STR);
                 PreparedStatement ps = conn.prepareStatement("SELECT * FROM Supplier")) {

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                	String id = String.valueOf(rs.getInt("SupplierID")).toLowerCase();
                	String contact = rs.getString("ContactPerson");
                	contact = contact != null ? contact.toLowerCase() : "";
                	if (id.contains(keyword) || contact.contains(keyword)) {
 
                        Object[] row = {
                            rs.getInt("SupplierID"),
                            rs.getString("SupplierName"),
                            rs.getString("ContactPerson"),
                            rs.getString("Phone"),
                            rs.getString("Email"),
                            rs.getString("SupplierAddress")
                        };
                        tableModel.addRow(row);
                        found = true;
                    }
                }

                if (!found) {
                    JOptionPane.showMessageDialog(this, "⚠️ No supplier found for: " + keyword);
                    statusLabel.setText("No match found");
                } else {
                    statusLabel.setText("Search completed for: " + keyword);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                statusLabel.setText("Error during search");
            }
        });
        
        refreshBtn.addActionListener(e -> {
            searchField.setText("");
            reloadTableFromDatabase();
            statusLabel.setText("Data refreshed.");
        });

        // === Supplier Details Form ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Supplier Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        nameField = new JTextField(20);
        contactField = new JTextField(20);
        phoneField = new JTextField(20);
        emailField = new JTextField(20);
        addressField = new JTextField(20);

        String[] labels = {"Name:", "Contact:", "Phone:", "Email:", "Address:"};
        JTextField[] fields = {nameField, contactField, phoneField, emailField, addressField};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            formPanel.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            formPanel.add(fields[i], gbc);
        }

        JButton insertBtn = new JButton("Insert");
        gbc.gridx = 1;
        gbc.gridy = labels.length;
        formPanel.add(insertBtn, gbc);

        centerPanel.add(formPanel, BorderLayout.WEST);

        // === Supplier Table ===
        String[] columns = {"Supplier ID", "Name", "Contact", "Phone", "Email", "Address"};
        tableModel = new DefaultTableModel(columns, 0) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false; // Disable editing for all cells
            }
        };        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Suppliers Table"));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // === BOTTOM PANEL ===
        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton saveBtn = new JButton("Save");
        JButton clearBtn = new JButton("Clear");
        actionsPanel.add(addBtn);
        actionsPanel.add(updateBtn);
        actionsPanel.add(deleteBtn);
        actionsPanel.add(saveBtn);
        actionsPanel.add(clearBtn);
        
        

        JPanel navPanel2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton firstBtn = new JButton("First");
        JButton prevBtn = new JButton("Previous");
        JButton nextBtn = new JButton("Next");
        JButton lastBtn = new JButton("Last");
        navPanel2.add(firstBtn);
        navPanel2.add(prevBtn);
        navPanel2.add(nextBtn);
        navPanel2.add(lastBtn);
        
     // === Action Listeners for remaining buttons ===

        addBtn.addActionListener(e -> insertSupplier());

        updateBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            
            if (selectedRow != -1) {
                try {
                	int supplierId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                    Supplier supplier = new Supplier(
                        supplierId,
                        nameField.getText(),
                        contactField.getText(),
                        phoneField.getText(),
                        emailField.getText(),
                        addressField.getText()
                    );
                    new SupplierManager().updateSupplier(supplier);
                    reloadTableFromDatabase();
                    statusLabel.setText("Updated supplier successfully.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "❌ Error updating supplier: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "⚠️ Please select a supplier to update.");
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Object idValue = tableModel.getValueAt(row, 0);

            if (idValue == null) {
                JOptionPane.showMessageDialog(this, "Selected row does not have a valid Supplier ID.", "Invalid Row", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int supplierId;
            try {
                supplierId = Integer.parseInt(idValue.toString());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Supplier ID is not a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this supplier and all related inventory?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                new InventoryManager().deleteInventoryBySupplier(supplierId);
                boolean success = new SupplierManager().deleteSupplier(supplierId);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Supplier and related inventory deleted.");
                    reloadTableFromDatabase();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete supplier.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        saveBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                try {
                	int supplierId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                    Supplier supplier = new Supplier(
                        supplierId,
                        nameField.getText(),
                        contactField.getText(),
                        phoneField.getText(),
                        emailField.getText(),
                        addressField.getText()
                    );
                    new SupplierManager().updateSupplier(supplier);
                    reloadTableFromDatabase();
                    statusLabel.setText("Saved supplier changes.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "❌ Error saving supplier: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "⚠️ Please select a supplier to save.");
            }
        });

        clearBtn.addActionListener(e -> clearForm());

        firstBtn.addActionListener(e -> {
            if (table.getRowCount() > 0) {
                table.setRowSelectionInterval(0, 0);
                statusLabel.setText("First row selected.");
            }
        });

        prevBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow > 0) {
                table.setRowSelectionInterval(selectedRow - 1, selectedRow - 1);
                statusLabel.setText("Previous row selected.");
            }
        });

        nextBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow < table.getRowCount() - 1) {
                table.setRowSelectionInterval(selectedRow + 1, selectedRow + 1);
                statusLabel.setText("Next row selected.");
            }
        });

        lastBtn.addActionListener(e -> {
            int rowCount = table.getRowCount();
            if (rowCount > 0) {
                table.setRowSelectionInterval(rowCount - 1, rowCount - 1);
                statusLabel.setText("Last row selected.");
            }
        });

        

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusLabel = new JLabel("Data refreshed successfully");
        statusPanel.add(statusLabel, BorderLayout.WEST);

        bottomPanel.add(actionsPanel, BorderLayout.WEST);
        bottomPanel.add(navPanel2, BorderLayout.EAST);
        bottomPanel.add(statusPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
        
        table.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                nameField.setText(getValueAsString(row, 1));
                contactField.setText(getValueAsString(row, 2));
                phoneField.setText(getValueAsString(row, 3));
                emailField.setText(getValueAsString(row, 4));
                addressField.setText(getValueAsString(row, 5));
            }
        });


        // === ACTION LISTENERS ===
        insertBtn.addActionListener((ActionEvent e) -> insertSupplier());

        reloadTableFromDatabase();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void insertSupplier() {
        try {
            String name = nameField.getText();
            String contact = contactField.getText();
            String phone = phoneField.getText();
            String email = emailField.getText();
            String address = addressField.getText();

            // === DUPLICATE CHECK ===
            try (Connection conn = DriverManager.getConnection(consts.CONN_STR);
                 PreparedStatement ps = conn.prepareStatement(
                         "SELECT COUNT(*) FROM Supplier WHERE SupplierName=? AND Phone=? AND Email=?")) {
                ps.setString(1, name);
                ps.setString(2, phone);
                ps.setString(3, email);
                ResultSet rs = ps.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this, "⚠️ Supplier already exists.");
                    statusLabel.setText("Duplicate supplier detected");
                    return;
                }
            }

            Supplier supplier = new Supplier(0, name, contact, phone, email, address);
            SupplierManager manager = new SupplierManager();
            manager.addSupplier(supplier);

            JOptionPane.showMessageDialog(this, "✅ Supplier inserted.");
            clearForm();
            reloadTableFromDatabase();
            statusLabel.setText("Inserted supplier successfully");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Error: " + e.getMessage());
            statusLabel.setText("Error inserting supplier");
        }
    }

    private void reloadTableFromDatabase() {
        try (Connection conn = DriverManager.getConnection(consts.CONN_STR);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Supplier")) {

            tableModel.setRowCount(0); // Clear table

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("SupplierID"),
                        rs.getString("SupplierName"),
                        rs.getString("ContactPerson"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getString("SupplierAddress")
                };
                tableModel.addRow(row);
            }
            statusLabel.setText("Data refreshed successfully");
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error loading data");
        }
    }
    

    private void clearForm() {
        nameField.setText("");
        contactField.setText("");
        phoneField.setText("");
        emailField.setText("");
        addressField.setText("");
    }
    private String getValueAsString(int row, int col) {
        Object value = tableModel.getValueAt(row, col);
        return value != null ? value.toString() : "";
    }


    public static void main(String[] args) {
        new SupplierForm();
    }
}
