package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.NhanVien;


public class DaoNhanVien {
	private ArrayList<NhanVien> dsnv;
	private NhanVien nv;
	private Connection con;

	public DaoNhanVien() {
		dsnv=new ArrayList<NhanVien>();
	}
	public List<NhanVien> getDatabase(){
		try {
			ConnectDB.getInstance();
			Connection con = ConnectDB.getConnection();
			String sql = "Select * from NhanVien";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				String maNV= rs.getString(1);
				String tenNV= rs.getString(2);
				String soCCCD= rs.getString(3);
				Date ngaySinh= rs.getDate(4);
				Boolean gioiTinh= rs.getBoolean(5);
				String sdt= rs.getString(6);
				String email= rs.getString(7);
				String chucVu= rs.getString(8);
				nv= new NhanVien(maNV, tenNV, soCCCD, ngaySinh, gioiTinh, sdt, email, chucVu);
				dsnv.add(nv);
			}
		} catch (SQLException e) {
			//TODO: handle exception
			e.printStackTrace();
		}
		return dsnv;
	}
	public NhanVien getNhanVienTheoTen(String tenNV){
		try {
			ConnectDB.getInstance();
			Connection con = ConnectDB.getConnection();
			String sql = "Select * from NhanVien where tenNhanVien like N'%"+tenNV+"%'";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				String maNV= rs.getString(1);
				String tennv= rs.getString(2);
				String soCCCD= rs.getString(3);
				Date ngaySinh= rs.getDate(4);
				Boolean gioiTinh= rs.getBoolean(5);
				String sdt= rs.getString(6);
				String email= rs.getString(7);
				String chucVu= rs.getString(8);
				nv= new NhanVien(maNV, tennv, soCCCD, ngaySinh, gioiTinh, sdt, email, chucVu);
				
			}
		} catch (SQLException e) {
			//TODO: handle exception
			e.printStackTrace();
		}
		return nv;
	}
	public NhanVien getNhanVienTheoMa(String maNV){
		try {
			ConnectDB.getInstance().connect();
			con = ConnectDB.getConnection();
			if (con == null) {
                System.out.println("Database connection failed!");
                return null;
            }
			String sql = "Select * from NhanVien where maNhanVien like N'%"+maNV+"%'";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				String manv= rs.getString(1);
				String tennv= rs.getString(2);
				String soCCCD= rs.getString(3);
				Date ngaySinh= rs.getDate(4);
				Boolean gioiTinh= rs.getBoolean(5);
				String sdt= rs.getString(6);
				String email= rs.getString(7);
				String chucVu= rs.getString(8);
				nv= new NhanVien(manv, tennv, soCCCD, ngaySinh, gioiTinh, sdt, email, chucVu);
			}
		} catch (SQLException e) {
			//TODO: handle exception
			e.printStackTrace();
		}
		return nv;
	}
	public boolean themNhanVien(NhanVien nv) {
		int n=0;
		try {
			ConnectDB.getInstance();
			Connection con = ConnectDB.getConnection();
			
			String sql = "INSERT INTO NhanVien ([maNhanVien], [tenNhanVien], [soCCCD], [ngaySinh], [gioiTinh], [sdt], [email], [chucVu]) VALUES(?,?,?,?,?,?,?,?)";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setString(1, nv.getMaNV());
			statement.setString(2, nv.getTenNV());
			statement.setString(3,nv.getSoCCCD());
			statement.setDate(4, nv.getNgaySinh());
			statement.setBoolean(5, nv.getGioiTinh());
			statement.setString(6, nv.getSoDT());
			statement.setString(7, nv.getEmail());
			statement.setString(8,nv.getChucVu());
			n = statement.executeUpdate();
			statement.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return n>0;
	}
	
	
	public boolean capnhatNhanVien(NhanVien nv) {
		int n=0;
		try {
			ConnectDB.getInstance().connect();
			Connection con = ConnectDB.getConnection();
			
			String sql = "UPDATE NhanVien set tenNhanVien=?,soCCCD=?, ngaySinh=?, gioiTinh=?, sdt=?, email=?, chucVu=? where maNhanVien=?";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setString(1, nv.getTenNV());
			statement.setString(2,nv.getSoCCCD());
			statement.setDate(3, nv.getNgaySinh());
			statement.setBoolean(4, nv.getGioiTinh());
			statement.setString(5, nv.getSoDT());
			statement.setString(6, nv.getEmail());
			statement.setString(7,nv.getChucVu());
			statement.setString(8, nv.getMaNV());
			n = statement.executeUpdate();
			statement.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return n>0;
	}
	public boolean xoaNhanVien(NhanVien nv) {
		int n=0;
		try {
			ConnectDB.getInstance().connect();;
			Connection con= ConnectDB.getConnection();
			String sql="DELETE FROM NhanVien Where maNhanVien=?";
			PreparedStatement statement= con.prepareStatement(sql);
			statement.setString(1, nv.getMaNV());
		}catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return n>0;
	}
	public static String taomaNV(ArrayList<NhanVien> dsnv2) {
        //return String.format("NV%03d", count);
		ArrayList<String> dsmanv = new ArrayList<String>();
		for (NhanVien nv :dsnv2) {
			dsmanv.add(nv.getMaNV());
		}
		String newID;
        int count = dsmanv.size() + 1;
		do {
			newID=String.format("NV%03d",count );
			count++;
		}while(dsmanv.contains(newID));
		return newID;
	}
	
}





