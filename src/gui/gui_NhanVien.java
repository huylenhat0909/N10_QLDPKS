package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import dao.DaoCTPhieuDP;
import dao.DaoKhachHang;
import dao.DaoKhuyenMai;
import dao.DaoLoaiPhong;
import dao.DaoNhanVien;
import dao.DaoPhieuDP;
import entity.ChiTietPhieuDatPhong;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.LoaiPhong;
import entity.NhanVien;
import entity.PhieuDatPhong;

public class gui_NhanVien extends JPanel implements ActionListener {
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
		private DaoLoaiPhong daolphong;
		private JTextField txttenLoaiPhong;
		private JTextField txtmota;
		private JTextField txtgiaphonggio;
		private JTextField txtgiaphongngay;
		private JButton btnAdd;
		private List<LoaiPhong> dslphong;
		private DaoNhanVien daonv;
		private List<NhanVien> dsnv;
		private JTextField txtemail;
		private JTextField txtgioitinh;
		private JTextField txtchucvu;
		private Object selectedDate;
		

	    public gui_NhanVien() {
	    	daonv= new DaoNhanVien();
	    	Font font = new Font("Arial",Font.BOLD, 16);
	        setLayout(new BorderLayout());
	        JPanel headerPanel = new JPanel();
	        originalData = new ArrayList<Object[]>();
	        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
	        JLabel lblSearch = new JLabel("Nhập mã nhân viên:");
	        lblSearch.setFont(font);
	        txtSearch = new JTextField(15);
	        txtSearch.setPreferredSize(new Dimension(30, 30));
	        txtSearch.setFont(font);
	        btnSearch = new JButton("Tìm");
	        btnReset = new JButton("Tải lại");
	        btnDelete= new JButton("Xóa");
	        btnAdd= new JButton("Thêm");
	        btnSearch.setFont(font);
	        btnReset.setFont(font);
	        btnDelete.setFont(font);
	        btnAdd.setFont(font);
	        headerPanel.add(lblSearch);
	        headerPanel.add(txtSearch);
	        headerPanel.add(btnSearch);
	        headerPanel.add(btnReset);
	        headerPanel.add(btnDelete);
	        headerPanel.add(btnAdd);
	        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 0));
	        add(headerPanel,BorderLayout.NORTH);
	        // Khởi tạo model cho bảng với các cột cần thiết
	        tableModel = new DefaultTableModel(new Object[]{"Mã nhân viên","Tên nhân viên", "Số CCCD", "Ngày sinh","Giới tính","Số điện thoại","Email","Chức vụ"}, 0) {
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
	        btnAdd.addActionListener(this);
	        loadDataFromDatabase();
	        scrollPane.setViewportView(table);
	    }
	    
	    /** 
	     * Phương thức mở cửa sổ dialog cập nhật thông tin cho dòng được chọn.
	     * @param row chỉ số dòng trong bảng
	     */
	    private void openUpdateDialog(int row) {
	        // Lấy thông tin hiện tại từ bảng
	        String maNV = tableModel.getValueAt(row, 0).toString();
	        String tenNV = (String) tableModel.getValueAt(row, 1);
	        String socccd = (String) tableModel.getValueAt(row, 2);
	        String ngaysinh = tableModel.getValueAt(row, 3).toString();
	        String gioitinh = tableModel.getValueAt(row, 4).toString();
	        String sdt = tableModel.getValueAt(row, 5).toString();
	        String email = tableModel.getValueAt(row, 6).toString();
	        String chucvu = tableModel.getValueAt(row, 7).toString();

	        // Tạo các trường nhập liệu với dữ liệu ban đầu
	        txttenLoaiPhong = new JTextField(tenNV);
	        txtmota = new JTextField(socccd);
	        txtgiaphongngay = new JTextField(sdt);
	        txtemail = new JTextField(email);

	        // Giới tính
	        String[] gioiTinhOptions = {"Nam", "Nữ"};
	        JComboBox<String> cbGioiTinh = new JComboBox<>(gioiTinhOptions);
	        cbGioiTinh.setSelectedItem(gioitinh);

	        // Chức vụ
	        String[] chucVuOptions = {"Lễ tân", "Quản lý"};
	        JComboBox<String> cbChucVu = new JComboBox<>(chucVuOptions);
	        cbChucVu.setSelectedItem(chucvu);

	        // Ngày sinh
	        JDateChooser dateChooser = new JDateChooser();
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        try {
	            java.util.Date date = sdf.parse(ngaysinh);
	            dateChooser.setDate(date);
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }

	        // Panel nhập liệu
	        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
	        panel.add(new JLabel("Tên nhân viên:"));
	        panel.add(txttenLoaiPhong);
	        panel.add(new JLabel("Số CCCD:"));
	        panel.add(txtmota);
	        panel.add(new JLabel("Ngày sinh:"));
	        panel.add(dateChooser);
	        panel.add(new JLabel("Giới tính:"));
	        panel.add(cbGioiTinh);
	        panel.add(new JLabel("Số điện thoại:"));
	        panel.add(txtgiaphongngay);
	        panel.add(new JLabel("Email:"));
	        panel.add(txtemail);
	        panel.add(new JLabel("Chức vụ:"));
	        panel.add(cbChucVu);

	        int result = JOptionPane.showConfirmDialog(
	            this, panel, "Cập nhật thông tin nhân viên",
	            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

	        if (result == JOptionPane.OK_OPTION) {
	            String tennv = txttenLoaiPhong.getText().trim();
	            String soCCCD = txtmota.getText().trim();
	            String SDT = txtgiaphongngay.getText().trim();
	            String EMAIL = txtemail.getText().trim();
	            boolean GIOITINH = cbGioiTinh.getSelectedItem().toString().equalsIgnoreCase("Nam");
	            String CHUCVU = cbChucVu.getSelectedItem().toString();
	            java.util.Date utilDate = dateChooser.getDate();

	            // Kiểm tra dữ liệu
	            if (tennv.isEmpty() || soCCCD.isEmpty() || SDT.isEmpty() || EMAIL.isEmpty() || utilDate == null) {
	                JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin.");
	                return;
	            }

	            if (tennv.matches(".*\\d.*")) {
	                JOptionPane.showMessageDialog(null, "Tên không được chứa ký tự số!");
	                txttenLoaiPhong.requestFocus();
	                return;
	            }

	            if (!soCCCD.matches("\\d{9,12}")) {
	                JOptionPane.showMessageDialog(null, "Số CCCD phải là 9 đến 12 chữ số.");
	                txtmota.requestFocus();
	                return;
	            }

	            if (!SDT.matches("^(03|09|07|08|05)\\d{8}$")) {
	                JOptionPane.showMessageDialog(null, "Số điện thoại không hợp lệ.");
	                txtgiaphongngay.requestFocus();
	                return;
	            }

	            if (!EMAIL.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
	                JOptionPane.showMessageDialog(null, "Email không hợp lệ.");
	                txtemail.requestFocus();
	                return;
	            }

	            // ==== CHUYỂN java.util.Date -> java.sql.Date ====
	            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

	            // Cập nhật vào model bảng
	            tableModel.setValueAt(tennv, row, 1);
	            tableModel.setValueAt(soCCCD, row, 2);
	            tableModel.setValueAt(sdf.format(utilDate), row, 3); // hiển thị yyyy-MM-dd
	            tableModel.setValueAt(cbGioiTinh.getSelectedItem().toString(), row, 4);
	            tableModel.setValueAt(SDT, row, 5);
	            tableModel.setValueAt(EMAIL, row, 6);
	            tableModel.setValueAt(CHUCVU, row, 7);

	            // Gửi vào database
	            NhanVien nv = new NhanVien(maNV, tennv, soCCCD, sqlDate, GIOITINH, SDT, EMAIL, CHUCVU);

	            if (daonv.capnhatNhanVien(nv)) {
	                JOptionPane.showMessageDialog(null, "Cập nhật thông tin nhân viên thành công!");
	            } else {
	                JOptionPane.showMessageDialog(null, "Cập nhật thông tin nhân viên không thành công!");
	            }
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
			if (o.equals(btnAdd)) {
			    openAddNhanVienDialog();
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
			dsnv = daonv.getDatabase();
		    tableModel.setRowCount(0); // Xóa dữ liệu cũ
		    originalData.clear();      // Xóa dữ liệu gốc
		    for (NhanVien pdp : dsnv) {
		    	String gioitinh="Nữ";
		    	if(pdp.getGioiTinh()) {
		    		gioitinh="Nam";
		    	}
		        Object[] row = new Object[]{
		           pdp.getMaNV(),
		           pdp.getTenNV(),
		           pdp.getSoCCCD(),
		           pdp.getNgaySinh(),
		           gioitinh,
		           pdp.getSoDT(),
		           pdp.getEmail(),
		           pdp.getChucVu()
		        };
		        tableModel.addRow(row);
		        originalData.add(row); // Lưu dữ liệu để reset/tìm kiếm
		    }
		}
		private void openAddNhanVienDialog() {
			JDialog addDialog = new JDialog();
			addDialog.setTitle("Thêm Nhân Viên"); // Đặt tên cho dialog
			addDialog.setSize(400, 300);
			addDialog.setLocationRelativeTo(this);

			// Tạo panel với padding
			JPanel contentPanel = new JPanel(new GridLayout(0, 2, 10, 10));
			contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // top, left, bottom, right

			// Tạo các thành phần giao diện nhập thông tin nhân viên
			JLabel lblTenNV = new JLabel("Tên nhân viên:");
			JTextField txtTenNV = new JTextField();

			JLabel lblCCCD = new JLabel("Số CCCD:");
			JTextField txtCCCD = new JTextField();

			JLabel lblNgaySinh = new JLabel("Ngày sinh:");
			JDateChooser dateChooser = new JDateChooser();

			JLabel lblGioiTinh = new JLabel("Giới tính:");
			JComboBox<String> cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});

			JLabel lblSDT = new JLabel("Số điện thoại:");
			JTextField txtSDT = new JTextField();

			JLabel lblEmail = new JLabel("Email:");
			JTextField txtEmail = new JTextField();

			JLabel lblChucVu = new JLabel("Chức vụ:");
			JComboBox<String> cbChucVu = new JComboBox<>(new String[]{"Lễ tân", "Quản lý"});

			JButton btnSave = new JButton("Lưu");
			JButton btnCancel = new JButton("Hủy");

			// Thêm các thành phần vào contentPanel
			contentPanel.add(lblTenNV); contentPanel.add(txtTenNV);
			contentPanel.add(lblCCCD); contentPanel.add(txtCCCD);
			contentPanel.add(lblNgaySinh); contentPanel.add(dateChooser);
			contentPanel.add(lblGioiTinh); contentPanel.add(cbGioiTinh);
			contentPanel.add(lblSDT); contentPanel.add(txtSDT);
			contentPanel.add(lblEmail); contentPanel.add(txtEmail);
			contentPanel.add(lblChucVu); contentPanel.add(cbChucVu);
			contentPanel.add(btnSave); contentPanel.add(btnCancel);

			// Gán contentPanel vào dialog
			addDialog.setContentPane(contentPanel);
		    // Sự kiện nút lưu
		    btnSave.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		            String tenNV = txtTenNV.getText().trim();
		            String cccd = txtCCCD.getText().trim();
		            java.util.Date ngaySinhUtil = dateChooser.getDate();
		            String gioiTinh = cbGioiTinh.getSelectedItem().toString();
		            String sdt = txtSDT.getText().trim();
		            String email = txtEmail.getText().trim();
		            String chucVu = cbChucVu.getSelectedItem().toString();

		            if (tenNV.isEmpty() || cccd.isEmpty() || ngaySinhUtil == null || sdt.isEmpty() || email.isEmpty()) {
		                JOptionPane.showMessageDialog(addDialog, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
		                return;
		            }

		            if (tenNV.matches(".*\\d.*")) {
		                JOptionPane.showMessageDialog(addDialog, "Tên không được chứa số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
		                txtTenNV.requestFocus();
		                return;
		            }

		            if (!cccd.matches("\\d{9,12}")) {
		                JOptionPane.showMessageDialog(addDialog, "CCCD phải là 9-12 chữ số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
		                txtCCCD.requestFocus();
		                return;
		            }

		            if (!sdt.matches("^(03|09)\\d{8}$")) {
		                JOptionPane.showMessageDialog(addDialog, "Số điện thoại không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
		                txtSDT.requestFocus();
		                return;
		            }

		            if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
		                JOptionPane.showMessageDialog(addDialog, "Email không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
		                txtEmail.requestFocus();
		                return;
		            }

		            try {
		                String maNV = daonv.taomaNV(dsnv); // Tự tạo mã nhân viên
		                boolean gt = gioiTinh.equalsIgnoreCase("Nam");
		                java.sql.Date sqlDate = new java.sql.Date(ngaySinhUtil.getTime());
		                NhanVien nv = new NhanVien(maNV, tenNV, cccd, sqlDate, gt, sdt, email, chucVu);

		                if (daonv.themNhanVien(nv)) {
		                    JOptionPane.showMessageDialog(addDialog, "Thêm nhân viên thành công!");
		                    addDialog.dispose();
		                } else {
		                    JOptionPane.showMessageDialog(addDialog, "Thêm nhân viên thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
		                }
		            } catch (Exception ex) {
		                ex.printStackTrace();
		                JOptionPane.showMessageDialog(addDialog, "Đã xảy ra lỗi!", "Lỗi", JOptionPane.ERROR_MESSAGE);
		            }
		        }
		    });

		    btnCancel.addActionListener(e1 -> addDialog.dispose());

		    addDialog.setVisible(true);
		}
}

