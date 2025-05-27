package gui;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
import org.jfree.data.general.DefaultPieDataset;

import connectDB.ConnectDB;
import dao.DaoDichVu;
import dao.DaoKhachHang;
import entity.DichVu;
import entity.KhachHang;

public class gui_SoLuongKH extends JPanel {

    private JTable tblKhachHang, tblHoaDon;
    private DefaultTableModel modelKhachHang, modelHoaDon;

    public gui_SoLuongKH() {
    	DaoKhachHang daokh= new DaoKhachHang();
        setLayout(new BorderLayout());
        Font font = new Font("Arial", Font.BOLD, 16);
    	NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
    	nf.setMaximumFractionDigits(0); // Không có phần thập phân
    	nf.setRoundingMode(RoundingMode.HALF_UP);
        // NORTH - Header
        JLabel lblTitle = new JLabel("Thống kê doanh thu của khách hàng", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);
        String[] colKH = {"Tên khách hàng","Số điện thoại", "Tổng tiền đã thanh toán"};
        modelKhachHang = new DefaultTableModel(colKH, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho chỉnh sửa ô nào cả
            }
        };	
        tblKhachHang = new JTable(modelKhachHang);
        
        JScrollPane scrollKH = new JScrollPane(tblKhachHang);
        scrollKH.setPreferredSize(new Dimension(300, 0));
        add(scrollKH, BorderLayout.WEST);
        Map<String, Double> dsKhachHang = layDSKHvaDT();
        for (Map.Entry<String, Double> entry : dsKhachHang.entrySet()) {
            String maKH = entry.getKey();
            KhachHang kh= daokh.getKhachHangtheoma(maKH);
            double tongTien = entry.getValue();
            modelKhachHang.addRow(new Object[]{kh.getTenKH(),kh.getSoDT(), String.format(nf.format(tongTien)+" VNĐ")});
        }

        // CENTER - Bảng hóa đơn
        String[] colHD = {"Mã hóa đơn", "Ngày lập", "Tổng tiền", "Phương thức thanh toán"};
        modelHoaDon = new DefaultTableModel(colHD, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho chỉnh sửa ô nào cả
            }
        };
        tblHoaDon = new JTable(modelHoaDon);
        JScrollPane scrollHD = new JScrollPane(tblHoaDon);
        add(scrollHD, BorderLayout.CENTER);

        Map<String, Integer> dsDichVu = layDsdv(); // Gọi hàm trả về dữ liệu dịch vụ
        DaoDichVu daodv= new DaoDichVu();
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        for (Map.Entry<String, Integer> entry : dsDichVu.entrySet()) {
        	String maDV=entry.getKey();
        	DichVu dv= daodv.getLoaiDVTheoMa(maDV);
            pieDataset.setValue(dv.getTenDichVu(), entry.getValue());
        }

        JFreeChart pieChart = ChartFactory.createPieChart(
            "Tỷ lệ dịch vụ sử dụng", // Tiêu đề biểu đồ
            pieDataset,              // Dữ liệu
            true, true, false        // Hiển thị chú thích, công cụ, URLs
        );

        ChartPanel chartPanel = new ChartPanel(pieChart);
        //chartPanel.setPreferredSize(new Dimension(800, 250));
        add(chartPanel, BorderLayout.SOUTH);
     // Xử lý sự kiện khi chọn khách hàng
        tblKhachHang.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tblKhachHang.getSelectedRow();
                if (selectedRow != -1) {
                    String sdt = tblKhachHang.getValueAt(selectedRow, 1).toString();
                    KhachHang kh= daokh.getKhachHangtheoSDT(sdt);
                    hienThiHoaDonTheoKH(kh);
                }
            }
        });
    }
    
 private void hienThiHoaDonTheoKH(KhachHang kh) {
		// TODO Auto-generated method stub
	 // Xóa dữ liệu cũ
	    modelHoaDon.setRowCount(0);
	    String sdt= kh.getSoDT();
	    // Lấy danh sách hóa đơn từ DB
	    java.util.List<Object[]> danhSach = layDanhSachHoaDonTheosdtKH(sdt);

	    // Đổ dữ liệu vào bảng
	    for (Object[] row : danhSach) {
	        modelHoaDon.addRow(row);
	    }
	}

	// Hàm main để chạy thử giao diện
    public static void main(String[] args) {
        JFrame frame = new JFrame("Thống kê doanh thu của khách hàng");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new gui_SoLuongKH());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    // Bạn có thể thêm các hàm để cập nhật dữ liệu cho bảng và biểu đồ ở đây
    private Map<String, Double> layDSKHvaDT() {
        Map<String,Double> result = new HashMap<>();

        String sql = """
            SELECT kh.maKH,
                   ROUND(ISNULL(tp.TongTienPhong, 0) + ISNULL(dv.TongTienDV, 0), 0) AS TongTien
            FROM
                (SELECT pd.maKhachHang, SUM(ctpdp.giaPhongtheoKieuThue) AS TongTienPhong
                 FROM CTPhieuDatPhong ctpdp
                 JOIN PhieuDatPhong pd ON pd.maPhieuDatPhong = ctpdp.maPhieuDatPhong
                 GROUP BY pd.maKhachHang) tp
            LEFT JOIN
                (SELECT hd.maKhachHang, SUM(cthd.soLuongDV * dv.giaTien) AS TongTienDV
                 FROM ChiTietHoaDon cthd
                 JOIN DichVu dv ON dv.maDichVu = cthd.maDichVu
                 JOIN HoaDon hd ON hd.maHoaDon = cthd.maHoaDon
                 GROUP BY hd.maKhachHang) dv
            ON tp.maKhachHang = dv.maKhachHang
            JOIN KhachHang kh ON kh.maKH = COALESCE(tp.maKhachHang, dv.maKhachHang)
        """;

        try{ 	
        	ConnectDB.getInstance().connect();
			Connection con= ConnectDB.getConnection();
			Statement stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String tenKhachHang = rs.getString("maKH");
                double tongTien = rs.getDouble("TongTien");
                result.put(tenKhachHang, tongTien);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // hoặc ghi log nếu dùng log framework
        }

        return result;
    }
    private java.util.List<Object[]> layDanhSachHoaDonTheosdtKH(String tenKH) {
        java.util.List<Object[]> danhSachHD = new java.util.ArrayList<>();
        
        String sql = """
            SELECT hd.maHoaDon, 
        	CONVERT(VARCHAR(10), MIN(cthd.ngayLapHoaDon), 103) AS ngayLapHoaDon, 
        	ROUND(SUM(cthd.soLuongDV * dv.giaTien), 0) AS TongTien,
        	MAX(cthd.phuongThucTT) AS phuongThucTT
        	FROM HoaDon hd
        	JOIN ChiTietHoaDon cthd ON cthd.maHoaDon = hd.maHoaDon
        	JOIN DichVu dv ON dv.maDichVu = cthd.maDichVu 
        	JOIN KhachHang kh ON kh.maKH = hd.maKhachHang
        	WHERE kh.sdt = N'""" +tenKH +"'GROUP BY hd.maHoaDon";
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String maHD = rs.getString("maHoaDon");
                String ngayLap = rs.getString("ngayLapHoaDon");
                double tongTien = rs.getDouble("TongTien");
                String trangthai = rs.getString("phuongThucTT");

                danhSachHD.add(new Object[]{maHD, ngayLap, tongTien + " VNĐ",trangthai});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return danhSachHD;
    }
    private Map<String, Integer> layDsdv() {
		Map<String, Integer> dsdv = new HashMap<>();
	    try {
	    	ConnectDB.getInstance().connect();
			Connection con= ConnectDB.getConnection();
	        String sql = 
	            "select maDichVu, sum( soLuongDV) as soluong  from ChiTietHoaDon group by maDichVu";
	        PreparedStatement stmt = con.prepareStatement(sql);
	        ResultSet rs = stmt.executeQuery();
	        boolean isFirstRow = true;
	        while (rs.next()) {
	            if (isFirstRow) {
	                isFirstRow = false; // Bỏ dòng đầu tiên
	                continue;
	            }
	            String maHD = rs.getString("maDichVu");
	            Integer tongTien = rs.getInt("soluong");
	            dsdv.put(maHD, tongTien);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();   
	}
	    return dsdv;
}
}
