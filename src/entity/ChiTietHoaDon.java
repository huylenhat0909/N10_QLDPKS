package entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class ChiTietHoaDon {
	private ChiTietDichVu ctDV;
	private Phong phong;
	private LocalDateTime ngayLapHD;
	private Boolean trangThai;
	private String phuongThuc;
	private String maCTHD;
	
	public ChiTietHoaDon(ChiTietDichVu ctDV, Phong phong, LocalDateTime ngayLapHD, Boolean trangThai, String phuongThuc,
			String maCTHD) {
		super();
		this.ctDV = ctDV;
		this.phong = phong;
		this.ngayLapHD = ngayLapHD;
		this.trangThai = trangThai;
		this.phuongThuc = phuongThuc;
		this.maCTHD = maCTHD;
	}
	
	public ChiTietHoaDon() {
		super();
	}

	public String getMaCTHD() {
		return maCTHD;
	}

	public void setMaCTHD(String maCTHD) {
		this.maCTHD = maCTHD;
	}

	public ChiTietDichVu getCtDV() {
		return ctDV;
	}
	public void setCtDV(ChiTietDichVu ctDV) {
		this.ctDV = ctDV;
	}
	public Phong getPhong() {
		return phong;
	}
	public void setPhong(Phong phong) {
		this.phong = phong;
	}
	public LocalDateTime getNgayLapHD() {
		return ngayLapHD;
	}
	public void setNgayLapHD(LocalDateTime ngayLapHD) {
		this.ngayLapHD = ngayLapHD;
	}
	public Boolean getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(Boolean trangThai) {
		this.trangThai = trangThai;
	}
	public String getPhuongThuc() {
		return phuongThuc;
	}
	public void setPhuongThuc(String phuongThuc) {
		this.phuongThuc = phuongThuc;
	}

	@Override
	public int hashCode() {
		return Objects.hash(maCTHD);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChiTietHoaDon other = (ChiTietHoaDon) obj;
		return Objects.equals(maCTHD, other.maCTHD);
	}
	
}
