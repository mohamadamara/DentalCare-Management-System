package control;

import entity.Supplier;
import entity.consts;

import java.sql.*;
import java.util.ArrayList;

public class SupplierManager {

    public void addSupplier(Supplier supplier) {
        try (Connection conn = DriverManager.getConnection(consts.CONN_STR)) {
            PreparedStatement stmt = conn.prepareStatement(consts.SQL_insert_Supplier);

            stmt.setString(1, supplier.getSupplierName());
            stmt.setString(2, supplier.getContactPerson());
            stmt.setString(3, supplier.getPhone());
            stmt.setString(4, supplier.getEmail());
            stmt.setString(5, supplier.getSupplierAddress());

            stmt.executeUpdate();
            System.out.println("✅ Supplier inserted.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateSupplier(Supplier supplier) {
        try (Connection conn = DriverManager.getConnection(consts.CONN_STR)) {
            PreparedStatement stmt = conn.prepareStatement(consts.SQL_update_Supplier);

            stmt.setString(1, supplier.getSupplierName());
            stmt.setString(2, supplier.getContactPerson());
            stmt.setString(3, supplier.getPhone());
            stmt.setString(4, supplier.getEmail());
            stmt.setString(5, supplier.getSupplierAddress());
            stmt.setInt(6, supplier.getSupplierID());

            int rows = stmt.executeUpdate();
            System.out.println("✅ Updated " + rows + " supplier(s).");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean deleteSupplier(int supplierId) {
        String sql = "DELETE FROM Supplier WHERE supplierId = ?";
        try (Connection conn = DriverManager.getConnection(consts.CONN_STR);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, supplierId);
            stmt.executeUpdate(); // ✅ Don't check result
            return true; // ✅ If no error, assume success

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Supplier getSupplierById(int supplierId) {
        try (Connection conn = DriverManager.getConnection(consts.CONN_STR)) {
            PreparedStatement stmt = conn.prepareStatement(consts.SQL_select_Supplier_By_Id);

            stmt.setInt(1, supplierId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Supplier(
                        rs.getInt("SupplierID"),
                        rs.getString("SupplierName"),
                        rs.getString("ContactPerson"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getString("SupplierAddress")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<Supplier> getAllSuppliers() {
        ArrayList<Supplier> list = new ArrayList<>();
        String sql = "SELECT * FROM Supplier";

        try (Connection conn = DriverManager.getConnection(consts.CONN_STR);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Supplier supplier = new Supplier(
                        rs.getInt("SupplierID"),
                        rs.getString("SupplierName"),
                        rs.getString("ContactPerson"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getString("SupplierAddress")
                );
                list.add(supplier);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
