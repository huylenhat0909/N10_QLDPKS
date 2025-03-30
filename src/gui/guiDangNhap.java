package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Image;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import connectDB.ConnectDB;
import dao.DaoTaiKhoan;
import entity.TaiKhoan;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class guiDangNhap extends JFrame implements MouseListener, ActionListener {
	private boolean isPasswordVisible = false;
	private JButton login;
	private JTextField username;
	private JPasswordField password;
	private DaoTaiKhoan daotk;
	private TaiKhoan tk;
	class BackgroundPanel extends JPanel {
	    private Image backgroundImage;

	    public BackgroundPanel(String filePath) {
	        backgroundImage = new ImageIcon(filePath).getImage();
	    }

	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
	    }
	}
	public guiDangNhap() {
		setTitle("SkyHotel Manager Login");
        setSize(600, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Sử dụng JPanel vẽ nền ảnh
        BackgroundPanel background = new BackgroundPanel("icon/hinhnen.jpg");
        setContentPane(background);
        background.setLayout(null);

        // Tiêu đề
        JLabel title = new JLabel("LOGIN TO SKYHOTEL MANAGER");
        title.setForeground(Color.RED);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(150, 20, 300, 30);
        background.add(title);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.RED);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameLabel.setBounds(80, 70, 100, 30);
        background.add(usernameLabel);

        // Username
        username = new JTextField();
        username.setBounds(80, 100, 160, 30);
        background.add(username);

        // Label Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.RED);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setBounds(260, 70, 100, 30);
        background.add(passwordLabel);

        // Password
        password = new JPasswordField();
        password.setEchoChar('*');
        password.setBounds(260, 100, 160, 30);
        background.add(password);

        // Nút hiện/ẩn password
        JButton showButton = new JButton("Show");
        showButton.setBounds(430, 100, 100, 30);
        showButton.setFocusPainted(false);
        background.add(showButton);

        showButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isPasswordVisible = !isPasswordVisible;
                if (isPasswordVisible) {
                    password.setEchoChar((char) 0);
                    showButton.setText("Hide");
                } else {
                    password.setEchoChar('*');
                    showButton.setText("Show");
                }
            }
        });

        // Nút login
        login = new JButton("LOGIN");
        login.setBounds(320, 140, 100, 30);
        login.setBackground(new Color(0, 150, 255));
        login.setForeground(Color.RED);
        login.setFocusable(false);
        login.setBorderPainted(false);
        background.add(login);

        // Sự kiện hover + click
        login.addMouseListener(new MouseAdapter() {
            Color originalColor = new Color(0, 150, 255);
            Color clickColor = new Color(0, 120, 220);

            @Override
            public void mouseEntered(MouseEvent e) {
                login.setBackground(Color.ORANGE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                login.setBackground(originalColor);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                login.setBackground(clickColor);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (login.getBounds().contains(e.getPoint())) {
                    login.setBackground(Color.ORANGE);
                } else {
                    login.setBackground(originalColor);
                }
            }
        });
        login.addActionListener(this);
	}
	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new guiDangNhap().setVisible(true);
        });
    }
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object o= e.getSource();
		if(o.equals(login)) {
			kiemtra();
			}
		}
	private void kiemtra() {
		// TODO Auto-generated method stub
		String name=username.getText();
		String pass= password.getText();
		guiDangNhap dangnhap= new guiDangNhap();
		gui_TrangChu trangchu= new gui_TrangChu();
		if(name.equals("") | pass.equals("")) {
			JOptionPane.showMessageDialog(null,"Không được bỏ trống!!!");
		}
		daotk=new DaoTaiKhoan();
		tk=daotk.getTaiKhoantheoTen(name);
		if(tk==null) {
			JOptionPane.showMessageDialog(null,"Tên đăng nhập không tồn tại!!!");
			username.requestFocus();
			username.selectAll();
			return;
		}
		if(tk.getMatKhau().equals(pass)) {
			dangnhap.setVisible(false);
			trangchu.setVisible(true);
			trangchu.setLocationRelativeTo(null);
			dispose();
			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
			return;
		}else {
			JOptionPane.showMessageDialog(this, "Sai mật khẩu!");
			password.requestFocus();
			password.selectAll();
		}
		
	}
	
}

