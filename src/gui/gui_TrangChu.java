package gui;

import java.awt.BorderLayout;
import java.awt.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

public class gui_TrangChu extends JFrame{
	private JPanel header;
	private JPanel sidebar;
	private JTree menuTree;
	private JPanel mainPanel;

	public gui_TrangChu() {
		setTitle("SkyHotel Management");
        setSize(1200, 700);
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
        
        // Sidebar Menu
        sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setBackground(Color.LIGHT_GRAY);

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Danh mục quản lý");
        DefaultMutableTreeNode qlPhong = new DefaultMutableTreeNode("Quản lý phòng");
        qlPhong.add(new DefaultMutableTreeNode("Danh sách phòng"));
        qlPhong.add(new DefaultMutableTreeNode("Đặt phòng"));
        
        DefaultMutableTreeNode nhanVien = new DefaultMutableTreeNode("Nhân viên");
        nhanVien.add(new DefaultMutableTreeNode("Danh sách nhân viên"));
        nhanVien.add(new DefaultMutableTreeNode("Chấm công"));
        
        DefaultMutableTreeNode khachHang = new DefaultMutableTreeNode("Khách hàng");
        khachHang.add(new DefaultMutableTreeNode("Danh sách khách hàng"));
        khachHang.add(new DefaultMutableTreeNode("Lịch sử đặt phòng"));
        
        DefaultMutableTreeNode dichVu = new DefaultMutableTreeNode("Dịch vụ");
        DefaultMutableTreeNode khuyenMai = new DefaultMutableTreeNode("Khuyến mãi");
        DefaultMutableTreeNode thongKe = new DefaultMutableTreeNode("Thống kê");

        root.add(qlPhong);
        root.add(nhanVien);
        root.add(khachHang);
        root.add(dichVu);
        root.add(khuyenMai);
        root.add(thongKe);

        menuTree = new JTree(root);
        menuTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        sidebar.add(new JScrollPane(menuTree), BorderLayout.CENTER);

        add(sidebar, BorderLayout.WEST);
        
        // Main Panel
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel, BorderLayout.CENTER);
	}
	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new gui_TrangChu().setVisible(true);
        });
    }
}
