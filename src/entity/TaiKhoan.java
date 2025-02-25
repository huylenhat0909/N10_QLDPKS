package entity;

import java.util.Objects;

public class TaiKhoan {
	private NhanVien nhanVien;
	private String taiKhoan;
	private String matKhau;
	public TaiKhoan(NhanVien nhanVien, String taiKhoan, String matKhau) {
		super();
		this.nhanVien = nhanVien;
		this.taiKhoan = taiKhoan;
		this.matKhau = matKhau;
	}
	public TaiKhoan() {
		
	}
	public NhanVien getNhanVien() {
		return nhanVien;
	}
	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}
	public String getTaiKhoan() {
		return taiKhoan;
	}
	public void setTaiKhoan(String taiKhoan) {
		this.taiKhoan = taiKhoan;
	}
	public String getMatKhau() {
		return matKhau;
	}
	public void setMatKhau(String matKhau) {
		this.matKhau = matKhau;
	}
	@Override
	public int hashCode() {
		return Objects.hash(taiKhoan);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaiKhoan other = (TaiKhoan) obj;
		return Objects.equals(taiKhoan, other.taiKhoan);
	}
	
}
