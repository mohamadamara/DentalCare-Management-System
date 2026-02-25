package boundary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

import control.SupirImporter;

public class UploadXMLForm extends JFrame {

    private static final long serialVersionUID = 1L;

    public UploadXMLForm() {
        setTitle("XML Import/Export – DentalCare");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // === TOP NAVIGATION (no XML button here) ===
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton inventoryBtn = new JButton("🧾 Inventory");
        JButton supplierBtn = new JButton("🏬 Supplier");

        topBar.add(inventoryBtn);
        topBar.add(supplierBtn);
        add(topBar, BorderLayout.NORTH);

        inventoryBtn.addActionListener(e -> {
            dispose();
            new InventoryForm();
        });

        supplierBtn.addActionListener(e -> {
            dispose();
            new SupplierForm();
        });

        // === CENTER PANEL with Import/Export options ===
        JPanel center = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel label = new JLabel("Select XML Action:");
        label.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        center.add(label, gbc);

        String[] options = { "Import XML", "Export XML" };
        JComboBox<String> actionSelector = new JComboBox<>(options);
        gbc.gridx = 1;
        center.add(actionSelector, gbc);

        JButton runBtn = new JButton("▶ Run Selected Action");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        center.add(runBtn, gbc);

        add(center, BorderLayout.CENTER);

        // === Action logic for Import or Export ===
        runBtn.addActionListener((ActionEvent e) -> {
            String choice = (String) actionSelector.getSelectedItem();
            JFileChooser chooser = new JFileChooser();

            if ("Import XML".equals(choice)) {
                chooser.setDialogTitle("Choose an XML file to import");
                int result = chooser.showOpenDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File xmlFile = chooser.getSelectedFile();
                    if (!xmlFile.exists() || !xmlFile.getName().toLowerCase().endsWith(".xml")) {
                        JOptionPane.showMessageDialog(this,
                                "⚠️ Please select a valid .xml file.",
                                "Invalid File",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    try {
                        new SupirImporter().importFromXML(xmlFile.getAbsolutePath());
                        JOptionPane.showMessageDialog(this, "✅ XML imported successfully.");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this,
                                "❌ Failed to import: " + ex.getMessage(),
                                "Import Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

            else if ("Export XML".equals(choice)) {
                chooser.setDialogTitle("Choose where to save the XML");
                int result = chooser.showSaveDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    if (!file.getName().toLowerCase().endsWith(".xml")) {
                        file = new File(file.getAbsolutePath() + ".xml");
                    }

                    try {
                        new SupirImporter().exportToXML(file.getAbsolutePath());
                        JOptionPane.showMessageDialog(this, "✅ Exported to: " + file.getName());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this,
                                "❌ Export failed: " + ex.getMessage(),
                                "Export Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new UploadXMLForm();
    }
}
