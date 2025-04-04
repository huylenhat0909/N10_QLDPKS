package entity;

import java.util.Objects;

public class LoaiPhong {
	private String maLoaiP;
	private String tenLoaiP;
	private String moTa;
	private Double giaPhongtheogio;
	private Double giaPhongtheongay;
	public LoaiPhong(String maLoaiP, String tenLoaiP, int soTang, String moTa, Double giaPhongtheogio,
			Double giaPhongtheongay) {
		super();
		this.maLoaiP = maLoaiP;
		this.tenLoaiP = tenLoaiP;
		this.moTa = moTa;
		this.giaPhongtheogio = giaPhongtheogio;
		this.giaPhongtheongay = giaPhongtheongay;
	}
	public LoaiPhong() {
	}
	public String getMaLoaiP() {
		return maLoaiP;
	}
	public void setMaLoaiP(String maLoaiP) {
		this.maLoaiP = maLoaiP;
	}
	public String getTenLoaiP() {
		return tenLoaiP;
	}
	public void setTenLoaiP(String tenLoaiP) {
		this.tenLoaiP = tenLoaiP;
	}
	public String getMoTa() {
		return moTa;
	}
	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}
	public Double getGiaPhongtheogio() {
		return giaPhongtheogio;
	}
	public void setGiaPhongtheogio(Double giaPhongtheogio) {
		this.giaPhongtheogio = giaPhongtheogio;
	}
	public Double getGiaPhongtheongay() {
		return giaPhongtheongay;
	}
	public void setGiaPhongtheongay(Double giaPhongtheongay) {
		this.giaPhongtheongay = giaPhongtheongay;
	}
	@Override
	public int hashCode() {
		return Objects.hash(maLoaiP);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LoaiPhong other = (LoaiPhong) obj;
		return Objects.equals(maLoaiP, other.maLoaiP);
	}
	
}
