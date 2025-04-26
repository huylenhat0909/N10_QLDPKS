package entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class KhuyenMai {
	private String maKM;
	private String tenKM;
	private LocalDateTime ngayApDung;
	private LocalDateTime ngayHetHan;
	private Double tienApdungKM;
	private Double phanTramKM;
	public KhuyenMai(String maKM, String tenKM, LocalDateTime ngayApDung, LocalDateTime ngayHetHan,
			Double tienApdungKM,Double phanTramKM) {
		super();
		this.maKM = maKM;
		this.tenKM = tenKM;
		this.ngayApDung = ngayApDung;
		this.ngayHetHan = ngayHetHan;
		this.tienApdungKM = tienApdungKM;
		this.setPhanTramKM(phanTramKM);
	}
	public KhuyenMai() {
	}
	public String getMaKM() {
		return maKM;
	}
	public void setMaKM(String maKM) {
		this.maKM = maKM;
	}
	public String getTenKM() {
		return tenKM;
	}
	public void setTenKM(String tenKM) {
		this.tenKM = tenKM;
	}
	public LocalDateTime getNgayApDung() {
		return ngayApDung;
	}
	public void setNgayApDung(LocalDateTime ngayApDung) {
		this.ngayApDung = ngayApDung;
	}
	public LocalDateTime getNgayHetHan() {
		return ngayHetHan;
	}
	public void setNgayHetHan(LocalDateTime ngayHetHan) {
		this.ngayHetHan = ngayHetHan;
	}
	public Double getTienApdungKM() {
		return tienApdungKM;
	}
	public void setTienApdungKM(Double tienApdungKM) {
		this.tienApdungKM = tienApdungKM;
	}
	@Override
	public int hashCode() {
		return Objects.hash(maKM);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KhuyenMai other = (KhuyenMai) obj;
		return Objects.equals(maKM, other.maKM);
	}
	public Double getPhanTramKM() {
		return phanTramKM;
	}
	public void setPhanTramKM(Double phanTramKM) {
		this.phanTramKM = phanTramKM;
	}
	
}
