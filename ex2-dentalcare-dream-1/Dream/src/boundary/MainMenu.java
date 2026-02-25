package boundary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainMenu extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MainMenu() {
        setTitle("DentalCare – Main Menu");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Welcome to DentalCare System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        JButton inventoryBtn = new JButton("🧾 Open Inventory Page");
        JButton supplierBtn = new JButton("🏬 Open Supplier Page");
        JButton xmlBtn = new JButton("📦 Open XML Import Page");

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        buttonPanel.add(inventoryBtn);
        buttonPanel.add(supplierBtn);
        buttonPanel.add(xmlBtn);

        add(buttonPanel, BorderLayout.CENTER);

        // === Button actions ===
        inventoryBtn.addActionListener((ActionEvent e) -> {
            dispose(); // close this window
            new InventoryForm(); // open Inventory
        });

        supplierBtn.addActionListener((ActionEvent e) -> {
            dispose();
            new SupplierForm();
        });

        xmlBtn.addActionListener((ActionEvent e) -> {
            dispose();
            new UploadXMLForm();
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new MainMenu();
    }
}
