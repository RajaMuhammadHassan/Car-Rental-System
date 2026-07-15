# 🚗 Car Rental System

A **desktop GUI application** built in **Java (Swing)** for managing a car rental business — supporting both **Admin** and **Customer** roles with a login/signup system and persistent file-based storage.

## ✨ Features

### 👤 Customer
- Sign up and log in
- Browse available cars with rent-per-day pricing
- Submit a rental request for a chosen car
- View rental bill based on number of days

### 🛠️ Admin
- Log in with admin credentials
- Add and manage cars in the system
- Approve or reject customer rental requests
- Track total revenue generated

## 🧰 Tech Stack

- **Language:** Java
- **GUI:** Java Swing
- **Data Storage:** File-based persistence (`.dat` files via Java Serialization)
- **IDE:** NetBeans

## 📁 Project Structure

```
src/
├── app/         → Main entry point (CarRentalSystem.java)
├── model/       → Data models (User, Admin, Customer, Car, RentRequest)
├── ui/          → Swing GUI screens (Login, Signup, Admin panel, Customer panel)
├── util/        → File handling / data persistence logic
└── images/      → UI assets
```

## 🚀 How to Run

1. Make sure you have the **JDK** installed.
2. Open the project in **NetBeans** (recommended), or compile manually:
   ```bash
   javac -d out $(find src -name "*.java")
   java -cp out app.CarRentalSystem
   ```
3. On first run, an admin account is auto-created:
   - **Username:** `admin`
   - **Password:** `admin123`

## 👨‍💻 Author

**Raja Muhammad Hassan**
Software Engineering Student @ NUML
[LinkedIn](https://www.linkedin.com/in/raja-muhammad-hassan)
