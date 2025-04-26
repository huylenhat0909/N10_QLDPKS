package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.KhachHang;
import entity.LoaiPhong;
import entity.NhanVien;
import entity.PhieuDatPhong;
import entity.Phong;

public class DaoPhieuDP {
	private ArrayList<PhieuDatPhong> dsphieudp;
	private DaoNhanVien daonv= new DaoNhanVien();
	private DaoKhachHang daokh= new DaoKhachHang();
	private NhanVien nv;
	private KhachHang kh;
	private PhieuDatPhong pdp;
	public DaoPhieuDP() {
		dsphieudp= new ArrayList<PhieuDatPhong>();
	}
	public List<PhieuDatPhong> getDatabase(){
		try {
			ConnectDB.getInstance().connect();
			Connection con = ConnectDB.getConnection();
			String sql = "Select * from PhieuDatPhong";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				String maPhieuDatPhong= rs.getString(1);
				String maNhanVien= rs.getString(2);
				String maKhachHang= rs.getString(3);
				nv= daonv.getNhanVienTheoMa(maNhanVien);
				kh= daokh.getKhachHangtheoma(maKhachHang);
				pdp=new PhieuDatPhong(maPhieuDatPhong, nv, kh);
				dsphieudp.add(pdp);
			}
		} catch (SQLException e) {
			//TODO: handle exception
			e.printStackTrace();
		}
		return dsphieudp;
	}
	public PhieuDatPhong getPDPtheoMa(String ma) {
		try {
			ConnectDB.getInstance();
			Connection con= ConnectDB.getConnection();
			String sql = "Select * from PhieuDatPhong where maPhieuDatPhong like N'%"+ma+"%'";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				String maPhieuDatPhong= rs.getString(1);
				String maNhanVien= rs.getString(2);
				String maKhachHang= rs.getString(3);
				nv= daonv.getNhanVienTheoMa(maNhanVien);
				kh= daokh.getKhachHangtheoma(maKhachHang);
				pdp=new PhieuDatPhong(maPhieuDatPhong, nv, kh);
			}
		}catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return pdp;
	}
	public boolean themPhieuDatPhong(PhieuDatPhong pdp) {
		int n =0;
		try {
			ConnectDB.getInstance().connect();;
			Connection con= ConnectDB.getConnection();
			String sql= "INSERT INTO PhieuDatPhong([maPhieuDatPhong],[maNhanVien],[maKhachHang]) VALUES(?,?,?)";
			PreparedStatement statement= con.prepareStatement(sql);
			statement.setString(1,pdp.getMaPDP());
			statement.setString(2,pdp.getNhanvien().getMaNV());
			statement.setString(3,pdp.getKhachhang().getMaKH());
			n = statement.executeUpdate();
		}catch(SQLException E) {
			E.printStackTrace();
		}
		return n>0;
	}
	public String taomaPDP(List<PhieuDatPhong> dspdp2) {
        //return String.format("NV%03d", count);
		ArrayList<String> dsmaks = new ArrayList<String>();
		for (PhieuDatPhong a: dspdp2) {
			dsmaks.add(a.getMaPDP());
		}
		String newID;
        int count = dsmaks.size() + 1;
		do {
			newID=String.format("PDP%03d",count );
			count++;
		}while(dsmaks.contains(newID));
		return newID;
	}
	public boolean xoaPhieuDatPhongTheoMaPDP(String maPhieuDatPhong) {
	    int n = 0;
	    try {
	        ConnectDB.getInstance().connect();
	        Connection con = ConnectDB.getConnection();
	        String sql = "DELETE FROM PhieuDatPhong WHERE maPhieuDatPhong = ?";
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
