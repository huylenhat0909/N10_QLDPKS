package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.KhachHang;

public class DaoKhachHang {
	private ArrayList<KhachHang> dskh;
	private KhachHang kh;

	public DaoKhachHang() {
		dskh=new ArrayList<KhachHang>();
	}
	public List<KhachHang> getDatabase(){
		try {
			ConnectDB.getInstance().connect();
			Connection con = ConnectDB.getConnection();
			String sql = "Select * from KhachHang";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				String maKH= rs.getString(1);
				String tenKH= rs.getString(2);
				String soCCCD= rs.getString(3);
				String sdt= rs.getString(4);
				String email=rs.getString(5);
				kh= new KhachHang(maKH, tenKH, soCCCD, sdt, email);
				dskh.add(kh);
			}
		} catch (SQLException e) {
			//TODO: handle exception
			e.printStackTrace();
		}
		return dskh;
	}
	public List<KhachHang> getKhachHangtheoSDT(String sdtKH){
		try {
			ConnectDB.getInstance();
			Connection con = ConnectDB.getConnection();
			String sql = "Select * from KhachHang where sdt like N'%"+sdtKH+"%'";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				String maKH= rs.getString(1);
				String tenKH= rs.getString(2);
				String soCCCD= rs.getString(3);
				String sdt= rs.getString(4);
				String email=rs.getString(5);
				kh= new KhachHang(maKH, tenKH, soCCCD, sdt, email);
				dskh.add(kh);
			}
		} catch (SQLException e) {
			//TODO: handle exception
			e.printStackTrace();
		}
		return dskh;
	}
	public KhachHang getKhachHangtheoma(String makh){
		try {
			ConnectDB.getInstance().connect();;
			Connection con = ConnectDB.getConnection();
			String sql = "Select * from KhachHang where maKH like N'%"+makh+"%'";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				String maKH= rs.getString(1);
				String tenKH= rs.getString(2);
				String soCCCD= rs.getString(3);
				String sdt= rs.getString(4);
				String email=rs.getString(5);
				kh= new KhachHang(maKH, tenKH, soCCCD, sdt, email);
			}
		} catch (SQLException e) {
			//TODO: handle exception
			e.printStackTrace();
		}
		return kh;
	}
	public boolean themKhachHang(KhachHang kh) {
		int n=0;
		try {
			ConnectDB.getInstance();
			Connection con = ConnectDB.getConnection();
			
			String sql = "INSERT INTO KhachHang ([maKH], [tenKH], [CCCD], [sdt], [email] ) VALUES(?,?,?,?,?)";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setString(1, kh.getMaKH());
			statement.setString(2, kh.getTenKH());
			statement.setString(3,kh.getCCCD());
			statement.setString(4, kh.getSoDT());
			statement.setString(5, kh.getEmail());
			n = statement.executeUpdate();
			statement.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return n>0;
	}
	
	
	public boolean capnhatKhachHang(KhachHang kh) {
		int n=0;
		try {
			ConnectDB.getInstance();
			Connection con = ConnectDB.getConnection();
			
			String sql = "UPDATE KhachHang set tenKH=?,CCCD=?,sdt=?, email=? where maKH=?";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setString(1, kh.getTenKH());
			statement.setString(2,kh.getCCCD());
			statement.setString(3, kh.getSoDT());
			statement.setString(4, kh.getEmail());
			statement.setString(5, kh.getMaKH());
			n = statement.executeUpdate();
			statement.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return n>0;
	}
	public boolean xoaKhachHang(KhachHang kh) {
		int n=0;
		try {
			ConnectDB.getInstance();
			Connection con= ConnectDB.getConnection();
			String sql="DELETE FROM KhachHang Where maKH=?";
			PreparedStatement statement= con.prepareStatement(sql);
			statement.setString(1, kh.getMaKH());
		}catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return n>0;
	}
	public String taomaKH(List<KhachHang> dskh2) {
		ArrayList<String> dsmakh = new ArrayList<String>();
		for (KhachHang kh :dskh2) {
			dsmakh.add(kh.getMaKH());
		}
		String newID;
        int count = dsmakh.size() + 1;
		do {
			newID=String.format("KH%03d",count );
			count++;
		}while(dsmakh.contains(newID));
		return newID;
	}
}
