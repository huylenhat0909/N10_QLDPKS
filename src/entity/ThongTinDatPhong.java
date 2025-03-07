package entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class ThongTinDatPhong {
	private String maTTDP;
	private NhanVien nhanvien;
	private KhachHang khachHang;
	private Phong phong;
	private LocalDateTime thoigianDatPhong;
	private LocalDateTime thoigianTraPhong;
	public ThongTinDatPhong(String maTTDP, NhanVien nhanvien, KhachHang khachHang, Phong phong,
			LocalDateTime thoigianDatPhong, LocalDateTime thoigianTraPhong) {
		super();
		this.maTTDP = maTTDP;
		this.nhanvien = nhanvien;
		this.khachHang = khachHang;
		this.phong = phong;
		this.thoigianDatPhong = thoigianDatPhong;
		this.thoigianTraPhong = thoigianTraPhong;
	}
	public ThongTinDatPhong() {
		super();
	}
	public String getMaTTDP() {
		return maTTDP;
	}
	public void setMaTTDP(String maTTDP) {
		this.maTTDP = maTTDP;
	}
	public NhanVien getNhanvien() {
		return nhanvien;
	}
	public void setNhanvien(NhanVien nhanvien) {
		this.nhanvien = nhanvien;
	}
	public KhachHang getKhachHang() {
		return khachHang;
	}
	public void setKhachHang(KhachHang khachHang) {
		this.khachHang = khachHang;
	}
	public Phong getPhong() {
		return phong;
	}
	public void setPhong(Phong phong) {
		this.phong = phong;
	}
	public LocalDateTime getThoigianDatPhong() {
		return thoigianDatPhong;
	}
	public void setThoigianDatPhong(LocalDateTime thoigianDatPhong) {
		this.thoigianDatPhong = thoigianDatPhong;
	}
	public LocalDateTime getThoigianTraPhong() {
		return thoigianTraPhong;
	}
	public void setThoigianTraPhong(LocalDateTime thoigianTraPhong) {
		this.thoigianTraPhong = thoigianTraPhong;
	}
	@Override
	public int hashCode() {
		return Objects.hash(maTTDP);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ThongTinDatPhong other = (ThongTinDatPhong) obj;
		return Objects.equals(maTTDP, other.maTTDP);
	}
	
}
