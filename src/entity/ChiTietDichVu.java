package entity;

import java.util.Objects;

public class ChiTietDichVu {
	private Integer soluong;
	private DichVu dichvu;
	private String maCTDV;
	public ChiTietDichVu(Integer soluong, DichVu dichvu, String maCTDV) {
		super();
		this.soluong = soluong;
		this.dichvu = dichvu;
		this.maCTDV = maCTDV;
	}
	public ChiTietDichVu() {
		super();
	}
	public Integer getSoluong() {
		return soluong;
	}
	public void setSoluong(Integer soluong) {
		this.soluong = soluong;
	}
	public DichVu getDichvu() {
		return dichvu;
	}
	public void setDichvu(DichVu dichvu) {
		this.dichvu = dichvu;
	}
	public String getMaCTDV() {
		return maCTDV;
	}
	public void setMaCTDV(String maCTDV) {
		this.maCTDV = maCTDV;
	}
	@Override
	public int hashCode() {
		return Objects.hash(maCTDV);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChiTietDichVu other = (ChiTietDichVu) obj;
		return Objects.equals(maCTDV, other.maCTDV);
	}
	
}
