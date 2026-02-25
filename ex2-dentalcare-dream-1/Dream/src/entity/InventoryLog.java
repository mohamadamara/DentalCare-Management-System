package entity;

import java.sql.Date;

public class InventoryLog {
	 private int logID;
	    private int itemID;
	    private int usedByStaffID;
	    private Date dateUsed;
	    public int getLogID() {
			return logID;
		}
		public void setLogID(int logID) {
			this.logID = logID;
		}
		public int getItemID() {
			return itemID;
		}
		public void setItemID(int itemID) {
			this.itemID = itemID;
		}
		public int getUsedByStaffID() {
			return usedByStaffID;
		}
		public void setUsedByStaffID(int usedByStaffID) {
			this.usedByStaffID = usedByStaffID;
		}
		public Date getDateUsed() {
			return dateUsed;
		}
		public void setDateUsed(Date dateUsed) {
			this.dateUsed = dateUsed;
		}
		public int getQuantityUsed() {
			return quantityUsed;
		}
		public void setQuantityUsed(int quantityUsed) {
			this.quantityUsed = quantityUsed;
		}
		public InventoryLog(int logID, int itemID, int usedByStaffID, Date dateUsed, int quantityUsed) {
			super();
			this.logID = logID;
			this.itemID = itemID;
			this.usedByStaffID = usedByStaffID;
			this.dateUsed = dateUsed;
			this.quantityUsed = quantityUsed;
		}
		private int quantityUsed;


}
