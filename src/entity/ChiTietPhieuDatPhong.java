package entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class ChiTietPhieuDatPhong {
	private PhieuDatPhong phietDP;
	private Phong phong;
	private LocalDateTime gioDatPhong;
	private LocalDateTime gioTraPhong;
	private Boolean kieuThue;
	private Double giaPhongtheoKieuThue;
	public ChiTietPhieuDatPhong(PhieuDatPhong phietDP, Phong phong, LocalDateTime gioDatPhong,
			LocalDateTime gioTraPhong,Boolean kieuThue) {
		super();
		this.phietDP = phietDP;
		this.phong = phong;
		this.gioDatPhong = gioDatPhong;
		this.gioTraPhong = gioTraPhong;
		this.kieuThue=kieuThue;
		this.setGiaPhongtheoKieuThue();
	}
	public ChiTietPhieuDatPhong() {
		super();
	}
	public PhieuDatPhong getPhietDP() {
		return phietDP;
	}
	public void setPhietDP(PhieuDatPhong phietDP) {
		this.phietDP = phietDP;
	}
	public Phong getPhong() {
		return phong;
	}
	public void setPhong(Phong phong) {
		this.phong = phong;
	}
	public LocalDateTime getGioDatPhong() {
		return gioDatPhong;
	}
	public void setGioDatPhong(LocalDateTime gioDatPhong) {
		this.gioDatPhong = gioDatPhong;
	}
	public LocalDateTime getGioTraPhong() {
		return gioTraPhong;
	}
	public void setGioTraPhong(LocalDateTime gioTraPhong) {
		this.gioTraPhong = gioTraPhong;
	}
	@Override
	public int hashCode() {
		return Objects.hash(phietDP, phong);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChiTietPhieuDatPhong other = (ChiTietPhieuDatPhong) obj;
		return Objects.equals(phietDP, other.phietDP) && Objects.equals(phong, other.phong);
	}
	public Boolean getKieuThue() {
		return kieuThue;
	}
	public void setKieuThue(Boolean kieuThue) {
		this.kieuThue = kieuThue;
	}
	public Double getGiaPhongtheoKieuThue() {
		return giaPhongtheoKieuThue;
	}
	public void setGiaPhongtheoKieuThue() {
		if(kieuThue) {
			this.giaPhongtheoKieuThue=phong.getGiaPhong()*phong.getLoaiPhong().getGiaPhongtheongay();
		}else {
			this.giaPhongtheoKieuThue=phong.getGiaPhong()*phong.getLoaiPhong().getGiaPhongtheogio();
		}
	}
	
	
}
