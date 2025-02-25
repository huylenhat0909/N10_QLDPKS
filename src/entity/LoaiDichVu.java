package entity;

public class LoaiDichVu {
	private String maLoaiDV;
	private String tenDV;
	public LoaiDichVu(String maLoaiDV, String tenDV) {
		super();
		this.maLoaiDV = maLoaiDV;
		this.tenDV = tenDV;
	}
	public LoaiDichVu() {
		
	}
	public String getMaLoaiDV() {
		return maLoaiDV;
	}
	public void setMaLoaiDV(String maLoaiDV) {
		this.maLoaiDV = maLoaiDV;
	}
	public String getTenDV() {
		return tenDV;
	}
	public void setTenDV(String tenDV) {
		this.tenDV = tenDV;
	}
	
}
