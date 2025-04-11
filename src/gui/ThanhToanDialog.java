package gui;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

import java.awt.*;

import javax.swing.*;

public class ThanhToanDialog extends JDialog {
    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> comboDichVu;
    private JTextField tfSoLuong;
    private JLabel lblTongTien;

    private double tongTien = 0;

    public ThanhToanDialog(JFrame parent, String maPhong, String tenPhong, String tenKhach, String sdt) {
        super(parent, "Thanh toán phòng", true);
        setSize(600, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Thông tin khách
        JPanel panelInfo = new JPanel(new GridLayout(2, 2, 5, 5));
        panelInfo.setBorder(BorderFactory.createTitledBorder("Thông tin phòng & khách"));
        panelInfo.add(new JLabel("Phòng: " + maPhong + " - " + tenPhong));
        panelInfo.add(new JLabel("Khách: " + tenKhach + " - " + sdt));

        // Bảng dịch vụ đã sử dụng
        model = new DefaultTableModel(new Object[]{"Dịch vụ", "Đơn giá", "Số lượng", "Thành tiền"}, 0);
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        // Panel thêm dịch vụ
        JPanel panelThemDV = new JPanel(new FlowLayout());
        comboDichVu = new JComboBox<>(new String[]{"Nước suối", "Giặt ủi", "Ăn sáng"});
        tfSoLuong = new JTextField(5);
        JButton btnThem = new JButton("Thêm dịch vụ");
        panelThemDV.add(new JLabel("Dịch vụ:"));
        panelThemDV.add(comboDichVu);
        panelThemDV.add(new JLabel("Số lượng:"));
        panelThemDV.add(tfSoLuong);
        panelThemDV.add(btnThem);

        // Label tổng tiền
        lblTongTien = new JLabel("Tổng tiền: 0 VNĐ");
        lblTongTien.setFont(new Font("Arial", Font.BOLD, 16));
        lblTongTien.setHorizontalAlignment(SwingConstants.RIGHT);

        // Nút thanh toán
        JButton btnThanhToan = new JButton("Xác nhận thanh toán");

        // Add listener cho thêm dịch vụ
        btnThem.addActionListener(e -> {
            String tenDV = (String) comboDichVu.getSelectedItem();
            int soLuong = Integer.parseInt(tfSoLuong.getText());

            // TODO: Lấy giá từ DB, tạm thời fix cứng
            double gia = switch (tenDV) {
                case "Nước suối" -> 10000;
                case "Giặt ủi" -> 20000;
                case "Ăn sáng" -> 50000;
                default -> 0;
            };

            double thanhTien = gia * soLuong;
            model.addRow(new Object[]{tenDV, gia, soLuong, thanhTien});
            tongTien += thanhTien;
            lblTongTien.setText("Tổng tiền: " + String.format("%,.0f", tongTien) + " VNĐ");
        });

        // Nút thanh toán
        btnThanhToan.addActionListener(e -> {
            // TODO: Lưu hóa đơn và chi tiết vào DB
            JOptionPane.showMessageDialog(this, "Thanh toán thành công!\nTổng: " + tongTien + " VNĐ");
            dispose();
        });

        // Add toàn bộ vào dialog
        add(panelInfo, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelThemDV, BorderLayout.WEST);
        add(lblTongTien, BorderLayout.EAST);
        add(btnThanhToan, BorderLayout.SOUTH);
    }
}

