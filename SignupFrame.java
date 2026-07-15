package ui;

import app.CarRentalSystem;
import javax.swing.*;
import model.Customer;
import util.FileHandler;

public class SignupFrame extends JFrame {

    public SignupFrame() {
        setTitle("Customer Sign Up");
        setSize(400, 350);
        setLayout(null);
        setLocationRelativeTo(null);

        // Labels
        JLabel nameLabel = new JLabel("Full Name:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel phoneLabel = new JLabel("Phone:");
        JLabel licenseLabel = new JLabel("License No:");
        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");

        // Fields
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField licenseField = new JTextField();
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JButton registerBtn = new JButton("Register");

        // Set bounds
        nameLabel.setBounds(30, 30, 100, 25);
        nameField.setBounds(140, 30, 200, 25);

        emailLabel.setBounds(30, 65, 100, 25);
        emailField.setBounds(140, 65, 200, 25);

        phoneLabel.setBounds(30, 100, 100, 25);
        phoneField.setBounds(140, 100, 200, 25);

        licenseLabel.setBounds(30, 135, 100, 25);
        licenseField.setBounds(140, 135, 200, 25);

        userLabel.setBounds(30, 170, 100, 25);
        usernameField.setBounds(140, 170, 200, 25);

        passLabel.setBounds(30, 205, 100, 25);
        passwordField.setBounds(140, 205, 200, 25);

        registerBtn.setBounds(140, 250, 120, 30);

        // Add components
        add(nameLabel);
        add(nameField);
        add(emailLabel);
        add(emailField);
        add(phoneLabel);
        add(phoneField);
        add(licenseLabel);
        add(licenseField);
        add(userLabel);
        add(usernameField);
        add(passLabel);
        add(passwordField);
        add(registerBtn);

        // Button action
        registerBtn.addActionListener(e -> {
            try {
                String fullName = nameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                String license = licenseField.getText();
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (fullName.isEmpty() || email.isEmpty() ||
                    phone.isEmpty() || license.isEmpty() ||
                    username.isEmpty() || password.isEmpty()) {

                    throw new Exception("All fields are required");
                }

                Customer customer = new Customer(
                        username,
                        password,
                        fullName,
                        email,
                        phone,
                        license
                );

                CarRentalSystem.users.add(customer);
                FileHandler.saveUsers(CarRentalSystem.users);

                JOptionPane.showMessageDialog(this,
                        "Registration successful");

                dispose();
                new LoginFrame();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage());
            }
        });

        setVisible(true);
    }
}
