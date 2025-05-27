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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import dao.DaoCTPhieuDP;
import dao.DaoKhachHang;
import dao.DaoKhuyenMai;
import dao.DaoLoaiPhong;
import dao.DaoNhanVien;
import dao.DaoPhieuDP;
import entity.ChiTietPhieuDatPhong;
import entity.DichVu;
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
	        String maKM = tableModel.getValueAt(row, 0).toString();
	        KhuyenMai khuyenmai= daokm.getKhuyenMaitheoMa(maKM);
	        String tenKM = tableModel.getValueAt(row, 1).toString();
	        String ngayApDungStr = tableModel.getValueAt(row, 2).toString();
	        String ngayHetHanStr = tableModel.getValueAt(row, 3).toString();
	        // Trường nhập liệu
	        JTextField txtTenKM = new JTextField(tenKM);
	        JTextField txtTienApDung = new JTextField(khuyenmai.getTienApdungKM().toString());
	        JTextField txtPhanTram = new JTextField(khuyenmai.getPhanTramKM().toString());

	        // Spinner chọn ngày
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        Date ngayApDung = new Date();
	        Date ngayHetHan = new Date();

	        try {
	            ngayApDung = dateFormat.parse(ngayApDungStr);
	            ngayHetHan = dateFormat.parse(ngayHetHanStr);
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }

	        JSpinner spinnerApDung = new JSpinner(new SpinnerDateModel(ngayApDung, null, null, Calendar.DAY_OF_MONTH));
	        JSpinner spinnerHetHan = new JSpinner(new SpinnerDateModel(ngayHetHan, null, null, Calendar.DAY_OF_MONTH));
	        JSpinner.DateEditor editorApDung = new JSpinner.DateEditor(spinnerApDung, "yyyy-MM-dd");
	        spinnerApDung.setEditor(editorApDung);
	        JSpinner.DateEditor editorHetHan = new JSpinner.DateEditor(spinnerHetHan, "yyyy-MM-dd");
	        spinnerHetHan.setEditor(editorHetHan);

	        // Panel hiển thị form
	        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
	        panel.add(new JLabel("Tên khuyến mãi:"));
	        panel.add(txtTenKM);
	        panel.add(new JLabel("Ngày áp dụng:"));
	        panel.add(spinnerApDung);
	        panel.add(new JLabel("Ngày hết hạn:"));
	        panel.add(spinnerHetHan);
	        panel.add(new JLabel("Tiền áp dụng:"));
	        panel.add(txtTienApDung);
	        panel.add(new JLabel("Phần trăm khuyến mãi:"));
	        panel.add(txtPhanTram);

	        int result = JOptionPane.showConfirmDialog(
	                null, panel, "Cập nhật khuyến mãi",
	                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

	        if (result == JOptionPane.OK_OPTION) {
	            // Cập nhật lại dữ liệu trên bảng
	            tableModel.setValueAt(txtTenKM.getText(), row, 1);
	            tableModel.setValueAt(dateFormat.format((Date) spinnerApDung.getValue()), row, 2);
	            tableModel.setValueAt(dateFormat.format((Date) spinnerHetHan.getValue()), row, 3);
	            tableModel.setValueAt(txtTienApDung.getText(), row, 4);
	            tableModel.setValueAt(txtPhanTram.getText(), row, 5);

	            // Gọi DAO để cập nhật cơ sở dữ liệu nếu cần
	            KhuyenMai km = new KhuyenMai(
	                    maKM,
	                    txtTenKM.getText(),
	                    (LocalDateTime) spinnerApDung.getValue(),
	                    (LocalDateTime) spinnerHetHan.getValue(),
	                    Double.parseDouble(txtTienApDung.getText()),
	                    Double.parseDouble(txtPhanTram.getText())
	            );
	            daokm.capNhatKhuyenMai(km); // cần có lớp DAO với phương thức tương ứng
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
			    openAddKhuyenMai();
			}
		}

		private void deleteRow() {
			// TODO Auto-generated method stub
			int selectedRow = table.getSelectedRow();

		    if (selectedRow == -1) {
		        JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
		        return;
		    }

		    int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
		    if (confirm == JOptionPane.YES_OPTION) {
		    	String makm= tableModel.getValueAt(selectedRow,0).toString();
		        tableModel.removeRow(selectedRow);
		        if(daokm.xoaKhuyenMai(makm)) {
		        	JOptionPane.showConfirmDialog(null, "Xóa khuyến mãi thành công ");
		        }else {
		        	JOptionPane.showConfirmDialog(null, "Xóa khuyến mãi không thành công ");
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
		private void openAddKhuyenMai() {
			JDialog addDialog = new JDialog();
			addDialog.setTitle("Thêm Khuyến Mãi");
			addDialog.setSize(400, 300);
			addDialog.setLocationRelativeTo(this); // Center dialog

			// Tạo content panel với GridLayout và padding
			JPanel contentPanel = new JPanel(new GridLayout(0, 2, 10, 10));
			contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

			// Các thành phần giao diện
			JLabel lblName = new JLabel("Tên khuyến mãi:");
			JTextField txtName = new JTextField();

			// Thay thế bằng JDateChooser
			JLabel lblNgayApDung = new JLabel("Ngày áp dụng:");
			JDateChooser dateChooserApDung = new JDateChooser();
			dateChooserApDung.setDate(new Date()); // Gán mặc định ngày hiện tại

			JLabel lblNgayHetHan = new JLabel("Ngày hết hạn:");
			JDateChooser dateChooserHetHan = new JDateChooser();
			dateChooserHetHan.setDate(new Date()); // Gán mặc định ngày hiện tại

			JLabel lblTienApDung = new JLabel("Tiền áp dụng:");
			JTextField txtTienApDung = new JTextField();

			JLabel lblPhanTram = new JLabel("Phần trăm khuyến mãi:");
			JTextField txtPhanTram = new JTextField();

			JButton btnSave = new JButton("Lưu");
			JButton btnCancel = new JButton("Hủy");

			// Thêm thành phần vào content panel
			contentPanel.add(lblName);           contentPanel.add(txtName);
			contentPanel.add(lblNgayApDung);     contentPanel.add(dateChooserApDung);
			contentPanel.add(lblNgayHetHan);     contentPanel.add(dateChooserHetHan);
			contentPanel.add(lblTienApDung);     contentPanel.add(txtTienApDung);
			contentPanel.add(lblPhanTram);       contentPanel.add(txtPhanTram);
			contentPanel.add(btnSave);           contentPanel.add(btnCancel);

			// Gán panel vào dialog và hiển thị
			addDialog.setContentPane(contentPanel);

		    btnSave.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		            String name = txtName.getText().trim();
		            java.util.Date ngayADUtil = dateChooserApDung.getDate();
		            java.util.Date ngayHHUtil = dateChooserHetHan.getDate();

		            // Kiểm tra chuỗi rỗng
		            if (name.isEmpty()) {
		                JOptionPane.showMessageDialog(addDialog, "Tên khuyến mãi không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
		           
		            }

		            // Kiểm tra ngày
		            if (ngayADUtil == null || ngayHHUtil == null) {
		                JOptionPane.showMessageDialog(addDialog, "Vui lòng chọn ngày áp dụng và ngày hết hạn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
		             
		            }

		            // Chuyển sang LocalDateTime để dễ so sánh
		            LocalDateTime ngayApDung = ngayADUtil.toInstant()
		            	    .atZone(ZoneId.systemDefault())
		            	    .toLocalDateTime()
		            	    .truncatedTo(ChronoUnit.MINUTES);

		            	LocalDateTime ngayHetHan = ngayHHUtil.toInstant()
		            	    .atZone(ZoneId.systemDefault())
		            	    .toLocalDateTime()
		            	    .truncatedTo(ChronoUnit.MINUTES);
		            if (!ngayApDung.isBefore(ngayHetHan)) {
		                JOptionPane.showMessageDialog(addDialog, "Ngày áp dụng phải trước ngày hết hạn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
		              
		            }

		            // Kiểm tra tiền và phần trăm
		            double tienapdung = Double.parseDouble(txtTienApDung.getText().trim());
		            double phantram = Double.parseDouble(txtPhanTram.getText().trim());
		            try {
		                if (tienapdung < 50000) {
		                    JOptionPane.showMessageDialog(addDialog, "Tiền áp dụng phải lớn hơn hoặc bằng 50,000!", "Lỗi", JOptionPane.ERROR_MESSAGE);
		                 
		                }
		                if (phantram < 0 || phantram > 1) {
		                    JOptionPane.showMessageDialog(addDialog, "Phần trăm khuyến mãi phải nằm trong khoảng từ 0 đến 1!", "Lỗi", JOptionPane.ERROR_MESSAGE);
		                
		                }
		            } catch (NumberFormatException ex) {
		                JOptionPane.showMessageDialog(addDialog, "Tiền áp dụng và phần trăm phải là số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
		            }

		            try {
		                String makm = daokm.taomaDV(dskm);
		                KhuyenMai km = new KhuyenMai(makm, name, ngayApDung, ngayHetHan, tienapdung, phantram);
		                boolean kiem = daokm.themKhuyenMai(km);
		                if (kiem) {
		                    JOptionPane.showMessageDialog(addDialog, "Thêm khuyến mãi thành công!");
		                    addDialog.dispose();
		                } else {
		                    JOptionPane.showMessageDialog(addDialog, "Thêm khuyến mãi không thành công!");
		                }
		                loadDataFromDatabase();
		            } catch (Exception ex) {
		                JOptionPane.showMessageDialog(addDialog, "Đã xảy ra lỗi khi thêm khuyến mãi!", "Lỗi", JOptionPane.ERROR_MESSAGE);
		                ex.printStackTrace();
		            }
		        }
		    });

		    btnCancel.addActionListener(e1 -> addDialog.dispose());

		    addDialog.setVisible(true);
		}
		
}

