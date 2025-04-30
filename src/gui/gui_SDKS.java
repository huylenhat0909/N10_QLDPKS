package gui;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;

import dao.DaoCTPhieuDP;
import dao.DaoChiTietHoaDon;
import dao.DaoPhong;
import entity.ChiTietHoaDon;
import entity.ChiTietPhieuDatPhong;
import entity.NhanVien;
import entity.Phong;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class gui_SDKS extends JPanel {
    private static final int TANG = 5;           // Số tầng
    private static final int PHONG_MOI_TANG = 7;   // Số phòng mỗi tầng

    private JButton[][] phongButtons;
    private String[] trangThai = {"Trống", "Đang sử dụng", "Đã đặt"};
    private JDateChooser dateChooser;
    private Map<String, String> phongTheoNgay = new HashMap<>();
    private Font font = new Font("Arial", Font.BOLD, 16);

    // Label để hiển thị đếm số phòng theo trạng thái
    private JLabel countLabel;
	private DaoPhong daophong;
	private List<Phong> dsphong;
	private NhanVien nv;
	private DaoChiTietHoaDon daocthd;
    public gui_SDKS(NhanVien ttnv) {
    	nv=ttnv;
        // Sử dụng BorderLayout để giãn full không gian
        setLayout(new BorderLayout());
        

        
        // ========== PHẦN TRÊN (NORTH) ==========
        JPanel topPanel = new JPanel(new BorderLayout());

        // Panel thống kê (bên trái)
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        countLabel = new JLabel("Trống: 0, Đã đặt: 0, Đang sử dụng: 0");
        countLabel.setFont(font);
        statsPanel.add(countLabel);
        topPanel.add(statsPanel, BorderLayout.WEST);

        // Panel chú thích màu sắc (ở giữa)
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        legendPanel.add(createLegendItem("Trống", Color.GREEN));
        legendPanel.add(createLegendItem("Đã đặt", Color.ORANGE));
        legendPanel.add(createLegendItem("Đang sử dụng", Color.RED));
        topPanel.add(legendPanel, BorderLayout.CENTER);

        // Panel chọn ngày (bên phải)
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel chongay=new JLabel("Chọn ngày: ");
        chongay.setFont(font);
        datePanel.add(chongay);
        dateChooser = new JDateChooser();
        dateChooser.setDate(new Date());
        dateChooser.setDateFormatString("yyyy-MM-dd");
        dateChooser.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("date".equals(evt.getPropertyName())) {
                    loadTrangThaiTheoNgay();
                    capNhatCountLabel();
                }
            }
        });
        
        datePanel.add(dateChooser);
        topPanel.add(datePanel, BorderLayout.EAST);
     // Tạo nút Load lại sơ đồ
        JButton btnReload = new JButton("Load lại sơ đồ");
        btnReload.setFont(font);
        btnReload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadTrangThaiTheoNgay(); // Gọi lại hàm cập nhật sơ đồ phòng
                capNhatCountLabel();     // Cập nhật lại thống kê
                revalidate();            // Cập nhật lại layout nếu có thay đổi
                repaint();               // Vẽ lại giao diện
            }
        });

        // Thêm nút vào bên phải
        datePanel.add(btnReload);
        // Thêm topPanel vào khu vực NORTH
        add(topPanel, BorderLayout.NORTH);

        // ========== PHẦN CHÍNH (CENTER) ==========
        // Tạo lưới với số dòng là TANG và số cột là (PHONG_MOI_TANG + 1) 
        // (cột đầu tiên hiển thị tầng, các cột còn lại là các phòng)
        daophong= new DaoPhong();
        dsphong=daophong.getDatabase();
        JPanel gridPanel = new JPanel(new GridLayout(TANG, PHONG_MOI_TANG + 1, 5, 5));
        phongButtons = new JButton[TANG][PHONG_MOI_TANG];

        for (int i = 0; i < TANG; i++) {
            int tangHienTai = i + 1;
            JLabel tangLabel = new JLabel("Tầng " + tangHienTai, SwingConstants.CENTER);
            tangLabel.setFont(font);
            tangLabel.setOpaque(true);
            tangLabel.setBackground(Color.LIGHT_GRAY);
            tangLabel.setPreferredSize(new Dimension(50, 100));
            gridPanel.add(tangLabel);

            // Lọc danh sách phòng thuộc tầng hiện tại
            List<Phong> phongTangHienTai = dsphong.stream()
                .filter(p -> p.getTang() == tangHienTai)
                .collect(Collectors.toList());

            for (int j = 0; j < PHONG_MOI_TANG; j++) {
                JButton btn;

                if (j < phongTangHienTai.size()) {
                    Phong phong = phongTangHienTai.get(j);
                    btn = new JButton(phong.getTenPhong());
                    btn.putClientProperty("maPhong", phong.getMaPhong());
                    btn.putClientProperty("tenPhong", phong.getTenPhong());
                    btn.putClientProperty("tang", phong.getTang());
                    btn.putClientProperty("trangThai", phong.getTrangThai());
                    // Thêm gì cần nữa từ phong
                } else {
                    // Nếu không có phòng đủ số lượng, tạo nút trống hoặc disabled
                    btn = new JButton("Trống");
                    btn.setEnabled(false); // hoặc vẫn bật nếu muốn
                }

                btn.setFont(font);
                btn.putClientProperty("tang", tangHienTai);
                btn.addActionListener(new PhongButtonListener());
                phongButtons[i][j] = btn;
                btn.setPreferredSize(new Dimension(150, 100));
                gridPanel.add(btn);
            }
        }

        // Đặt gridPanel vào JScrollPane để có thanh cuộn nếu cần
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        // Tải dữ liệu ban đầu
        capNhatTrangThaiPhong();
    }

    /**
     * Đọc trạng thái phòng theo ngày được chọn, tô màu cho button.
     */
    private void loadTrangThaiTheoNgay() {
        Date date = dateChooser.getDate();
        if (date == null) return;

        LocalDate selectedDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        DaoPhong daoPhong = new DaoPhong();

        // Lấy trạng thái phòng theo ngày từ database
        Map<String, String> trangThaiPhongMap = daoPhong.getTrangThaiPhongTheoNgay(selectedDate);

        for (int i = 0; i < TANG; i++) {
            for (int j = 0; j < PHONG_MOI_TANG; j++) {
                int soPhong = i * PHONG_MOI_TANG + j + 1;
                String maPhong = String.format("MP%03d", soPhong);

                String state = trangThaiPhongMap.getOrDefault(maPhong, "Trống");

                JButton btn = phongButtons[i][j];
                btn.setBackground(getColorByState(state));
                btn.putClientProperty("trangThai", state);
            }
        }
    }

    /**
     * Cập nhật label thống kê (Trống / Đã đặt / Đang sử dụng).
     */
    private void capNhatCountLabel() {
    	DaoPhong daoPhong = new DaoPhong();
        List<Phong> danhSachPhong = daoPhong.getDatabase(); // hoặc getAllPhong()
        DaoChiTietHoaDon daocthd= new DaoChiTietHoaDon();
        DaoCTPhieuDP daoctpdp= new DaoCTPhieuDP();
        int soPhongTrong =danhSachPhong.size()-daocthd.sluonghdchuathanhtoan();
        int soPhongDat = daoctpdp.sluongctpdpchuacocthd();
        int soPhongSuDung = daocthd.sluonghdchuathanhtoan();

        countLabel.setText("Trống: " + soPhongTrong
                + ", Đã đặt: " + soPhongDat
                + ", Đang sử dụng: " + soPhongSuDung);
    }
    /**
     * Trả về màu sắc theo trạng thái phòng.
     */
    private Color getColorByState(String state) {
        return switch (state) {
            case "Đang sử dụng" -> Color.RED;
            case "Đã đặt" -> Color.ORANGE;
            case "Trống" -> Color.GREEN;
            default -> Color.LIGHT_GRAY;
        };
    }

    /**
     * Lắng nghe sự kiện click vào phòng, cho phép đổi trạng thái.
     */
    private class PhongButtonListener implements ActionListener {
        private Phong phong;
        DaoCTPhieuDP daoctpd= new DaoCTPhieuDP();
		@Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
			LocalDateTime giohientai = LocalDateTime.now();
			Date date = dateChooser.getDate();
			//Set ngày giờ trên datachoose
			LocalDateTime giotrendatechooser = date.toInstant()
			        .atZone(ZoneId.systemDefault())
			        .toLocalDate()
			        .atTime(LocalTime.now()); 
			
            String maPhong = (String) clickedButton.getClientProperty("maPhong");
            String tenPhong = (String) clickedButton.getClientProperty("tenPhong");
            phong=daophong.getPhongtheoTen(tenPhong);
            daocthd= new DaoChiTietHoaDon();
            ChiTietHoaDon cthd = daocthd.getCTHDtheophongtt(maPhong,false);
            ChiTietPhieuDatPhong ctpdp= daoctpd.getCTPDPtheoMaPhongDay(maPhong, giohientai.toLocalDate());
            boolean coCTHD = (cthd != null && cthd.getHD() != null && cthd.getNgayLapHD() != null);
            boolean coCTPDP = (ctpdp != null && ctpdp.getGioDatPhong() != null && ctpdp.getGioTraPhong() != null);
			/*
			 * if ("Đang sử dụng".equals(phong.getTrangThai())) { ThanhToanDialog dialog =
			 * new
			 * ThanhToanDialog(null,maPhong,cthd.getHD().getKhachHang().getSoDT(),cthd.getHD
			 * ().getMaHD()); dialog.setVisible(true); capNhatTrangThaiPhong(); } if
			 * ("Trống".equals(phong.getTrangThai())) { DatPhongDialog dialog = new
			 * DatPhongDialog(null, phong,nv ); dialog.setVisible(true);
			 * capNhatTrangThaiPhong(); } if ("Đã đặt".equals(phong.getTrangThai())) { Date
			 * date1 = dateChooser.getDate(); LocalDate selectedDate =
			 * date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			 * NhanPhongDialog dialog = new NhanPhongDialog(null, phong,nv, selectedDate );
			 * dialog.setVisible(true); capNhatTrangThaiPhong(); }
			 */
            if (coCTHD) {
            	LocalDateTime gioTruaNgayHomSau = cthd.getNgayLapHD().toLocalDate().plusDays(1).atTime(14, 0);
                LocalDateTime thoiGianLap = cthd.getNgayLapHD();
                if ((giohientai.isEqual(thoiGianLap) || giohientai.isAfter(thoiGianLap))&& giohientai.isBefore(gioTruaNgayHomSau)) {
                    // Hiện đang sử dụng → Thanh toán
                    ThanhToanDialog dialog = new ThanhToanDialog(null, maPhong,
                            cthd.getHD().getKhachHang().getSoDT(),
                            cthd.getHD().getMaHD());
                    dialog.setVisible(true);
                    capNhatTrangThaiPhong();
                    return;
                }else {
                	DatPhongDialog dialog = new DatPhongDialog(null, phong, nv,giotrendatechooser );
                    dialog.setVisible(true);
                    capNhatTrangThaiPhong();
                    return;
                }
                
            }

            if (!coCTHD && coCTPDP) {
                LocalDateTime ngayNhan = ctpdp.getGioDatPhong();
                LocalDateTime ngayTra = ctpdp.getGioTraPhong();
                if ((giotrendatechooser.isEqual(ngayNhan) || giotrendatechooser.isAfter(ngayNhan)) &&
                		giotrendatechooser.isBefore(ngayTra)) {
                    // Trong khoảng đặt phòng → Nhận phòng
                	Date date1 = dateChooser.getDate();
                	LocalDate selectedDate = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    NhanPhongDialog dialog = new NhanPhongDialog(null, phong, nv,selectedDate);
                    dialog.setVisible(true);
                    capNhatTrangThaiPhong();
                    return;
                }
            }
            
         // Không có hóa đơn và không có phiếu đặt → cho phép đặt mới
            DatPhongDialog dialog = new DatPhongDialog(null, phong, nv,giotrendatechooser);
            dialog.setVisible(true);
            capNhatTrangThaiPhong();
            System.out.println(coCTPDP);
    }
    }
    /**
     * Tạo một ô chú thích (legend) với màu và nhãn tương ứng.
     */
    private JPanel createLegendItem(String label, Color color) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton colorBox = new JButton();
        colorBox.setEnabled(false);
        colorBox.setBackground(color);
        colorBox.setPreferredSize(new Dimension(40, 40));
        panel.add(colorBox);

        JLabel lbl = new JLabel(label);
        lbl.setFont(font);
        panel.add(lbl);

        return panel;
    }
    public void capNhatTrangThaiPhong() {
    	loadTrangThaiTheoNgay();
        capNhatCountLabel();
        
    }
}
