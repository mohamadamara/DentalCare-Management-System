package control;

import java.sql.*;
import java.util.ArrayList;
import entity.InventoryItem;
import entity.consts;

public class InventoryManager {

    public ArrayList<InventoryItem> getAllInventory() {
        ArrayList<InventoryItem> list = new ArrayList<>();
        String sql = "SELECT * FROM Inventory";

        try (Connection conn = DriverManager.getConnection(consts.CONN_STR);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                InventoryItem inventory = new InventoryItem(
                    rs.getInt("ItemID"),
                    rs.getString("ItemName"),
                    rs.getString("Description"),
                    rs.getInt("CategoryID"),
                    rs.getInt("Quantity"),
                    rs.getInt("Supplier"), // FIXED: changed from getInt to getString
                    rs.getString("ExpirationDate"),
                    rs.getString("SerialNumber")
                );
                list.add(inventory);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public InventoryItem getInventoryById(int itemId) {
        String sql = "SELECT * FROM Inventory WHERE ItemID = ?";
        try (Connection conn = DriverManager.getConnection(consts.CONN_STR);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, itemId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new InventoryItem(
                    rs.getInt("ItemID"),
                    rs.getString("ItemName"),
                    rs.getString("Description"),
                    rs.getInt("CategoryID"),
                    rs.getInt("Quantity"),
                    rs.getInt("Supplier"), // FIXED: changed from getInt to getString
                    rs.getString("ExpirationDate"),
                    rs.getString("SerialNumber")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean addItem(InventoryItem item) {
        String sql = "INSERT INTO Inventory (ItemName, Description, CategoryID, Quantity, Supplier, ExpirationDate, SerialNumber) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(consts.CONN_STR);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getItemName());
            stmt.setString(2, item.getDescription());
            stmt.setInt(3, item.getCategoryID());
            stmt.setInt(4, item.getQuantity());
            stmt.setInt(5, item.getSupplierID()); // FIXED: changed from setInt to setString
            stmt.setString(6, item.getExpirationDate());
            stmt.setString(7, item.getSerialNumber());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateInventory(InventoryItem item) {
        String sql = "UPDATE Inventory SET ItemName = ?, Description = ?, CategoryID = ?, Quantity = ?, Supplier = ?, " +
                     "ExpirationDate = ?, SerialNumber = ? WHERE ItemID = ?";

        try (Connection conn = DriverManager.getConnection(consts.CONN_STR);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getItemName());
            stmt.setString(2, item.getDescription());
            stmt.setInt(3, item.getCategoryID());
            stmt.setInt(4, item.getQuantity());
            stmt.setInt(5, item.getSupplierID());
            stmt.setString(6, item.getExpirationDate());
            stmt.setString(7, item.getSerialNumber());
            stmt.setInt(8, item.getItemID());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteInventory(int itemId) {
        String sql = "DELETE FROM Inventory WHERE ItemID = ?";

        try (Connection conn = DriverManager.getConnection(consts.CONN_STR);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, itemId);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public boolean deleteInventoryBySupplier(int supplierId) {
        String sql = "DELETE FROM Inventory WHERE Supplier = ?";
        try (Connection conn = DriverManager.getConnection(consts.CONN_STR);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

        	stmt.setInt(1, supplierId);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
