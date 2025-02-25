package entity;

import java.time.LocalDate;
import java.util.Objects;

public class NhanVien {
	private String maNV;
	private String tenNV;
	private String soCCCD;
	private LocalDate ngaySinh;
	private Boolean gioiTinh;
	private String soDT;
	private String email;
	private String chucVu;
	private Boolean tinhTrangCV;
	public NhanVien() {
		
	}
	public NhanVien(String maNV) {
		this.maNV= maNV;
		this.tenNV = "";
		this.soCCCD = "";
		this.ngaySinh = null;
		this.gioiTinh = false;
		this.soDT = "";
		this.email = "";
		this.chucVu = "";
		this.tinhTrangCV = false;
	}
	public NhanVien(String maNV, String tenNV, String soCCCD, LocalDate ngaySinh, Boolean gioiTinh, String soDT,
			String email, String chucVu, Boolean tinhTrangCV) {
		this.maNV = maNV;
		this.tenNV = tenNV;
		this.soCCCD = soCCCD;
		this.ngaySinh = ngaySinh;
		this.gioiTinh = gioiTinh;
		this.soDT = soDT;
		this.email = email;
		this.chucVu = chucVu;
		this.tinhTrangCV = tinhTrangCV;
	}
	public String getMaNV() {
		return maNV;
	}
	public void setMaNV(String maNV) {
		this.maNV = maNV;
	}
	public String getTenNV() {
		return tenNV;
	}
	public void setTenNV(String tenNV) {
		this.tenNV = tenNV;
	}
	public String getSoCCCD() {
		return soCCCD;
	}
	public void setSoCCCD(String soCCCD) {
		this.soCCCD = soCCCD;
	}
	public LocalDate getNgaySinh() {
		return ngaySinh;
	}
	public void setNgaySinh(LocalDate ngaySinh) {
		this.ngaySinh = ngaySinh;
	}
	public Boolean getGioiTinh() {
		return gioiTinh;
	}
	public void setGioiTinh(Boolean gioiTinh) {
		this.gioiTinh = gioiTinh;
	}
	public String getSoDT() {
		return soDT;
	}
	public void setSoDT(String soDT) {
		this.soDT = soDT;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getChucVu() {
		return chucVu;
	}
	public void setChucVu(String chucVu) {
		this.chucVu = chucVu;
	}
	public Boolean getTinhTrangCV() {
		return tinhTrangCV;
	}
	public void setTinhTrangCV(Boolean tinhTrangCV) {
		this.tinhTrangCV = tinhTrangCV;
	}
	@Override
	public int hashCode() {
		return Objects.hash(maNV);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NhanVien other = (NhanVien) obj;
		return Objects.equals(maNV, other.maNV);
	}
	
}
