package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
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
			statement.close();
			con.close();
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
			statement.close();
			con.close();
		}catch(SQLException E) {
			E.printStackTrace();
		}
		return n>0;
	}
	public ChiTietPhieuDatPhong getCTPDPtheoMaPhong(String ma) {
		try {
			ConnectDB.getInstance().connect();
			Connection con= ConnectDB.getConnection();
			String sql = "Select * from CTPhieuDatPhong where maPhong like N'%"+ma+"%'";
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
				
			}
			rs.close();
			statement.close();
			con.close();
		}catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ctpdp;
	}
	public ChiTietPhieuDatPhong getCTPDPtheoMaPhongDay(String ma,LocalDate date) {
		try {
			ConnectDB.getInstance().connect();
	        Connection con = ConnectDB.getConnection();

	        // Sử dụng PreparedStatement để tránh SQL injection và đúng định dạng
	        String sql = "SELECT * FROM CTPhieuDatPhong WHERE maPhong = ? AND CAST(gioDatPhong AS DATE) = ?";
	        PreparedStatement ps = con.prepareStatement(sql);

	        // Gán giá trị cho tham số maPhong
	        ps.setString(1, ma);
	        
	        // Chuyển từ LocalDate sang java.sql.Date để so sánh với kiểu DATE trong SQL
	        java.sql.Date sqlDate = java.sql.Date.valueOf(date); // Chuyển LocalDate sang java.sql.Date
	        ps.setDate(2, sqlDate);  // Truyền tham số ngày
			ResultSet rs = ps.executeQuery();
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
				
			}
			rs.close();
			ps.close();
			con.close();
		}catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ctpdp;
	} 
	public boolean isPhongCoTheDat(String maPhong, LocalDate ngayMuonDat) {
	    ChiTietPhieuDatPhong ctpd= this.getCTPDPtheoMaPhong(maPhong);
	        // Kiểm tra xem ngày muốn đặt có trùng trong khoảng đặt phòng nào không
	        if ((ngayMuonDat.isEqual(ctpd.getGioDatPhong().toLocalDate())) || ngayMuonDat.isAfter(ctpd.getGioDatPhong().toLocalDate())&&ngayMuonDat.isBefore(ctpd.getGioTraPhong().toLocalDate())) {
	            return false; // Có người đã đặt vào ngày đó
	        }
	    return true; // Không có ai đặt => có thể đặt
	}
	public int sluongctpdpchuacocthd() {
		int tong=0;
		try {
	        ConnectDB.getInstance().connect();
	        Connection con = ConnectDB.getConnection();

	        String sql = "SELECT COUNT(*) AS soLuongChuaLapHoaDon " +
	                     "FROM CTPhieuDatPhong ctpdp JOIN Phong p ON ctpdp.maPhong = p.maPhong LEFT JOIN ChiTietHoaDon cthd ON ctpdp.maPhong = cthd.maPhong " +
	                     "WHERE cthd.maPhong IS NULL and ctpdp.kieuThue='1'";

	        PreparedStatement ps = con.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	            tong = rs.getInt("soLuongChuaLapHoaDon");}

	        rs.close();
	        ps.close();

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return tong;
	}
	public boolean xoaCTPhieuDatPhongTheoMaPDP(String maPhieuDatPhong) {
	    int n = 0;
	    try {
	        ConnectDB.getInstance().connect();
	        Connection con = ConnectDB.getConnection();
	        String sql = "DELETE FROM CTPhieuDatPhong WHERE maPhieuDatPhong = ?";
	        PreparedStatement statement = con.prepareStatement(sql);
	        statement.setString(1, maPhieuDatPhong);

	        n = statement.executeUpdate();

	        statement.close();
	        con.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return n > 0;
	}
}
