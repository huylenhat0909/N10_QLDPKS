package entity;

import java.util.Objects;

public class DichVu {
	private String maDichVu;
	private String tenDichVu;
	private String moTa;
	private Double giaTien;
	private Integer soLuong;
	private Boolean trangThai;
	private LoaiDichVu loaiDV;
	public DichVu(String maDichVu, String tenDichVu, String moTa, Double giaTien, Integer soLuong, Boolean trangThai,
			LoaiDichVu loaiDV) {
		super();
		this.maDichVu = maDichVu;
		this.tenDichVu = tenDichVu;
		this.moTa = moTa;
		this.giaTien = giaTien;
		this.soLuong = soLuong;
		this.trangThai = trangThai;
		this.loaiDV = loaiDV;
	}
	public DichVu() {
		
	}
	public String getMaDichVu() {
		return maDichVu;
	}
	public void setMaDichVu(String maDichVu) {
		this.maDichVu = maDichVu;
	}
	public String getTenDichVu() {
		return tenDichVu;
	}
	public void setTenDichVu(String tenDichVu) {
		this.tenDichVu = tenDichVu;
	}
	public String getMoTa() {
		return moTa;
	}
	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}
	public Double getGiaTien() {
		return giaTien;
	}
	public void setGiaTien(Double giaTien) {
		this.giaTien = giaTien;
	}
	public Integer getSoLuong() {
		return soLuong;
	}
	public void setSoLuong(Integer soLuong) {
		this.soLuong = soLuong;
	}
	public Boolean getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(Boolean trangThai) {
		this.trangThai = trangThai;
	}
	public LoaiDichVu getLoaiDV() {
		return loaiDV;
	}
	public void setLoaiDV(LoaiDichVu loaiDV) {
		this.loaiDV = loaiDV;
	}
	@Override
	public int hashCode() {
		return Objects.hash(maDichVu);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DichVu other = (DichVu) obj;
		return Objects.equals(maDichVu, other.maDichVu);
	}
	
}
