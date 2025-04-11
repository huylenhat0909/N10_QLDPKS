package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
	
}
