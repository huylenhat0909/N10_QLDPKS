package entity;

import java.util.Objects;

public class Phong {
	private String maPhong;
	private String tenPhong;
	private LoaiPhong loaiPhong;
	private Integer soGiuong;
	private Double hesoGiatheoGiuong;
	public Phong(String maPhong, String tenPhong, LoaiPhong loaiPhong, Integer soGiuong, Double hesoGiatheoGiuong) {
		super();
		this.maPhong = maPhong;
		this.tenPhong = tenPhong;
		this.loaiPhong = loaiPhong;
		this.soGiuong = soGiuong;
		this.hesoGiatheoGiuong = hesoGiatheoGiuong;
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
	public Double getHesoGiatheoGiuong() {
		return hesoGiatheoGiuong;
	}
	public void setHesoGiatheoGiuong(Double hesoGiatheoGiuong) {
		this.hesoGiatheoGiuong = hesoGiatheoGiuong;
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
	
}
