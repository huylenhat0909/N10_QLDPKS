package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.HeadlessException;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import connectDB.ConnectDB;

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
	public guiDangNhap() {
		setTitle("SkyHotel Manager Login");
        setSize(600, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.DARK_GRAY);
        // Tiêu đề
        JLabel title = new JLabel("LOGIN TO SKYHOTEL MANAGER");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(150, 20, 300, 30);
        add(title);

        // Username
        username = new JTextField();
        username.setBounds(80, 80, 160, 30);
        add(username);

        // Password
        password = new JPasswordField();
        password.setEchoChar('*');
        password.setBounds(260, 80, 160, 30);
        add(password);

     //  // Nút hiện/ẩn password
        JButton showButton = new JButton("Show");
        showButton.setBounds(430, 80, 100, 30);
        showButton.setFocusPainted(false);
        add(showButton);

        showButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isPasswordVisible = !isPasswordVisible;
                if (isPasswordVisible) {
                    password.setEchoChar((char)0);
                    showButton.setText("Hide");
                } else {
                    password.setEchoChar('*');
                    showButton.setText("Show");
                }
            }
        });
        // Nút login
        login = new JButton("LOGIN");
        login.setBounds(320, 120, 100, 30);
        login.setBackground(new Color(0, 150, 255));
        login.setForeground(Color.WHITE);
        login.setFocusable(false);
        ((AbstractButton) login).setBorderPainted(false);

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
                // Nếu chuột vẫn trong nút sau khi nhả ra
                if (login.getBounds().contains(e.getPoint())) {
                    login.setBackground(Color.ORANGE);
                } else {
                    login.setBackground(originalColor);
                }
            }
        });
        add(login);
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
		try {
			ConnectDB.getInstance();
			Connection con = ConnectDB.getConnection();
			String sql = "select * from TaiKhoan where taikhoan='"+name+"' and matkhau='"+pass+"'";
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				dangnhap.setVisible(false);
				trangchu.setVisible(true);
				trangchu.setLocationRelativeTo(null);
				dispose();
				setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
				return;
			}
		} catch (HeadlessException e) {
			//TODO: handle exception
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu!");
		password.requestFocus();
		password.selectAll();
	}
	
}

