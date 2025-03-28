package entity;

import java.util.Objects;

public class PhieuDatPhong {
	private String maPDP ;
	private NhanVien nhanvien;
	private KhachHang khachhang;
	public PhieuDatPhong(String maPDP, NhanVien nhanvien, KhachHang khachhang) {
		this.maPDP = maPDP;
		this.nhanvien = nhanvien;
		this.khachhang = khachhang;
	}
	public PhieuDatPhong() {
	}
	public String getMaPDP() {
		return maPDP;
	}
	public void setMaPDP(String maPDP) {
		this.maPDP = maPDP;
	}
	public NhanVien getNhanvien() {
		return nhanvien;
	}
	public void setNhanvien(NhanVien nhanvien) {
		this.nhanvien = nhanvien;
	}
	public KhachHang getKhachhang() {
		return khachhang;
	}
	public void setKhachhang(KhachHang khachhang) {
		this.khachhang = khachhang;
	}
	@Override
	public int hashCode() {
		return Objects.hash(maPDP);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PhieuDatPhong other = (PhieuDatPhong) obj;
		return Objects.equals(maPDP, other.maPDP);
	}
	
}
