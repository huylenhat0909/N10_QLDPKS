package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import connectDB.ConnectDB;
import dao.DaoPhong;
import entity.KhachHang;
import entity.Phong;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class gui_SoLuongPhong extends JPanel {

    private JLabel lblTongSoPhong;
    private JPanel pnlChiTiet;
	private DefaultTableModel modelPhong;
	private JTable tblPhong;

    public gui_SoLuongPhong() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        DaoPhong daophong = new DaoPhong();

        // Tiêu đề
        JLabel lblTieuDe = new JLabel("THỐNG KÊ SỐ LƯỢNG PHÒNG ĐÃ ĐẶT", JLabel.CENTER);
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTieuDe, BorderLayout.NORTH);

        // Panel chính chứa nội dung
        JPanel pnlCenter = new JPanel(new BorderLayout(10, 10));
        add(pnlCenter, BorderLayout.CENTER);

        // Tổng số phòng
        lblTongSoPhong = new JLabel("Tổng số phòng đã đặt: " + tongSoLuongPhong(), JLabel.CENTER);
        lblTongSoPhong.setFont(new Font("Arial", Font.PLAIN, 16));
        pnlCenter.add(lblTongSoPhong, BorderLayout.NORTH);

        // Bảng thống kê phòng
        String[] colPhong = {"Tên phòng", "Số lượt đặt"};
        modelPhong = new DefaultTableModel(colPhong, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho chỉnh sửa ô nào cả
            }
        };
        tblPhong = new JTable(modelPhong);

        JScrollPane scrollPhong = new JScrollPane(tblPhong);
        scrollPhong.setPreferredSize(new Dimension(400, 200));
        JPanel pnlBang = new JPanel(new BorderLayout());
        pnlBang.setBorder(BorderFactory.createTitledBorder("Top 5 phòng được đặt nhiều nhất"));
        pnlBang.add(scrollPhong, BorderLayout.CENTER);
        pnlCenter.add(pnlBang, BorderLayout.CENTER);

        // Load dữ liệu bảng
        Map<String, Integer> dsPhong = layDstop5Phong();
        for (Map.Entry<String, Integer> entry : dsPhong.entrySet()) {
            String maPhong = entry.getKey();
            Phong phong = daophong.getPhongtheoMa(maPhong);
            if (phong != null) {
                modelPhong.addRow(new Object[]{phong.getTenPhong(), entry.getValue()});
            }
        }
    }

    /**
     * Cập nhật số lượng phòng đã đặt và hiển thị lên giao diện
     */
    public int tongSoLuongPhong() {
    	int tong=0;
    	try  {
	    	ConnectDB.getInstance().connect();
			Connection con= ConnectDB.getConnection();
			String sql =  "Select Count (distinct maPhong) as tong  from ChiTietHoaDon ";
			PreparedStatement stmt = con.prepareStatement(sql);
	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
                tong=rs.getInt("tong");
            }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
    	return tong;
    }
    private Map<String, Integer> layDstop5Phong() {
		Map<String, Integer> dsPhong = new HashMap<>();
	    try {
	    	ConnectDB.getInstance().connect();
			Connection con= ConnectDB.getConnection();
	        String sql = 
	            "Select top 5 cthd.maPhong,Count (maHoaDon) as soluong from ChiTietHoaDon cthd join Phong p on cthd.maPhong=p.maPhong group by maHoaDon,cthd.maPhong order by soluong DESC";
	        PreparedStatement stmt = con.prepareStatement(sql);
	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
	            String maHD = rs.getString("maPhong");
	            Integer tongTien = rs.getInt("soluong");
	            dsPhong.put(maHD, tongTien);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();   
	}
	    return dsPhong;
}
    // Hàm test
    public static void main(String[] args) {
        JFrame frame = new JFrame("Số lượng phòng đã đặt");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new gui_SoLuongPhong());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
