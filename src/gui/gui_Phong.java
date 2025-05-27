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
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ComboBoxModel;
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

import dao.DaoCTPhieuDP;
import dao.DaoKhachHang;
import dao.DaoLoaiPhong;
import dao.DaoNhanVien;
import dao.DaoPhieuDP;
import dao.DaoPhong;
import entity.ChiTietPhieuDatPhong;
import entity.LoaiPhong;
import entity.PhieuDatPhong;
import entity.Phong;

public class gui_Phong extends JPanel implements ActionListener {
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
		private JTextField txttenPhong;
		private JTextField txtsogiuong;
		private JTextField txthesogiatheogiuong;
		private JTextField txttrangthai;
		private JTextField txtsotang;
		private List<Phong> dsphong;
		

	    public gui_Phong() {
	    	daolphong= new DaoLoaiPhong();
	    	daophong= new DaoPhong();
	    	Font font = new Font("Arial",Font.BOLD, 16);
	        setLayout(new BorderLayout());
	        JPanel headerPanel = new JPanel();
	        originalData = new ArrayList<Object[]>();
	        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
	        JLabel lblSearch = new JLabel("Nhập tên phòng :");
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
	        tableModel = new DefaultTableModel(new Object[]{"Mã phòng","Tên phòng","Tên loại phòng", "Số giường", "Hệ số giá theo giường","Trạng thái hiện tại","Số tầng"}, 0) {
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
	        tableModel.setRowCount(0);
	        loadDataFromDatabase();
	        scrollPane.setViewportView(table);
	    }
	    
	    /** 
	     * Phương thức mở cửa sổ dialog cập nhật thông tin cho dòng được chọn.
	     * @param row chỉ số dòng trong bảng
	     */
	    private void openUpdateDialog(int row) {
	        // Lấy thông tin hiện tại từ bảng
	    	dslphong=daolphong.getDatabase();
	    	String maPhong= tableModel.getValueAt(row,0).toString();
	        String tenPhong = (String) tableModel.getValueAt(row, 1);
	        String tenLoaiPhong = (String) tableModel.getValueAt(row, 2);
	        String sogiuong = tableModel.getValueAt(row, 3).toString();
	        String hesogiaphong = tableModel.getValueAt(row, 4).toString();
	        String trangthai= tableModel.getValueAt(row, 5).toString();
	        String sotang= tableModel.getValueAt(row, 6).toString();
	        // Tạo các trường nhập liệu với dữ liệu ban đầu
	        txttenPhong = new JTextField(tenPhong);
	        //txttenLoaiPhong	= new JTextField(tenLoaiPhong);
	        Set<String> uniqueMaLoaiPhong = new HashSet<>();
		    for (LoaiPhong lp : dslphong) {
		        uniqueMaLoaiPhong.add(lp.getTenLoaiP());
		    }
	        JComboBox<String> cdlphong1 = new JComboBox<>();
		    for (String ma : uniqueMaLoaiPhong) {
		        cdlphong1.addItem(ma);
		    }
		    cdlphong1.setSelectedItem(tenLoaiPhong);
	        txtsogiuong = new JTextField(sogiuong);
	        txthesogiatheogiuong = new JTextField(hesogiaphong);
		    String[] trangThaiOptions = {"Trống", "Đang sử dụng", "Đã đặt"};
		    JComboBox<String> cboTrangThai = new JComboBox<>(trangThaiOptions);
		    cboTrangThai.setSelectedItem(trangthai);
	        txtsotang = new JTextField(sotang);
	        // Sắp xếp các trường nhập liệu trong một panel
	        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
	        panel.add(new JLabel("Tên phòng:"));
	        panel.add(txttenPhong);
	        panel.add(new JLabel("Tên loại phòng:"));
	        panel.add(cdlphong1);
	        panel.add(new JLabel("Số giường:"));
	        panel.add(txtsogiuong);
	        panel.add(new JLabel("Hệ số giá:"));
	        panel.add(txthesogiatheogiuong);
	        panel.add(new JLabel("Trạng thái:"));
	        panel.add(cboTrangThai);
	        panel.add(new JLabel("Tầng:"));
	        panel.add(txtsotang);
	        
	        int result = JOptionPane.showConfirmDialog(
	                this, panel, "Cập nhật thông tin loại phòng",
	                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	        
	        if (result == JOptionPane.OK_OPTION) {
	        	LoaiPhong lphong_new= daolphong.getLoaiPhongTheoTen(cdlphong1.getSelectedItem().toString());
	        	String tenPhong_new=txttenPhong.getText().trim();
	        	String tenLoaiPhong_new = cdlphong1.getSelectedItem().toString();
	        	Integer sogiuong_new = Integer.parseInt(txtsogiuong.getText().trim());
	        	Float giaphong_new= Float.parseFloat(txthesogiatheogiuong.getText().trim());
	        	String trangthai_new= cboTrangThai.getSelectedItem().toString();
	        	Integer sotang_new= Integer.parseInt(txtsotang.getText().trim());
	            // Cập nhật thông tin vào bảng
	            tableModel.setValueAt(txttenPhong.getText(), row, 1);
	            tableModel.setValueAt(cdlphong1.getSelectedItem().toString(), row, 2);
	            tableModel.setValueAt(txtsogiuong.getText(), row, 3);
	            tableModel.setValueAt(txthesogiatheogiuong.getText(), row, 4);
	            tableModel.setValueAt(cboTrangThai.getSelectedItem().toString(), row, 5);
	            tableModel.setValueAt(txtsotang.getText(), row, 6);
	            Phong phong= new Phong(maPhong, tenPhong_new, lphong_new, sogiuong_new, giaphong_new, trangthai_new, sotang_new);
	            
	            // Ở ứng dụng thực tế, bạn có thể cập nhật thêm các thông tin ngày giờ đặt/trả vào cơ sở dữ liệu hoặc một model riêng.
	            boolean capnhat=daophong.capnhattPhong(phong);
	            if(capnhat) {
	            	JOptionPane.showMessageDialog(null, "Cập nhật phòng thành công");
	            }else {
	            	JOptionPane.showMessageDialog(null, "Cập nhật phòng không thành công");
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
				addDialog.setSize(500, 400);
				addDialog.setLocationRelativeTo(this);
				addDialog.setTitle("Thêm phòng");
				// Tạo panel chính và set layout + padding
				JPanel mainPanel = new JPanel(new GridLayout(7, 2, 10, 10)); // 10 là khoảng cách giữa các thành phần
				mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Padding: top, left, bottom, right

				ArrayList<String> dsmalp = new ArrayList<String>();
				dslphong = daolphong.getDatabase();
				for (LoaiPhong lp : dslphong) {
				    dsmalp.add(lp.getMaLoaiP());
				}
				JLabel lblName = new JLabel("Tên phòng:");
				JTextField txtName = new JTextField();

				Set<String> uniqueMaLoaiPhong = new HashSet<>();
				for (LoaiPhong lp : dslphong) {
				    uniqueMaLoaiPhong.add(lp.getTenLoaiP());
				}

				JComboBox<String> cdlphong = new JComboBox<>();
				for (String ma : uniqueMaLoaiPhong) {
				    cdlphong.addItem(ma);
				}
				JLabel lblmalphong = new JLabel("Tên loại phòng:");
				JComboBox<String> cdlphong1 = new JComboBox<>();
				for (String ma : uniqueMaLoaiPhong) {
				    cdlphong1.addItem(ma);
				}
				JLabel lblsogiuong = new JLabel("Số giường:");
				JTextField txtsogiuong = new JTextField();
				JLabel lblheso = new JLabel("Hệ số giá phòng theo giường:");
				JTextField txtheso = new JTextField();
				JLabel lbltrangthai = new JLabel("Trạng thái ban đầu:");
				String[] trangThaiOptions = {"Trống", "Đang sử dụng", "Đã đặt"};
				JComboBox<String> cboTrangThai = new JComboBox<>(trangThaiOptions);
				JLabel lblsotang = new JLabel("Số tầng:");
				JTextField txtsotang = new JTextField();
				JButton btnSave = new JButton("Thêm");
				JButton btnCancel = new JButton("Hủy");

				// Thêm các thành phần vào mainPanel
				mainPanel.add(lblName);
				mainPanel.add(txtName);
				mainPanel.add(lblmalphong);
				mainPanel.add(cdlphong1);
				mainPanel.add(lblsogiuong);
				mainPanel.add(txtsogiuong);
				mainPanel.add(lblheso);
				mainPanel.add(txtheso);
				mainPanel.add(lbltrangthai);
				mainPanel.add(cboTrangThai);
				mainPanel.add(lblsotang);
				mainPanel.add(txtsotang);
				mainPanel.add(btnSave);
				mainPanel.add(btnCancel);

				// Thêm panel vào dialog
				addDialog.setContentPane(mainPanel);

			    btnSave.addActionListener(new ActionListener() {
			        public void actionPerformed(ActionEvent e) {
			            String name = txtName.getText().trim();
			            String malp = cdlphong.getSelectedItem().toString();
			            String sogiuong = txtsogiuong.getText().trim();
			            String heso = txtheso.getText().trim();
			            String trangThai = cboTrangThai.getSelectedItem().toString();
			            String tang = txtsotang.getText().trim();
			            if (name.isEmpty()) {
			                JOptionPane.showMessageDialog(addDialog, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			                return;
			            }
			            try {
			            	String maphong=daophong.taomaP(dsphong);
			            	LoaiPhong lphong = daolphong.getLoaiPhongTheoMa(maphong);
			            	Phong phong= new Phong(maphong, name, lphong, Integer.parseInt(sogiuong),Float.parseFloat(heso), trangThai, Integer.parseInt(tang));
			                daophong.themPhong(phong);
			                JOptionPane.showMessageDialog(addDialog, "Thêm phòng thành công!");
			                addDialog.dispose();
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
			dsphong = daophong.getDatabase();

		    tableModel.setRowCount(0); // Xóa dữ liệu cũ
		    originalData.clear();      // Xóa dữ liệu gốc
		    
		    for (Phong pdp : dsphong) {
		        Object[] row = new Object[]{
		            pdp.getMaPhong(),
		            pdp.getTenPhong(),
		            pdp.getLoaiPhong().getTenLoaiP(),
		            pdp.getSoGiuong(),
		            pdp.getGiaPhong(),
		            pdp.getTrangThai(),
		            pdp.getTang()
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

