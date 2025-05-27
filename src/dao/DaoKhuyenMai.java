package dao;

import java.awt.geom.Arc2D.Double;
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
import entity.LoaiPhong;
import entity.Phong;

public class DaoKhuyenMai {
	private ArrayList<KhuyenMai> dsKM;
	private KhuyenMai khuyenmai;

	public DaoKhuyenMai() {
		dsKM= new ArrayList<KhuyenMai>();
	}
	public List<KhuyenMai> getDatabase(){
		try {
			ConnectDB.getInstance().connect();
			Connection con = ConnectDB.getConnection();
			String sql = "Select * from KhuyenMai";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				String maKhuyenmai=rs.getString(1);
				String moTa= rs.getString(2);
				Timestamp time= rs.getTimestamp(3);
				LocalDateTime ngayApDung =time.toLocalDateTime();
				Timestamp time1= rs.getTimestamp(4);
				LocalDateTime ngayhetHan =time1.toLocalDateTime();
				double tienapKM = rs.getDouble(5);
				double phantrankm = rs.getDouble(6);
				khuyenmai=new KhuyenMai(maKhuyenmai, moTa, ngayApDung, ngayhetHan, tienapKM,phantrankm);
				dsKM.add(khuyenmai);
			}
		}catch (SQLException e) {
			//TODO: handle exception
			e.printStackTrace();
	}
		return dsKM;
	}
	public KhuyenMai getKhuyenMaitheoMa(String ma) {
		try {
			ConnectDB.getInstance().connect();;
			Connection con= ConnectDB.getConnection();
			String sql = "Select * from KhuyenMai where maKM like N'%"+ma+"%'";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				String maKhuyenmai=rs.getString(1);
				String moTa= rs.getString(2);
				Timestamp time= rs.getTimestamp(3);
				LocalDateTime ngayApDung =time.toLocalDateTime();
				Timestamp time1= rs.getTimestamp(4);
				LocalDateTime ngayhetHan =time1.toLocalDateTime();
				double tienapKM = rs.getDouble(5);
				double phantrankm = rs.getDouble(6);
				khuyenmai=new KhuyenMai(maKhuyenmai, moTa, ngayApDung, ngayhetHan, tienapKM,phantrankm);
			}
		}catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return khuyenmai;
	}
	public KhuyenMai getKhuyenMaitheoTen(String tenKM) {
		try {
			ConnectDB.getInstance().connect();;
			Connection con= ConnectDB.getConnection();
			String sql = "Select * from KhuyenMai where moTa like N'%"+tenKM+"%'";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				String maKhuyenmai=rs.getString(1);
				String moTa= rs.getString(2);
				Timestamp time= rs.getTimestamp(3);
				LocalDateTime ngayApDung =time.toLocalDateTime();
				Timestamp time1= rs.getTimestamp(4);
				LocalDateTime ngayhetHan =time1.toLocalDateTime();
				double tienapKM = rs.getDouble(5);
				double phantrankm = rs.getDouble(6);
				khuyenmai=new KhuyenMai(maKhuyenmai, moTa, ngayApDung, ngayhetHan, tienapKM,phantrankm);
			}
		}catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return khuyenmai;
	}
	public boolean capNhatKhuyenMai(KhuyenMai km) {
	    int n = 0;
	    try {
	        ConnectDB.getInstance().connect();
	        Connection con = ConnectDB.getConnection();
	        String sql = "UPDATE KhuyenMai SET moTa = ?, ngayApDung = ?, ngayHetHan = ?, tienApDungKM = ?, phanTramKM = ? WHERE maKM = ?";
	        PreparedStatement statement = con.prepareStatement(sql);
	        statement.setString(1, km.getTenKM());
	        statement.setTimestamp(2, Timestamp.valueOf(km.getNgayApDung()));
	        statement.setTimestamp(3, Timestamp.valueOf(km.getNgayHetHan()));
	        statement.setDouble(4, km.getTienApdungKM());
	        statement.setDouble(5, km.getPhanTramKM());
	        statement.setString(6, km.getMaKM());
	        n = statement.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return n > 0;
	}
	
	
	
	public boolean xoaKhuyenMai(String maKM) {
	    int n = 0;
	    try {
	        ConnectDB.getInstance().connect();
	        Connection con = ConnectDB.getConnection();
	        String sql = "DELETE FROM KhuyenMai WHERE maKM = ?";
	        PreparedStatement statement = con.prepareStatement(sql);
	        statement.setString(1, maKM);
	        n = statement.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return n > 0;
	}
	public boolean themKhuyenMai(KhuyenMai km) {
	    int n = 0;
	    try {
	        ConnectDB.getInstance().connect();
	        Connection con = ConnectDB.getConnection();
	        String sql = "INSERT INTO KhuyenMai([maKM], [moTa], [ngayApDung], [ngayHetHan], [tienApDungKM], [phanTramKM]) VALUES (?, ?, ?, ?, ?, ?)";
	        PreparedStatement statement = con.prepareStatement(sql);
	        statement.setString(1, km.getMaKM());
	        statement.setString(2, km.getTenKM());
	        statement.setTimestamp(3, Timestamp.valueOf(km.getNgayApDung()));
	        statement.setTimestamp(4, Timestamp.valueOf(km.getNgayHetHan()));
	        statement.setDouble(5, km.getTienApdungKM());
	        statement.setDouble(6, km.getPhanTramKM());
	        n = statement.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return n > 0;
	}
	public static String taomaDV(List<KhuyenMai> dskm) {
        //return String.format("NV%03d", count);
		ArrayList<String> dsma = new ArrayList<String>();
		for (KhuyenMai km: dskm) {
			dsma.add(km.getMaKM());
		}
		String newID;
        int count = dsma.size() + 1;
		do {
			newID=String.format("MKM%03d",count );
			count++;
		}while(dsma.contains(newID));
		return newID;
	}
}
