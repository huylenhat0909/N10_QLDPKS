package gui;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    public gui_SDKS() {
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

        // Thêm topPanel vào khu vực NORTH
        add(topPanel, BorderLayout.NORTH);

        // ========== PHẦN CHÍNH (CENTER) ==========
        // Tạo lưới với số dòng là TANG và số cột là (PHONG_MOI_TANG + 1) 
        // (cột đầu tiên hiển thị tầng, các cột còn lại là các phòng)
        JPanel gridPanel = new JPanel(new GridLayout(TANG, PHONG_MOI_TANG + 1, 5, 5));
        phongButtons = new JButton[TANG][PHONG_MOI_TANG];

        for (int i = 0; i < TANG; i++) {
            // Cột đầu tiên: hiển thị tầng
            JLabel tangLabel = new JLabel("Tầng " + (i + 1), SwingConstants.CENTER);
            tangLabel.setFont(font);
            tangLabel.setOpaque(true);
            tangLabel.setBackground(Color.LIGHT_GRAY);
            tangLabel.setPreferredSize(new Dimension(50, 100));
            gridPanel.add(tangLabel);

            // Các cột tiếp theo: các phòng
            for (int j = 0; j < PHONG_MOI_TANG; j++) {
                // Hiển thị tên phòng theo định dạng "Phòng X"
                String roomName = "Phòng " + (j + 1);// LẤY THÔNG TIN TÊN PHÒNG Ở ĐÂY
                JButton btn = new JButton(roomName);
                btn.setFont(font);
                btn.putClientProperty("tang", i + 1);
                btn.putClientProperty("phong", j + 1);
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
        loadTrangThaiTheoNgay();
        capNhatCountLabel();
    }

    /**
     * Đọc trạng thái phòng theo ngày được chọn, tô màu cho button.
     */
    private void loadTrangThaiTheoNgay() {
        Date date = dateChooser.getDate();
        if (date == null) return;
        LocalDate selectedDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        for (int i = 0; i < TANG; i++) {
            for (int j = 0; j < PHONG_MOI_TANG; j++) {
                String key = selectedDate + "-T" + (i + 1) + "P" + (j + 1);
                String state = phongTheoNgay.getOrDefault(key, trangThai[0]); // mặc định "Trống"
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
        int soPhongTrong = 0;
        int soPhongDat = 0;
        int soPhongSuDung = 0;

        for (int i = 0; i < TANG; i++) {
            for (int j = 0; j < PHONG_MOI_TANG; j++) {
                String state = (String) phongButtons[i][j].getClientProperty("trangThai");
                if ("Trống".equals(state)) soPhongTrong++;
                else if ("Đã đặt".equals(state)) soPhongDat++;
                else if ("Đang sử dụng".equals(state)) soPhongSuDung++;
            }
        }

        countLabel.setText("Trống: " + soPhongTrong
                + ", Đã đặt: " + soPhongDat
                + ", Đang sử dụng: " + soPhongSuDung);
    }

    /**
     * Trả về màu sắc theo trạng thái phòng.
     */
    private Color getColorByState(String state) {
        switch (state) {
            case "Đang sử dụng":
                return Color.RED;
            case "Đã đặt":
                return Color.ORANGE;
            default:
                return Color.GREEN;
        }
    }

    /**
     * Lắng nghe sự kiện click vào phòng, cho phép đổi trạng thái.
     */
    private class PhongButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            int tang = (int) clickedButton.getClientProperty("tang");
            int phong = (int) clickedButton.getClientProperty("phong");
            String trangThaiHienTai = (String) clickedButton.getClientProperty("trangThai");

            // Tạo panel cho hộp thoại
            JPanel panel = new JPanel(new GridLayout(3, 1));
            panel.add(new JLabel("Tầng: " + tang));
            panel.add(new JLabel("Phòng: " + phong));
            JComboBox<String> trangThaiBox = new JComboBox<>(trangThai);
            trangThaiBox.setSelectedItem(trangThaiHienTai);
            panel.add(trangThaiBox);

            int result = JOptionPane.showConfirmDialog(null, panel, "Thông tin phòng", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String trangThaiMoi = (String) trangThaiBox.getSelectedItem();
                Date date = dateChooser.getDate();
                if (date == null) return;
                LocalDate selectedDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String key = selectedDate + "-T" + tang + "P" + phong;

                // Cập nhật trạng thái
                phongTheoNgay.put(key, trangThaiMoi);
                clickedButton.setBackground(getColorByState(trangThaiMoi));
                clickedButton.putClientProperty("trangThai", trangThaiMoi);

                // Cập nhật label thống kê
                capNhatCountLabel();
            }
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
}
