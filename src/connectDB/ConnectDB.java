package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectDB {
	public static Connection con = null;
	private static ConnectDB instance = new ConnectDB();
	public static ConnectDB getInstance() {
		return instance;
	}
	public void connect() {
		String url="jdbc:sqlserver://localhost:1433;databasename=QLDPKS";
		String user = "sa";
		String password = "sapassword";
		try { 
			con =DriverManager.getConnection(url,user,password);
			
		} catch (SQLException e) {
			//TODO: handle exception
			e.printStackTrace();
		}
	}
	public static Connection getConnection() {
		return con;
	}
	public void disconnect()
	{
		if(con!= null)
			try {
				con.close();
			} catch (SQLException e) {
				//TODO: handle exception
				e.printStackTrace();
			}
	}
	public static void main(String[] args) {
		ConnectDB db = ConnectDB.getInstance(); // Lấy instance
	    db.connect(); // Gọi hàm connect() để thiết lập kết nối
		if (con == null) {
		    System.out.println("Kết nối that bai!");
		} else {
		    System.out.println("Kết nối thanh cong!");
		}
	}
}
