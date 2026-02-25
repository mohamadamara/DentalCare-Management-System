package entity;

public class InventoryItem {
    private int itemID;
    private String itemName;
    private String description;
    private int categoryID;
    private int quantity;
    private int supplierID;  // CHANGED from String to int
    private String expirationDate;
    private String serialNumber;

    // === Constructor with all fields ===
    public InventoryItem(int itemID, String itemName, String description, int categoryID, int quantity,
                         int supplierID, String expirationDate, String serialNumber) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.description = description;
        this.categoryID = categoryID;
        this.quantity = quantity;
        this.supplierID = supplierID;
        this.expirationDate = expirationDate;
        this.serialNumber = serialNumber;
    }

    // === Getters and Setters ===
    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}
