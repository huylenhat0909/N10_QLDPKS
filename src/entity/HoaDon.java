package entity;

import java.util.Objects;

public class HoaDon {
	private String maHD;
	private NhanVien nhanvien;
	private KhuyenMai khuyenmai;
	private KhachHang khachHang;
	public HoaDon(String maHD, NhanVien nhanvien, KhuyenMai khuyenmai, KhachHang khachHang) {
		super();
		this.maHD = maHD;
		this.nhanvien = nhanvien;
		this.khuyenmai = khuyenmai;
		this.khachHang = khachHang;
	}
	public HoaDon() {
		super();
	}
	public String getMaHD() {
		return maHD;
	}
	public void setMaHD(String maHD) {
		this.maHD = maHD;
	}
	public NhanVien getNhanvien() {
		return nhanvien;
	}
	public void setNhanvien(NhanVien nhanvien) {
		this.nhanvien = nhanvien;
	}
	public KhuyenMai getKhuyenmai() {
		return khuyenmai;
	}
	public void setKhuyenmai(KhuyenMai khuyenmai) {
		this.khuyenmai = khuyenmai;
	}
	public KhachHang getKhachHang() {
		return khachHang;
	}
	public void setKhachHang(KhachHang khachHang) {
		this.khachHang = khachHang;
	}
	@Override
	public int hashCode() {
		return Objects.hash(maHD);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HoaDon other = (HoaDon) obj;
		return Objects.equals(maHD, other.maHD);
	}
	
	
}
