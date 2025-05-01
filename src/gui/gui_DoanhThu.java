package gui;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import connectDB.ConnectDB;

public class gui_DoanhThu extends JPanel {

    private double tongdvthang;
	private double tongphongthang;
	private ChartPanel chartPanel;
	private JPanel panel_thang;
	public gui_DoanhThu() {
    	setLayout(new BorderLayout());
    	Font font = new Font("Arial",Font.BOLD, 16);
    	Map<String, Double> ds_tiendv = this.tongDTDichVu();
    	Map<String, Double> ds_tienPhong = this.tongDTPhong();
    	Double tongdv=0.0;
    	Double tongphong=0.0;
    	for (Double tien: ds_tiendv.values()) {
    		tongdv+=tien;
    	}
    	for (Double tien: ds_tienPhong.values()) {
    		tongphong+=tien;
    	}
    	Double tongTatCa= tongdv+tongphong;
    	
    	// ========== 1. Panel phía trên: Hiển thị tổng doanh thu ==========
    	JPanel infoPanel = new JPanel(new GridLayout(1, 3, 20, 10));
    	infoPanel.setBorder(BorderFactory.createTitledBorder("Tổng Doanh Thu"));
    	NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
    	nf.setMaximumFractionDigits(0); // Không có phần thập phân
    	nf.setRoundingMode(RoundingMode.HALF_UP);
    	// Format hiển thị số tiền (theo triệu VNĐ, làm tròn 2 chữ số)
    	String tongStr = String.format("Tổng:" +nf.format(tongTatCa) +" VNĐ");
    	String dvStr = String.format("Dịch vụ:" + nf.format(tongdv)+" VNĐ" );
    	String phongStr = String.format("Giá phòng: "+ nf.format(tongphong) +" VNĐ");

    	JLabel lblTong = new JLabel(tongStr, SwingConstants.CENTER);
    	lblTong.setFont(font);
    	JLabel lblDichVu = new JLabel(dvStr, SwingConstants.CENTER);
    	lblDichVu.setFont(font);
    	JLabel lblGiaPhong = new JLabel(phongStr, SwingConstants.CENTER);
    	lblGiaPhong.setFont(font);
    	// Thêm label vào panel
    	infoPanel.add(lblTong);
    	infoPanel.add(lblDichVu);
    	infoPanel.add(lblGiaPhong);
    	infoPanel.setPreferredSize(new Dimension(WIDTH, 100));
        add(infoPanel, BorderLayout.NORTH);
        
     // ========== 0. Panel chọn tháng/năm ==========
        panel_thang= new JPanel(new BorderLayout());
        JPanel filterPanel = new JPanel();

        JComboBox<Integer> cbThang = new JComboBox<>();
        for (int i = 1; i <= 12; i++) cbThang.addItem(i);

        JComboBox<Integer> cbNam = new JComboBox<>();
        for (int i = 2024; i <= 2025; i++) cbNam.addItem(i);

        JButton btnXem = new JButton("Xem Doanh Thu");
        filterPanel.add(new JLabel("Tháng:"));
        filterPanel.add(cbThang);
        filterPanel.add(new JLabel("Năm:"));
        filterPanel.add(cbNam);
        filterPanel.add(btnXem);
        panel_thang.add(filterPanel,BorderLayout.NORTH);
        // Thêm filterPanel vào frame hoặc container phía trên infoPanel


        // ========== 1. Panel hiển thị tổng doanh thu ==========
        JPanel infoPanel_theothang = new JPanel(new GridLayout(1, 3, 20, 10));
        infoPanel_theothang.setBorder(BorderFactory.createTitledBorder("Tổng Doanh Thu theo tháng"));

        JLabel lblTongthang = new JLabel("Tổng: 0 VNĐ", SwingConstants.CENTER);
        JLabel lblDichVuthang = new JLabel("Dịch vụ: 0 VNĐ", SwingConstants.CENTER);
        JLabel lblGiaPhongthang = new JLabel("Giá phòng: 0 VNĐ", SwingConstants.CENTER);
        lblTongthang.setFont(font);
        lblDichVuthang.setFont(font);
        lblGiaPhongthang.setFont(font);
        infoPanel_theothang.add(lblTongthang);
        infoPanel_theothang.add(lblDichVuthang);
        infoPanel_theothang.add(lblGiaPhongthang);

        // Xử lý khi nhấn nút "Xem Doanh Thu"
        btnXem.addActionListener(e -> {
            int thang = (int) cbThang.getSelectedItem();
            int nam = (int) cbNam.getSelectedItem();

            // Gọi phương thức để lấy dữ liệu từ SQL
            Map<String, Double> dsdvthang = layDsDichVu(thang, nam);
            Map<String, Double> dsphongthang = layDsGiaPhong(thang, nam);
            tongdvthang=0.0;
        	tongphongthang=0.0;
        	for (Double tien: dsdvthang.values()) {
        		tongdvthang+=tien;
        	}
        	for (Double tien: dsphongthang.values()) {
        		tongphongthang+=tien;
        	}
            Double tongTatCathang = tongdvthang+tongphongthang; //layTongDoanhThu(thang, nam);
            lblTongthang.setText("Tổng: " + nf.format(tongTatCathang) + " VNĐ");
            lblDichVuthang.setText("Dịch vụ: " + nf.format(tongdvthang) + " VNĐ");
            lblGiaPhongthang.setText("Giá phòng: " + nf.format(tongphongthang) + " VNĐ");
        });
        
        
        panel_thang.add(infoPanel_theothang,BorderLayout.CENTER);
        
     // ========== Biểu đồ cột 2 dữ liệu: Phòng và Dịch vụ ==========
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int nam = (int) cbNam.getSelectedItem();

        for (int i = 1; i <= 12; i++) {
        	Map<String, Double> dsdvthang = layDsDichVu(i, 2025);
            Map<String, Double> dsphongthang = layDsGiaPhong(i, 2025);
            Double tongdvthang=0.0;
        	Double tongphongthang=0.0;
        	for (Double tien: dsdvthang.values()) {
        		tongdvthang+=tien;
        	}
        	for (Double tien: dsphongthang.values()) {
        		tongphongthang+=tien;
        	}
            dataset.addValue(tongphongthang, "Phòng", "Tháng " + i);
            dataset.addValue(tongdvthang, "Dịch vụ", "Tháng " + i);
        }

        // Tạo biểu đồ cột
        JFreeChart chart = ChartFactory.createBarChart(
            "Biểu đồ Doanh Thu Phòng và Dịch Vụ Theo Tháng",
            "Tháng",
            "Doanh thu (triệu VNĐ)",
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false
        );

        // Hiển thị biểu đồ trong ChartPanel
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(WIDTH, 600));
        panel_thang.add(chartPanel, BorderLayout.SOUTH);
        add(panel_thang, BorderLayout.CENTER);
        cbNam.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    int nam = (int) cbNam.getSelectedItem();
                    veBieuDoDoanhThu(nam);  // gọi hàm vẽ lại biểu đồ
                }
            }
        });
    }
    
	private void veBieuDoDoanhThu(int nam) {
	    // Tạo dataset mới
	    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

	    for (int i = 1; i <= 12; i++) {
	        Map<String, Double> dsdvthang = layDsDichVu(i, nam);
	        Map<String, Double> dsphongthang = layDsGiaPhong(i, nam);

	        double tongdvthang = dsdvthang.values().stream().mapToDouble(Double::doubleValue).sum();
	        double tongphongthang = dsphongthang.values().stream().mapToDouble(Double::doubleValue).sum();

	        dataset.addValue(tongphongthang, "Phòng", "Tháng " + i);
	        dataset.addValue(tongdvthang, "Dịch vụ", "Tháng " + i);
	    }

	    // Tạo biểu đồ mới
	    JFreeChart chart = ChartFactory.createBarChart(
	        "Biểu đồ Doanh Thu Phòng và Dịch Vụ Theo Tháng",
	        "Tháng",
	        "Doanh thu (triệu VNĐ)",
	        dataset,
	        PlotOrientation.VERTICAL,
	        true, true, false
	    );

	    // Nếu chartPanel đã tồn tại, gỡ bỏ khỏi panel_thang
	    if (chartPanel != null) {
	        panel_thang.remove(chartPanel);
	    }

	    // Tạo và thêm chartPanel mới
	    chartPanel = new ChartPanel(chart);
	    chartPanel.setPreferredSize(new Dimension(WIDTH, 600));
	    panel_thang.add(chartPanel, BorderLayout.SOUTH);

	    panel_thang.revalidate();
	    panel_thang.repaint();
	}

    
    private Map<String, Double> layDsGiaPhong(int thang, int nam) {
    	Map<String, Double> result = new HashMap<>();

    	try  {
	    	ConnectDB.getInstance().connect();
			Connection con= ConnectDB.getConnection();
			String sql =  "SELECT ctpdp.giaPhongtheoKieuThue, ctpdp.maPhong FROM CTPhieuDatPhong ctpdp "
					+ "WHERE MONTH(ctpdp.gioDatPhong) = ? AND YEAR(ctpdp.gioDatPhong) =?";
			PreparedStatement stmt = con.prepareStatement(sql);
	        stmt.setInt(1, thang);
	        stmt.setInt(2, nam);

	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
                String maPhong = rs.getString("maPhong");
                double tienPhong = rs.getDouble("giaPhongtheoKieuThue");
                result.put(maPhong, tienPhong);
            }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
    	return result;
	}



	private Map<String, Double> layDsDichVu(int thang, int nam) {
		Map<String, Double> dsGiaPhong = new HashMap<>();
	    try {
	    	ConnectDB.getInstance().connect();
			Connection con= ConnectDB.getConnection();
	        String sql = 
	            "SELECT c.maHoaDon, SUM( (d.giaTien*c.soLuongDV)) AS tongTien  " +
	            "FROM ChiTietHoaDon c " +
	            "JOIN DichVu d ON c.maDichVu = d.maDichVu " +
	            "WHERE MONTH(c.ngayLapHoaDon) = ? AND YEAR(c.ngayLapHoaDon) =?  " +
	            "GROUP BY c.maHoaDon";
	        PreparedStatement stmt = con.prepareStatement(sql);
	        stmt.setInt(1, thang);
	        stmt.setInt(2, nam);
	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
	            String maHD = rs.getString("maHoaDon");
	            Double tongTien = rs.getDouble("tongTien");
	            dsGiaPhong.put(maHD, tongTien);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();   
	}
	    return dsGiaPhong;
}



	public  Map<String, Double> tongDTDichVu() {
    	Map<String, Double> result = new HashMap<>();

    	try  {
	    	ConnectDB.getInstance().connect();
			Connection con= ConnectDB.getConnection();
			String sql =  "SELECT hd.maHoaDon ,SUM( (dv.giaTien*hd.soLuongDV))TongDV FROM ChiTietHoaDon hd \r\n"
					+ "JOIN DichVu dv on dv.maDichVu=hd.maDichVu GROUP BY hd.maHoaDon";
			Statement stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery(sql);
	        while (rs.next()) {
                String maHd = rs.getString("maHoaDon");
                double tongDv = rs.getDouble("TongDV");
                result.put(maHd, tongDv);
            }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
    	return result;
    }
    public  Map<String, Double> tongDTPhong() {
    	Map<String, Double> result = new HashMap<>();

    	try  {
	    	ConnectDB.getInstance().connect();
			Connection con= ConnectDB.getConnection();
			String sql =  "SELECT ctpdp.giaPhongtheoKieuThue, ctpdp.maPhong FROM CTPhieuDatPhong ctpdp ";
			Statement stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery(sql);
	        while (rs.next()) {
                String maPhong = rs.getString("maPhong");
                double tienPhong = rs.getDouble("giaPhongtheoKieuThue");
                result.put(maPhong, tienPhong);
            }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
    	return result;
    }
    // Hàm main để chạy thử giao diện
    public static void main(String[] args) {
        JFrame frame = new JFrame("Thống kê Doanh Thu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new gui_DoanhThu());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

