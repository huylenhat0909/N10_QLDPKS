package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.LoaiDichVu;
import entity.LoaiPhong;
import entity.NhanVien;

public class DaoLoaiDV {
	private ArrayList<LoaiDichVu> dslDV;
	private LoaiDichVu ldicvu;

	public DaoLoaiDV() {
		// TODO Auto-generated constructor stub
		dslDV= new ArrayList<LoaiDichVu>();
	}
	public List<entity.LoaiDichVu> getDatabase(){
		try {
			ConnectDB.getInstance().connect();
			Connection con = ConnectDB.getConnection();
			String sql = "Select * from LoaiDichVu";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				String maLDV= rs.getString(1);
				String tenLDV= rs.getString(2);
				ldicvu= new LoaiDichVu(maLDV, tenLDV);
				dslDV.add(ldicvu);
			}
		} catch (SQLException e) {
			//TODO: handle exception
			e.printStackTrace();
		}
		return dslDV;
	}
	public LoaiDichVu getLoaiDVTheoTen(String tenLDV){
		try {
			ConnectDB.getInstance().connect();
			Connection con= ConnectDB.getConnection();
			String sql = "Select * from LoaiDichVu where tenLoaiDV like N'%"+tenLDV+"%'";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				String maLPhong= rs.getString(1);
				String tenlPhong= rs.getString(2);
				ldicvu= new LoaiDichVu(maLPhong, tenlPhong);
			}
		}catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ldicvu;
	}
	public LoaiDichVu getLoaiDVTheoMa(String ma){
		try {
			ConnectDB.getInstance();
			Connection con= ConnectDB.getConnection();
			String sql = "Select * from LoaiDichVu where maLoaiDV like N'%"+ma+"%'";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				String maLPhong= rs.getString(1);
				String tenlPhong= rs.getString(2);
				ldicvu= new LoaiDichVu(maLPhong, tenlPhong);
			}
		}catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ldicvu;
	}
	public boolean themLoaiDV(LoaiDichVu ldv) {
		int n =0;
		try {
			ConnectDB.getInstance().connect();;
			Connection con= ConnectDB.getConnection();
			String sql= "INSERT INTO LoaiDichVu([maLoaiDV],[tenLoaiDV]) VALUES(?,?)";
			PreparedStatement statement= con.prepareStatement(sql);
			statement.setString(1,ldv.getMaLoaiDV());
			statement.setString(2, ldv.getTenDV());
			n=statement.executeUpdate();
		}catch(SQLException E) {
			E.printStackTrace();
		}
		return n>0;
	}
	public boolean capnhatLoaiDichVu(LoaiDichVu ldv) {
		int n=0;
		try {
			ConnectDB.getInstance().connect();
			Connection con= ConnectDB.getConnection();
			String sql="UPDATE LoaiDichVu set tenLoaiDV=? where maLoaiDV=?";
			PreparedStatement statement= con.prepareStatement(sql);
			statement.setString(1, ldv.getTenDV());
			statement.setString(2, ldv.getMaLoaiDV());
			n=statement.executeUpdate();
			statement.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return n>0;
	}
	public boolean xoaLoaiDV(LoaiDichVu ldv) {
		int n=0;
		try {
			ConnectDB.getInstance().connect();
			Connection con= ConnectDB.getConnection();
			String sql="DELETE FROM LoaiDichVu where maLoaiDV=?";
			PreparedStatement statement= con.prepareStatement(sql);
			statement.setString(1, ldv.getMaLoaiDV());
			n=statement.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return n>0;
	}
	public static String taomaLDV(List<LoaiDichVu> dsldv2) {
        //return String.format("NV%03d", count);
		ArrayList<String> dsma = new ArrayList<String>();
		for (LoaiDichVu lp: dsldv2) {
			dsma.add(lp.getMaLoaiDV());
		}
		String newID;
        int count = dsma.size() + 1;
		do {
			newID=String.format("MLDV%03d",count );
			count++;
		}while(dsma.contains(newID));
		return newID;
	}
}


























