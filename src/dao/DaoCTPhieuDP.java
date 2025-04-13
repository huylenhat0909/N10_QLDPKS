package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.ChiTietPhieuDatPhong;
import entity.KhachHang;
import entity.PhieuDatPhong;
import entity.Phong;

public class DaoCTPhieuDP {
	private ArrayList<ChiTietPhieuDatPhong> dsctpdp;
	private DaoPhong daophong;
	private Phong phong;
	private DaoPhieuDP daopdp;
	private PhieuDatPhong pdp;
	private ChiTietPhieuDatPhong ctpdp;

	public DaoCTPhieuDP() {
		dsctpdp= new ArrayList<ChiTietPhieuDatPhong>();
	}
	public List<ChiTietPhieuDatPhong> getDatabase(){
		try {
			ConnectDB.getInstance().connect();
			Connection con = ConnectDB.getConnection();
			String sql = "Select * from CTPhieuDatPhong";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				String maPhieuDatPhong= rs.getString(1);
				String maPhong= rs.getString(2);
				Timestamp time= rs.getTimestamp(3);
				LocalDateTime gionhanphong =time.toLocalDateTime();
				Timestamp time_1= rs.getTimestamp(4);
				LocalDateTime giotraphong =time_1.toLocalDateTime();
				Boolean kieuThue= rs.getBoolean(5);
				daophong=new DaoPhong();
				phong=daophong.getPhongtheoMa(maPhong);
				daopdp=new DaoPhieuDP();
				pdp=daopdp.getPDPtheoMa(maPhieuDatPhong);
				ctpdp=new ChiTietPhieuDatPhong(pdp, phong, gionhanphong, giotraphong, kieuThue);
				dsctpdp.add(ctpdp);
			}
		} catch (SQLException e) {
			//TODO: handle exception
			e.printStackTrace();
		}
		return dsctpdp;
	}
	public boolean themCTPhieuDatPhong(ChiTietPhieuDatPhong ctpdp) {
		int n =0;
		try {
			ConnectDB.getInstance().connect();
			Connection con= ConnectDB.getConnection();
			String sql= "INSERT INTO CTPhieuDatPhong([maPhieuDatPhong],[maPhong],[gioDatPhong],[gioTraPhong],[kieuThue],[giaPhongtheoKieuThue]) VALUES(?,?,?,?,?,?)";
			PreparedStatement statement= con.prepareStatement(sql);
			statement.setString(1,ctpdp.getPhietDP().getMaPDP());
			statement.setString(2,ctpdp.getPhong().getMaPhong());
			statement.setTimestamp(3, Timestamp.valueOf(ctpdp.getGioDatPhong()));
			if (ctpdp.getGioTraPhong() != null) {
			    statement.setTimestamp(4, Timestamp.valueOf(ctpdp.getGioTraPhong()));
			} else {
			    statement.setNull(4, java.sql.Types.TIMESTAMP);
			}	
			statement.setBoolean(5,ctpdp.getKieuThue());
			statement.setDouble(6,ctpdp.getGiaPhongtheoKieuThue());
			n = statement.executeUpdate();
		}catch(SQLException E) {
			E.printStackTrace();
		}
		return n>0;
	}
	
}
