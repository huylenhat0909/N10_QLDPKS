package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import connectDB.ConnectDB;
import entity.DichVu;
import entity.LoaiDichVu;
public class DaoDichVu {
	private ArrayList<DichVu> dsDV;
	private DichVu dichvu;
	private LoaiDichVu loaiDV;

	public DaoDichVu() {
		// TODO Auto-generated constructor stub
		dsDV= new ArrayList<DichVu>();
	}
	public List<entity.DichVu> getDatabase(){
		try {
			ConnectDB.getInstance().connect();
			Connection con = ConnectDB.getConnection();
			String sql = "Select * from DichVu";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				String maDV= rs.getString(1);
				String tenDV= rs.getString(2);
				double giaTien= rs.getDouble(3);
				boolean trangthai= rs.getBoolean(4);
				String mota= rs.getString(5);
				String maLDV= rs.getString(6);
				DaoLoaiDV daoldv= new DaoLoaiDV();
				LoaiDichVu ldv = daoldv.getLoaiDVTheoMa(maLDV);
				dichvu= new DichVu(maDV, tenDV, mota, giaTien, trangthai, ldv);
				dsDV.add(dichvu);
			}
		} catch (SQLException e) {
			//TODO: handle exception
			e.printStackTrace();
		}
		return dsDV;
	}
	public DichVu getDVTheoTen(String tenDV){
		try {
			ConnectDB.getInstance().connect();
			Connection con= ConnectDB.getConnection();
			String sql = "Select * from DichVu where tenDichVu like N'%"+tenDV+"%'";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				String maDV= rs.getString(1);
				String tendV= rs.getString(2);
				double giaTien= rs.getDouble(3);
				boolean trangthai= rs.getBoolean(4);
				String mota= rs.getString(5);
				String maLDV= rs.getString(6);
				DaoLoaiDV daoldv= new DaoLoaiDV();
				LoaiDichVu ldv = daoldv.getLoaiDVTheoMa(maLDV);
				dichvu= new DichVu(maDV, tendV, mota, giaTien, trangthai, ldv);
			}
		}catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return dichvu;
	}
	public DichVu getLoaiDVTheoMa(String ma){
		try {
			ConnectDB.getInstance().connect();
			Connection con= ConnectDB.getConnection();
			String sql = "Select * from DichVu where maDichVu like N'%"+ma+"%'";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				String maDV= rs.getString(1);
				String tendV= rs.getString(2);
				double giaTien= rs.getDouble(3);
				boolean trangthai= rs.getBoolean(4);
				String mota= rs.getString(5);
				String maLDV= rs.getString(6);
				DaoLoaiDV daoldv= new DaoLoaiDV();
				LoaiDichVu ldv = daoldv.getLoaiDVTheoMa(maLDV);
				dichvu= new DichVu(maDV, tendV, mota, giaTien, trangthai, ldv);
			}
		}catch (SQLException e2) {
			// TODO: handle exception
			e2.printStackTrace();
		}
		return dichvu;
	}
	public boolean themDV(DichVu dv) {
		int n =0;
		try {
			ConnectDB.getInstance().connect();;
			Connection con= ConnectDB.getConnection();
			String sql= "INSERT INTO DichVu([maDichVu],[tenDichVu],[giaTien],[trangThai],[moTa],[maLoaiDV]) VALUES(?,?,?,?,?,?)";
			PreparedStatement statement= con.prepareStatement(sql);
			statement.setString(1,dv.getMaDichVu());
			statement.setString(2, dv.getTenDichVu());
			statement.setDouble(3, dv.getGiaTien());
			statement.setBoolean(4, dv.getTrangThai());
			statement.setString(5, dv.getMoTa());
			statement.setString(6, dv.getLoaiDV().getMaLoaiDV());
			n=statement.executeUpdate();
		}catch(SQLException E) {
			E.printStackTrace();
		}
		return n>0;
	}
	public boolean capnhatDichVu(DichVu dv) {
		int n=0;
		try {
			ConnectDB.getInstance().connect();
			Connection con= ConnectDB.getConnection();
			String sql="UPDATE DichVu set tenDichVu=?,giaTien=?,trangThai=?,moTa=?,maLoaiDV=? where maDichVu=?";
			PreparedStatement statement= con.prepareStatement(sql);
			statement.setString(6,dv.getMaDichVu());
			statement.setString(1, dv.getTenDichVu());
			statement.setDouble(2, dv.getGiaTien());
			statement.setBoolean(3, dv.getTrangThai());
			statement.setString(4, dv.getMoTa());
			statement.setString(5, dv.getLoaiDV().getMaLoaiDV());
			n=statement.executeUpdate();
			statement.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return n>0;
	}
	public boolean xoaDV(DichVu ldv) {
		int n=0;
		try {
			ConnectDB.getInstance().connect();
			Connection con= ConnectDB.getConnection();
			String sql="DELETE FROM DichVu where maDichVu=?";
			PreparedStatement statement= con.prepareStatement(sql);
			statement.setString(1, ldv.getMaDichVu());
			n=statement.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return n>0;
	}
	public static String taomaDV(List<DichVu> dsdv2) {
        //return String.format("NV%03d", count);
		ArrayList<String> dsma = new ArrayList<String>();
		for (DichVu lp: dsdv2) {
			dsma.add(lp.getMaDichVu());
		}
		String newID;
        int count = dsma.size() + 1;
		do {
			newID=String.format("MDV%03d",count );
			count++;
		}while(dsma.contains(newID));
		return newID;
	}
	public Map<String, List<DichVu>> getDichVuTheoLoai() {
		// TODO Auto-generated method stub
		Map<String, List<DichVu>> map = new HashMap<>();

	    String sql = """
	        SELECT dv.maDichVu, dv.tenDichVu, dv.giaTien, dv.trangThai, dv.moTa,
	               dv.maLoaiDV, ldv.tenLoaiDV
	        FROM DichVu dv
	        JOIN LoaiDichVu ldv ON dv.maLoaiDV = ldv.maLoaiDV
	    """;

	    try {
	    	ConnectDB.getInstance().connect();
    		Connection con= ConnectDB.getConnection();
    		PreparedStatement ps = con.prepareStatement(sql);
    		ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	            String maDichVu = rs.getString("maDichVu");
	            String tenDichVu = rs.getString("tenDichVu");
	            double giaTien = rs.getDouble("giaTien");
	            Boolean trangThai = rs.getBoolean("trangThai");
	            String moTa = rs.getString("moTa");
	            String maLoaiDV = rs.getString("maLoaiDV");
	            String tenLoai = rs.getString("tenLoaiDV");
	            DaoLoaiDV daoldv= new DaoLoaiDV();
	            loaiDV= daoldv.getLoaiDVTheoMa(maLoaiDV);
	            // Tạo đối tượng DichVu (giả sử bạn có constructor tương ứng)
	            DichVu dv = new DichVu(maDichVu, tenDichVu,moTa, giaTien, trangThai, loaiDV);

	            // Thêm vào map theo tên loại
	            map.computeIfAbsent(tenLoai, k -> new ArrayList<>()).add(dv);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return map;
	}
}


























