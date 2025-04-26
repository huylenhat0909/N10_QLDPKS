package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.HoaDon;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.NhanVien;
import entity.PhieuDatPhong;
import entity.Phong;

public class DaoHoaDon {
	private ArrayList<HoaDon> dshoadon;
	private DaoNhanVien daonv= new DaoNhanVien();
	private DaoKhachHang daokh= new DaoKhachHang();
	private DaoKhuyenMai daokm= new DaoKhuyenMai();
	private NhanVien nv;
	private KhachHang kh;
	private KhuyenMai km;
	private HoaDon hd;
	private Statement statement;
	private ResultSet rs;
	private Connection con;
	private PreparedStatement statement1;
	public DaoHoaDon() {
		dshoadon= new ArrayList<HoaDon>();
	}
	public List<HoaDon> getDatabase(){
		try {
			ConnectDB.getInstance().connect();
			con = ConnectDB.getConnection();
			String sql = "Select * from HoaDon";
			statement = con.createStatement();
			rs = statement.executeQuery(sql);
			while(rs.next()) {
				String maHoaDon= rs.getString(1);
				String maNhanVien= rs.getString(2);
				String maKhachHang= rs.getString(3);
				String maKhuyenMai= rs.getString(4);
				nv= daonv.getNhanVienTheoMa(maNhanVien);
				kh= daokh.getKhachHangtheoma(maKhachHang);
				km=daokm.getKhuyenMaitheoMa(maKhuyenMai);
				hd= new HoaDon(maHoaDon, nv, km, kh);
				dshoadon.add(hd);
			}
		} catch (SQLException e) {
			//TODO: handle exception
			e.printStackTrace();
		}finally {
	        try {
	            if (rs != null) rs.close();
	            if (statement != null) statement.close();
	            // Nếu bạn dùng pool thì có thể không cần con.close()
	            // Nhưng nếu không thì nên đóng:
	            if (con != null) con.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
		return dshoadon;
	}
	public HoaDon getHoaDontheoma(String ma) {
		try {
			ConnectDB.getInstance().connect();
			con = ConnectDB.getConnection();
			String sql = "Select * from HoaDon where maHoaDon like N'%"+ma+"%' ";
			statement = con.createStatement();
			rs = statement.executeQuery(sql);
			while(rs.next()) {
				String maHoaDon= rs.getString(1);
				String maNhanVien= rs.getString(2);
				String maKhachHang= rs.getString(3);
				String maKhuyenMai= rs.getString(4);
				nv= daonv.getNhanVienTheoMa(maNhanVien);
				kh= daokh.getKhachHangtheoma(maKhachHang);
				km=daokm.getKhuyenMaitheoMa(maKhuyenMai);
				hd= new HoaDon(maHoaDon, nv, km, kh);
			}
		} catch (SQLException e) {
			//TODO: handle exception
			e.printStackTrace();
		}finally {
	        try {
	            if (rs != null) rs.close();
	            if (statement != null) statement.close();
	            // Nếu bạn dùng pool thì có thể không cần con.close()
	            // Nhưng nếu không thì nên đóng:
	            if (con != null) con.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
		return hd;
	}
	public boolean themHoaDon(HoaDon hd) {
		int n=0;
		try {
			ConnectDB.getInstance().connect();
			con= ConnectDB.getConnection();
			String sql= "INSERT INTO HoaDon([maHoaDon],[maNhanVien],[maKhachHang],[maKM]) VALUES(?,?,?,?)";
			statement1= con.prepareStatement(sql);
			statement1.setString(1,hd.getMaHD());
			statement1.setString(2,hd.getNhanvien().getMaNV());
			statement1.setString(3,hd.getKhachHang().getMaKH());
			// Nếu khuyến mãi là null thì thêm null, ngược lại thêm mã khuyến mãi
		    if (hd.getKhuyenmai() == null) {
		        statement1.setString(4,"MKM001");;
		    } else {
		        statement1.setString(4, hd.getKhuyenmai().getMaKM());
		    }
			n = statement1.executeUpdate();
		}catch(SQLException E) {
			E.printStackTrace();
		}finally {
	        try {
	            if (rs != null) rs.close();
	            if (statement1 != null) statement.close();
	            // Nếu bạn dùng pool thì có thể không cần con.close()
	            // Nhưng nếu không thì nên đóng:
	            if (con != null) con.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
		return n>0;
	}
	public String taomaHD(List<HoaDon> dsp2) {
        //return String.format("NV%03d", count);
		ArrayList<String> dsma = new ArrayList<String>();
		for (HoaDon lp: dsp2) {
			dsma.add(lp.getMaHD());
		}
		String newID;
        int count = dsma.size() + 1;
		do {
			newID=String.format("MHD%03d",count );
			count++;
		}while(dsma.contains(newID));
		return newID;
	}
	public boolean capnhatKMvaoHD(KhuyenMai km, HoaDon hd) {
	    int n = 0;
	    try {
	        ConnectDB.getInstance().connect();
	        Connection con = ConnectDB.getConnection();
	        
	        String sql = "UPDATE HoaDon SET maKM = ? WHERE maHoaDon = ?";
	        PreparedStatement ps = con.prepareStatement(sql);
	        
	        ps.setString(1, km.getMaKM());       // Lấy mã khuyến mãi
	        ps.setString(2, hd.getMaHD());   // Lấy mã hóa đơn

	        n = ps.executeUpdate();
	        ps.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return n>0;
	    }
}
