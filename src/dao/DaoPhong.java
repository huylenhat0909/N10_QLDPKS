package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTextField;

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
				Float giaPhongtheogiuong= rs.getFloat(5);
				String trangThai=rs.getString(6);
				Integer tang= rs.getInt(7);
				daolp=new DaoLoaiPhong();
				LoaiPhong loaiPhong = daolp.getLoaiPhongTheoMa(maLoaiPhong);
				phong= new Phong(maPhong, tenPhong, loaiPhong, soGiuong, giaPhongtheogiuong, trangThai, tang);
				dsPhong.add(phong);
			}
			statement.close();
		} catch (SQLException e) {
			//TODO: handle exception
			e.printStackTrace();
		}
		return dsPhong;
	}
	public Phong getPhongtheoTen(String name) {
		try {
			ConnectDB.getInstance().connect();
			Connection con= ConnectDB.getConnection();
			String sql = "Select * from Phong where tenPhong like N'%"+name+"%'";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				String maPhong= rs.getString(1);
				String tenPhong= rs.getString(2);
				String maLoaiPhong= rs.getString(3);
				Integer soGiuong= rs.getInt(4);
				Float giaPhongtheogiuong= rs.getFloat(5);
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
			ConnectDB.getInstance().connect();
			Connection con= ConnectDB.getConnection();
			String sql = "Select * from Phong where maPhong like N'%"+ma+"%'";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				String maPhong= rs.getString(1);
				String tenPhong= rs.getString(2);
				String maLoaiPhong= rs.getString(3);
				Integer soGiuong= rs.getInt(4);
				Float giaPhongtheogiuong= rs.getFloat(5);
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
	public Map<String, String> getTrangThaiPhongTheoNgay(LocalDate date) {
	    Map<String, String> map = new HashMap<>();

	    try {
	        ConnectDB.getInstance().connect();
	        Connection con = ConnectDB.getConnection();

	        // Xác định thời gian bắt đầu và kết thúc của ngày
	        LocalDateTime startOfDay = date.atStartOfDay();         // 00:00
	        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay(); // 00:00 ngày hôm sau

	        Timestamp startTimestamp = Timestamp.valueOf(startOfDay);
	        Timestamp endTimestamp = Timestamp.valueOf(endOfDay);

	        // 1. Lấy các phòng đang sử dụng (ChiTietHoaDon)
	        String sqlHD = """
	            SELECT DISTINCT p.maPhong  
	            FROM ChiTietHoaDon cthd  
	            JOIN Phong p ON cthd.maPhong = p.maPhong  
	            WHERE cthd.ngayLapHoaDon >= ?  
	            AND cthd.ngayLapHoaDon < ?
	            AND cthd.trangThai='0'; 
	        """;

	        PreparedStatement psHD = con.prepareStatement(sqlHD);
	        psHD.setTimestamp(1, startTimestamp);
	        psHD.setTimestamp(2, endTimestamp);
	        ResultSet rsHD = psHD.executeQuery();
	        while (rsHD.next()) {
	            String maPhong = rsHD.getString("maPhong");
	            map.put(maPhong, "Đang sử dụng");
	        }

	        // 2. Lấy các phòng đã đặt (CTPhieuDatPhong) — nếu chưa bị gán là "Đang sử dụng"
	        String sqlPDP = """
	            SELECT DISTINCT p.maPhong
	            FROM CTPhieuDatPhong ctpdp
	            JOIN Phong p ON ctpdp.maPhong = p.maPhong
	            WHERE ctpdp.gioDatPhong < ? 
	            AND ctpdp.gioTraPhong > ?
	            AND kieuThue='1';
	        """;

	        PreparedStatement psPDP = con.prepareStatement(sqlPDP);
	        psPDP.setTimestamp(1, endTimestamp); // Đặt trước khi ngày kết thúc
	        psPDP.setTimestamp(2, startTimestamp); // Trả sau khi ngày bắt đầu
	        ResultSet rsPDP = psPDP.executeQuery();
	        while (rsPDP.next()) {
	            String maPhong = rsPDP.getString("maPhong");
	            if (!map.containsKey(maPhong)) {
	                map.put(maPhong, "Đã đặt");
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return map;
	}


	public static String taomaP(List<Phong> dsp2) {
        //return String.format("NV%03d", count);
		ArrayList<String> dsma = new ArrayList<String>();
		for (Phong lp: dsp2) {
			dsma.add(lp.getMaPhong());
		}
		String newID;
        int count = dsma.size() + 1;
		do {
			newID=String.format("MP%03d",count );
			count++;
		}while(dsma.contains(newID));
		return newID;
	}
	public boolean capnhattPhong(Phong phong_new) {
		int n = 0;

		try {
			ConnectDB.getInstance().connect();
			Connection con = ConnectDB.getConnection();
			String sql = "UPDATE Phong SET tenPhong=?, maLoaiPhong=?, soGiuong=?, giaPhongTheoGiuong=?, trangThai=?, tang=? WHERE maPhong=?";
			PreparedStatement statement = con.prepareStatement(sql);

			statement.setString(1, phong_new.getTenPhong());
			statement.setString(2, phong_new.getLoaiPhong().getMaLoaiP());
			statement.setInt(3, phong_new.getSoGiuong());
			statement.setFloat(4,phong_new.getGiaPhong());
			statement.setString(5, phong_new.getTrangThai());
			statement.setInt(6, phong_new.getTang());
			statement.setString(7,phong_new.getMaPhong());

			n = statement.executeUpdate();
			statement.close();

		} catch (SQLException e) {
			System.out.println("Lỗi khi cập nhật phòng:");
			e.printStackTrace();
		}
		return n > 0;
	}

	public boolean themPhong(Phong phong2) {
		// TODO Auto-generated method stub
		int n =0;
		try {
			ConnectDB.getInstance();
			Connection con= ConnectDB.getConnection();
			String sql= "INSERT INTO Phong([maPhong],[tenPhong],[maLoaiPhong],[soGiuong],[giaPhongTheoGiuong],[trangThai],[tang]) VALUES(?,?,?,?,?,?,?)";
			PreparedStatement statement= con.prepareStatement(sql);
			statement.setString(1,phong2.getMaPhong());
			statement.setString(2,phong2.getTenPhong());
			statement.setString(3,phong2.getLoaiPhong().getMaLoaiP());
			statement.setInt(4,phong2.getSoGiuong());
			statement.setDouble(5, phong2.getGiaPhong());
			statement.setString(6,phong2.getTrangThai());
			statement.setInt(7, phong2.getTang());
			n=statement.executeUpdate();
			statement.close();
		}catch(SQLException E) {
			E.printStackTrace();
		}
		return n>0;
	}
	
}
