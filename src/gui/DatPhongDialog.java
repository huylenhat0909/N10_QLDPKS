package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;

import entity.KhachHang;
import entity.NhanVien;
import entity.Phong;

public class DatPhongDialog extends JDialog {
    private JTextField tfTenKhach;
	private JTextField tfSDT;
	private Component dateNhan;
	private Component dateTra;
	private Date ngayNhan;
	private Date ngayTra;
	private JTextField tfEmail;
	private JComboBox cbKieuThue;
	private JLabel lblGiaPhong;

	public DatPhongDialog(JFrame parent, Phong phong, NhanVien nhanVien ) {
		super(parent, "Tạo phiếu đặt phòng", true);
        setSize(500, 520);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        //Panle hiển thi thông tin nhân viên phụ trách đặt phòng
     // Panel hiển thị thông tin nhân viên phụ trách đặt phòng
        JPanel panelNhanVien = new JPanel(new GridLayout(3, 2, 5, 5));
        panelNhanVien.setBorder(BorderFactory.createTitledBorder("Thông tin nhân viên"));
        panelNhanVien.add(new JLabel("Mã nhân viên:"));
        panelNhanVien.add(new JLabel(nhanVien.getMaNV()));
        panelNhanVien.add(new JLabel("Tên nhân viên:"));
        panelNhanVien.add(new JLabel(nhanVien.getTenNV()));
        panelNhanVien.add(new JLabel("Chức vụ:"));
        panelNhanVien.add(new JLabel(nhanVien.getChucVu()));
        // Panel hiển thị thông tin phòng
        JPanel panelPhong = new JPanel(new GridLayout(5, 2, 5, 5));
        panelPhong.setBorder(BorderFactory.createTitledBorder("Thông tin phòng"));
        panelPhong.add(new JLabel("Mã phòng:"));
        panelPhong.add(new JLabel(phong.getMaPhong()));
        panelPhong.add(new JLabel("Tên phòng:"));
        panelPhong.add(new JLabel(phong.getTenPhong()));
        panelPhong.add(new JLabel("Loại phòng:"));
        panelPhong.add(new JLabel(phong.getLoaiPhong().getTenLoaiP()));
        panelPhong.add(new JLabel("Trạng thái:"));
        panelPhong.add(new JLabel(phong.getTrangThai()));
        panelPhong.add(new JLabel("Giá phòng:"));
        lblGiaPhong = new JLabel();  // Sẽ cập nhật sau
        panelPhong.add(lblGiaPhong);
        // Panel nhập thông tin khách hàng
        JPanel panelKhach = new JPanel(new GridLayout(4, 2, 5, 5));
        panelKhach.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));
        tfTenKhach = new JTextField();
        tfSDT = new JTextField();
        tfEmail = new JTextField();
        cbKieuThue = new JComboBox<>(new String[]{"Theo giờ", "Theo ngày"});
        cbKieuThue.setSelectedIndex(0);
        panelKhach.add(new JLabel("Tên khách:"));
        panelKhach.add(tfTenKhach);
        panelKhach.add(new JLabel("SĐT:"));
        panelKhach.add(tfSDT);
        panelKhach.add(new JLabel("Email:"));
        panelKhach.add(tfEmail);
        panelKhach.add(new JLabel("Kiểu thuê:"));
        panelKhach.add(cbKieuThue);
        
        // Panel chọn ngày đặt
     // Panel chọn ngày đặt
        JPanel panelNgay = new JPanel(new GridLayout(2, 2, 5, 5));
        panelNgay.setBorder(BorderFactory.createTitledBorder("Thời gian đặt"));
        dateNhan = new JDateChooser();
        dateTra = new JDateChooser();

        // Đồng bộ logic theo đúng kiểu text đã gán vào JComboBox
        String kieuThueHienTai = (String) cbKieuThue.getSelectedItem();
        dateTra.setEnabled("Theo ngày".equals(kieuThueHienTai)); // sửa lại so với "Thuê ngày"
        DecimalFormat formatter = new DecimalFormat("#,### VNĐ");
        // Gắn listener để xử lý thay đổi kiểu thuê
        cbKieuThue.addActionListener(e -> {
            String kieu = (String) cbKieuThue.getSelectedItem();
            dateTra.setEnabled("Theo ngày".equals(kieu));
            if ("Theo ngày".equals(kieu)) {
            	lblGiaPhong.setText(formatter.format(
            		    phong.getGiaPhong() * phong.getLoaiPhong().getGiaPhongtheongay()
            		) + " VNĐ/ngày");
            } else {
            	lblGiaPhong.setText(formatter.format(
            		    phong.getGiaPhong() * phong.getLoaiPhong().getGiaPhongtheogio()
            		) + " VNĐ/giờ");
            }
        });

        panelNgay.add(new JLabel("Ngày nhận:"));
        panelNgay.add(dateNhan);
        panelNgay.add(new JLabel("Ngày trả:"));
        panelNgay.add(dateTra);
        // Nút xác nhận
        JButton btnXacNhan = new JButton("Tạo phiếu đặt");
        btnXacNhan.addActionListener(e -> {
            String tenKhach = tfTenKhach.getText().trim();
            String sdt = tfSDT.getText().trim();
            String email = tfEmail.getText().trim();
            String kieuThue = (String) cbKieuThue.getSelectedItem();
            ngayNhan = ((JDateChooser) dateNhan).getDate();
            ngayTra = ((JDateChooser) dateTra).getDate();

            if (tenKhach.isEmpty() || sdt.isEmpty() || email.isEmpty() || ngayNhan == null || ngayTra == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, "Email không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if ("Thuê ngày".equals(kieuThue)) {
                if (ngayTra == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày trả!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!ngayTra.after(ngayNhan)) {
                    JOptionPane.showMessageDialog(this, "Ngày trả phải sau ngày nhận!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                ngayTra = null; // Không dùng
            }
            // TODO: Ghi vào SQL bảng phieu_dat_phong hoặc in ra console thử
       

            JOptionPane.showMessageDialog(this, "Tạo phiếu đặt thành công!");
            dispose(); // Đóng cửa sổ
        });

        // Add toàn bộ vào dialog
        JPanel centerPanel = new JPanel(new GridLayout(4, 1));
        centerPanel.add(panelNhanVien);
        centerPanel.add(panelPhong);
        centerPanel.add(panelKhach);
        centerPanel.add(panelNgay);
        add(centerPanel, BorderLayout.CENTER);
        add(btnXacNhan, BorderLayout.SOUTH);
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$");
    }
}