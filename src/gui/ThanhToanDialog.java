package gui;

import javax.swing.table.DefaultTableModel;

import dao.DaoCTPhieuDP;
import dao.DaoChiTietHoaDon;
import dao.DaoDichVu;
import dao.DaoHoaDon;
import dao.DaoKhachHang;
import dao.DaoKhuyenMai;
import dao.DaoPhong;
import entity.ChiTietHoaDon;
import entity.ChiTietPhieuDatPhong;
import entity.DichVu;
import entity.HoaDon;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.Phong;

import java.awt.*;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;

	public class ThanhToanDialog extends JDialog {
	    private JTable table;
	    private DefaultTableModel model;
	    private JComboBox<String> comboDichVu, comboKhuyenMai;
	    private JTextField tfSoLuong;
	    private JLabel lblTongTien;
	    private double tongTien = 0;

	    private DaoKhachHang daokh;
	    private DaoPhong daophong;
	    private DaoHoaDon daohd;
	    private DaoChiTietHoaDon daoCTHD ;
	    private DaoCTPhieuDP daoCTPDP ;
	    private ChiTietPhieuDatPhong ctpdp;
		private Object guiSDKS;
	    public ThanhToanDialog(JFrame parent, String maPhong, String sdt, String maHD) {
	        super(parent, "Thanh toán phòng", true);
	        daoCTHD= new DaoChiTietHoaDon();
	        setSize(600, 500);
	        setLocationRelativeTo(parent);
	        setLayout(new BorderLayout());

	        // Lấy dữ liệu
	        daokh = new DaoKhachHang();
	        daophong = new DaoPhong();
	        daohd = new DaoHoaDon();
	        KhachHang kh = daokh.getKhachHangtheoSDT(sdt);
	        Phong ph = daophong.getPhongtheoMa(maPhong);
	        HoaDon hd = daohd.getHoaDontheoma(maHD);
	        System.out.println(hd.getKhuyenmai().getTenKM());
	        // Panel thông tin
	        JPanel panelInfo = new JPanel(new GridLayout(2, 1, 5, 5));
	        panelInfo.setBorder(BorderFactory.createTitledBorder("Thông tin phòng & khách"));
	        panelInfo.add(new JLabel("Phòng: " + maPhong + " - " + ph.getTenPhong()));
	        panelInfo.add(new JLabel("Khách: " + kh.getTenKH() + " - " + kh.getSoDT()));
	        panelInfo.add(new JLabel("Mã hóa đơn: " + maHD));
	        // ===== Bảng dịch vụ =====
	        model = new DefaultTableModel(new Object[]{"Dịch vụ", "Đơn giá", "Số lượng", "Thành tiền"}, 0);
	        table = new JTable(model);
	        JScrollPane scrollPane = new JScrollPane(table);
	        scrollPane.setPreferredSize(new Dimension(550, 150));
	        table.getTableHeader().setBackground(new Color(180, 0, 0));
	        table.getTableHeader().setForeground(Color.WHITE);
	        // Load dịch vụ từ CTHD
	        List<ChiTietHoaDon> dsCTHD = daoCTHD.getCTHDByMaHD(maHD);
	        for (ChiTietHoaDon ct : dsCTHD) {
	        	// Bỏ qua nếu tên dịch vụ là "Không sử dụng"
	            if ("Không sử dụng".equalsIgnoreCase(ct.getDicvu().getTenDichVu())) {
	                continue;
	            }
	            double tien = ct.getsoLuong() * ct.getDicvu().getGiaTien();
	            model.addRow(new Object[]{
	                ct.getDicvu().getTenDichVu(),
	                ct.getDicvu().getGiaTien(),
	                ct.getsoLuong(),
	                tien
	            });
	            tongTien += tien;
	        }
	        
	     // ===== Panel thêm dịch vụ =====
	        JPanel panelThemDV = new JPanel(new FlowLayout(FlowLayout.LEFT));

	        // Combo loại dịch vụ và dịch vụ
	        JComboBox<String> comboLoaiDichVu = new JComboBox<>();
	        comboDichVu = new JComboBox<>();

	        // Số lượng và nút thêm
	        tfSoLuong = new JTextField(5);
	        JButton btnThem = new JButton("Thêm dịch vụ");

	        // Load loại dịch vụ từ DB
	        DaoDichVu daoDV = new DaoDichVu();
	        Map<String, List<DichVu>> mapLoaiToDichVu = daoDV.getDichVuTheoLoai(); // map: Loại -> List<DichVu>

	        // Thêm loại vào combo
	        for (String loai : mapLoaiToDichVu.keySet()) {
	            comboLoaiDichVu.addItem(loai);
	        }

	        // Khi chọn loại dịch vụ, load dịch vụ tương ứng
	        comboLoaiDichVu.addActionListener(e -> {
	            comboDichVu.removeAllItems();
	            String selectedLoai = (String) comboLoaiDichVu.getSelectedItem();
	            List<DichVu> list = mapLoaiToDichVu.get(selectedLoai);
	            if (list != null) {
	                for (DichVu dv : list) {
	                    comboDichVu.addItem(dv.getTenDichVu());
	                }
	            }
	        });

	        // Load dịch vụ của loại đầu tiên (mặc định)
	        if (comboLoaiDichVu.getItemCount() > 0) {
	            comboLoaiDichVu.setSelectedIndex(0);
	        }

	        // Thêm các thành phần vào panel
	        panelThemDV.add(new JLabel("Loại DV:"));
	        panelThemDV.add(comboLoaiDichVu);
	        panelThemDV.add(new JLabel("Dịch vụ:"));
	        panelThemDV.add(comboDichVu);
	        panelThemDV.add(new JLabel("Số lượng:"));
	        panelThemDV.add(tfSoLuong);
	        panelThemDV.add(btnThem);

	        // ===== Panel extra: xóa + khuyến mãi =====
	        JPanel panelExtra = new JPanel(new FlowLayout(FlowLayout.LEFT));
	        JButton btnXoa = new JButton("Xóa dịch vụ");
	        panelExtra.add(btnXoa);
	        panelExtra.add(new JLabel("Khuyến mãi:"));
	         
	        comboKhuyenMai = new JComboBox<>();
	        DaoKhuyenMai daoKM = new DaoKhuyenMai();  // hoặc dao bạn đang dùng
	        List<KhuyenMai> dsKhuyenMai = daoKM.getDatabase();
	        for (KhuyenMai km : dsKhuyenMai) {
	            comboKhuyenMai.addItem(km.getTenKM());  // toString sẽ được hiển thị
	        }
	        comboKhuyenMai.setSelectedIndex(0);
	        panelExtra.add(comboKhuyenMai);
	        // ===== Panel cuối: tổng tiền + nút =====
	        
	        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	        lblTongTien = new JLabel("Tổng tiền: " + formatCurrencyVND(tongTien));
	        lblTongTien.setFont(new Font("Arial", Font.BOLD, 16));
	        JButton btnCapNhat = new JButton("Cập nhật hóa đơn");
	        JButton btnThanhToan = new JButton("Xác nhận thanh toán");
	        panelBottom.add(lblTongTien);
	        panelBottom.add(btnCapNhat);
	        panelBottom.add(btnThanhToan);

	        // ======= Layout chính =======
	        JPanel mainPanel = new JPanel();
	        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
	        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	        panelInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
	        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
	        panelThemDV.setAlignmentX(Component.LEFT_ALIGNMENT);
	        panelExtra.setAlignmentX(Component.LEFT_ALIGNMENT);
	        panelBottom.setAlignmentX(Component.LEFT_ALIGNMENT);

	        mainPanel.add(panelInfo);
	        mainPanel.add(Box.createVerticalStrut(10));
	        mainPanel.add(scrollPane);
	        mainPanel.add(Box.createVerticalStrut(10));
	        mainPanel.add(panelThemDV);
	        mainPanel.add(Box.createVerticalStrut(10));
	        mainPanel.add(panelExtra);
	        mainPanel.add(Box.createVerticalStrut(10));
	        mainPanel.add(panelBottom);

	        add(mainPanel);
	        capNhatTongTien(hd);
	        btnThem.addActionListener(e -> {
	            try {
	                String tenDV = (String) comboDichVu.getSelectedItem();
	                DichVu selectedDV= daoDV.getDVTheoTen(tenDV);
	                if (selectedDV == null) {
	                    JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ.", "Thông báo", JOptionPane.WARNING_MESSAGE);
	                    return;
	                }

	                int soLuong = Integer.parseInt(tfSoLuong.getText().trim());
	                if (soLuong <= 0) throw new NumberFormatException();

	                double gia = selectedDV.getGiaTien();
	                double thanhTien = gia * soLuong;

	                // Thêm vào SQL
	                ChiTietHoaDon cthd = new ChiTietHoaDon(hd, ph,LocalDateTime.now(), false,"Rỗng",soLuong , selectedDV);
	                JOptionPane.showMessageDialog(this,"Cập nhật hóa đơn thành công");
	                boolean inserted = daoCTHD.themCTHoaDon(cthd);
	                if (!inserted) {
	                    JOptionPane.showMessageDialog(this, "Lỗi khi thêm vào CSDL!", "Lỗi", JOptionPane.ERROR_MESSAGE);
	                    return;
	                }

	                // Thêm vào bảng
	                model.addRow(new Object[]{selectedDV.getTenDichVu(), gia, soLuong, thanhTien});
	                tongTien += thanhTien;
	                capNhatTongTien(hd);
	                tfSoLuong.setText("");

	            } catch (NumberFormatException ex) {
	                JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng số lượng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
	            }
	        });

	        btnThanhToan.addActionListener(e -> {
	        	DaoKhuyenMai daokm= new DaoKhuyenMai();
	        	String tenKM= comboKhuyenMai.getSelectedItem().toString();
	        	KhuyenMai km= daokm.getKhuyenMaitheoTen(tenKM);
	        	daohd.capnhatKMvaoHD(km, hd);
	            // Bước 1: Hỏi phương thức thanh toán
	            String[] options = {"Tiền mặt", "Chuyển khoản", "Momo", "ZaloPay"};
	            String ppThanhToan = (String) JOptionPane.showInputDialog(
	                this,
	                "Chọn phương thức thanh toán:",
	                "Phương thức thanh toán",
	                JOptionPane.PLAIN_MESSAGE,
	                null,
	                options,
	                options[0]
	            );

	            if (ppThanhToan == null) {
	                return; // Người dùng bấm Cancel
	            }

	            // Bước 2: Cập nhật trạng thái phòng và CTHD
	            daoCTPDP= new DaoCTPhieuDP();
	            ctpdp=daoCTPDP.getCTPDPtheoMaPhong(ph.getMaPhong());
	            boolean success1 = daoCTHD.capNhatTrangThaiVaPPThanhToan(hd.getMaHD(), ppThanhToan);
	            boolean success2 = daophong.capnhatttPhong("Trống", ctpdp.getPhong().getMaPhong());
	            daoCTHD= new DaoChiTietHoaDon();
	            List<ChiTietHoaDon> dsdv=daoCTHD.getCTHDByMaHD(maHD);
	            
	            if (success1 && success2) {
	                // Bước 3: Xuất file thanh toán
	                try {
	                    String tenFile = "HoaDon_" + hd.getMaHD() + ".txt";
	                    File file = new File("hoadon/" + tenFile);
	                    file.getParentFile().mkdirs();

	                    try (PrintWriter writer = new PrintWriter(file)) {
	                    	writer.println("======== HOÁ ĐƠN THANH TOÁN ========");
	                    	writer.println("Mã hoá đơn: " + hd.getMaHD());
	                    	writer.println("Tên khách hàng: " + hd.getKhachHang().getTenKH());
	                    	writer.println("SĐT: " + hd.getKhachHang().getSoDT());
	                    	writer.println("Phòng: " + ctpdp.getPhong().getTenPhong());
	                    	writer.println("Ngày đặt: " + ctpdp.getGioDatPhong());
	                    	writer.println("Ngày trả: " + ctpdp.getGioTraPhong());
	                    	writer.println("Phương thức thanh toán: " + ppThanhToan);
	                    	writer.println("------------------------------------");
	                    	writer.println("DỊCH VỤ ĐÃ SỬ DỤNG:");
	                    	writer.printf("%-25s %-10s %-15s %-15s\n", "Tên dịch vụ", "Số lượng", "Đơn giá", "Thành tiền");
	                    	for (ChiTietHoaDon cthd : dsdv) {
	                    	    String tenDV = cthd.getDicvu().getTenDichVu();
	                    	    int soLuong = cthd.getsoLuong();
	                    	    double donGia = cthd.getDicvu().getGiaTien();
	                    	    double thanhTien = soLuong * donGia;

	                    	    writer.printf("%-25s %-10d %-15s %-15s\n",
	                    	        tenDV,
	                    	        soLuong,
	                    	        formatCurrencyVND(donGia),
	                    	        formatCurrencyVND(thanhTien)
	                    	    );
	                    	}
	                    	double tongTienDichVu =daoCTHD.tinhTongtiendv(hd.getMaHD());
	                    	double tongtiensaukm= daoCTHD.tinhTongtienPhongvaDichVu(hd.getMaHD());
	                    	writer.println("------------------------------------");
	                    	writer.println("TỔNG TIỀN DỊCH VỤ: " + formatCurrencyVND(tongTienDichVu));
	                    	writer.println("TỔNG TIỀN : " + lblTongTien.getText());
	                    	writer.println("TỔNG TIỀN SAU KHI ÁP DỤNG KHUYẾN MÃI : " +formatCurrencyVND(tongtiensaukm) );
	                    	writer.println("Ngày thanh toán: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
	                    	writer.println("====================================");
	                    }

	                    JOptionPane.showMessageDialog(this, "Thanh toán thành công!\nFile: " + tenFile, "Thành công", JOptionPane.INFORMATION_MESSAGE);
	                    dispose(); // Đóng dialog

	                } catch (IOException ex) {
	                    ex.printStackTrace();
	                    JOptionPane.showMessageDialog(this, "Lỗi khi xuất file hoá đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
	                }
	            } else {
	                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi cập nhật trạng thái!", "Lỗi", JOptionPane.ERROR_MESSAGE);
	            }
	        });
	    }

	    private void capNhatTongTien(HoaDon hd) {   	
	        double tienGoc=daoCTHD.tinhTongTien(hd.getMaHD());
	        for (int i = 0; i < model.getRowCount(); i++) {
	            tienGoc += (double) model.getValueAt(i, 3);
	        }
	        DaoKhuyenMai daokm= new DaoKhuyenMai();
	        String selectedKM =  comboKhuyenMai.getSelectedItem().toString();
	        KhuyenMai km= daokm.getKhuyenMaitheoTen(selectedKM);
	        double tiLeGiam = 1.0;

	        if (selectedKM != null) {
	            tiLeGiam -= km.getPhanTramKM();  // Ví dụ: 0.1 nếu giảm 10%
	        }

	        tongTien = tienGoc * tiLeGiam;
	        lblTongTien.setText("Tổng tiền: " + formatCurrencyVND(tongTien));
	    }

	    private String formatCurrencyVND(double amount) {
		    NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
		    formatter.setMaximumFractionDigits(0); // Không hiển thị phần thập phân
		    formatter.setRoundingMode(RoundingMode.HALF_UP); // Làm tròn lên hoặc xuống
		    return formatter.format(amount) + " VND";
		}
	}
