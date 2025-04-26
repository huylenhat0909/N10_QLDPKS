package dao;

import java.awt.geom.Arc2D.Double;
import java.sql.Connection;
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
}
