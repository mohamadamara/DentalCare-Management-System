package entity;

public class Supplier {
	 private int supplierID;
	    private String supplierName;
	    private String contactPerson;
	    private String phone;
	    private String email;
	    private String supplierAddress;
		public Supplier(int supplierID, String supplierName, String contactPerson, String phone, String email,
				String supplierAddress) {
			super();
			this.supplierID = supplierID;
			this.supplierName = supplierName;
			this.contactPerson = contactPerson;
			this.phone = phone;
			this.email = email;
			this.supplierAddress = supplierAddress;
		}
		public int getSupplierID() {
			return supplierID;
		}
		public void setSupplierID(int supplierID) {
			this.supplierID = supplierID;
		}
		public String getSupplierName() {
			return supplierName;
		}
		public void setSupplierName(String supplierName) {
			this.supplierName = supplierName;
		}
		public String getContactPerson() {
			return contactPerson;
		}
		public void setContactPerson(String contactPerson) {
			this.contactPerson = contactPerson;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getSupplierAddress() {
			return supplierAddress;
		}
		public void setSupplierAddress(String supplierAddress) {
			this.supplierAddress = supplierAddress;
		}


}
