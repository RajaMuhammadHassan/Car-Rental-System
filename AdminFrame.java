package ui;

import app.CarRentalSystem;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import model.*;
import util.FileHandler;

public class AdminFrame extends JFrame {

    // ---------- COLORS ----------
    Color bgColor = new Color(236, 240, 241);      // light gray
    Color topColor = new Color(52, 152, 219);      // blue
    Color btnColor = new Color(46, 204, 113);      // green
    Color dangerColor = new Color(231, 76, 60);    // red
    Color textDark = new Color(44, 62, 80);

    // --- Tables ---
    DefaultTableModel carTableModel = new DefaultTableModel(
            new String[]{"ID", "Brand", "Model", "Rent/Day", "Status"}, 0);
    JTable carTableView = new JTable(carTableModel);

    DefaultTableModel customerModel = new DefaultTableModel(
            new String[]{"Name", "Username", "Email", "Phone", "License"}, 0);

    // --- Labels ---
    JLabel revenueLabel = new JLabel();
    JLabel pendingLabel = new JLabel();
    JLabel welcomeLabel = new JLabel();

    public AdminFrame() {
        setTitle("Owner Dashboard");
        setSize(1100, 550);
        setLayout(new BorderLayout());
        getContentPane().setBackground(bgColor);

        // ---------- TOP PANEL ----------
        welcomeLabel.setText("Welcome, " + CarRentalSystem.currentUser.getName());
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);

        revenueLabel.setText("Revenue: $" + CarRentalSystem.totalRevenue);
        revenueLabel.setForeground(Color.WHITE);

        pendingLabel.setText("Pending Requests: " + getPendingCount());
        pendingLabel.setForeground(Color.WHITE);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        topPanel.setBackground(topColor);
        topPanel.add(welcomeLabel);
        topPanel.add(revenueLabel);
        topPanel.add(pendingLabel);
        add(topPanel, BorderLayout.NORTH);

        // ---------- TABLE STYLING ----------
        styleTable(carTableView);

        JLabel carTableLabel = new JLabel("Car Inventory", SwingConstants.CENTER);
        carTableLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        carTableLabel.setForeground(textDark);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(bgColor);
        centerPanel.add(carTableLabel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(carTableView), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // ---------- BUTTONS ----------
        JButton addCarBtn = createButton("Add Car", btnColor);
        JButton removeCarBtn = createButton("Remove Car", dangerColor);
        JButton refreshBtn = createButton("Refresh", topColor);
        JButton pendingBtn = createButton("Pending Requests", new Color(155, 89, 182));
        JButton customersBtn = createButton("Customers", new Color(241, 196, 15));
        JButton logoutBtn = createButton("Logout", dangerColor);

        addCarBtn.addActionListener(e -> addCar());
        removeCarBtn.addActionListener(e -> removeCar());
        refreshBtn.addActionListener(e -> refreshCars());
        pendingBtn.addActionListener(e -> openRequestPopup());
        customersBtn.addActionListener(e -> showCustomerPopup());
        logoutBtn.addActionListener(e -> logout());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(bgColor);
        bottomPanel.add(addCarBtn);
        bottomPanel.add(removeCarBtn);
        bottomPanel.add(refreshBtn);
        bottomPanel.add(pendingBtn);
        bottomPanel.add(customersBtn);
        bottomPanel.add(logoutBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshCars();
        refreshCustomers();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ---------- HELPER METHODS ----------
    JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return btn;
    }

    void styleTable(JTable table) {
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JTableHeader header = table.getTableHeader();
        header.setBackground(topColor);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    // ---------- LOGIC (UNCHANGED) ----------
    void refreshCars() {
        carTableModel.setRowCount(0);
        for (Car c : CarRentalSystem.cars) {
            carTableModel.addRow(new Object[]{c.id, c.brand, c.model, c.rentPerDay, c.status});
        }
    }

    void addCar() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Car ID:"));
            String brand = JOptionPane.showInputDialog("Brand:");
            String model = JOptionPane.showInputDialog("Model:");
            double rent = Double.parseDouble(JOptionPane.showInputDialog("Rent per day:"));

            CarRentalSystem.cars.add(new Car(id, brand, model, rent));
            FileHandler.saveCars(CarRentalSystem.cars);
            refreshCars();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input");
        }
    }

    void removeCar() {
        int row = carTableView.getSelectedRow();
        if (row == -1) return;
        CarRentalSystem.cars.remove(row);
        FileHandler.saveCars(CarRentalSystem.cars);
        refreshCars();
    }
    void refreshCustomers() {
    customerModel.setRowCount(0);

    for (User u : CarRentalSystem.users) {
        if (u instanceof Customer c) {
            customerModel.addRow(new Object[]{
                c.getName(),
                c.getUsername(),
                c.getEmail(),
                c.getPhone(),
                c.getLicenseNumber()
            });
        }
    }
}
void openRequestPopup() {

    JDialog popup = new JDialog(this, "Pending Requests", true);
    popup.setSize(550, 350);
    popup.setLayout(new BorderLayout());

    DefaultTableModel model = new DefaultTableModel(
            new String[]{"Customer", "Car", "Days", "Bill"}, 0
    );
    JTable table = new JTable(model);

    // Load pending requests
    for (RentRequest r : CarRentalSystem.requests) {
        if (!r.processed) {
            model.addRow(new Object[]{
                r.customer.getName(),
                r.car.brand + " " + r.car.model,
                r.days,
                r.bill
            });
        }
    }

    // ---------- BUTTONS ----------
    JButton approveBtn = new JButton("Approve");
    JButton rejectBtn = new JButton("Reject");

    approveBtn.setBackground(new Color(46, 204, 113)); // green
    approveBtn.setForeground(Color.WHITE);

    rejectBtn.setBackground(new Color(231, 76, 60)); // red
    rejectBtn.setForeground(Color.WHITE);

    // ---------- APPROVE ----------
    approveBtn.addActionListener(e -> {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(popup, "Select a request first");
            return;
        }

        // Get selected pending request
        RentRequest req = CarRentalSystem.requests.stream()
                .filter(r -> !r.processed)
                .skip(row)
                .findFirst()
                .orElse(null);

        if (req == null) return;

        if (!req.car.status.equals("Available")) {
            JOptionPane.showMessageDialog(popup, "Car already rented");
            return;
        }

        req.processed = true;
        req.approved = true;
        req.car.status = "Rented";
        req.car.rentedBy = req.customer;

        CarRentalSystem.totalRevenue += req.bill;

        // Remove other pending requests for same car
        CarRentalSystem.requests.removeIf(
                r -> !r.processed && r.car.id == req.car.id
        );

        FileHandler.saveCars(CarRentalSystem.cars);
        FileHandler.saveRequests(CarRentalSystem.requests);
        FileHandler.saveRevenue(CarRentalSystem.totalRevenue);

        refreshCars();
        pendingLabel.setText("Pending Requests: " + getPendingCount());

        popup.dispose();
        JOptionPane.showMessageDialog(this,
                "Request Approved\nBill: " + req.bill);
    });

    // ---------- REJECT ----------
    rejectBtn.addActionListener(e -> {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(popup, "Select a request first");
            return;
        }

        RentRequest req = CarRentalSystem.requests.stream()
                .filter(r -> !r.processed)
                .skip(row)
                .findFirst()
                .orElse(null);

        if (req == null) return;

        req.processed = true;
        req.approved = false;
        CarRentalSystem.requests.remove(req);

        FileHandler.saveRequests(CarRentalSystem.requests);
        pendingLabel.setText("Pending Requests: " + getPendingCount());

        popup.dispose();
        JOptionPane.showMessageDialog(this, "Request Rejected");
    });

    // ---------- BUTTON PANEL ----------
    JPanel btnPanel = new JPanel();
    btnPanel.add(approveBtn);
    btnPanel.add(rejectBtn);

    popup.add(new JScrollPane(table), BorderLayout.CENTER);
    popup.add(btnPanel, BorderLayout.SOUTH);

    popup.setLocationRelativeTo(this);
    popup.setVisible(true);
}

void showCustomerPopup() {
    refreshCustomers();

    JDialog popup = new JDialog(this, "Customers", true);
    popup.setSize(600, 350);
    popup.setLayout(new BorderLayout());

    JTable table = new JTable(customerModel);
    popup.add(new JScrollPane(table), BorderLayout.CENTER);

    popup.setLocationRelativeTo(this);
    popup.setVisible(true);
}


    int getPendingCount() {
        return (int) CarRentalSystem.requests.stream().filter(r -> !r.processed).count();
    }

    void logout() {
        CarRentalSystem.currentUser = null;
        dispose();
        new LoginFrame();
    }
}
