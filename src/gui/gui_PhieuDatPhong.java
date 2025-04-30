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
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import dao.DaoNhanVien;
import dao.DaoPhieuDP;
import dao.DaoPhong;
import entity.ChiTietPhieuDatPhong;
import entity.KhachHang;
import entity.NhanVien;
import entity.PhieuDatPhong;
import entity.Phong;

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
		private DaoPhieuDP daoPDP;
		private DaoCTPhieuDP daoCTPDP;
		private DaoKhachHang daoKH;
		private DaoNhanVien daoNV;
		private JTextField giaPhongField;
		private JDateChooser dateNhan;
		private JDateChooser dateTra;

	    public gui_PhieuDatPhong() {
	    	daoPDP= new DaoPhieuDP();
	    	daoCTPDP= new DaoCTPhieuDP();
	    	daoKH= new DaoKhachHang();
	    	daoNV= new DaoNhanVien();
	    	Font font = new Font("Arial",Font.BOLD, 16);
	        setLayout(new BorderLayout());
	        JPanel headerPanel = new JPanel();
	        originalData = new ArrayList<Object[]>();
	        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
	        JLabel lblSearch = new JLabel("Nhập mã phiếu:");
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
	        headerPanel.add(btnDelete);
	        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 0));
	        add(headerPanel,BorderLayout.NORTH);
	        // Khởi tạo model cho bảng với các cột cần thiết
	        tableModel = new DefaultTableModel(new Object[]{"Mã phiếu đặt phòng","Tên nhân viên","Tên phòng", "Tên khách hàng", "SĐT khách hàng","Giá phòng đã đặt"}, 0) {
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
	                        loadDataFromDatabase();
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
	    	String tenPhong = (String) tableModel.getValueAt(row, 2);
	    	String tenKhachHang = (String) tableModel.getValueAt(row, 3);
	    	String sdt = (String) tableModel.getValueAt(row, 4);
	    	String kieuThue = (String) tableModel.getValueAt(row, 3);
	    	String giaPhong = tableModel.getValueAt(row, 5).toString();
	    	String maPhieuDP= tableModel.getValueAt(row, 0).toString();
	    	String tenNhanVien= tableModel.getValueAt(row, 1).toString();
	    	DaoKhachHang daokh= new DaoKhachHang();
	    	KhachHang kh= daokh.getKhachHangtheoSDT(sdt);
	    	DaoNhanVien daonv= new DaoNhanVien();
	    	NhanVien nv= daonv.getNhanVienTheoTen(tenNhanVien);
	    	// Tạo các trường nhập liệu
	    	tenPhongField = new JTextField(tenPhong);
	    	tenKhachHangField = new JTextField(tenKhachHang);
	    	sdtField = new JTextField(sdt);
	    	giaPhongField = new JTextField(giaPhong);
	    	DaoCTPhieuDP daoctpdp= new DaoCTPhieuDP();
	    	ChiTietPhieuDatPhong ctpdp= daoctpdp.getCTPDPtheoMaPDP(maPhieuDP);
	    	LocalDateTime ngayNhan=ctpdp.getGioDatPhong();
	    	LocalDateTime ngayTra=ctpdp.getGioTraPhong();
	    	// ComboBox kiểu thuê
	    	String[] kieuThueOptions =  {"Theo ngày"};
	    	JComboBox<String> kieuThueComboBox = new JComboBox<>(kieuThueOptions);
	    	kieuThueComboBox.setSelectedItem(kieuThue);

	    	// DateChooser ngày nhận và ngày trả
	    	dateNhan = new JDateChooser();
	    	dateTra = new JDateChooser();

	    	// Gán giá trị ngày vào JDateChooser
	    	dateNhan.setDate(Date.from(ngayNhan.atZone(ZoneId.systemDefault()).toInstant()));
	    	dateTra.setDate(Date.from(ngayTra.atZone(ZoneId.systemDefault()).toInstant()));

	    	// Gán giá trị giờ vào JSpinner
	    	// Spinner cho ngày nhận
	    	// Spinner cho ngày nhận
	    	Calendar calendarNhan = Calendar.getInstance();
	    	calendarNhan.set(Calendar.YEAR, ngayNhan.getYear());
	    	calendarNhan.set(Calendar.MONTH, ngayNhan.getMonthValue() - 1); // Calendar.MONTH bắt đầu từ 0 (JANUARY=0)
	    	calendarNhan.set(Calendar.DAY_OF_MONTH, ngayNhan.getDayOfMonth());
	    	calendarNhan.set(Calendar.HOUR_OF_DAY, ngayNhan.getHour());
	    	calendarNhan.set(Calendar.MINUTE, ngayNhan.getMinute());
	    	calendarNhan.set(Calendar.SECOND, 0);
	    	calendarNhan.set(Calendar.MILLISECOND, 0);

	    	JSpinner spinnerGioNhan = new JSpinner(new SpinnerDateModel());
	    	spinnerGioNhan.setValue(calendarNhan.getTime());

	    	// Spinner cho ngày trả
	    	Calendar calendarTra = Calendar.getInstance();
	    	calendarTra.set(Calendar.YEAR, ngayTra.getYear());
	    	calendarTra.set(Calendar.MONTH, ngayTra.getMonthValue() - 1);
	    	calendarTra.set(Calendar.DAY_OF_MONTH, ngayTra.getDayOfMonth());
	    	calendarTra.set(Calendar.HOUR_OF_DAY, ngayTra.getHour());
	    	calendarTra.set(Calendar.MINUTE, ngayTra.getMinute());
	    	calendarTra.set(Calendar.SECOND, 0);
	    	calendarTra.set(Calendar.MILLISECOND, 0);

	    	JSpinner spinnerGioTra = new JSpinner(new SpinnerDateModel());
	    	spinnerGioTra.setValue(calendarTra.getTime());


	    	// ====== BẮT ĐẦU GHÉP PANEL ======

	    	// Panel thông tin phòng
	    	JPanel panelPhong = new JPanel(new GridLayout(2, 2, 5, 5));
	    	panelPhong.setBorder(BorderFactory.createTitledBorder("Thông tin phòng"));
	    	panelPhong.add(new JLabel("Tên phòng:"));
	    	panelPhong.add(tenPhongField);
	    	panelPhong.add(new JLabel("Giá phòng:"));
	    	panelPhong.add(giaPhongField);

	    	// Panel thông tin khách
	    	JPanel panelKhach = new JPanel(new GridLayout(3, 2, 5, 5));
	    	panelKhach.setBorder(BorderFactory.createTitledBorder("Thông tin khách"));
	    	panelKhach.add(new JLabel("Tên khách hàng:"));
	    	panelKhach.add(tenKhachHangField);
	    	panelKhach.add(new JLabel("SĐT:"));
	    	panelKhach.add(sdtField);
	    	panelKhach.add(new JLabel("Kiểu thuê:"));
	    	panelKhach.add(kieuThueComboBox);

	    	// Panel thời gian đặt
	    	JPanel panelThoiGian = new JPanel(new GridLayout(2, 2, 5, 5));
	    	panelThoiGian.setBorder(BorderFactory.createTitledBorder("Thời gian đặt phòng"));
	    	panelThoiGian.add(new JLabel("Ngày/giờ nhận:"));
	    	panelThoiGian.add(spinnerGioNhan);
	    	panelThoiGian.add(new JLabel("Ngày/giờ trả:"));
	    	panelThoiGian.add(spinnerGioTra);

	    	// Panel tổng
	    	JPanel panelTong = new JPanel();
	    	panelTong.setLayout(new BoxLayout(panelTong, BoxLayout.Y_AXIS));
	    	panelTong.add(panelPhong);
	    	panelTong.add(panelKhach);
	    	panelTong.add(panelThoiGian);
	    	LocalDateTime localDateTimeNhan = ((Date) spinnerGioNhan.getValue()).toInstant()
	    		    .atZone(ZoneId.systemDefault())
	    		    .toLocalDateTime();

	    		LocalDateTime localDateTimeTra = ((Date) spinnerGioTra.getValue()).toInstant()
	    		    .atZone(ZoneId.systemDefault())
	    		    .toLocalDateTime();
	        
	        int result = JOptionPane.showConfirmDialog(
	                this, panelTong, "Cập nhật thông tin đặt phòng",
	                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	        
	        if (result == JOptionPane.OK_OPTION) {
	            // Cập nhật thông tin vào bảng
	            tableModel.setValueAt(maPhieuDP, row, 0);
	            tableModel.setValueAt(tenNhanVien, row, 1);
	            tableModel.setValueAt(tenPhong, row, 2);
	            tableModel.setValueAt(kh.getTenKH(), row, 3);
	            tableModel.setValueAt(kh.getSoDT(), row, 4);
	            tableModel.setValueAt(ctpdp.getGiaPhongtheoKieuThue(), row, 5);
	            PhieuDatPhong pdp_new= new PhieuDatPhong(maPhieuDP, nv, kh);
	            DaoPhong daop= new DaoPhong();
	            Phong phong= daop.getPhongtheoTen(tenPhong);
	            boolean kiemtra1= daoPDP.capNhatPhieuDatPhong(pdp_new);
	            ChiTietPhieuDatPhong ctpdp_new= new ChiTietPhieuDatPhong(pdp_new,phong, localDateTimeNhan, localDateTimeTra, true);
	            boolean kiemtra2=daoctpdp.capNhatCTPhieuDatPhong(ctpdp_new);
	            if(kiemtra1 && kiemtra2) {
	            	JOptionPane.showMessageDialog(this,"Cập nhật phiếu đặt phòng thành công");
	            }else{
	            	JOptionPane.showMessageDialog(this,"Cập nhật phiếu đặt phòng không thành công");
	            }
	        }
	    }

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Object o= e.getSource();
			if(o.equals(btnSearch)) {
				timkiemPhong();
				loadDataFromDatabase();
			}
			if(o.equals(btnReset)) {
				reloadData();
			}
			if(o.equals(btnDelete)) {
				deleteRow();
				loadDataFromDatabase();
			}
		}

		private void deleteRow() {
			// TODO Auto-generated method stub
			int selectedRow = table.getSelectedRow();
			String ma=(String) tableModel.getValueAt(selectedRow, 0);
		    if (selectedRow == -1) {
		        JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
		        return;
		    }

		    int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn hủy phiếu đặt phòng này?", "Xác nhận hủy", JOptionPane.YES_NO_OPTION);
		    if (confirm == JOptionPane.YES_OPTION) {
		    	daoCTPDP.xoaCTPhieuDatPhongTheoMaPDP(ma);
		    	daoPDP.xoaPhieuDatPhongTheoMaPDP(ma);
		        tableModel.removeRow(selectedRow);
		        JOptionPane.showMessageDialog(this, "Đã hủy phòng thành công!");
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
		    daoCTPDP= new DaoCTPhieuDP();
		    List<ChiTietPhieuDatPhong> dsctpdp= daoCTPDP.getDatabaseThueNgay();

		    tableModel.setRowCount(0); // Xóa dữ liệu cũ
		    originalData.clear();      // Xóa dữ liệu gốc

		    for (ChiTietPhieuDatPhong pdp : dsctpdp) {
		        Object[] row = new Object[]{
		            pdp.getPhietDP().getMaPDP(),
		            pdp.getPhietDP().getNhanvien().getTenNV(),
		            pdp.getPhong().getTenPhong(),
		            pdp.getPhietDP().getKhachhang().getTenKH(),
		            pdp.getPhietDP().getKhachhang().getSoDT(),
		            formatCurrencyVND(pdp.getGiaPhongtheoKieuThue()),
		        };
		        tableModel.addRow(row);
		        originalData.add(row); // Lưu dữ liệu để reset/tìm kiếm
		    }
		    
		}
		private String formatCurrencyVND(double amount) {
		    NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
		    formatter.setMaximumFractionDigits(0); // Không hiển thị phần thập phân
		    formatter.setRoundingMode(RoundingMode.HALF_UP); // Làm tròn lên hoặc xuống
		    return formatter.format(amount) + " VND";
		}
}
