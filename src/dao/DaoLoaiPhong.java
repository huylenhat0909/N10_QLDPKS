package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.LoaiPhong;

public class DaoLoaiPhong {
	private ArrayList<LoaiPhong> dslPhong;
	private LoaiPhong lphong;

	public DaoLoaiPhong() {
		dslPhong= new ArrayList<LoaiPhong>();
	}
	public List<entity.LoaiPhong> getDatabase(){
		try {
			ConnectDB.getInstance().connect();
			Connection con = ConnectDB.getConnection();
			String sql = "Select * from LoaiPhong";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				String maLPhong= rs.getString(1);
				String tenLPhong= rs.getString(2);
				Integer soTang = rs.getInt(3);
				String moTa= rs.getString(4);
				Double giaPhongGio= rs.getDouble(5);
				Double giaPhongNgay= rs.getDouble(6);
				lphong= new LoaiPhong(maLPhong, tenLPhong, soTang, moTa, giaPhongGio, giaPhongNgay);
				dslPhong.add(lphong);
			}
		} catch (SQLException e) {
			//TODO: handle exception
			e.printStackTrace();
		}
		return dslPhong;
	}
	public List<LoaiPhong> getLoaiPhongTheoTen(String tenLPhong){
		try {
			ConnectDB.getInstance();
			Connection con= ConnectDB.getConnection();
			String sql = "Select * from LoaiPhong where tenLoaiPhong like N'%"+tenLPhong+"%'";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				String maLPhong= rs.getString(1);
				String tenlPhong= rs.getString(2);
				Integer soTang = rs.getInt(3);
				String moTa= rs.getString(4);
				Double giaPhongGio= rs.getDouble(5);
				Double giaPhongNgay= rs.getDouble(6);
				lphong= new LoaiPhong(maLPhong, tenlPhong, soTang, moTa, giaPhongGio, giaPhongNgay);
				dslPhong.add(lphong);
			}
		}catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return dslPhong;
	}
	public boolean themLoaiPhong(LoaiPhong lphong) {
		int n =0;
		try {
			ConnectDB.getInstance();
			Connection con= ConnectDB.getConnection();
			String sql= "INSERT INTO LoaiPhong([maLoaiPhong],[tenLoaiPhong],[soTang],[moTa],[giaPhongTheoGio],[giaPhongTheoNgay]) VALUES(?,?,?,?,?,?)";
			PreparedStatement statement= con.prepareStatement(sql);
			statement.setString(1,lphong.getMaLoaiP());
			statement.setString(2, lphong.getTenLoaiP());
			statement.setString(3, lphong.getMoTa());
			statement.setDouble(4,lphong.getGiaPhongtheogio());
			statement.setString(5, lphong.getTenLoaiP());
		}catch(SQLException E) {
			E.printStackTrace();
		}
		return n>0;
	}
	
}
