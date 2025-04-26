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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
import entity.ChiTietPhieuDatPhong;
import entity.KhuyenMai;
import entity.LoaiPhong;
import entity.PhieuDatPhong;

public class gui_KhuyenMai extends JPanel implements ActionListener {
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
		

	    public gui_KhuyenMai() {
	    	daokm= new DaoKhuyenMai();
	    	Font font = new Font("Arial",Font.BOLD, 16);
	        setLayout(new BorderLayout());
	        JPanel headerPanel = new JPanel();
	        originalData = new ArrayList<Object[]>();
	        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
	        JLabel lblSearch = new JLabel("Nhập mã khuyến mãi:");
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
	        tableModel = new DefaultTableModel(new Object[]{"Mã khuyến mãi","Mô tả", "Ngày áp dụng", "Ngày hết hạn","Tiền áp dụng","Phần trăm khuyến mãi"}, 0) {
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
	        String tenLoaiPhong = (String) tableModel.getValueAt(row, 1);
	        String moTa = (String) tableModel.getValueAt(row, 2);
	        String giaphonggio = tableModel.getValueAt(row, 3).toString();
	        String giaphongngay = tableModel.getValueAt(row, 4).toString();
	        
	        // Tạo các trường nhập liệu với dữ liệu ban đầu
	        txttenLoaiPhong = new JTextField(tenLoaiPhong);
	        txtmota	= new JTextField(moTa);
	        txtgiaphonggio = new JTextField(giaphonggio);
	        txtgiaphongngay = new JTextField(giaphongngay);
	        
	        // Sắp xếp các trường nhập liệu trong một panel
	        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
	        panel.add(new JLabel("Tên loại phòng:"));
	        panel.add(txttenLoaiPhong);
	        panel.add(new JLabel("Mô tả loại phòng:"));
	        panel.add(txtmota);
	        panel.add(new JLabel("Giá phòng theo giờ:"));
	        panel.add(txtgiaphonggio);
	        panel.add(new JLabel("Giá phòng theo ngày:"));
	        panel.add(txtgiaphongngay);
	        
	        int result = JOptionPane.showConfirmDialog(
	                this, panel, "Cập nhật thông tin loại phòng",
	                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	        
	        if (result == JOptionPane.OK_OPTION) {
	            // Cập nhật thông tin vào bảng
	            tableModel.setValueAt(tenPhongField.getText(), row, 0);
	            tableModel.setValueAt(tenKhachHangField.getText(), row, 1);
	            tableModel.setValueAt(sdtField.getText(), row, 2);
	            tableModel.setValueAt(kieuThueField.getText(), row, 3);
	            LoaiPhong lphpng = new LoaiPhong(tableModel.getValueAt(row, 0).toString(), tenLoaiPhong, moTa, Double.parseDouble(giaphonggio),Double.parseDouble(giaphonggio));
	            // Ở ứng dụng thực tế, bạn có thể cập nhật thêm các thông tin ngày giờ đặt/trả vào cơ sở dữ liệu hoặc một model riêng.
	            daolphong.capnhatLoaiPhong(lphpng);
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
			    addDialog.setSize(300, 400);
			    addDialog.setLayout(new GridLayout(6, 2, 10, 10));
			    addDialog.setLocationRelativeTo(this);

			    JLabel lblName = new JLabel("Tên khuyến mãi:");
			    JTextField txtName = new JTextField();

			    JLabel lblmota = new JLabel("Ngày áp dụng:");
			    JTextField txtmota = new JTextField();
			    JLabel lblgiagio = new JLabel("Ngày hết hạn:");
			    JTextField txtgiagio = new JTextField();
			    JLabel lblgiangay = new JLabel("Tiền áp dụng:");
			    JTextField txtngay = new JTextField();
			    JLabel lblkm = new JLabel("Phần trăm khuyến mãi:");
			    JTextField txtkm = new JTextField();
			    JButton btnSave = new JButton("Lưu");
			    JButton btnCancel = new JButton("Hủy");

			    addDialog.add(lblName);
			    addDialog.add(txtName);
			    addDialog.add(lblmota);
			    addDialog.add(txtmota);
			    addDialog.add(lblgiagio);
			    addDialog.add(txtgiagio);
			    addDialog.add(lblgiangay);
			    addDialog.add(txtngay);
			    addDialog.add(lblkm);
			    addDialog.add(txtkm);
			    addDialog.add(btnSave);
			    addDialog.add(btnCancel);

			    btnSave.addActionListener(new ActionListener() {
			        public void actionPerformed(ActionEvent e) {
			            String name = txtName.getText().trim();
			            String mota = txtmota.getText().trim();
			            Double giaphonggio= Double.parseDouble(txtgiagio.getText().trim());
			            Double giaphongngay= Double.parseDouble(txtngay.getText().trim());
			            if (name.isEmpty()) {
			                JOptionPane.showMessageDialog(addDialog, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			                return;
			            }

			            try {
			            	String malphong=daolphong.taomaLP(dslphong);
			            	LoaiPhong lphong = new LoaiPhong(mota, name, mota, giaphonggio, giaphongngay);
			                daolphong.themLoaiPhong(lphong);
			                
		

			                JOptionPane.showMessageDialog(addDialog, "Thêm loại phòng thành công!");
			                addDialog.dispose();

			                loadDataFromDatabase();; // Tải lại bảng sau khi thêm
			            } catch (NumberFormatException ex) {
			                JOptionPane.showMessageDialog(addDialog, "Giá phòng không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
			dskm = daokm.getDatabase();

		    tableModel.setRowCount(0); // Xóa dữ liệu cũ
		    originalData.clear();      // Xóa dữ liệu gốc

		    for (KhuyenMai pdp : dskm) {
		        Object[] row = new Object[]{
		            pdp.getMaKM(),
		            pdp.getTenKM(),
		            pdp.getNgayApDung(),
		            pdp.getNgayHetHan(),
		            formatCurrencyVND(pdp.getTienApdungKM()),
		            formatPercent(pdp.getPhanTramKM())
		        };
		        tableModel.addRow(row);
		        originalData.add(row); // Lưu dữ liệu để reset/tìm kiếm
		    }
		}
		private String formatCurrencyVND(double amount) {
	        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
	        return formatter.format(amount) + " VND";
	    }
		private String formatPercent(double value) {
	        NumberFormat percentFormat = NumberFormat.getPercentInstance();
	        percentFormat.setMinimumFractionDigits(2); // giữ 2 chữ số sau dấu thập phân
	        percentFormat.setMaximumFractionDigits(2);
	        return percentFormat.format(value);
	    }
}

