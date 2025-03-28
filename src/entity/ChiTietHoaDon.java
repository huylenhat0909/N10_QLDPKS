package entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class ChiTietHoaDon {
	private Phong phong;
	private LocalDateTime ngayLapHD;
	private Boolean trangThai;
	private String phuongThuc;
	private Integer soLuong;
	private HoaDon hoaDon;
	private DichVu dicvu;
	
	public ChiTietHoaDon(HoaDon hoaDon, Phong phong, LocalDateTime ngayLapHD, Boolean trangThai, String phuongThuc,
			Integer soluong, DichVu dv) {
		super();
		this.hoaDon=hoaDon;
		this.phong = phong;
		this.ngayLapHD = ngayLapHD;
		this.trangThai = trangThai;
		this.phuongThuc = phuongThuc;
		this.soLuong = soluong;
		this.dicvu=dv;
	}
	
	public ChiTietHoaDon() {
		super();
	}

	public Integer getsoLuong() {
		return soLuong;
	}

	public void setMaCTHD(Integer sl) {
		this.soLuong = sl;
	}

	public HoaDon getHD() {
		return hoaDon;
	}
	public void setCtDV(HoaDon hd) {
		this.hoaDon = hd;
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

	public DichVu getDicvu() {
		return dicvu;
	}

	public void setDicvu(DichVu dicvu) {
		this.dicvu = dicvu;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dicvu, hoaDon, phong);
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
		return Objects.equals(dicvu, other.dicvu) && Objects.equals(hoaDon, other.hoaDon)
				&& Objects.equals(phong, other.phong);
	}

	
	
}
