package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.DaoCTPhieuDP;
import dao.DaoKhachHang;
import dao.DaoLoaiPhong;
import dao.DaoNhanVien;
import dao.DaoPhieuDP;
import dao.DaoPhong;
import entity.ChiTietPhieuDatPhong;
import entity.LoaiPhong;
import entity.PhieuDatPhong;

public class gui_LoaiPhong extends JPanel implements ActionListener {
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
		private DaoPhong daophong;
		

	    public gui_LoaiPhong() {
	    	daophong= new DaoPhong();
	    	daolphong= new DaoLoaiPhong();
	    	Font font = new Font("Arial",Font.BOLD, 16);
	        setLayout(new BorderLayout());
	        JPanel headerPanel = new JPanel();
	        originalData = new ArrayList<Object[]>();
	        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
	        JLabel lblSearch = new JLabel("Nhập tên loại phòng :");
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
	        tableModel = new DefaultTableModel(new Object[]{"Mã loại phòng","Tên loại phòng","Mô tả", "Giá theo giờ", "Giá theo phòng"}, 0) {
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
	        String tenLoaiPhong     = (String) tableModel.getValueAt(row, 1);
	        String moTa             = (String) tableModel.getValueAt(row, 2);
	        String giaphonggio      = tableModel.getValueAt(row, 3).toString();
	        String giaphongngay     = tableModel.getValueAt(row, 4).toString();

	        // Tạo dialog
	        JDialog dialog = new JDialog();
	        dialog.setTitle("Cập nhật thông tin loại phòng");
	        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	        dialog.setSize(400, 250);             // Kích thước cố định
	        dialog.setResizable(false);           // Không cho resize
	        dialog.setLocationRelativeTo(this);   // Canh giữa

	        // Tạo panel nhập liệu với GridLayout
	        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
	        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	        panel.add(new JLabel("Tên loại phòng:"));
	        JTextField txtTenLoaiPhong    = new JTextField(tenLoaiPhong);
	        panel.add(txtTenLoaiPhong);

	        panel.add(new JLabel("Mô tả loại phòng:"));
	        JTextField txtMoTa            = new JTextField(moTa);
	        panel.add(txtMoTa);

	        panel.add(new JLabel("Giá phòng theo giờ:"));
	        JTextField txtGiaGio          = new JTextField(giaphonggio);
	        panel.add(txtGiaGio);

	        panel.add(new JLabel("Giá phòng theo ngày:"));
	        JTextField txtGiaNgay         = new JTextField(giaphongngay);
	        panel.add(txtGiaNgay);

	        // Panel chứa nút
	        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	        JButton btnSave   = new JButton("Lưu");
	        JButton btnCancel = new JButton("Hủy");
	        buttonPanel.add(btnSave);
	        buttonPanel.add(btnCancel);

	        // Xử lý sự kiện nút
	        btnSave.addActionListener(e -> {
	            // Cập nhật model và database
	            tableModel.setValueAt(txtTenLoaiPhong.getText(), row, 1);
	            tableModel.setValueAt(txtMoTa.getText(), row, 2);
	            tableModel.setValueAt(txtGiaGio.getText(), row, 3);
	            tableModel.setValueAt(txtGiaNgay.getText(), row, 4);

	            LoaiPhong lp = new LoaiPhong(
	                /* id lấy từ cột 0 */ tableModel.getValueAt(row, 0).toString(),
	                txtTenLoaiPhong.getText(),
	                txtMoTa.getText(),
	                Double.parseDouble(txtGiaGio.getText()),
	                Double.parseDouble(txtGiaNgay.getText())
	            );
	            daolphong.capnhatLoaiPhong(lp);
	            dialog.dispose();
	        });
	        btnCancel.addActionListener(e -> dialog.dispose());

	        // Lấy content pane, thêm các panel
	        Container cp = dialog.getContentPane();
	        cp.setLayout(new BorderLayout());
	        cp.add(panel, BorderLayout.CENTER);
	        cp.add(buttonPanel, BorderLayout.SOUTH);

	        dialog.setVisible(true);
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
		        addDialog.setSize(300, 200);
		        addDialog.setLocationRelativeTo(this);
		        addDialog.setTitle("Thêm loại phòng");
		        // 1. Tạo panel chính với GridLayout và thêm EmptyBorder để tạo khoảng cách
		        JPanel content = new JPanel(new GridLayout(5, 2, 10, 10));
		        content.setBorder(new EmptyBorder(15, 15, 15, 15)); // top, left, bottom, right

		        // 2. Tạo các thành phần
		        JLabel lblName    = new JLabel("Tên loại phòng:");
		        JTextField txtName = new JTextField();

		        JLabel lblMota    = new JLabel("Mô tả loại phòng:");
		        JTextField txtMota = new JTextField();

		        JLabel lblGiaGio  = new JLabel("Giá theo giờ:");
		        JTextField txtGiaGio = new JTextField();

		        JLabel lblGiaNgay = new JLabel("Giá theo ngày:");
		        JTextField txtNgay   = new JTextField();

		        JButton btnSave   = new JButton("Lưu");
		        JButton btnCancel = new JButton("Hủy");

		        // 3. Thêm vào panel
		        content.add(lblName);
		        content.add(txtName);
		        content.add(lblMota);
		        content.add(txtMota);
		        content.add(lblGiaGio);
		        content.add(txtGiaGio);
		        content.add(lblGiaNgay);
		        content.add(txtNgay);
		        content.add(btnSave);
		        content.add(btnCancel);

		        // 4. Đưa panel vào dialog
		        addDialog.setContentPane(content);

			    btnSave.addActionListener(new ActionListener() {
			        public void actionPerformed(ActionEvent e) {
			            String name = txtName.getText().trim();
			            String mota = txtMota.getText().trim();
			            Double giaphonggio= Double.parseDouble(txtGiaGio.getText().trim());
			            Double giaphongngay= Double.parseDouble(txtNgay.getText().trim());
			            if (name.isEmpty()) {
			                JOptionPane.showMessageDialog(addDialog, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			                return;
			            }

			            try {
			            	String malphong=daolphong.taomaLP(dslphong);
			            	LoaiPhong lphong = new LoaiPhong(malphong, name, mota, giaphonggio, giaphongngay);
			                daolphong.themLoaiPhong(lphong);
			                JOptionPane.showMessageDialog(addDialog, "Thêm loại phòng thành công!");
			                addDialog.dispose();
			            } catch (NumberFormatException ex) {
			                JOptionPane.showMessageDialog(addDialog, "Giá phòng không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			            }
			        }
			    });

			    btnCancel.addActionListener(e1 -> addDialog.dispose());
			    addDialog.setVisible(true);
			    loadDataFromDatabase();
			}
		}

		private void deleteRow() {
			// TODO Auto-generated method stub
			int selectedRow = table.getSelectedRow();

		    if (selectedRow == -1) {
		        JOptionPane.showMessageDialog(this, "Vui lòng chọn loại phòng cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
		        return;
		    }

		    int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
		    if (confirm == JOptionPane.YES_OPTION) {
		    	String mlp= tableModel.getValueAt(selectedRow, 0).toString();
		        tableModel.removeRow(selectedRow);
		        if(daolphong.xoaLoaiPhong(mlp)) {
		        	JOptionPane.showMessageDialog(this, "Đã xóa thành công!");
		        }else {
		        	JOptionPane.showMessageDialog(this, "Xóa không thành công!");
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
			            String maPhieu = row[1].toString().toLowerCase(); // Cột "Mã phiếu đặt phòng"
			            if (maPhieu.contains(searchTerm)) {
			                tableModel.addRow(row);
			            }
			        }
			    }
		}
		private void loadDataFromDatabase() {
			dslphong = daolphong.getDatabase();
		    tableModel.setRowCount(0); // Xóa dữ liệu cũ
		    originalData.clear();      // Xóa dữ liệu gốc

		    for (LoaiPhong pdp : dslphong) {
		        Object[] row = new Object[]{
		            pdp.getMaLoaiP(),
		            pdp.getTenLoaiP(),
		            pdp.getMoTa(),
		            formatCurrencyVND(pdp.getGiaPhongtheogio()),
		            formatCurrencyVND(pdp.getGiaPhongtheongay())
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
