package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

import dao.DaoCTPhieuDP;
import dao.DaoChiTietHoaDon;
import dao.DaoHoaDon;
import dao.DaoKhachHang;
import dao.DaoNhanVien;
import dao.DaoPhieuDP;
import entity.ChiTietPhieuDatPhong;
import entity.HoaDon;
import entity.PhieuDatPhong;

public class gui_HoaDon extends JPanel implements ActionListener {
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
		private DaoPhieuDP daoPDP;
		private DaoCTPhieuDP daoCTPDP;
		private DaoKhachHang daoKH;
		private DaoNhanVien daoNV;
		private DaoHoaDon daohd;
		private JTextField txttenKhachHangField;
		private JTextField txttenKhuyenmai;
		private JTextField txttenNhanVien;
		private JTextField txtmaHD;
		private JTextField txttongtien;

	    public gui_HoaDon() {
	    	daohd= new DaoHoaDon();
	    	daoPDP= new DaoPhieuDP();
	    	daoCTPDP= new DaoCTPhieuDP();
	    	daoKH= new DaoKhachHang();
	    	daoNV= new DaoNhanVien();
	    	Font font = new Font("Arial",Font.BOLD, 16);
	        setLayout(new BorderLayout());
	        JPanel headerPanel = new JPanel();
	        originalData = new ArrayList<Object[]>();
	        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
	        JLabel lblSearch = new JLabel("Nhập mã hóa đơn:");
	        lblSearch.setFont(font);
	        txtSearch = new JTextField(15);
	        txtSearch.setPreferredSize(new Dimension(30, 30));
	        txtSearch.setFont(font);
	        btnSearch = new JButton("Tìm");
	        btnReset = new JButton("Tải lại");
	        btnDelete= new JButton("Xóa");
	        btnSearch.setFont(font);
	        btnReset.setFont(font);
	        btnDelete.setFont(font);
	        headerPanel.add(lblSearch);
	        headerPanel.add(txtSearch);
	        headerPanel.add(btnSearch);
	        headerPanel.add(btnReset);
	        //headerPanel.add(btnDelete);
	        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 0));
	        add(headerPanel,BorderLayout.NORTH);
	        // Khởi tạo model cho bảng với các cột cần thiết
	        tableModel = new DefaultTableModel(new Object[]{"Mã hóa đơn","Tên nhân viên",  "Khuyến mãi đã áp dụng","Tên khách hàng"}, 0) {
	            @Override
	            public boolean isCellEditable(int row, int column) {
	                return false;  // Người dùng không chỉnh sửa trực tiếp trên bảng
	            }
	        };
	        
	        // Tạo bảng từ model
	        table = new JTable(tableModel);
	        table.setFillsViewportHeight(true);
	        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	        table.setFont(new Font("Arial", Font.PLAIN, 14));
	        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
	        table.getTableHeader().setBackground(new Color(180, 0, 0));
	        table.getTableHeader().setForeground(Color.WHITE);
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
	        loadDataFromDatabase();
	        scrollPane.setViewportView(table);
	    }
	    
	    /** 
	     * Phương thức mở cửa sổ dialog cập nhật thông tin cho dòng được chọn.
	     * @param row chỉ số dòng trong bảng
	     */
	    private void openUpdateDialog(int row) {
	        // Lấy thông tin hiện tại từ bảng
	        String maHD = (String) tableModel.getValueAt(row, 0);
	        String tenNhanVien = (String) tableModel.getValueAt(row, 1);
	        String tenKhuyenMai = (String) tableModel.getValueAt(row, 2);
	        String tenKhachHang = (String) tableModel.getValueAt(row, 3);
	        DaoChiTietHoaDon daocthd= new DaoChiTietHoaDon();
	        Double tongtien= daocthd.tinhTongtienPhongvaDichVu(maHD);
	        // Tạo các trường nhập liệu với dữ liệu ban đầu
	        txtmaHD = new JTextField(maHD);
	        txtmaHD.setEditable(false);

	        txttenKhachHangField = new JTextField(tenKhachHang);
	        txttenKhachHangField.setEditable(false);

	        txttenKhuyenmai = new JTextField(tenKhuyenMai);
	        txttenKhuyenmai.setEditable(false);

	        txttenNhanVien = new JTextField(tenNhanVien);
	        txttenNhanVien.setEditable(false);

	        txttongtien = new JTextField(formatCurrencyVND(tongtien));
	        txttongtien.setEditable(false);
	        // Dùng JFormattedTextField hay JTextField để nhập ngày giờ (ở đây dùng JTextField cho đơn giản)
	        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	        ngayGioDat = LocalDateTime.now().format(formatter);
	        ngayGioTra = LocalDateTime.now().plusHours(1).format(formatter);
	        ngayGioDatField = new JTextField(ngayGioDat.toString());
	        ngayGioTraField = new JTextField(ngayGioTra.toString());
	        
	        // Sắp xếp các trường nhập liệu trong một panel
	        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
	        panel.add(new JLabel("Mã hóa đơn:"));
	        panel.add(txtmaHD);
	        panel.add(new JLabel("Tên nhân viên:"));
	        panel.add(txttenNhanVien);
	        panel.add(new JLabel("Tên khách hàng:"));
	        panel.add(txttenKhachHangField);
	        panel.add(new JLabel("Tên khuyến mãi áp dụng:"));
	        panel.add(txttenKhuyenmai);
	        panel.add(new JLabel("Ngày giờ đặt (yyyy-MM-dd HH:mm):"));
	        panel.add(ngayGioDatField);
	        panel.add(new JLabel("Ngày giờ trả (yyyy-MM-dd HH:mm):"));
	        panel.add(ngayGioTraField);
	        panel.add(new JLabel("Tổng tiền hóa đơn"));
	        panel.add(txttongtien);
	        int result = JOptionPane.showConfirmDialog(
	                this, panel, "Thông tin của hóa đơn",
	                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	        
	        if (result == JOptionPane.OK_OPTION) {
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
		private void loadDataFromDatabase() {
		    daohd= new DaoHoaDon();
		    List<HoaDon> dshd= daohd.getDatabase();

		    tableModel.setRowCount(0); // Xóa dữ liệu cũ
		    originalData.clear();      // Xóa dữ liệu gốc

		    for (HoaDon pdp : dshd) {
		        Object[] row = new Object[]{
		            pdp.getMaHD(),
		            pdp.getNhanvien().getTenNV(),
		            pdp.getKhuyenmai().getTenKM(),
		            pdp.getKhachHang().getTenKH()
		        };
		        tableModel.addRow(row);
		        originalData.add(row); // Lưu dữ liệu để reset/tìm kiếm
		    }
		}
		private String formatCurrencyVND(double amount) {
	        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
	        return formatter.format(amount) + " VND";
	    }
}
