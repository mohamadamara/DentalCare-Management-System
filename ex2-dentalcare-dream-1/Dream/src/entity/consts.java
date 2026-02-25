package entity;

import java.net.URLDecoder;

public class consts {
	
	private consts() {
        throw new AssertionError();
    }

 protected static final String DB_FILEPATH = getDBPath();


    public static final String CONN_STR = "jdbc:ucanaccess://"  + DB_FILEPATH + ";COLUMNORDER=DISPLAY";
    
 // === SUPPLIER SQL ===
    public static final String SQL_select_Supplier_By_Id =
        "SELECT * FROM Supplier WHERE SupplierID = ?";

    public static final String SQL_insert_Supplier =
        "INSERT INTO Supplier (SupplierName, ContactPerson, Phone, Email, SupplierAddress) " +
        "VALUES (?, ?, ?, ?, ?)";

    public static final String SQL_update_Supplier =
        "UPDATE Supplier SET SupplierName = ?, ContactPerson = ?, Phone = ?, Email = ?, SupplierAddress = ? " +
        "WHERE SupplierID = ?";

    public static final String SQL_delete_Supplier =
        "DELETE FROM Supplier WHERE SupplierID = ?";

    // === INVENTORY SQL ===
    public static final String SQL_select_Inventory_By_Id =
        "SELECT * FROM Inventory WHERE ItemID = ?";

    public static final String SQL_insert_Inventory =
        "INSERT INTO Inventory (ItemName, Description, CategoryID, Quantity, Supplier, ExpirationDate, SerialNumber) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";

    public static final String SQL_update_Inventory =
        "UPDATE Inventory SET ItemName = ?, Description = ?, CategoryID = ?, Quantity = ?, Supplier = ?, " +
        "ExpirationDate = ?, SerialNumber = ?, SupplierID = ? " +
        "WHERE ItemID = ?";

    public static final String SQL_delete_Inventory =
        "DELETE FROM Inventory WHERE ItemID = ?";

    private static String getDBPath() {
        try {
            String path = consts.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String decoded = URLDecoder.decode(path, "UTF-8");
            if (decoded.contains(".jar")) {
                decoded = decoded.substring(0, decoded.lastIndexOf('/'));
                System.out.println("USING DB PATH: " + decoded + "/database/EX1_access_final_Dream.accdb");
                return decoded + "/database/EX1_access_final_Dream.accdb";
            } else {
                decoded = decoded.substring(0, decoded.lastIndexOf('/'));
                System.out.println("USING DB PATH: " + decoded + "/entity/EX1_access_final_Dream.accdb");
                return decoded + "/entity/EX1_access_final_Dream.accdb";
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

	
}