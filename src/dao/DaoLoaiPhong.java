package dao;

import java.sql.Connection;
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
	
}
