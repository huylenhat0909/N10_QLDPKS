package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import connectDB.ConnectDB;
import entity.LoaiPhong;
import entity.Phong;

public class DaoPhong {
	private ArrayList<Phong> dsPhong;
	private Phong phong;
	private DaoLoaiPhong daolp;
	public DaoPhong() {
		dsPhong=new ArrayList<Phong>();
	}
	public List<Phong> getDatabase(){
		try {
			ConnectDB.getInstance().connect();
			Connection con = ConnectDB.getConnection();
			String sql = "Select * from Phong";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				String maPhong= rs.getString(1);
				String tenPhong= rs.getString(2);
				String maLoaiPhong= rs.getString(3);
				Integer soGiuong= rs.getInt(4);
				Double giaPhongtheogiuong= rs.getDouble(5);
				String trangThai=rs.getString(6);
				Integer tang= rs.getInt(7);
				daolp=new DaoLoaiPhong();
				LoaiPhong loaiPhong = daolp.getLoaiPhongTheoMa(maLoaiPhong);
				phong= new Phong(maPhong, tenPhong, loaiPhong, soGiuong, giaPhongtheogiuong, trangThai, tang);
				dsPhong.add(phong);
			}
		} catch (SQLException e) {
			//TODO: handle exception
			e.printStackTrace();
		}
		return dsPhong;
	}
	public Phong getPhongtheoTen(String name) {
		try {
			ConnectDB.getInstance();
			Connection con= ConnectDB.getConnection();
			String sql = "Select * from Phong where tenPhong like N'%"+name+"%'";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				String maPhong= rs.getString(1);
				String tenPhong= rs.getString(2);
				String maLoaiPhong= rs.getString(3);
				Integer soGiuong= rs.getInt(4);
				Double giaPhongtheogiuong= rs.getDouble(5);
				String trangThai=rs.getString(6);
				Integer tang= rs.getInt(7);
				daolp=new DaoLoaiPhong();
				LoaiPhong loaiPhong = daolp.getLoaiPhongTheoMa(maLoaiPhong);
				phong= new Phong(maPhong, tenPhong, loaiPhong, soGiuong, giaPhongtheogiuong, trangThai, tang);
			}
		}catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return phong;
	}
	public Phong getPhongtheoMa(String ma) {
		try {
			ConnectDB.getInstance();
			Connection con= ConnectDB.getConnection();
			String sql = "Select * from Phong where maPhong like N'%"+ma+"%'";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				String maPhong= rs.getString(1);
				String tenPhong= rs.getString(2);
				String maLoaiPhong= rs.getString(3);
				Integer soGiuong= rs.getInt(4);
				Double giaPhongtheogiuong= rs.getDouble(5);
				String trangThai=rs.getString(6);
				Integer tang= rs.getInt(7);
				daolp=new DaoLoaiPhong();
				LoaiPhong loaiPhong = daolp.getLoaiPhongTheoMa(maLoaiPhong);
				phong= new Phong(maPhong, tenPhong, loaiPhong, soGiuong, giaPhongtheogiuong, trangThai, tang);
			}
		}catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return phong;
	}
	public boolean capnhatttPhong(String trangthai,String ma) {
		int n=0;
		try {
			ConnectDB.getInstance().connect();;
			Connection con = ConnectDB.getConnection();
			String sql = "UPDATE Phong set trangThai=? where maPhong=?";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setString(1, trangthai);
			statement.setString(2,ma);
			n = statement.executeUpdate();
			statement.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return n>0;
	}
	public Map<String, String> getTrangThaiPhongTheoNgay(LocalDate ngay) {
		Map<String, String> ketQua = new HashMap<>();
	    Connection con1 = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;

	    try {
	    	ConnectDB.getInstance().connect();
			con1 = ConnectDB.getConnection(); // Giả định bạn có lớp Database để lấy connection
	        String sql = "SELECT ct.maPhong, ct.gioDatPhong, p.trangThai " +
	                     "FROM CTPhieuDatPhong ct " +
	                     "JOIN Phong p ON ct.maPhong = p.maPhong " +
	                     "WHERE CAST(? AS DATE) BETWEEN CAST(ct.gioDatPhong AS DATE) AND CAST(ct.gioTraPhong AS DATE)";
	        stmt = con1.prepareStatement(sql);
	        stmt.setDate(1, java.sql.Date.valueOf(ngay));

	        rs = stmt.executeQuery();
	        while (rs.next()) {
	            String maPhong = rs.getString("maPhong");
	            LocalDate ngayNhan = rs.getTimestamp("ngayNhan").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	            String trangThai = rs.getString("trangThai");

	            // Ghép key để đồng bộ với cách bạn xử lý key trong loadTrangThaiTheoNgay()
	            String key = ngay + "-" + maPhong;
	            ketQua.put(key, trangThai);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try { if (rs != null) rs.close(); } catch (Exception e) {}
	        try { if (stmt != null) stmt.close(); } catch (Exception e) {}
	        try { if (con1 != null) con1.close(); } catch (Exception e) {}
	    }

	    return ketQua;
	}
}
