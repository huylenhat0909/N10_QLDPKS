package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import dao.DaoChiTietHoaDon;
import dao.DaoHoaDon;
import dao.DaoKhachHang;
import dao.DaoPhieuDP;
import dao.DaoPhong;
import entity.ChiTietHoaDon;
import entity.ChiTietPhieuDatPhong;
import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;
import entity.PhieuDatPhong;
import entity.Phong;

public class NhanPhongDialog extends JDialog {
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
	private JOptionPane spinnerGioNhan;
	private DaoPhong daophong;
	private ChiTietHoaDon cthd;
	private HoaDon hd;
	private gui_SDKS guisdks;
	private KhachHang kh;

	public NhanPhongDialog(JFrame parent, Phong phong, NhanVien nhanVien,LocalDate date ) {
		
		super(parent, "Xác nhận nhận phòng", true);
        setSize(500, 620);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        daophieudp=new DaoPhieuDP();
        daoctphieudp=new DaoCTPhieuDP();
        daokh= new DaoKhachHang();
        guisdks= new gui_SDKS(nhanVien);
        ChiTietPhieuDatPhong ctpdp2= daoctphieudp.getCTPDPtheoMaPhongDay(phong.getMaPhong(), date);
        //Panle hiển thi thông tin nhân viên phụ trách đặt phòng
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
        lblGiaPhong = new JLabel(ctpdp2.getGiaPhongtheoKieuThue().toString());  // Sẽ cập nhật sau
        panelPhong.add(lblGiaPhong);
        // Panel nhập thông tin khách hàng
        JPanel panelKhach = new JPanel(new GridLayout(5, 2, 5, 5));
        panelKhach.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));
        tfTenKhach = new JTextField(ctpdp2.getPhietDP().getKhachhang().getTenKH());
        tfSDT = new JTextField(ctpdp2.getPhietDP().getKhachhang().getSoDT());
        tfEmail = new JTextField(ctpdp2.getPhietDP().getKhachhang().getEmail());
        tfCCCD= new JTextField(ctpdp2.getPhietDP().getKhachhang().getCCCD());
        cbKieuThue = new JComboBox<>(new String[]{"Chọn kiểu đặt","Theo giờ", "Theo ngày"});
        int index;
        if(ctpdp2.getKieuThue()) {
        	index=2;
        }else {
        	index=1;
        }
        cbKieuThue.setSelectedIndex(index);
        panelKhach.add(new JLabel("Tên khách:"));
        panelKhach.add(tfTenKhach);
     // Tạo panel riêng cho dòng "SĐT + nút Tìm"
        JPanel sdtPanel = new JPanel(new BorderLayout());
        JButton btnTimKiem = new JButton("Tìm");
        sdtPanel.add(tfSDT, BorderLayout.CENTER);
        sdtPanel.add(btnTimKiem, BorderLayout.EAST);

        // Thêm vào panelKhach
        panelKhach.add(new JLabel("SĐT:"));
        panelKhach.add(sdtPanel);
        panelKhach.add(new JLabel("Email:"));
        panelKhach.add(tfEmail);
        panelKhach.add(new JLabel("So CCCD:"));
        panelKhach.add(tfCCCD);
        panelKhach.add(new JLabel("Kiểu thuê:"));
        panelKhach.add(cbKieuThue);
        
        // Panel chọn ngày đặt
     // Panel chọn ngày đặt
        LocalDateTime ngaygioNhan= ctpdp2.getGioDatPhong();
        LocalDateTime ngaygioTra= ctpdp2.getGioTraPhong();
     // Tách phần ngày
        LocalDate ngayNhan = ngaygioNhan.toLocalDate();
        LocalDate ngayTra = ngaygioTra.toLocalDate();

        // Tách phần giờ
        LocalTime gioNhan = ngaygioNhan.toLocalTime();
        LocalTime gioTra = ngaygioTra.toLocalTime();
     // Chuyển LocalDate -> java.util.Date
        Date utilNgayNhan = java.sql.Date.valueOf(ngayNhan);
        Date utilNgayTra = java.sql.Date.valueOf(ngayTra);

        // Chuyển LocalTime -> java.util.Date
        Date utilGioNhan = java.sql.Time.valueOf(gioNhan);
        Date utilGioTra = java.sql.Time.valueOf(gioTra);
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
        ((JDateChooser) dateNhan).setDate(utilNgayNhan);
        ((JDateChooser) dateTra).setDate(utilNgayTra);

        spinnerGioNhan.setValue(utilGioNhan);
        spinnerGioTra.setValue(utilGioTra);
		/*
		 * // Đồng bộ logic theo đúng kiểu text đã gán vào JComboBox String
		 * kieuThueHienTai = (String) cbKieuThue.getSelectedItem();
		 * dateTra.setEnabled("Theo ngày".equals(kieuThueHienTai));
		 * spinnerGioTra.setEnabled(!"Theo ngày".equals(kieuThueHienTai)); // Giờ trả
		 * chỉ khi thuê theo giờ
		 */
		/*
		 * DecimalFormat formatter = new DecimalFormat("#,### VNĐ");
		 * 
		 * // Gắn listener để xử lý thay đổi kiểu thuê cbKieuThue.addActionListener(e ->
		 * { String kieu = (String) cbKieuThue.getSelectedItem();
		 * dateTra.setEnabled("Theo ngày".equals(kieu));
		 * spinnerGioTra.setEnabled(!"Theo ngày".equals(kieu)); boolean isTheoNgay =
		 * "Theo ngày".equals(kieu); // Theo ngày: disable giờ, enable ngày trả
		 * spinnerGioNhan.setEnabled(!isTheoNgay); spinnerGioTra.setEnabled(false); //
		 * luôn mặc định 14:00 dateTra.setEnabled(isTheoNgay); if
		 * ("Theo ngày".equals(kieu)) { lblGiaPhong.setText(formatter.format(
		 * phong.getGiaPhong() * phong.getLoaiPhong().getGiaPhongtheongay() ) +
		 * " VNĐ/ngày"); } else { lblGiaPhong.setText(formatter.format(
		 * phong.getGiaPhong() * phong.getLoaiPhong().getGiaPhongtheogio() ) +
		 * " VNĐ/giờ"); } });
		 */

        // Thêm các thành phần vào panel
        panelNgay.add(new JLabel("Ngày nhận:"));
        panelNgay.add(dateNhan);
        panelNgay.add(new JLabel("Giờ nhận:"));
        panelNgay.add(spinnerGioNhan);
        panelNgay.add(new JLabel("Ngày trả:"));
        panelNgay.add(dateTra);
        panelNgay.add(new JLabel("Giờ trả:"));
        panelNgay.add(spinnerGioTra);
        // Nút xác nhận nhận phòng
        JButton btnXacNhan = new JButton("Xác nhận nhận phòng");
        btnXacNhan.addActionListener(e -> {
            String tenKhach = tfTenKhach.getText().trim();
            String sdt = tfSDT.getText().trim();
            String email = tfEmail.getText().trim();
            String kieuThue = (String) cbKieuThue.getSelectedItem();
            String cccd= tfCCCD.getText().trim();
            Date ngayNhan1 = ((JDateChooser) dateNhan).getDate();
            Date ngayTra1 = ((JDateChooser) dateTra).getDate();       
            try {
                	DaoHoaDon daohd= new DaoHoaDon();
                	DaoChiTietHoaDon daocthd= new DaoChiTietHoaDon();
                	List<HoaDon> dshd = daohd.getDatabase();
                	String mahd = daohd.taomaHD(dshd);
                	hd=new HoaDon(mahd, nhanVien, null, ctpdp2.getPhietDP().getKhachhang());
                	cthd= new ChiTietHoaDon(hd, phong,ctpdp2.getGioDatPhong(),false,"Rỗng",0, null);
                	boolean themhd = daohd.themHoaDon(hd);
                    boolean themCThd = daocthd.themCTHoaDon(cthd);

                    if (themhd && themCThd) {
                    	DaoPhong daophong = new DaoPhong();
                        daophong.capnhatttPhong("Đang sử dụng", phong.getMaPhong());
                        JOptionPane.showMessageDialog(this, "Nhận phòng thành công!");
                        guisdks.capNhatTrangThaiPhong();
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Nhận phòng thất bại!");
                    }
                }     
            	catch (Exception e1) {
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

    private LocalDateTime getNgayTra(JSpinner spinnerGioNhan) {
        String kieuThue = (String) cbKieuThue.getSelectedItem();
        
        if ("Theo ngày".equals(kieuThue)) {
            Date ngay = ((JDateChooser) dateTra).getDate();
            if (ngay == null) return null;

            // Mặc định giờ trả là 14:00 nếu thuê theo ngày
            return ngay.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .atTime(14, 0);
        } else {
            // Nếu thuê theo giờ thì trả về chính giờ nhận
        	Date gioNhan = (Date) spinnerGioNhan.getValue();
            if (gioNhan == null) return null;

            return gioNhan.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        }
    }
    

}