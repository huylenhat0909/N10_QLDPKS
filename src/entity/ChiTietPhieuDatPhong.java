package entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class ChiTietPhieuDatPhong {
	private PhieuDatPhong phietDP;
	private Phong phong;
	private LocalDateTime gioDatPhong;
	private LocalDateTime gioTraPhong;
	public ChiTietPhieuDatPhong(PhieuDatPhong phietDP, Phong phong, LocalDateTime gioDatPhong,
			LocalDateTime gioTraPhong) {
		super();
		this.phietDP = phietDP;
		this.phong = phong;
		this.gioDatPhong = gioDatPhong;
		this.gioTraPhong = gioTraPhong;
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
	
	
}
