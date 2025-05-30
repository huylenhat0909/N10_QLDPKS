package gui;

import javax.swing.*;

import entity.NhanVien;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class gui_TrangChu extends JFrame {
    private JPanel menuPanel;
    private Map<JPanel, Boolean> subMenuVisibility; // Lưu trạng thái mở rộng của từng submenu
	private JPanel header;
	private JPanel currentlyOpenSubMenu = null; // Theo dõi menu nào đang mở
	private JPanel mainPanel;
	private NhanVien thongtin_nv;

    public gui_TrangChu(NhanVien nv) {
    	thongtin_nv= nv;
        setTitle("SkyHotel Management");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


     // Header
        header = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Căn trái để gọn hơn
        header.setBackground(new Color(180, 0, 0));

        // Tạo tiêu đề với icon
        JLabel titleLabel = new JLabel("SKYHOTEL");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        ImageIcon icon = getResizedIcon("icon/hotel.png", 24, 24);
        titleLabel.setIcon(icon);

        // Thêm thông tin nhân viên đăng nhập
        JLabel userInfoLabel = new JLabel("Xin chào, " + nv.getTenNV() + " (" + nv.getChucVu() + ")");
        userInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        userInfoLabel.setForeground(Color.WHITE);

        // Nút đăng xuất
        JButton logoutButton = new JButton("Thoát");
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(new Color(200, 0, 0));
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn thoát?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new guiDangNhap().setVisible(true); // Quay lại màn hình đăng nhập
                dispose(); // Đóng cửa sổ chính
            }
        });

        // Thêm các thành phần vào header
        header.add(titleLabel);
        header.add(Box.createHorizontalStrut(20)); // Khoảng cách giữa title và thông tin user
        header.add(userInfoLabel);
        header.add(Box.createHorizontalGlue()); // Đẩy logoutButton về bên phải
        header.add(logoutButton);

        // Thêm header vào Frame
        add(header, BorderLayout.NORTH);
        subMenuVisibility = new HashMap<>();

        // Tạo menu bên trái
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setPreferredSize(new Dimension(250, 600));
        
     // Thêm menu không có submenu
        addMenuWithoutSubmenu(menuPanel, "Sơ đồ phòng Khách sạn","icon/hotel.png");
        addMenuWithoutSubmenu(menuPanel,"Phiếu đặt phòng","icon/booking-online.png");
        // Thêm menu chính với submenu
        addMenuItem(menuPanel, "Phòng", new String[]{"Danh sách loại phòng", "Danh sách phòng"},new String[] {"icon/bed.png"});
        addMenuItem(menuPanel, "Dịch vụ", new String[]{"Danh sách loại dịch vụ", "Danh sách dịch vụ"},new String[] {"icon/luggage-cart.png"});
        addMenuWithoutSubmenu(menuPanel, "Khuyến mãi","icon/customer-loyalty.png");
        addMenuWithoutSubmenu(menuPanel, "Khách hàng","icon/office-man (1).png");
        addMenuItem(menuPanel, "Nhân viên", new String[]{"Danh sách nhân viên", "Tài khoản nhân viên"},new String[] {"icon/office-man.png","icon/add-user.png"});
        addMenuItem(menuPanel, "Hóa đơn", new String[]{"Danh sách hóa đơn","Chi tiết hóa đơn"},new String[] {"icon/money.png","icon/detail.png"});
        addMenuItem(menuPanel, "Thống kê", new String[]{"Doanh thu", "Số lượng khách hàng đã đặt","Số lượng phòng đã được đặt"},new String[] {"icon/dashboard.png"});

        // Thêm menuPanel vào JFrame
        JScrollPane scrollPane = new JScrollPane(menuPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.WEST);

        // Main panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
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
        
        Map<String, Supplier<JPanel>> panelFactoryMap = new HashMap<>();
        panelFactoryMap.put("Sơ đồ phòng Khách sạn", () -> new gui_SDKS(thongtin_nv));
        panelFactoryMap.put("Phiếu đặt phòng", () -> new gui_PhieuDatPhong());
        panelFactoryMap.put("Khuyến mãi", () -> new gui_KhuyenMai());
        panelFactoryMap.put("Khách hàng", () -> new gui_KhachHang());

        if (panelFactoryMap.containsKey(title)) {
            mainButton.addActionListener(e -> showPanel(panelFactoryMap.get(title).get()));
        }
        parent.add(mainButton);
    }
    // Phương thức thay đổi nội dung panel bên phải
    private void showPanel(JPanel panel) {
        mainPanel.removeAll();
        mainPanel.add(panel);
        mainPanel.revalidate();
        mainPanel.repaint();
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
        int i=0;
        int len=paths_icon.length;
        for (String item : subItems) {
            JButton subButton = new JButton("   " + item);
            subButton.setFont(new Font("Arial", Font.BOLD, 14));
            subButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            subButton.setMaximumSize(new Dimension(240, 50));
            subButton.setHorizontalAlignment(SwingConstants.LEFT); // Căn chữ sang trái
            subButton.setMargin(new Insets(5, 30, 5, 5)); // Tạo khoảng cách giữa icon và chữ
            // Load và resize icon
            ImageIcon icon_subs = getResizedIcon(paths_icon[i], 24, 24); // Resize icon về 24x24
            if(len-i>1) {
            	i++;
            }
            subButton.setIcon(icon_subs);
            subMenuPanel.add(subButton);
            subButton.setBackground(Color.lightGray);
         // Sự kiện khi bấm vào menu
	         // Map chứa hàm tạo JPanel
	            Map<String, Supplier<JPanel>> panelFactoryMap = new HashMap<>();
	            panelFactoryMap.put("Danh sách loại phòng", () -> new gui_LoaiPhong());
	            panelFactoryMap.put("Danh sách phòng", () -> new gui_Phong());
	            panelFactoryMap.put("Danh sách loại dịch vụ", () -> new gui_LoaiDichVu());
	            panelFactoryMap.put("Danh sách dịch vụ", () -> new gui_DichVu());
	            panelFactoryMap.put("Danh sách nhân viên", () -> new gui_NhanVien());
	            panelFactoryMap.put("Tài khoản nhân viên", () -> new gui_TaiKhoan());
	            panelFactoryMap.put("Danh sách hóa đơn", () -> new gui_HoaDon());
	            panelFactoryMap.put("Chi tiết hóa đơn", () -> new gui_ChiTietHoaDon());
	            panelFactoryMap.put("Doanh thu", () -> new gui_DoanhThu());
	            panelFactoryMap.put("Số lượng khách hàng đã đặt", () -> new gui_SoLuongKH());
	            panelFactoryMap.put("Số lượng phòng đã được đặt", () -> new gui_SoLuongPhong());
	
	            if (panelFactoryMap.containsKey(item)) {
	                subButton.addActionListener(e -> showPanel(panelFactoryMap.get(item).get()));
	            }
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
    	NhanVien nv=new NhanVien();
        SwingUtilities.invokeLater(() -> {
            new gui_TrangChu(nv).setVisible(true);
        });
    }
}
