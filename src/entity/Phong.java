package entity;

import java.util.Objects;

public class Phong {
	private String maPhong;
	private String tenPhong;
	private LoaiPhong loaiPhong;
	private Integer soGiuong;
	private Double giaPhong;
	private String trangThai;
	private Integer tang;
	public Phong(String maPhong, String tenPhong, LoaiPhong loaiPhong, Integer soGiuong, Double giaPhong, String trangThai,Integer tang) {
		this.maPhong = maPhong;
		this.tenPhong = tenPhong;
		this.loaiPhong = loaiPhong;
		this.soGiuong = soGiuong;
		this.setGiaPhong(giaPhong);
		this.trangThai=trangThai;
		this.setTang(tang);
	}
	
	public Phong(String maPhong) {
		super();
		this.maPhong = maPhong;
	}
	public String getMaPhong() {
		return maPhong;
	}
	public void setMaPhong(String maPhong) {
		this.maPhong = maPhong;
	}
	public String getTenPhong() {
		return tenPhong;
	}
	public void setTenPhong(String tenPhong) {
		this.tenPhong = tenPhong;
	}
	public LoaiPhong getLoaiPhong() {
		return loaiPhong;
	}
	public void setLoaiPhong(LoaiPhong loaiPhong) {
		this.loaiPhong = loaiPhong;
	}
	public Integer getSoGiuong() {
		return soGiuong;
	}
	public void setSoGiuong(Integer soGiuong) {
		this.soGiuong = soGiuong;
	}
	@Override
	public int hashCode() {
		return Objects.hash(maPhong);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Phong other = (Phong) obj;
		return Objects.equals(maPhong, other.maPhong);
	}
	public String getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}
	public Double getGiaPhong() {
		return giaPhong;
	}
	public void setGiaPhong(Double giaPhong) {
		this.giaPhong = giaPhong;
	}

	public Integer getTang() {
		return tang;
	}

	public void setTang(Integer tang) {
		this.tang = tang;
	}
	
}
