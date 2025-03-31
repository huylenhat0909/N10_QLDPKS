package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class gui_TrangChu extends JFrame {
    private JPanel menuPanel;
    private Map<JPanel, Boolean> subMenuVisibility; // Lưu trạng thái mở rộng của từng submenu
	private JPanel header;
	private JPanel currentlyOpenSubMenu = null; // Theo dõi menu nào đang mở

    public gui_TrangChu() {
        setTitle("SkyHotel Management");
        setSize(1500, 1000);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        // Header
        header = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        header.setBackground(new Color(180, 0, 0));
        JLabel titleLabel = new JLabel("SKYHOTEL");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        JButton logoutButton = new JButton("Thoát");
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(new Color(200, 0, 0));
        header.add(titleLabel);
        header.add(Box.createHorizontalStrut(800));
        header.add(logoutButton);
        add(header, BorderLayout.NORTH);
        subMenuVisibility = new HashMap<>();

        // Tạo menu bên trái
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setPreferredSize(new Dimension(250, getHeight()));
        
     // Thêm menu không có submenu
        addMenuWithoutSubmenu(menuPanel, "Sơ đồ Khách sạn","icon/hotel.png");
        // Thêm menu chính với submenu
        addMenuItem(menuPanel, "Phòng", new String[]{"Danh sách loại phòng", "Danh sách phòng"},new String[] {"icon/money.png"});
        addMenuItem(menuPanel, "Dịch vụ", new String[]{"Đặt dịch vụ", "Quản lý kho"},new String[] {"icon/money.png"});
        addMenuItem(menuPanel, "Khuyến mãi", new String[]{"Chương trình KM", "Mã giảm giá"},new String[] {"icon/money.png"});
        addMenuItem(menuPanel, "Nhân viên", new String[]{"Danh sách", "Chấm công"},new String[] {"icon/money.png"});
        addMenuItem(menuPanel, "Khách hàng", new String[]{"Danh sách khách hàng", "Thông tin cá nhân"},new String[] {"icon/money.png"});
        addMenuItem(menuPanel, "Thống kê", new String[]{"Doanh thu", "Số lượng khách"},new String[] {"icon/money.png"});

        // Thêm menuPanel vào JFrame
        JScrollPane scrollPane = new JScrollPane(menuPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.WEST);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel, BorderLayout.CENTER);
    }
    private void addMenuWithoutSubmenu(JPanel parent, String title, String path_icon) {
        JButton mainButton = new JButton(title);
        mainButton.setFont(new Font("Arial", Font.BOLD, 16));
        mainButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainButton.setMaximumSize(new Dimension(240, 50));
        mainButton.setHorizontalAlignment(SwingConstants.LEFT); // Căn chữ sang trái
        mainButton.setMargin(new Insets(5, 30, 5, 5)); // Tạo khoảng cách giữa icon và chữ

        // Load và resize icon
        ImageIcon icon = getResizedIcon(path_icon, 24, 24); // Resize icon về 24x24
        mainButton.setIcon(icon);
        
        // Sự kiện khi bấm vào menu
        mainButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Bạn đã chọn: " + title));

        parent.add(mainButton);
    }

    // Hàm resize icon để tránh bị phình to
    private ImageIcon getResizedIcon(String path, int width, int height) {
        ImageIcon originalIcon = new ImageIcon(path);
        Image resizedImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }
    // Hàm thêm menu item có submenu
    private void addMenuItem(JPanel parent, String title, String[] subItems, String[] paths_icon) {
        // Tạo nút menu chính
        JButton mainButton = new JButton(title);
        mainButton.setFont(new Font("Arial", Font.BOLD, 16));
        mainButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainButton.setMaximumSize(new Dimension(240, 50));
        mainButton.setHorizontalAlignment(SwingConstants.LEFT); // Căn chữ sang trái
        mainButton.setMargin(new Insets(5, 30, 5, 5)); // Tạo khoảng cách giữa icon và chữ
        // Load và resize icon
        ImageIcon icon = getResizedIcon(paths_icon[0], 24, 24); // Resize icon về 24x24
        mainButton.setIcon(icon);
        // Tạo submenu panel
        JPanel subMenuPanel = new JPanel();
        subMenuPanel.setLayout(new BoxLayout(subMenuPanel, BoxLayout.Y_AXIS));
        subMenuPanel.setBackground(Color.GRAY);
        subMenuPanel.setVisible(false);

        for (String item : subItems) {
            JButton subButton = new JButton("   " + item);
            subButton.setFont(new Font("Arial", Font.BOLD, 14));
            subButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            subButton.setMaximumSize(new Dimension(240, 50));
            subButton.setHorizontalAlignment(SwingConstants.LEFT); // Căn chữ sang trái
            subButton.setMargin(new Insets(5, 30, 5, 5)); // Tạo khoảng cách giữa icon và chữ
            // Load và resize icon
            ImageIcon icon_subs = getResizedIcon(paths_icon[0], 24, 24); // Resize icon về 24x24
            subButton.setIcon(icon_subs);
            subMenuPanel.add(subButton);
            subButton.setBackground(Color.lightGray);
        }

        // Lưu trạng thái submenu (ẩn/hiện)
        subMenuVisibility.put(subMenuPanel, false);

        // Xử lý sự kiện khi nhấn vào menu chính
        mainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	boolean isVisible = subMenuVisibility.get(subMenuPanel);

                // Đóng tất cả các submenu khác trước khi mở cái mới
                closeAllSubMenus();
                
                if (!isVisible) {
                    subMenuPanel.setVisible(true);
                    currentlyOpenSubMenu = subMenuPanel;
                    subMenuVisibility.put(subMenuPanel, true);
                }

                // Cập nhật giao diện khi mở/đóng submenu
                parent.revalidate();
                parent.repaint();
            }
        });

        parent.add(mainButton);
        parent.add(subMenuPanel);
    }
    private void closeAllSubMenus() {
        for (Map.Entry<JPanel, Boolean> entry : subMenuVisibility.entrySet()) {
            entry.getKey().setVisible(false);
            subMenuVisibility.put(entry.getKey(), false);
        }
        currentlyOpenSubMenu = null;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new gui_TrangChu().setVisible(true));
    }
}
