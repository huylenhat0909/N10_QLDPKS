package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class gui_PhieuDatPhong extends JPanel implements ActionListener {
		private JTable table;
	    private DefaultTableModel tableModel;
		private JTextField tenPhongField;
		private JTextField tenKhachHangField;
		private JTextField sdtField;
		private JTextField kieuThueField;
		private DateTimeFormatter formatter;
		private Object ngayGioDat;
		private Object ngayGioTra;
		private JTextField ngayGioDatField;
		private JTextField ngayGioTraField;
		private JTextField txtSearch;
		private JButton btnSearch;
		private JButton btnReset;
		private ArrayList<Object[]> originalData;
		private JButton btnDelete;

	    public gui_PhieuDatPhong() {
	    	Font font = new Font("Arial",Font.BOLD, 16);
	        setLayout(new BorderLayout());
	        JPanel headerPanel = new JPanel();
	        originalData = new ArrayList<Object[]>();
	        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
	        JLabel lblSearch = new JLabel("Nhập mã phiếu:");
	        lblSearch.setFont(font);
	        txtSearch = new JTextField(15);
	        btnSearch = new JButton("Find");
	        btnReset = new JButton("Reset");
	        btnDelete= new JButton("Delete");
	        btnSearch.setFont(font);
	        btnReset.setFont(font);
	        btnDelete.setFont(font);
	        headerPanel.add(lblSearch);
	        headerPanel.add(txtSearch);
	        headerPanel.add(btnSearch);
	        headerPanel.add(btnReset);
	        headerPanel.add(btnDelete);
	        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 0));
	        add(headerPanel,BorderLayout.NORTH);
	        // Khởi tạo model cho bảng với các cột cần thiết
	        tableModel = new DefaultTableModel(new Object[]{"Mã phiếu đặt phòng","Tên nhân viên","Tên phòng", "Tên khách hàng", "SĐT khách hàng", "Kiểu thuê"}, 0) {
	            @Override
	            public boolean isCellEditable(int row, int column) {
	                return false;  // Người dùng không chỉnh sửa trực tiếp trên bảng
	            }
	        };
	        
	     // Thêm dữ liệu mẫu
	        Object[] row1 = {"Phòng 101", "Nguyễn Văn A", "Phòng 1", "Khách A", "0123456789", "Theo giờ"};
	        Object[] row2 = {"Phòng 102", "Trần Thị B", "Phòng 2", "Khách B", "0987654321", "Theo ngày"};

	        tableModel.addRow(row1);
	        tableModel.addRow(row2);

	        originalData.add(row1);
	        originalData.add(row2);
	        
	        // Tạo bảng từ model
	        table = new JTable(tableModel);
	        table.setFillsViewportHeight(true);
	        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	        table.setFont(new Font("Arial", Font.PLAIN, 14));
	        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
	        table.setRowHeight(25);
	        // Lắng nghe sự kiện nhấp chuột vào dòng (double click để mở dialog cập nhật)
	        table.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	                // Nếu nhấp đúp vào dòng
	                if (e.getClickCount() == 2) {
	                    int selectedRow = table.getSelectedRow();
	                    if (selectedRow != -1) {
	                        openUpdateDialog(selectedRow);
	                    }
	                }
	            }
	        });
	        add(Box.createVerticalStrut(10), BorderLayout.CENTER); // thêm khoảng cách giữa header và bảng
	        JScrollPane scrollPane = new JScrollPane(table);
	        add(scrollPane,BorderLayout.CENTER);
	        btnSearch.addActionListener(this);
	        btnReset.addActionListener(this);
	        btnDelete.addActionListener(this);
	    }
	    
	    /** 
	     * Phương thức mở cửa sổ dialog cập nhật thông tin cho dòng được chọn.
	     * @param row chỉ số dòng trong bảng
	     */
	    private void openUpdateDialog(int row) {
	        // Lấy thông tin hiện tại từ bảng
	        String tenPhong = (String) tableModel.getValueAt(row, 0);
	        String tenKhachHang = (String) tableModel.getValueAt(row, 1);
	        String sdt = (String) tableModel.getValueAt(row, 2);
	        String kieuThue = (String) tableModel.getValueAt(row, 3);
	        
	        // Tạo các trường nhập liệu với dữ liệu ban đầu
	        tenPhongField = new JTextField(tenPhong);
	        tenKhachHangField = new JTextField(tenKhachHang);
	        sdtField = new JTextField(sdt);
	        kieuThueField = new JTextField(kieuThue);
	        
	        // Dùng JFormattedTextField hay JTextField để nhập ngày giờ (ở đây dùng JTextField cho đơn giản)
	        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	        ngayGioDat = LocalDateTime.now().format(formatter);
	        ngayGioTra = LocalDateTime.now().plusHours(1).format(formatter);
	        ngayGioDatField = new JTextField(ngayGioDat.toString());
	        ngayGioTraField = new JTextField(ngayGioTra.toString());
	        
	        // Sắp xếp các trường nhập liệu trong một panel
	        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
	        panel.add(new JLabel("Tên phòng:"));
	        panel.add(tenPhongField);
	        panel.add(new JLabel("Tên khách hàng:"));
	        panel.add(tenKhachHangField);
	        panel.add(new JLabel("SĐT:"));
	        panel.add(sdtField);
	        panel.add(new JLabel("Kiểu thuê:"));
	        panel.add(kieuThueField);
	        panel.add(new JLabel("Ngày giờ đặt (yyyy-MM-dd HH:mm):"));
	        panel.add(ngayGioDatField);
	        panel.add(new JLabel("Ngày giờ trả (yyyy-MM-dd HH:mm):"));
	        panel.add(ngayGioTraField);
	        
	        int result = JOptionPane.showConfirmDialog(
	                this, panel, "Cập nhật thông tin đặt phòng",
	                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	        
	        if (result == JOptionPane.OK_OPTION) {
	            // Cập nhật thông tin vào bảng
	            tableModel.setValueAt(tenPhongField.getText(), row, 0);
	            tableModel.setValueAt(tenKhachHangField.getText(), row, 1);
	            tableModel.setValueAt(sdtField.getText(), row, 2);
	            tableModel.setValueAt(kieuThueField.getText(), row, 3);
	            
	            // Ở ứng dụng thực tế, bạn có thể cập nhật thêm các thông tin ngày giờ đặt/trả vào cơ sở dữ liệu hoặc một model riêng.
	            System.out.println("Cập nhật thành công:");
	            System.out.println("Tên phòng: " + tenPhongField.getText());
	            System.out.println("Tên khách hàng: " + tenKhachHangField.getText());
	            System.out.println("SĐT: " + sdtField.getText());
	            System.out.println("Kiểu thuê: " + kieuThueField.getText());
	            System.out.println("Ngày giờ đặt: " + ngayGioDatField.getText());
	            System.out.println("Ngày giờ trả: " + ngayGioTraField.getText());
	        }
	    }

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Object o= e.getSource();
			if(o.equals(btnSearch)) {
				timkiemPhong();
			}
			if(o.equals(btnReset)) {
				reloadData();
			}
			if(o.equals(btnDelete)) {
				deleteRow();
			}
		}

		private void deleteRow() {
			// TODO Auto-generated method stub
			int selectedRow = table.getSelectedRow();

		    if (selectedRow == -1) {
		        JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
		        return;
		    }

		    int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa dòng này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
		    if (confirm == JOptionPane.YES_OPTION) {
		        tableModel.removeRow(selectedRow);
		        JOptionPane.showMessageDialog(this, "Đã xóa dòng thành công!");
		    }
		}

		private void reloadData() {
			// TODO Auto-generated method stub
			txtSearch.setText(""); // Xóa ô tìm kiếm
		    tableModel.setRowCount(0); // Xóa toàn bộ bảng

		    // Hiện lại dữ liệu gốc
		    for (Object[] row : originalData) {
		        tableModel.addRow(row);
		    }
		}

		private void timkiemPhong() {
			// TODO Auto-generated method stub
			 String searchTerm = txtSearch.getText().trim().toLowerCase();

			    // Xóa toàn bộ bảng
			    tableModel.setRowCount(0);

			    if (searchTerm.isEmpty()) {
			        // Hiện lại toàn bộ dữ liệu
			        for (Object[] row : originalData) {
			            tableModel.addRow(row);
			        }
			    } else {
			        for (Object[] row : originalData) {
			            String maPhieu = row[0].toString().toLowerCase(); // Cột "Mã phiếu đặt phòng"
			            if (maPhieu.contains(searchTerm)) {
			                tableModel.addRow(row);
			            }
			        }
			    }
		}
}
