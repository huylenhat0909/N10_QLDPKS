package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.ChiTietHoaDon;
import entity.ChiTietPhieuDatPhong;
import entity.DichVu;
import entity.HoaDon;
import entity.KhachHang;
import entity.LoaiPhong;
import entity.Phong;

public class DaoChiTietHoaDon {
	private ArrayList<ChiTietHoaDon> dscthd;
	private ChiTietHoaDon cthd;
	private double tienPhong;

	public DaoChiTietHoaDon() {
		dscthd= new ArrayList<ChiTietHoaDon>();
	}
	public List<ChiTietHoaDon> getDatabase(){
		try {
			ConnectDB.getInstance().connect();
			Connection con = ConnectDB.getConnection();
			String sql = "Select * from ChiTietHoaDon";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				String maHoaDon= rs.getString(1);
				String maDichVu= rs.getString(2);
				String maPhong=rs.getString(3);
				Timestamp time= rs.getTimestamp(4);
				LocalDateTime ngaylaphoadon =time.toLocalDateTime();
				Boolean trangthai= rs.getBoolean(5);
				String pptt= rs.getString(6);
				Integer soluongdv= rs.getInt(7);
				DaoHoaDon daohd= new DaoHoaDon();
				DaoDichVu daodv= new DaoDichVu();
				DaoPhong daophong= new DaoPhong();
				DichVu dv= daodv.getLoaiDVTheoMa(maDichVu);
				Phong phong= daophong.getPhongtheoMa(maPhong);
				HoaDon hd=daohd.getHoaDontheoma(maHoaDon);
				ChiTietHoaDon cthd= new ChiTietHoaDon(hd, phong, ngaylaphoadon, trangthai, pptt, soluongdv, dv);
				dscthd.add(cthd);
			}
		} catch (SQLException e) {
			//TODO: handle exception
			e.printStackTrace();
		}
		return dscthd;
	}
	public ChiTietHoaDon getCTHDtheophongtt(String maPhong,Boolean trangthai) {
		int i=0 ;
		if(trangthai) {
			i=1;
		}
		try {
			ConnectDB.getInstance().connect();
			Connection con= ConnectDB.getConnection();
			String sql= "Select * from ChiTietHoaDon where maPhong='"+maPhong+"'and trangThai="+i;
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				String maHoaDon= rs.getString(1);
				String maDichVu= rs.getString(2);
				String maPhong1=rs.getString(3);
				Timestamp time= rs.getTimestamp(4);
				LocalDateTime ngaylaphoadon =time.toLocalDateTime();
				Boolean trangthai1= rs.getBoolean(5);
				String pptt= rs.getString(6);
				Integer soluongdv= rs.getInt(7);
				DaoHoaDon daohd= new DaoHoaDon();
				DaoDichVu daodv= new DaoDichVu();
				DaoPhong daophong= new DaoPhong();
				DichVu dv= daodv.getLoaiDVTheoMa(maDichVu);
				Phong phong= daophong.getPhongtheoMa(maPhong1);
				HoaDon hd=daohd.getHoaDontheoma(maHoaDon);
				cthd= new ChiTietHoaDon(hd, phong, ngaylaphoadon, trangthai1, pptt, soluongdv, dv);
			}
		}catch (SQLException e) {
			//TODO: handle exception
			e.printStackTrace();
		}
		return cthd;
	}
	public boolean themCTHoaDon(ChiTietHoaDon cthd) {
		int n =0;
		try {
			ConnectDB.getInstance().connect();
			Connection con= ConnectDB.getConnection();
			String sql= "INSERT INTO ChiTietHoaDon([maHoaDon],[maDichVu],[maPhong],[ngayLapHoaDon],[trangThai],[phuongThucTT],[soLuongDV]) VALUES(?,?,?,?,?,?,?)";
			PreparedStatement statement= con.prepareStatement(sql);
			statement.setString(1,cthd.getHD().getMaHD());
			statement.setString(3,cthd.getPhong().getMaPhong());
			// Nếu khuyến mãi là null thì thêm null, ngược lại thêm mã khuyến mãi
		    if (cthd.getDicvu() == null) {
		        statement.setString(2, "MDV001");;
		    } else {
		        statement.setString(2, cthd.getDicvu().getMaDichVu());
		    }
			statement.setTimestamp(4, Timestamp.valueOf(cthd.getNgayLapHD()));
			statement.setBoolean(5,cthd.getTrangThai());
			statement.setString(6, cthd.getPhuongThuc());
			statement.setInt(7, cthd.getsoLuong());
			n = statement.executeUpdate();
		}catch(SQLException E) {
			E.printStackTrace();
		}
		return n>0;
	}
	public List<ChiTietHoaDon> getCTHDByMaHD(String maHD) {
		// TODO Auto-generated method stub
		try {
			ConnectDB.getInstance().connect();
			Connection con= ConnectDB.getConnection();
			String sql = "SELECT * FROM ChiTietHoaDon WHERE maHoaDon = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, maHD);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String maHoaDon= rs.getString(1);
				String maDichVu= rs.getString(2);
				String maPhong1=rs.getString(3);
				Timestamp time= rs.getTimestamp(4);
				LocalDateTime ngaylaphoadon =time.toLocalDateTime();
				Boolean trangthai1= rs.getBoolean(5);
				String pptt= rs.getString(6);
				Integer soluongdv= rs.getInt(7);
				DaoHoaDon daohd= new DaoHoaDon();
				DaoDichVu daodv= new DaoDichVu();
				DaoPhong daophong= new DaoPhong();
				DichVu dv= daodv.getLoaiDVTheoMa(maDichVu);
				Phong phong= daophong.getPhongtheoMa(maPhong1);
				HoaDon hd=daohd.getHoaDontheoma(maHoaDon);
				cthd= new ChiTietHoaDon(hd, phong, ngaylaphoadon, trangthai1, pptt, soluongdv, dv);
				dscthd.add(cthd);
			}
		} catch (SQLException e) {
			//TODO: handle exception
			e.printStackTrace();
		}
		return dscthd;
		
	}
	public double tinhTongTien(String maHD) {
	    try  {
	    	ConnectDB.getInstance().connect();
			Connection con= ConnectDB.getConnection();
			String sql =  "SELECT ct.giaPhongtheoKieuThue FROM ChiTietHoaDon cthd JOIN Phong p ON cthd.maPhong = p.maPhong JOIN CTPhieuDatPhong ct ON p.maPhong = ct.maPhong WHERE cthd.maHoaDon = '" + maHD + "'";
			Statement stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery(sql);
	        while (rs.next()) {
	            tienPhong = rs.getDouble("giaPhongtheoKieuThue");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return tienPhong;
	}
	public boolean capNhatTrangThaiVaPPThanhToan(String maHD, String ppThanhToan) {
		// TODO Auto-generated method stub
		try {
	        ConnectDB.getInstance().connect();
	        Connection con = ConnectDB.getConnection();
	        String sql = "UPDATE ChiTietHoaDon SET trangThai = 1, phuongthucTT = ? WHERE maHoaDon = ?";
	        PreparedStatement ps = con.prepareStatement(sql);
	        ps.setString(1, ppThanhToan);
	        ps.setString(2, maHD);
	        int rows = ps.executeUpdate();
	        return rows > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public double tinhTongtiendv(String maHD) {
		double tongTien = 0.0;

	    try {
	        ConnectDB.getInstance().connect();
	        Connection con = ConnectDB.getConnection();

	        String sql = "SELECT ctdv.soLuongDV, dv.giaTien " +
	                     "FROM ChiTietHoaDon ctdv " +
	                     "JOIN DichVu dv ON ctdv.maDichVu = dv.maDichVu " +
	                     "WHERE ctdv.maHoaDon = ?";

	        PreparedStatement ps = con.prepareStatement(sql);
	        ps.setString(1, maHD);

	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	            int soLuong = rs.getInt("soLuongDV");
	            double donGia = rs.getDouble("giaTien");
	            tongTien += soLuong * donGia;
	        }

	        rs.close();
	        ps.close();

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return tongTien;
	}
	public Double tinhTongtienPhongvaDichVu(String mahd) {
		DaoHoaDon daohoadon = new DaoHoaDon();
		HoaDon hd= daohoadon.getHoaDontheoma(mahd);
		Double tienphong=tinhTongTien(mahd);
		Double tiendv=tinhTongtiendv(mahd);
		return (tienphong+tiendv)-(tienphong+tiendv)*hd.getKhuyenmai().getPhanTramKM();
	}
	public int sluonghdchuathanhtoan() {
		int tong=0;
		try {
	        ConnectDB.getInstance().connect();
	        Connection con = ConnectDB.getConnection();

	        String sql = "SELECT COUNT(*) AS tongHoaDonChuaThanhToan " +
	                     "FROM ChiTietHoaDon hd  " +
	                     "WHERE hd.trangThai ='0'";

	        PreparedStatement ps = con.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	            tong = rs.getInt("tongHoaDonChuaThanhToan");}

	        rs.close();
	        ps.close();

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return tong;
	}
}
