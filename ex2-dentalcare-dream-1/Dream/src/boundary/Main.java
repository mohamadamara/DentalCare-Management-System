package boundary;


import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
    public static void main(String[] args) {

        System.out.println("🔧 Starting DentalCare System...");

        try {
            System.out.println("🔍 Testing DB Connection...");
            Connection conn = DriverManager.getConnection(entity.consts.CONN_STR);
            System.out.println("✅ DB Connected Successfully: " + entity.consts.CONN_STR);
            conn.close();
        } catch (Exception e) {
            System.err.println("❌ DB Connection Failed:");
            e.printStackTrace();
        }

        new MainMenu();     }
}
