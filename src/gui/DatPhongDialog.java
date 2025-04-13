package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;

import com.toedter.calendar.JDateChooser;

import dao.DaoCTPhieuDP;
import dao.DaoKhachHang;
import dao.DaoPhieuDP;
import dao.DaoPhong;
import entity.ChiTietPhieuDatPhong;
import entity.KhachHang;
import entity.NhanVien;
import entity.PhieuDatPhong;
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
	private DaoPhieuDP daophieudp;
	private DaoCTPhieuDP daoctphieudp;
	private List<PhieuDatPhong> dsphieudp;
	private PhieuDatPhong pdp;
	private DaoKhachHang daokh;
	private List<KhachHang> dskh;
	private JTextField tfCCCD;
	private ChiTietPhieuDatPhong ctpdp;
	private JOptionPane spinnerGioNhan;
	private DaoPhong daophong;

	public DatPhongDialog(JFrame parent, Phong phong, NhanVien nhanVien ) {
		super(parent, "Tạo phiếu đặt phòng", true);
        setSize(500, 620);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        daophieudp=new DaoPhieuDP();
        daoctphieudp=new DaoCTPhieuDP();
        daokh= new DaoKhachHang();
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
        JPanel panelKhach = new JPanel(new GridLayout(5, 2, 5, 5));
        panelKhach.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));
        tfTenKhach = new JTextField();
        tfSDT = new JTextField();
        tfEmail = new JTextField();
        tfCCCD= new JTextField();
        cbKieuThue = new JComboBox<>(new String[]{"Chọn kiểu đặt","Theo giờ", "Theo ngày"});
        cbKieuThue.setSelectedIndex(0);
        panelKhach.add(new JLabel("Tên khách:"));
        panelKhach.add(tfTenKhach);
        panelKhach.add(new JLabel("SĐT:"));
        panelKhach.add(tfSDT);
        panelKhach.add(new JLabel("Email:"));
        panelKhach.add(tfEmail);
        panelKhach.add(new JLabel("So CCCD:"));
        panelKhach.add(tfCCCD);
        panelKhach.add(new JLabel("Kiểu thuê:"));
        panelKhach.add(cbKieuThue);
        
        // Panel chọn ngày đặt
     // Panel chọn ngày đặt
        JPanel panelNgay = new JPanel(new GridLayout(4, 2, 5, 5)); // Thêm 1 dòng nữa để chứa giờ
        panelNgay.setBorder(BorderFactory.createTitledBorder("Thời gian đặt"));

        dateNhan = new JDateChooser();
        dateTra = new JDateChooser();

        // Tạo spinner giờ nhận và giờ trả (kiểu giờ: HH:mm)
        JSpinner spinnerGioNhan = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditorNhan = new JSpinner.DateEditor(spinnerGioNhan, "HH:mm");
        spinnerGioNhan.setEditor(timeEditorNhan);
        spinnerGioNhan.setValue(new Date()); // Giờ hiện tại

        JSpinner spinnerGioTra = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditorTra = new JSpinner.DateEditor(spinnerGioTra, "HH:mm");
        spinnerGioTra.setEditor(timeEditorTra);
        spinnerGioTra.setValue(new Date()); // Giờ hiện tại

        // Đồng bộ logic theo đúng kiểu text đã gán vào JComboBox
        String kieuThueHienTai = (String) cbKieuThue.getSelectedItem();
        dateTra.setEnabled("Theo ngày".equals(kieuThueHienTai));
        spinnerGioTra.setEnabled(!"Theo ngày".equals(kieuThueHienTai)); // Giờ trả chỉ khi thuê theo giờ

        DecimalFormat formatter = new DecimalFormat("#,### VNĐ");

        // Gắn listener để xử lý thay đổi kiểu thuê
        cbKieuThue.addActionListener(e -> {
            String kieu = (String) cbKieuThue.getSelectedItem();
            dateTra.setEnabled("Theo ngày".equals(kieu));
            spinnerGioTra.setEnabled(!"Theo ngày".equals(kieu));
            boolean isTheoNgay = "Theo ngày".equals(kieu);
         // Theo ngày: disable giờ, enable ngày trả
            spinnerGioNhan.setEnabled(!isTheoNgay);
            spinnerGioTra.setEnabled(false); // luôn mặc định 14:00
            dateTra.setEnabled(isTheoNgay);
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

        // Thêm các thành phần vào panel
        panelNgay.add(new JLabel("Ngày nhận:"));
        panelNgay.add(dateNhan);
        panelNgay.add(new JLabel("Giờ nhận:"));
        panelNgay.add(spinnerGioNhan);
        panelNgay.add(new JLabel("Ngày trả:"));
        panelNgay.add(dateTra);
        panelNgay.add(new JLabel("Giờ trả:"));
        panelNgay.add(spinnerGioTra);
        // Nút xác nhận
        JButton btnXacNhan = new JButton("Tạo phiếu đặt");
        btnXacNhan.addActionListener(e -> {
            String tenKhach = tfTenKhach.getText().trim();
            String sdt = tfSDT.getText().trim();
            String email = tfEmail.getText().trim();
            String kieuThue = (String) cbKieuThue.getSelectedItem();
            String cccd= tfCCCD.getText().trim();
            ngayNhan = ((JDateChooser) dateNhan).getDate();
            ngayTra = ((JDateChooser) dateTra).getDate();

            if (tenKhach.isEmpty() || sdt.isEmpty() || email.isEmpty() || ngayNhan == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, "Email không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (sdt.matches("^(03|09)\\d{8}$")) {
                // Số điện thoại hợp lệ
                System.out.println("Số điện thoại hợp lệ.");
            } else {
                // Số điện thoại không hợp lệ
                JOptionPane.showMessageDialog(this, "SĐT không hợp lệ! Phải có 10 chữ số và bắt đầu bằng 03 hoặc 09.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            if (cccd.matches("^\\d{12}$")) {
                // CCCD hợp lệ
                System.out.println("CCCD hợp lệ.");
            } else {
                // CCCD không hợp lệ
                JOptionPane.showMessageDialog(this, "CCCD không hợp lệ! Phải đúng 12 chữ số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
            try {
                // TODO: Ghi vào SQL bảng phieu_dat_phong hoặc in ra console thử
                dskh = daokh.getDatabase();
                String makh = daokh.taomaKH(dskh);
                KhachHang kh = new KhachHang(makh, tfTenKhach.getText(), tfCCCD.getText(), tfSDT.getText(), tfEmail.getText());
                daokh.themKhachHang(kh);

                dsphieudp = daophieudp.getDatabase();
                String mapdp = daophieudp.taomaPDP(dsphieudp);
                pdp = new PhieuDatPhong(mapdp, nhanVien, kh);

                boolean kieuthue = cbKieuThue.getSelectedItem().equals("Thuê ngày");
                LocalDateTime ngayNhan = getNgayNhan(spinnerGioNhan);
                LocalDateTime ngayTra = getNgayTra();

                String trangThai;
                if (ngayNhan.isBefore(LocalDateTime.now()) || ngayNhan.isEqual(LocalDateTime.now())) {
                    trangThai = "Đang sử dụng";
                } else {
                    trangThai = "Đã đặt";
                }

                ctpdp = new ChiTietPhieuDatPhong(pdp, phong, ngayNhan, ngayTra, kieuthue);
                boolean themPDP = daophieudp.themPhieuDatPhong(pdp);
                boolean themCTPDP = daoctphieudp.themCTPhieuDatPhong(ctpdp);

                System.out.println("Thêm PDP: " + themPDP);
                System.out.println("Thêm CT_PDP: " + themCTPDP);

                if (themPDP && themCTPDP) {
                	DaoPhong daophong = new DaoPhong();
                    daophong.capnhatttPhong(trangThai, phong.getMaPhong());
                    JOptionPane.showMessageDialog(this, "Đặt phòng thành công!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Đặt phòng thất bại!");
                }
       
            } catch (Exception e1) {
                // Hiển thị lỗi để dễ debug
                e1.printStackTrace(); // Log ra console
                JOptionPane.showMessageDialog(this, "Lỗi khi đặt phòng: " + e1.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

            
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
    private LocalDateTime getNgayNhan( JSpinner spinnerGioNhan) {
        String kieuThue = (String) cbKieuThue.getSelectedItem();
        Date ngay = ((JDateChooser) dateNhan).getDate();

        if (ngay == null) return null;

        if ("Theo ngày".equals(kieuThue)) {
            // Mặc định giờ 14:00
            return ngay.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .atTime(14, 0);
        } else {
            // Lấy thêm giờ từ spinner
            Date gio = (Date) spinnerGioNhan.getValue();

            // Gộp ngày + giờ
            LocalDate date = ngay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalTime time = gio.toInstant().atZone(ZoneId.systemDefault()).toLocalTime().withSecond(0).withNano(0);

            return LocalDateTime.of(date, time);
        }
    }

    private LocalDateTime getNgayTra() {
        String kieuThue = (String) cbKieuThue.getSelectedItem();
        if ("Theo ngày".equals(kieuThue)) {
            Date ngay = ((JDateChooser) dateTra).getDate();
            if (ngay == null) return null;

            // Mặc định giờ 14:00
            return ngay.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .atTime(14, 0);
        } else {
            // Thuê theo giờ thì không có ngày trả
            return null;
        }
    }


}