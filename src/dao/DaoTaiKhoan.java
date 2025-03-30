package dao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.NhanVien;
import entity.TaiKhoan;

public class DaoTaiKhoan {
	private ArrayList<TaiKhoan> dstk;
	private TaiKhoan tk;
	private DaoNhanVien daonv;
	private Connection con;
	public DaoTaiKhoan() {
		dstk= new ArrayList<TaiKhoan>();
	}
	public TaiKhoan getTaiKhoantheoTen(String username) {
		try {
			ConnectDB.getInstance().connect();
			con= ConnectDB.getConnection();
			if (con == null) {
                System.out.println("Database connection failed!");
                return null;
            }
			String sql="SELECT * FROM TaiKhoan WHERE taiKhoan like N'"+username+"'";
			Statement statemnet= con.createStatement();
			ResultSet rs= statemnet.executeQuery(sql);
			while(rs.next()) {
				String taiKhoan= rs.getString(1);
				String matkhau= rs.getString(2);
				String maNV= rs.getString(3);
				daonv= new DaoNhanVien();
				NhanVien nv= new DaoNhanVien().getNhanVienTheoMa(maNV);
				tk= new TaiKhoan(nv, taiKhoan, matkhau);
			}
			
		}catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return tk;
	}
}
