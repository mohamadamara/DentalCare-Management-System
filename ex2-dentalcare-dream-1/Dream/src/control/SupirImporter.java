																																							package control;

import entity.consts;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SupirImporter {

	public void importFromXML(String xmlPath) {
	    try {
	        // === Load XML File ===
	        File file = new File(xmlPath);
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        Document doc = builder.parse(file);
	        doc.getDocumentElement().normalize();

	        // === Open DB Connection ===
	        try (Connection conn = DriverManager.getConnection(consts.CONN_STR)) {

	            // === Insert Supplier ===
	            Element supplier = (Element) doc.getElementsByTagName("Supplier").item(0);
	            String name = getText(supplier, "SupplierName");
	            String contact = getText(supplier, "ContactPerson");
	            String phone = getText(supplier, "Phone");
	            String email = getText(supplier, "Email");
	            String address = getText(supplier, "SupplierAddress");

	            PreparedStatement sStmt = conn.prepareStatement(consts.SQL_insert_Supplier);
	            sStmt.setString(1, name);
	            sStmt.setString(2, contact);
	            sStmt.setString(3, phone);
	            sStmt.setString(4, email);
	            sStmt.setString(5, address);
	            sStmt.executeUpdate();

	            // === Get inserted Supplier ID ===
	            int supplierID = -1;
	            PreparedStatement getIdStmt = conn.prepareStatement("SELECT SupplierID FROM Supplier WHERE SupplierName = ?");
	            getIdStmt.setString(1, name);
	            ResultSet rs = getIdStmt.executeQuery();
	            if (rs.next()) {
	                supplierID = rs.getInt("SupplierID");
	            }

	            // === Insert Inventory Items ===
	            NodeList items = doc.getElementsByTagName("Item");
	            for (int i = 0; i < items.getLength(); i++) {
	                Element item = (Element) items.item(i);
	                String itemName = getText(item, "ItemName");
	                String description = getText(item, "Description");
	                int categoryID = Integer.parseInt(getText(item, "CategoryID"));
	                int quantity = Integer.parseInt(getText(item, "Quantity"));
	                String serial = getText(item, "SerialNumber");
	                String exp = getText(item, "ExpirationDate");

	                PreparedStatement iStmt = conn.prepareStatement(consts.SQL_insert_Inventory);
	                iStmt.setString(1, itemName);
	                iStmt.setString(2, description);
	                iStmt.setInt(3, categoryID);
	                iStmt.setInt(4, quantity);
	                iStmt.setInt(5, supplierID); // ✅ insert ID, not name
	                iStmt.setDate(6, java.sql.Date.valueOf(exp));
	                iStmt.setString(7, serial);
	                iStmt.executeUpdate();
	            }

	            System.out.println("✅ XML data imported successfully.");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


    public void exportToXML(String filePath) {
        try (Connection conn = DriverManager.getConnection(consts.CONN_STR)) {

            // === Set up XML DOM ===
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element root = doc.createElement("DentalCareExport");
            doc.appendChild(root);

            // === Export Suppliers ===
            PreparedStatement psSup = conn.prepareStatement("SELECT * FROM Supplier");
            ResultSet rsSup = psSup.executeQuery();

            Element suppliersElement = doc.createElement("Suppliers");
            root.appendChild(suppliersElement);

            while (rsSup.next()) {
                Element supplier = doc.createElement("Supplier");

                Element name = doc.createElement("SupplierName");
                name.appendChild(doc.createTextNode(rsSup.getString("SupplierName")));
                supplier.appendChild(name);

                Element contact = doc.createElement("ContactPerson");
                contact.appendChild(doc.createTextNode(rsSup.getString("ContactPerson")));
                supplier.appendChild(contact);

                Element phone = doc.createElement("Phone");
                phone.appendChild(doc.createTextNode(rsSup.getString("Phone")));
                supplier.appendChild(phone);

                Element email = doc.createElement("Email");
                email.appendChild(doc.createTextNode(rsSup.getString("Email")));
                supplier.appendChild(email);

                Element address = doc.createElement("SupplierAddress");
                address.appendChild(doc.createTextNode(rsSup.getString("SupplierAddress")));
                supplier.appendChild(address);

                suppliersElement.appendChild(supplier);
            }

            // === Export Inventory Items ===
            PreparedStatement psItems = conn.prepareStatement("SELECT * FROM Inventory");
            ResultSet rsItems = psItems.executeQuery();

            Element inventoryElement = doc.createElement("InventoryItems");
            root.appendChild(inventoryElement);

            while (rsItems.next()) {
                Element item = doc.createElement("Item");

                Element itemName = doc.createElement("ItemName");
                itemName.appendChild(doc.createTextNode(rsItems.getString("ItemName")));
                item.appendChild(itemName);

                Element description = doc.createElement("Description");
                description.appendChild(doc.createTextNode(rsItems.getString("Description")));
                item.appendChild(description);

                Element categoryID = doc.createElement("CategoryID");
                categoryID.appendChild(doc.createTextNode(String.valueOf(rsItems.getInt("CategoryID"))));
                item.appendChild(categoryID);

                Element quantity = doc.createElement("Quantity");
                quantity.appendChild(doc.createTextNode(String.valueOf(rsItems.getInt("Quantity"))));
                item.appendChild(quantity);

                Element supplier = doc.createElement("Supplier");
                supplier.appendChild(doc.createTextNode(rsItems.getString("Supplier")));
                item.appendChild(supplier);

                Element serial = doc.createElement("SerialNumber");
                serial.appendChild(doc.createTextNode(rsItems.getString("SerialNumber")));
                item.appendChild(serial);

                Element exp = doc.createElement("ExpirationDate");
                exp.appendChild(doc.createTextNode(String.valueOf(rsItems.getDate("ExpirationDate"))));
                item.appendChild(exp);

                Element supplierID = doc.createElement("SupplierID");
                supplierID.appendChild(doc.createTextNode(String.valueOf(rsItems.getInt("SupplierID"))));
                item.appendChild(supplierID);

                inventoryElement.appendChild(item);
            }

            // === Write XML to file ===
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new FileWriter(filePath));
            transformer.transform(source, result);

            System.out.println("✅ XML exported successfully to " + filePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getText(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes != null && nodes.getLength() > 0 && nodes.item(0) != null) {
            return nodes.item(0).getTextContent();
        } else {
            System.err.println("⚠️ Missing tag: <" + tagName + ">");
            return ""; // או null אם אתה רוצה לזהות שגיאות
        }
    }}
