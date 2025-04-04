package gui;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class gui_SDKS extends JPanel {
    private static final int TANG = 5;  // Số tầng
    private static final int PHONG_MOI_TANG = 10; // Số phòng mỗi tầng
    private JButton[][] phongButtons;
    private String[] trangThai = {"Trống", "Đang sử dụng", "Đã đặt"};
    private JDateChooser dateChooser;
    private Map<String, String> phongTheoNgay = new HashMap<>();
    // Tạo đối tượng Font để sử dụng cho các thành phần
    private Font font = new Font("Arial", Font.BOLD, 15); // Chỉnh kích thước chữ
    public gui_SDKS() {
        // Sử dụng BoxLayout để sắp xếp nội dung
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS)); // Sử dụng BoxLayout cho headerPanel

        // Panel thống kê trạng thái
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel countLabel = new JLabel("Trống: 0, Đã đặt: 0, Đang sử dụng: 0");
        countLabel.setFont(font);
        statsPanel.add(countLabel);

        // Panel top chứa chọn ngày và thống kê
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.add(statsPanel);

        // Panel chú thích màu sắc
        JPanel legendPanel = new JPanel();
        legendPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        legendPanel.add(createLegendItem("Trống", Color.GREEN));
        legendPanel.add(createLegendItem("Đã đặt", Color.ORANGE));
        legendPanel.add(createLegendItem("Đang sử dụng", Color.RED));
        headerPanel.add(topPanel);
        headerPanel.add(legendPanel);

        this.add(headerPanel); // Thêm headerPanel vào GUI

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS)); // Sử dụng BoxLayout cho mainPanel

        mainPanel.add(new JLabel("Chọn ngày: "));

        dateChooser = new JDateChooser();
        dateChooser.setDate(new Date());
        dateChooser.setDateFormatString("yyyy-MM-dd");
        dateChooser.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("date".equals(evt.getPropertyName())) {
                    loadTrangThaiTheoNgay();
                }
            }
        });
        mainPanel.add(dateChooser);

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(TANG + 1, PHONG_MOI_TANG + 1, 5, 5)); // khoảng cách giữa các ô
        phongButtons = new JButton[TANG][PHONG_MOI_TANG];

        // Tạo header cột số phòng
        gridPanel.add(new JLabel("Tầng/Phòng", SwingConstants.CENTER));
        for (int p = 1; p <= PHONG_MOI_TANG; p++) {
            gridPanel.add(new JLabel("Phòng " + p, SwingConstants.CENTER));
        }

        // Tạo các tầng và phòng
        for (int i = 0; i < TANG; i++) {
            gridPanel.add(new JLabel("Tầng " + (i + 1), SwingConstants.CENTER));
            for (int j = 0; j < PHONG_MOI_TANG; j++) {
                JButton btn = new JButton();
                btn.putClientProperty("tang", i + 1);
                btn.putClientProperty("phong", j + 1);
                btn.addActionListener(new PhongButtonListener());
                phongButtons[i][j] = btn;
                gridPanel.add(btn);
            }
        }
        this.add(mainPanel); // Thêm mainPanel vào GUI
        this.add(gridPanel); // Thêm gridPanel vào mainPanel
       

        loadTrangThaiTheoNgay();
    }

    private void loadTrangThaiTheoNgay() {
        Date date = dateChooser.getDate();
        if (date == null) return;
        LocalDate selectedDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        for (int i = 0; i < TANG; i++) {
            for (int j = 0; j < PHONG_MOI_TANG; j++) {
                String key = selectedDate + "-T" + (i + 1) + "P" + (j + 1);
                String state = phongTheoNgay.getOrDefault(key, trangThai[0]);
                JButton btn = phongButtons[i][j];
                btn.setBackground(getColorByState(state));
                btn.putClientProperty("trangThai", state);
            }
        }
    }

    private Color getColorByState(String state) {
        switch (state) {
            case "Đang sử dụng": return Color.RED;
            case "Đã đặt": return Color.ORANGE;
            default: return Color.GREEN;
        }
    }

    private class PhongButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            int tang = (int) clickedButton.getClientProperty("tang");
            int phong = (int) clickedButton.getClientProperty("phong");
            String trangThaiHienTai = (String) clickedButton.getClientProperty("trangThai");

            // Hiện cửa sổ thông tin phòng
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
                phongTheoNgay.put(key, trangThaiMoi);
                clickedButton.setBackground(getColorByState(trangThaiMoi));
                clickedButton.putClientProperty("trangThai", trangThaiMoi);
            }
        }
    }

    private JPanel createLegendItem(String label, Color color) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton colorBox = new JButton();
        colorBox.setEnabled(false);
        colorBox.setBackground(color);
        colorBox.setPreferredSize(new Dimension(40, 40));
        panel.add(colorBox);
        JLabel lable = new JLabel(label);
        lable.setFont(font);
        panel.add(lable);
        return panel;
    }
}
