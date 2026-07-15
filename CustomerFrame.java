package ui;

import app.CarRentalSystem;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.*;
import util.FileHandler;

public class CustomerFrame extends JFrame {

    DefaultTableModel availableModel = new DefaultTableModel(
            new String[]{"ID", "Brand", "Model", "Rent/Day"}, 0);
    JTable availableTable = new JTable(availableModel);

    DefaultTableModel activeModel = new DefaultTableModel(
            new String[]{"Car", "Days Left", "Bill"}, 0);
    JTable activeTable = new JTable(activeModel);

    DefaultTableModel historyModel = new DefaultTableModel(
            new String[]{"Car", "Days", "Bill"}, 0);
    JTable historyTable = new JTable(historyModel);

    JLabel welcomeLabel = new JLabel();
    JLabel notificationLabel = new JLabel();

    public CustomerFrame() {
        setTitle("Customer Dashboard");
        setSize(900, 600);
        setLayout(new BorderLayout());

        welcomeLabel.setText("Welcome to Rental Car System, " + CarRentalSystem.currentUser.getName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(welcomeLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 5, 5)); // spacing 5px

        // Available Cars
        JPanel availablePanel = new JPanel(new BorderLayout());
        JLabel availableLabel = new JLabel("Available Cars", SwingConstants.CENTER);
        availableLabel.setFont(new Font("Arial", Font.BOLD, 14));
        availablePanel.add(availableLabel, BorderLayout.NORTH);
        availablePanel.add(new JScrollPane(availableTable), BorderLayout.CENTER);

        // Active Rentals
        JPanel activePanel = new JPanel(new BorderLayout());
        JLabel activeLabel = new JLabel("Your Active Rentals", SwingConstants.CENTER);
        activeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        activePanel.add(activeLabel, BorderLayout.NORTH);
        activePanel.add(new JScrollPane(activeTable), BorderLayout.CENTER);

        // Rental History
        JPanel historyPanel = new JPanel(new BorderLayout());
        JLabel historyLabel = new JLabel("Rental History", SwingConstants.CENTER);
        historyLabel.setFont(new Font("Arial", Font.BOLD, 14));
        historyPanel.add(historyLabel, BorderLayout.NORTH);
        historyPanel.add(new JScrollPane(historyTable), BorderLayout.CENTER);

        centerPanel.add(availablePanel);
        centerPanel.add(activePanel);
        centerPanel.add(historyPanel);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom buttons
        JPanel bottomPanel = new JPanel();
        JButton rentBtn = new JButton("Rent Car");
        JButton returnBtn = new JButton("Return Car");
        JButton logoutBtn = new JButton("Logout");

        rentBtn.addActionListener(e -> rentCar());
        returnBtn.addActionListener(e -> returnCar());
        logoutBtn.addActionListener(e -> logout());

        bottomPanel.add(rentBtn);
        bottomPanel.add(returnBtn);
        bottomPanel.add(logoutBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        refreshTables();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    void refreshTables() {
        // Available cars
        availableModel.setRowCount(0);
        for (Car c : CarRentalSystem.cars) {
            if (c.status.equals("Available")) {
                availableModel.addRow(new Object[]{c.id, c.brand, c.model, c.rentPerDay});
            }
        }

        // Active rentals
        activeModel.setRowCount(0);
        for (RentRequest r : CarRentalSystem.requests) {
            if (r.customer == CarRentalSystem.currentUser && r.processed && r.car.rentedBy == CarRentalSystem.currentUser) {
                activeModel.addRow(new Object[]{r.car.brand + " " + r.car.model, r.days, r.bill});
            }
        }

        // Rental history (past processed)
        historyModel.setRowCount(0);
        for (RentRequest r : CarRentalSystem.requests) {
            if (r.customer == CarRentalSystem.currentUser && r.processed && r.car.rentedBy != CarRentalSystem.currentUser) {
                historyModel.addRow(new Object[]{r.car.brand + " " + r.car.model, r.days, r.bill});
            }
        }

        // Pending request notification
        boolean pending = false;
        for (RentRequest r : CarRentalSystem.requests) {
            if (r.customer == CarRentalSystem.currentUser && !r.processed) {
                pending = true;
                break;
            }
        }
        notificationLabel.setText(pending ? "You have pending rent requests!" : "");
    }

    void rentCar() {
        int row = availableTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a car first");
            return;
        }

        Car car = null;
        int count = 0;
        for (Car c : CarRentalSystem.cars) {
            if (c.status.equals("Available")) {
                if (count == row) {
                    car = c;
                    break;
                }
                count++;
            }
        }

        if (car == null) return;

        try {
            int days = Integer.parseInt(JOptionPane.showInputDialog("Days to rent:"));
            if (days <= 0) throw new Exception("Days must be positive");

            RentRequest request = new RentRequest(car, (Customer) CarRentalSystem.currentUser, days);
            CarRentalSystem.requests.add(request);
            FileHandler.saveRequests(CarRentalSystem.requests);

            JOptionPane.showMessageDialog(this, "Rent request sent for approval");
            refreshTables();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void returnCar() {
        int row = activeTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a car from your active rentals to return");
            return;
        }

        RentRequest requestToReturn = null;
        for (RentRequest r : CarRentalSystem.requests) {
            if (r.customer == CarRentalSystem.currentUser &&
                r.processed &&
                r.car.rentedBy == CarRentalSystem.currentUser &&
                (r.car.brand + " " + r.car.model).equals(activeModel.getValueAt(row, 0))) {
                requestToReturn = r;
                break;
            }
        }

        if (requestToReturn == null) {
            JOptionPane.showMessageDialog(this, "Cannot return this car");
            return;
        }

        requestToReturn.car.status = "Available";
        requestToReturn.car.rentedBy = null;
        FileHandler.saveCars(CarRentalSystem.cars);

        JOptionPane.showMessageDialog(this, "Car returned successfully");
        refreshTables();
    }

    void logout() {
        CarRentalSystem.currentUser = null;
        dispose();
        new LoginFrame();
    }
}
