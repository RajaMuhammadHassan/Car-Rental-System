package ui;

import app.CarRentalSystem;
import java.awt.*;
import javax.swing.*;
import model.Admin;
import model.Customer;
import model.User;

public class LoginFrame extends JFrame {

    // ---------- COLORS ----------
    Color bgColor = new Color(236, 240, 241);      // light gray
    Color topColor = new Color(52, 152, 219);      // blue
    Color btnColor = new Color(46, 204, 113);      // green
    Color textDark = new Color(44, 62, 80);

    public LoginFrame() {
        setTitle("Car Rental System - Login");
        setSize(420, 320);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(bgColor);

        // ---------- TOP PANEL ----------
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setBackground(topColor);

        JLabel systemName = new JLabel("CAR RENTAL SYSTEM", SwingConstants.CENTER);
        systemName.setFont(new Font("Segoe UI", Font.BOLD, 22));
        systemName.setForeground(Color.WHITE);

        JLabel systemDetail = new JLabel("Manage and rent cars easily", SwingConstants.CENTER);
        systemDetail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        systemDetail.setForeground(Color.WHITE);

        topPanel.add(systemName);
        topPanel.add(systemDetail);
        add(topPanel, BorderLayout.NORTH);

        // ---------- CENTER PANEL ----------
        BackgroundPanel centerPanel =
        new BackgroundPanel("/images/lamborghini.jpg");

        centerPanel.setBackground(bgColor);

        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");
        userLabel.setForeground(textDark);
        passLabel.setForeground(textDark);

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JButton loginBtn = createButton("Login", btnColor);
        JButton signupBtn = createButton("Sign Up", topColor);

        userLabel.setBounds(50, 30, 80, 25);
        usernameField.setBounds(140, 30, 200, 28);

        passLabel.setBounds(50, 75, 80, 25);
        passwordField.setBounds(140, 75, 200, 28);
loginBtn.setBounds(90, 130, 100, 35);
        signupBtn.setBounds(210, 130, 100, 35);
        userLabel.setForeground(Color.WHITE);
passLabel.setForeground(Color.WHITE);

usernameField.setOpaque(true);
passwordField.setOpaque(true);

usernameField.setBackground(new Color(255, 255, 255, 220));
passwordField.setBackground(new Color(255, 255, 255, 220));

        loginBtn.setBackground(Color.BLACK);
loginBtn.setForeground(Color.WHITE);

signupBtn.setBackground(new Color(52, 152, 219));
signupBtn.setForeground(Color.WHITE);


        centerPanel.add(userLabel);
        centerPanel.add(usernameField);
        centerPanel.add(passLabel);
        centerPanel.add(passwordField);
        centerPanel.add(loginBtn);
        centerPanel.add(signupBtn);

        add(centerPanel, BorderLayout.CENTER);

        // ---------- LOGIN LOGIC (UNCHANGED) ----------
        loginBtn.addActionListener(e -> {

            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and password are required");
                return;
            }

            User authenticatedUser = null;

            for (User u : CarRentalSystem.users) {
                if (u.getUsername().equals(username)) {
                    if (u.checkPassword(password)) {
                        authenticatedUser = u;
                    } else {
                        JOptionPane.showMessageDialog(this, "Incorrect password");
                        return;
                    }
                }
            }

            if (authenticatedUser == null) {
                JOptionPane.showMessageDialog(this, "User does not exist");
                return;
            }

            CarRentalSystem.currentUser = authenticatedUser;
            dispose();

            if (authenticatedUser instanceof Admin) {
                new AdminFrame();
            } else if (authenticatedUser instanceof Customer) {
                new CustomerFrame();
            }
        });

        signupBtn.addActionListener(e -> {
            dispose();
            new SignupFrame();
        });

        setVisible(true);
    }

    // ---------- BUTTON STYLE ----------
    JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return btn;
    }
}
