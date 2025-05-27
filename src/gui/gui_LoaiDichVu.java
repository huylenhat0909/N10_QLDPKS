package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dao.DaoDichVu;
import dao.DaoLoaiDV;
import dao.DaoLoaiPhong;
import entity.DichVu;
import entity.LoaiDichVu;
import entity.LoaiPhong;

public class gui_LoaiDichVu extends JPanel implements ActionListener{
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

	private JButton btnAdd;
	private JTable tableService;
	private DefaultTableModel tableServiceModel;
	private DaoDichVu daodv;
	private DaoLoaiDV daoldv;
	private List<DichVu> dsdv;
	private ArrayList<Object[]> originalData1;
	private List<LoaiDichVu> dsldv;
	private JTextField txtSearchdv;
	private JButton btnSearchdv;
	private JButton btnDeletedv;
	private JButton btnResetdv;
	private JButton btnAdddv;
	private JTextField txtDV;
	private JTextField txtLDV;
	public gui_LoaiDichVu() {
		daodv= new DaoDichVu();
		daoldv= new DaoLoaiDV();
	    daolphong = new DaoLoaiPhong();
	    Font font = new Font("Arial", Font.BOLD, 16);
	    setLayout(new BorderLayout());

	    JPanel headerPanel = new JPanel();
	    originalData = new ArrayList<Object[]>();
	    headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
	    JLabel lblSearch = new JLabel("Nhập tên loại dịch vụ:");
	    lblSearch.setFont(font);
	    txtSearch = new JTextField(15);
	    txtSearch.setPreferredSize(new Dimension(30, 30));
	    txtSearch.setFont(font);
	    btnSearch = new JButton("Tìm");
	    btnReset = new JButton("Tải lại");
	    btnDelete = new JButton("Xóa");
	    btnAdd = new JButton("Thêm");
	    btnSearch.setFont(font);
	    btnReset.setFont(font);
	    btnDelete.setFont(font);
	    btnAdd.setFont(font);
	    headerPanel.add(lblSearch);
	    headerPanel.add(txtSearch);
	    headerPanel.add(btnSearch);
	    headerPanel.add(btnDelete);
	    headerPanel.add(btnAdd);
	    headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 0));
	    add(headerPanel, BorderLayout.NORTH);

	    // === BẢNG 1: LOẠI DỊCH VỤ ===
	    tableModel = new DefaultTableModel(new Object[]{"Mã loại dịch vụ", "Tên loại dịch vụ"}, 0) {
	        @Override
	        public boolean isCellEditable(int row, int column) {
	            return false;
	        }
	    };
	    table = new JTable(tableModel);
	    table.setFillsViewportHeight(true);
	    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    table.setFont(new Font("Arial", Font.PLAIN, 14));
	    table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
	    table.getTableHeader().setBackground(new Color(180, 0, 0));
	    table.getTableHeader().setForeground(Color.WHITE);
	    table.setRowHeight(25);

	    table.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            if (e.getClickCount() == 2) {
	                int selectedRow = table.getSelectedRow();
	                if (selectedRow != -1) {
	                    openUpdateDialogLDV(selectedRow);
	                }
	            }
	        }
	    });

	    JScrollPane scrollPaneLoaiPhong = new JScrollPane(table);
	    scrollPaneLoaiPhong.setPreferredSize(new Dimension(1000, 200));
	    JPanel headerPanel_dv = new JPanel();
	    headerPanel_dv.setLayout(new FlowLayout(FlowLayout.LEFT));
	    JLabel lblSearch_dv = new JLabel("Nhập tên dịch vụ:");
	    lblSearch_dv.setFont(font);
	    txtSearchdv = new JTextField(15);
	    txtSearchdv.setPreferredSize(new Dimension(30, 30));
	    txtSearchdv.setFont(font);
	    btnSearchdv = new JButton("Tìm");
	    btnResetdv = new JButton("Tải lại");
	    btnDeletedv = new JButton("Xóa");
	    btnAdddv = new JButton("Thêm");
	    btnSearchdv.setFont(font);
	    btnResetdv.setFont(font);
	    btnDeletedv.setFont(font);
	    btnAdddv.setFont(font);
	    headerPanel_dv.add(lblSearch_dv);
	    headerPanel_dv.add(txtSearchdv);
	    headerPanel_dv.add(btnSearchdv);
	    headerPanel_dv.add(btnDeletedv);
	    headerPanel_dv.add(btnAdddv);
	    headerPanel_dv.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 0));
	    // === BẢNG 2: DỊCH VỤ ===
	    tableServiceModel = new DefaultTableModel(new Object[]{"Mã dịch vụ", "Tên dịch vụ", "Giá tiền", "Trạng thái", "Mô tả", "Mã loại dịch vụ"}, 0) {
	        @Override
	        public boolean isCellEditable(int row, int column) {
	            return false;
	        }
	    };
	    tableService = new JTable(tableServiceModel);
	    tableService.setFillsViewportHeight(true);
	    tableService.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    tableService.setFont(new Font("Arial", Font.PLAIN, 14));
	    tableService.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
	    tableService.getTableHeader().setBackground(new Color(0, 102, 204));
	    tableService.getTableHeader().setForeground(Color.WHITE);
	    tableService.setRowHeight(25);

	    JScrollPane scrollPaneDichVu = new JScrollPane(tableService);
	    scrollPaneDichVu.setPreferredSize(new Dimension(1000, 180));

	    // === PANEL CHỨA CẢ 2 BẢNG ===
	    originalData1 = new ArrayList<Object[]>();
	    JPanel tablePanel = new JPanel();
	    tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
	    tablePanel.add(scrollPaneLoaiPhong);
	    tablePanel.add(headerPanel_dv);
	    tablePanel.add(scrollPaneDichVu);
	    tableService.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            if (e.getClickCount() == 2) {
	                int selectedRow = tableService.getSelectedRow();
	                if (selectedRow != -1) {
	                    openUpdateDialogDV(selectedRow);
	                }
	            }
	        }
	    });
	    add(tablePanel, BorderLayout.CENTER);

	    // === SỰ KIỆN ===
	    btnSearch.addActionListener(this);
	    btnReset.addActionListener(this);
	    btnDelete.addActionListener(this);
	    btnAdd.addActionListener(this);
	    btnSearchdv.addActionListener(this);
	    btnResetdv.addActionListener(this);
	    btnDeletedv.addActionListener(this);
	    btnAdddv.addActionListener(this);
	    // === LOAD DATA ===
	    loadDataFromDatabaseLDV();
	    loadDataFromDatabaseDV();  // hiện đang load bảng loại phòng, bạn có thể thêm loadDichVu() tương tự
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
					Object o= e.getSource();
					if(o.equals(btnSearch)) {
						timkiemPhong();
					}
					if(o.equals(btnDelete)) {
						deleteRow();
					}
					if (o.equals(btnAdd)) {
						JDialog addDialog = new JDialog();
						addDialog.setSize(350, 150);
						addDialog.setTitle("Thêm loại dịch vụ");
						addDialog.setLocationRelativeTo(this);

						// Tạo panel chính với BoxLayout theo chiều dọc
						JPanel mainPanel = new JPanel();
						mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
						mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Padding 15px

						// Tạo panel cho ô nhập tên
						JPanel namePanel = new JPanel(new BorderLayout(10, 10));
						JLabel lblName = new JLabel("Tên loại dịch vụ:");
						JTextField txtName = new JTextField();
						namePanel.add(lblName, BorderLayout.WEST);
						namePanel.add(txtName, BorderLayout.CENTER);

						 // Tạo panel cho 2 nút bấm
						JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
						JButton btnSave = new JButton("Thêm");
						JButton btnCancel = new JButton("Hủy");
						buttonPanel.add(btnSave);
						buttonPanel.add(btnCancel);

						    // Thêm vào panel chính
						mainPanel.add(namePanel);
						mainPanel.add(Box.createVerticalStrut(15)); // khoảng cách dọc
						mainPanel.add(buttonPanel);

						// Gán panel chính vào dialog
						addDialog.setContentPane(mainPanel);
					    btnSave.addActionListener(new ActionListener() {
					        public void actionPerformed(ActionEvent e) {
					        	daoldv= new DaoLoaiDV();
					        	
					        	String maldv= daoldv.taomaLDV(dsldv);
					            String name = txtName.getText().trim();
					            if (name.isEmpty()) {
					                JOptionPane.showMessageDialog(addDialog, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
					                return;
					            }

					            try {
					            	LoaiDichVu ldv= new LoaiDichVu(maldv, name);
					            	daoldv.themLoaiDV(ldv);
					                JOptionPane.showMessageDialog(addDialog, "Thêm loại dịch vụ thành công!");
					                addDialog.dispose();
					            } catch (NumberFormatException ex) {
					                JOptionPane.showMessageDialog(addDialog, "Loại dịch vụ không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
					            }
					        }
					    });

					    btnCancel.addActionListener(e1 -> addDialog.dispose());
					    addDialog.setVisible(true);
					}
					if (o.equals(btnAdddv)) {
						JDialog addDialog = new JDialog();
						addDialog.setTitle("Thêm dịch vụ");
						addDialog.setSize(400, 300);
						addDialog.setLocationRelativeTo(this);

						// Tạo panel chính và set padding
						JPanel contentPanel = new JPanel(new GridLayout(0, 2, 10, 10));
						contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // padding trái, trên, phải, dưới

						JLabel lblName = new JLabel("Tên dịch vụ:");
						JTextField txtName = new JTextField();

						JLabel lblgia = new JLabel("Giá tiền:");
						JTextField txtgia = new JTextField();

						JLabel lblmota = new JLabel("Mô tả:");
						JTextField txtmota = new JTextField();

						JLabel lblgiangay = new JLabel("Mã loại dịch vụ:");
						JComboBox<String> cbMaLoaiDichVu = new JComboBox<>();
						for (LoaiDichVu ldv: dsldv) {
						    cbMaLoaiDichVu.addItem(ldv.getTenDV());
						}

						JButton btnSave = new JButton("Thêm");
						JButton btnCancel = new JButton("Hủy");

						// Thêm vào contentPanel
						contentPanel.add(lblName);
						contentPanel.add(txtName);
						contentPanel.add(lblgia);
						contentPanel.add(txtgia);
						contentPanel.add(lblmota);
						contentPanel.add(txtmota);
						contentPanel.add(lblgiangay);
						contentPanel.add(cbMaLoaiDichVu);
						contentPanel.add(btnSave);
						contentPanel.add(btnCancel);

						// Gán contentPanel vào JDialog
						addDialog.setContentPane(contentPanel);
					    btnSave.addActionListener(new ActionListener() {
					        public void actionPerformed(ActionEvent e) {
					        	String name = txtName.getText().trim();
					            String giaStr = txtgia.getText().trim();
					            String moTa = txtmota.getText().trim();
					            String tenLoaiDV = (String) cbMaLoaiDichVu.getSelectedItem();
					            
					            if (name.isEmpty()) {
					                JOptionPane.showMessageDialog(addDialog, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
					                return;
					            }

					            try {
					            	double gia = Double.parseDouble(giaStr);
					            	String ma= daodv.taomaDV(dsdv);
					            	LoaiDichVu ldv= daoldv.getLoaiDVTheoTen(tenLoaiDV);
					                DichVu dv_new= new DichVu(ma, name, moTa, gia, true, ldv);
					                daodv.themDV(dv_new);
					                loadDataFromDatabaseDV();
					                JOptionPane.showMessageDialog(addDialog, "Thêm dịch vụ thành công!");
					                addDialog.dispose();

					                //loadDataFromDatabase();; // Tải lại bảng sau khi thêm
					            } catch (NumberFormatException ex) {
					                JOptionPane.showMessageDialog(addDialog, "Thêm dịch vụ không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
	    
	    int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa loại dịch vụ này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
	    if (confirm == JOptionPane.YES_OPTION) {
	        tableModel.removeRow(selectedRow);
	        String maldv= table.getValueAt(selectedRow, 0).toString();
		    String tenldv= table.getValueAt(selectedRow, 1).toString();
		    LoaiDichVu ldv= new LoaiDichVu(maldv, tenldv);
		    daoldv.xoaLoaiDV(ldv);
	        JOptionPane.showMessageDialog(this, "Đã xóa dòng thành công!");
	        loadDataFromDatabaseLDV();
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
	private void loadDataFromDatabaseDV() {
		dsdv = daodv.getDatabase();

	    tableServiceModel.setRowCount(0); // Xóa dữ liệu cũ
	    originalData1.clear();      // Xóa dữ liệu gốc
	    String trangthai="Hết";
	    for (DichVu pdp : dsdv) {
	    	if(pdp.getTrangThai()) {
	    		trangthai="Còn";
	    	}
	        Object[] row = new Object[]{
	            pdp.getMaDichVu(),
	            pdp.getTenDichVu(),
	            formatCurrencyVND(pdp.getGiaTien()),
	            trangthai,
	            pdp.getMoTa(),
	            pdp.getLoaiDV().getTenDV()
	        };
	        tableServiceModel.addRow(row);
	        originalData1.add(row); // Lưu dữ liệu để reset/tìm kiếm
	    }
	}
	private void loadDataFromDatabaseLDV() {
		dsldv = daoldv.getDatabase();

	    tableModel.setRowCount(0); // Xóa dữ liệu cũ
	    originalData.clear();      // Xóa dữ liệu gốc

	    for (LoaiDichVu pdp : dsldv) {
	        Object[] row = new Object[]{
	           pdp.getMaLoaiDV(),
	           pdp.getTenDV()
	        };
	        tableModel.addRow(row);
	        originalData.add(row); // Lưu dữ liệu để reset/tìm kiếm
	    }
	}
	private void openUpdateDialogLDV(int row) {
        // Lấy thông tin hiện tại từ bảng
		String maLDV = tableModel.getValueAt(row,0).toString();
        String tenDV = (String) tableModel.getValueAt(row, 1);
        
        // Tạo các trường nhập liệu với dữ liệu ban đầu
        txtLDV = new JTextField(tenDV);
        
        // Sắp xếp các trường nhập liệu trong một panel
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Tên loại dịch vụ:"));
        panel.add(txtLDV);
    
        
        int result = JOptionPane.showConfirmDialog(
                this, panel, "Cập nhật thông tin Dịch Vụ",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            // Cập nhật thông tin vào bảng
            tableModel.setValueAt(maLDV, row, 0);
            tableModel.setValueAt(txtLDV, row, 1);
            LoaiDichVu dv_new =new LoaiDichVu(maLDV, txtLDV.getText());
            if(daoldv.capnhatLoaiDichVu(dv_new)) {
            	JOptionPane.showMessageDialog(null,"Cập nhật loại dịch vụ thành công");
            	return;
            }else {
            	JOptionPane.showMessageDialog(null,"Cập nhật loại dịch vụ thất bại");
            	return;
            }
            }
            
    }
	private void openUpdateDialogDV(int row) {
		// Lấy thông tin hiện tại từ bảng
		String maDV = tableServiceModel.getValueAt(row, 0).toString();
		String tenDV = tableServiceModel.getValueAt(row, 1).toString();
		String giatien = tableServiceModel.getValueAt(row, 2).toString();
		// Bước 1: Loại bỏ dấu chấm và VND
		String giatienChuoi = giatien.replace(".", "").replace("VND", "").trim();
		String trangthai = tableServiceModel.getValueAt(row, 3).toString();
		String mota = tableServiceModel.getValueAt(row, 4).toString();
		String maldv = tableServiceModel.getValueAt(row, 5).toString();

		// Tạo các trường nhập liệu với dữ liệu ban đầu
		JTextField txtTenDV = new JTextField(tenDV);
		JTextField txtGiaTien = new JTextField(giatienChuoi);
		String[] dsTrangThai = {"Còn","Hết"};
		JComboBox<String> cbTrangThai = new JComboBox<>(dsTrangThai);
		cbTrangThai.setSelectedItem(trangthai);
		JTextField txtMoTa = new JTextField(mota);
		JTextField txtMaLoaiDV = new JTextField(maldv);
		JLabel lblmaloaidv = new JLabel("Mã loại dịch vụ:");
		JComboBox<String> cbMaLoaiDichVu = new JComboBox<>();
		for (LoaiDichVu ldv: dsldv) {
		    cbMaLoaiDichVu.addItem(ldv.getTenDV());
		}

		// Tạo panel chứa form
		JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Padding khỏi viền

		panel.add(new JLabel("Tên dịch vụ:"));
		panel.add(txtTenDV);

		panel.add(new JLabel("Giá tiền:"));
		panel.add(txtGiaTien);

		panel.add(new JLabel("Trạng thái:"));
		panel.add(cbTrangThai);

		panel.add(new JLabel("Mô tả:"));
		panel.add(txtMoTa);

		panel.add(lblmaloaidv);
		panel.add(cbMaLoaiDichVu);
        
        int result = JOptionPane.showConfirmDialog(
                this, panel, "Cập nhật thông tin loại phòng",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            // Lấy dữ liệu từ các trường nhập liệu
            String newTenDV = txtTenDV.getText().trim();
            String newGiaTien = txtGiaTien.getText().trim();
            String newTrangThai = cbTrangThai.getSelectedItem().toString();
            String newMoTa = txtMoTa.getText().trim();
            String selectedLoaiDV = cbMaLoaiDichVu.getSelectedItem().toString();
            LoaiDichVu ldv= daoldv.getLoaiDVTheoMa(selectedLoaiDV);
            // Cập nhật thông tin vào tableServiceModel (giả sử đây là DefaultTableModel)
            tableServiceModel.setValueAt(newTenDV, row, 1);
            tableServiceModel.setValueAt(newGiaTien, row, 2);
            tableServiceModel.setValueAt(newTrangThai, row, 3);
            tableServiceModel.setValueAt(newMoTa, row, 4);
            tableServiceModel.setValueAt(selectedLoaiDV, row, 5); // Giả sử bạn hiển thị tên loại DV ở cột này
            boolean isCon = cbTrangThai.getSelectedItem().toString().equalsIgnoreCase("Còn");
            // Nếu có thao tác cập nhật vào DB hoặc một model riêng thì thực hiện ở đây
            DichVu dv = new DichVu(maDV, tenDV, newMoTa, Double.parseDouble(newGiaTien),isCon, ldv);
            daodv.capnhatDichVu(dv);
        }
    }
	private String formatCurrencyVND(double amount) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(amount) + " VND";
    }
}



