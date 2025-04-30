package gui;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
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

    public gui_DoanhThu() {
    	setLayout(new BorderLayout());
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
    	JLabel lblDichVu = new JLabel(dvStr, SwingConstants.CENTER);
    	JLabel lblGiaPhong = new JLabel(phongStr, SwingConstants.CENTER);

    	// Thêm label vào panel
    	infoPanel.add(lblTong);
    	infoPanel.add(lblDichVu);
    	infoPanel.add(lblGiaPhong);
    	infoPanel.setPreferredSize(new Dimension(WIDTH, 100));
        add(infoPanel, BorderLayout.NORTH);
        
     // ========== 0. Panel chọn tháng/năm ==========
        JPanel panel_thang= new JPanel(new BorderLayout());
        JPanel filterPanel = new JPanel();

        JComboBox<Integer> cbThang = new JComboBox<>();
        for (int i = 1; i <= 12; i++) cbThang.addItem(i);

        JComboBox<Integer> cbNam = new JComboBox<>();
        for (int i = 2022; i <= 2025; i++) cbNam.addItem(i);

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
        infoPanel_theothang.add(lblTongthang);
        infoPanel_theothang.add(lblDichVuthang);
        infoPanel_theothang.add(lblGiaPhongthang);

        // Xử lý khi nhấn nút "Xem Doanh Thu"
        btnXem.addActionListener(e -> {
            int thang = (int) cbThang.getSelectedItem();
            int nam = (int) cbNam.getSelectedItem();

            // Gọi phương thức để lấy dữ liệu từ SQL
            double tongTatCathang = 0.0; //layTongDoanhThu(thang, nam);
            double tongdvthang = 0.0; //layTongDichVu(thang, nam);
            double tongphongthang = 0.0; //layTongGiaPhong(thang, nam);

            lblTong.setText("Tổng: " + nf.format(tongTatCathang) + " VNĐ");
            lblDichVu.setText("Dịch vụ: " + nf.format(tongdvthang) + " VNĐ");
            lblGiaPhong.setText("Giá phòng: " + nf.format(tongphongthang) + " VNĐ");
        });
        
        
        panel_thang.add(infoPanel_theothang,BorderLayout.CENTER);
        
        // ========== 2. Biểu đồ Line Chart ==========
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(100, "Doanh thu", "Tháng 1");
        dataset.addValue(120, "Doanh thu", "Tháng 2");
        dataset.addValue(90, "Doanh thu", "Tháng 3");
        dataset.addValue(150, "Doanh thu", "Tháng 4");

        JFreeChart chart = ChartFactory.createLineChart(
            "Biểu đồ Doanh Thu Theo Tháng",
            "Tháng",
            "Doanh thu (triệu VNĐ)",
            dataset,
            PlotOrientation.VERTICAL,
            false, true, false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(WIDTH, 600));
        panel_thang.add(chartPanel, BorderLayout.SOUTH);
        add(panel_thang,BorderLayout.CENTER);
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

