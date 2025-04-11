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
import entity.NhanVien;

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
				String moTa= rs.getString(3);
				Double giaPhongGio= rs.getDouble(4);
				Double giaPhongNgay= rs.getDouble(5);
				lphong= new LoaiPhong(maLPhong, tenLPhong, moTa, giaPhongGio, giaPhongNgay);
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
				String moTa= rs.getString(3);
				Double giaPhongGio= rs.getDouble(4);
				Double giaPhongNgay= rs.getDouble(5);
				lphong= new LoaiPhong(maLPhong, tenlPhong, moTa, giaPhongGio, giaPhongNgay);
				dslPhong.add(lphong);
			}
		}catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return dslPhong;
	}
	public LoaiPhong getLoaiPhongTheoMa(String ma){
		try {
			ConnectDB.getInstance();
			Connection con= ConnectDB.getConnection();
			String sql = "Select * from LoaiPhong where maLoaiPhong like N'%"+ma+"%'";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				String maLPhong= rs.getString(1);
				String tenlPhong= rs.getString(2);
				String moTa= rs.getString(3);
				Double giaPhongGio= rs.getDouble(4);
				Double giaPhongNgay= rs.getDouble(5);
				lphong= new LoaiPhong(maLPhong, tenlPhong, moTa, giaPhongGio, giaPhongNgay);
			}
		}catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return lphong;
	}
	public boolean themLoaiPhong(LoaiPhong lphong) {
		int n =0;
		try {
			ConnectDB.getInstance();
			Connection con= ConnectDB.getConnection();
			String sql= "INSERT INTO LoaiPhong([maLoaiPhong],[tenLoaiPhong],[moTa],[giaPhongTheoGio],[giaPhongTheoNgay]) VALUES(?,?,?,?,?)";
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
	public boolean capnhatLoaiPhong(LoaiPhong lp) {
		int n=0;
		try {
			ConnectDB.getInstance().connect();
			Connection con= ConnectDB.getConnection();
			String sql="UPDATE LoaiPhong set tenLoaiPhong=?,moTa=?, giaPhonggio=?, giaPhongngay=? where maLoaiPhong=?";
			PreparedStatement statement= con.prepareStatement(sql);
			statement.setString(1, lp.getTenLoaiP());
			statement.setString(2, lp.getMoTa());
			statement.setDouble(3, lp.getGiaPhongtheogio());
			statement.setDouble(4, lp.getGiaPhongtheongay());
			statement.setString(5, lp.getMaLoaiP());
			n=statement.executeUpdate();
			statement.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return n>0;
	}
	public boolean xoaLoaiPhong(LoaiPhong lp) {
		int n=0;
		try {
			ConnectDB.getInstance().connect();
			Connection con= ConnectDB.getConnection();
			String sql="DELETE FROM LoaiPhong Where maLoaiPhong=?";
			PreparedStatement statement= con.prepareStatement(sql);
			statement.setString(1, lp.getMaLoaiP());
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return n>0;
	}
	public static String taomaLP(ArrayList<LoaiPhong> dslp2) {
        //return String.format("NV%03d", count);
		ArrayList<String> dsma = new ArrayList<String>();
		for (LoaiPhong lp: dslp2) {
			dsma.add(lp.getMaLoaiP());
		}
		String newID;
        int count = dsma.size() + 1;
		do {
			newID=String.format("NV%03d",count );
			count++;
		}while(dsma.contains(newID));
		return newID;
	}
}


























