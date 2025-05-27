package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.KhuyenMai;
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
	public List<TaiKhoan> getDatabase() {
		try {
			ConnectDB.getInstance().connect();
			Connection con = ConnectDB.getConnection();
			String sql = "Select * from TaiKhoan";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				String maNV=rs.getString(3);
				String taikhoan= rs.getString(1);
				String matkhau= rs.getString(2);
				DaoNhanVien daonv= new DaoNhanVien();
				NhanVien nv= daonv.getNhanVienTheoMa(maNV);
				tk= new TaiKhoan(nv, taikhoan, matkhau);
				dstk.add(tk);
			}
		}catch (SQLException e) {
			//TODO: handle exception
			e.printStackTrace();
	}
		return dstk;
	}
	public boolean capNhatTK(TaiKhoan tk_new) {
		int n=0;
		try {
			ConnectDB.getInstance().connect();
			Connection con= ConnectDB.getConnection();
			String sql="UPDATE TaiKhoan set taiKhoan=?,matKhau=? where maNhanVien=?";
			PreparedStatement statement= con.prepareStatement(sql);
			statement.setString(1,tk_new.getTaiKhoan());
			statement.setString(2,tk_new.getMatKhau());
			statement.setString(3,tk_new.getNhanVien().getMaNV());
			n=statement.executeUpdate();
			statement.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return n>0;
	}
	public boolean themTaiKhoan(TaiKhoan taikhoan) {
		// TODO Auto-generated method stub
		int n = 0;
		try {
		    ConnectDB.getInstance().connect();
		    Connection con = ConnectDB.getConnection();
		    
		    String sql = "INSERT INTO TaiKhoan (taiKhoan, matKhau, maNhanVien) VALUES (?, ?, ?)";
		    PreparedStatement statement = con.prepareStatement(sql);
		    statement.setString(1, taikhoan.getTaiKhoan());
		    statement.setString(2, taikhoan.getMatKhau());
		    statement.setString(3, taikhoan.getNhanVien().getMaNV());

		    n = statement.executeUpdate();
		    statement.close();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
		return n > 0;
	}
	public boolean xoaTaiKhoan(String tenTaiKhoan) {
	    int n = 0;
	    try {
	        ConnectDB.getInstance().connect();
	        Connection con = ConnectDB.getConnection();

	        String sql = "DELETE FROM TaiKhoan WHERE taiKhoan = ?";
	        PreparedStatement statement = con.prepareStatement(sql);
	        statement.setString(1, tenTaiKhoan);

	        n = statement.executeUpdate();
	        statement.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return n > 0;
	}
	}

