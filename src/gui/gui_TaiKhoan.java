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
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import dao.DaoCTPhieuDP;
import dao.DaoKhachHang;
import dao.DaoKhuyenMai;
import dao.DaoLoaiPhong;
import dao.DaoNhanVien;
import dao.DaoPhieuDP;
import dao.DaoTaiKhoan;
import entity.ChiTietPhieuDatPhong;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.LoaiPhong;
import entity.NhanVien;
import entity.PhieuDatPhong;
import entity.TaiKhoan;

public class gui_TaiKhoan extends JPanel implements ActionListener {
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
		private DaoKhuyenMai daokm;
		private List<KhuyenMai> dskm;
		private DaoKhachHang daokh;
		private List<KhachHang> dskh;
		private DaoTaiKhoan daotk;
		private List<TaiKhoan> dstk;
		private JPasswordField txtPassword;
		private JPasswordField txtConfirmPassword;
		private JTextField txtUsername;

		

	    public gui_TaiKhoan() {
	    	daotk= new DaoTaiKhoan();
	    	Font font = new Font("Arial",Font.BOLD, 16);
	        setLayout(new BorderLayout());
	        JPanel headerPanel = new JPanel();
	        originalData = new ArrayList<Object[]>();
	        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
	        JLabel lblSearch = new JLabel("Mã nhân viên:");
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
	        tableModel = new DefaultTableModel(new Object[]{"Mã nhân viên","Tên tài khoản", "Mật khẩu"}, 0) {
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
	    	String maNv= tableModel.getValueAt(row,0).toString();
	        String tenTk = (String) tableModel.getValueAt(row, 1);
	        String mk = (String) tableModel.getValueAt(row, 2);
	        
	        // Tạo các trường nhập liệu với dữ liệu ban đầu
	        txttenLoaiPhong = new JTextField(tenTk);
	        txtmota	= new JTextField(mk);
	        // Sắp xếp các trường nhập liệu trong một panel
	        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
	        panel.add(new JLabel("Tên tài khoản:"));
	        panel.add(txttenLoaiPhong);
	        panel.add(new JLabel("Mật khẩu:"));
	        panel.add(txtmota);
	        
	        int result = JOptionPane.showConfirmDialog(
	                this, panel, "Cập nhật thông tin loại phòng",
	                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	        
	        if (result == JOptionPane.OK_OPTION) {
	            // Cập nhật thông tin vào bảng
	            tableModel.setValueAt(txttenLoaiPhong.getText(), row, 1);
	            tableModel.setValueAt(txtmota.getText(), row, 2);
	            DaoNhanVien daonv= new DaoNhanVien();
	            NhanVien nv= daonv.getNhanVienTheoMa(maNv);
	            TaiKhoan tk_new= new TaiKhoan(nv, txttenLoaiPhong.getText(), txtmota.getText());
	            if(daotk.capNhatTK(tk_new)) {
	            	JOptionPane.showMessageDialog(null,"Cập nhật tài khoản thành công");
	            }else {
	            	JOptionPane.showMessageDialog(null,"Cập nhật không thành công");
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
				JDialog addDialog = new JDialog();
				addDialog.setTitle("Cấp tài khoản cho nhân viên");
				addDialog.setSize(400, 250);
				addDialog.setLocationRelativeTo(this);

				// Tạo JPanel chính và đặt layout + border
				JPanel mainPanel = new JPanel(new GridLayout(0, 2, 10, 10));
				mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // top, left, bottom, right

				DaoNhanVien daonv = new DaoNhanVien();
				List<NhanVien> dsNhanVien = daonv.getDatabase();

				// ComboBox chọn mã nhân viên
				JLabel lblMaNV = new JLabel("Chọn mã nhân viên:");
				JComboBox<String> cbMaNV = new JComboBox<>();
				for (NhanVien nv : dsNhanVien) {
				    cbMaNV.addItem(nv.getMaNV());
				}

				// Nhập tài khoản và mật khẩu
				JLabel lblUsername = new JLabel("Tên tài khoản:");
				 txtUsername = new JTextField();

				JLabel lblPassword = new JLabel("Mật khẩu:");
				JPasswordField txtPassword = new JPasswordField();

				JLabel lblConfirmPassword = new JLabel("Xác nhận mật khẩu:");
				JPasswordField txtConfirmPassword = new JPasswordField();

				JButton btnSave = new JButton("Lưu");
				JButton btnCancel = new JButton("Hủy");

				// Thêm thành phần vào mainPanel
				mainPanel.add(lblMaNV); mainPanel.add(cbMaNV);
				mainPanel.add(lblUsername); mainPanel.add(txtUsername);
				mainPanel.add(lblPassword); mainPanel.add(txtPassword);
				mainPanel.add(lblConfirmPassword); mainPanel.add(txtConfirmPassword);
				mainPanel.add(btnSave); mainPanel.add(btnCancel);
				addDialog.setContentPane(mainPanel);

			    // Xử lý sự kiện lưu
			    btnSave.addActionListener(new ActionListener() {
			        public void actionPerformed(ActionEvent e) {
			            String maNV = cbMaNV.getSelectedItem().toString();
			            String username = txtUsername.getText().trim();
			            String password = new String(txtPassword.getPassword()).trim();
			            String confirmPassword = new String(txtConfirmPassword.getPassword()).trim();
			            // Kiểm tra nếu nhân viên đã có tài khoản
			            for (TaiKhoan tk : dstk) {
			                if (tk.getNhanVien().getMaNV().equals(maNV)) {
			                    JOptionPane.showMessageDialog(addDialog, "Nhân viên này đã có tài khoản!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			                }
			            }
			            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
			                JOptionPane.showMessageDialog(addDialog, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			                return;
			            }

			            if (!password.equals(confirmPassword)) {
			                JOptionPane.showMessageDialog(addDialog, "Mật khẩu và xác nhận mật khẩu không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			                return;
			            }


			            try {
			                // Gọi DAO để thêm tài 
			            	NhanVien nv= daonv.getNhanVienTheoMa(maNV);
			                TaiKhoan taikhoan = new TaiKhoan(nv, username, password);
			                daotk.themTaiKhoan(taikhoan);

			                JOptionPane.showMessageDialog(addDialog, "Tạo tài khoản thành công!");
			                addDialog.dispose();
			            } catch (Exception ex) {
			                JOptionPane.showMessageDialog(addDialog, "Lỗi khi tạo tài khoản: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
			            }
			        }
			    });

			    btnCancel.addActionListener(e1 -> addDialog.dispose());

			    addDialog.setVisible(true);
			}
		}

		private void deleteRow() {
			// TODO Auto-generated method stub
			int selectedRow = table.getSelectedRow();

		    if (selectedRow == -1) {
		        JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
		        return;
		    }

		    int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa tài khoản này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
		    if (confirm == JOptionPane.YES_OPTION) {
		        tableModel.removeRow(selectedRow);
		        String tentk=tableModel.getValueAt(selectedRow, 1).toString();
		        
		        if(daotk.xoaTaiKhoan(tentk)) {
		        	JOptionPane.showMessageDialog(this, "Xóa tài khoản thành công!");
		        }else {
		        	JOptionPane.showMessageDialog(this, "Xóa tài khoản không thành công!");
		        }
		        
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
			dstk = daotk.getDatabase();

		    tableModel.setRowCount(0); // Xóa dữ liệu cũ
		    originalData.clear();      // Xóa dữ liệu gốc

		    for (TaiKhoan pdp : dstk) {
		        Object[] row = new Object[]{
		            pdp.getNhanVien().getMaNV(),
		            pdp.getTaiKhoan(),
		            pdp.getMatKhau()
		        };
		        tableModel.addRow(row);
		        originalData.add(row); // Lưu dữ liệu để reset/tìm kiếm
		    }
		}
}

