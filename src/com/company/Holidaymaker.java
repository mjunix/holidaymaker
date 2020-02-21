package com.company;

import java.sql.*;
import java.util.Scanner;

public class Holidaymaker {
    private ResultSet resultSet;
    private PreparedStatement statement;
    private Connection conn;
    private Scanner scanner = new Scanner(System.in);

    public Holidaymaker() throws SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://localhost/holidaymaker?user=root&password=toor&serverTimezone=UTC");
        showMainMenu();
    }

    private void showMainMenu() {
        while(true) {
            System.out.println("\nMain menu:");
            System.out.println("1. Register customer");
            System.out.println("2. Make reservation");
            System.out.println("0. Exit");
            System.out.print("Choice: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    registerCustomer();
                    break;
                case 2:
                    makeReservation();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Unknown choice, try again!");
            }
        }
    }

    private void makeReservation() {
    }

    private void registerCustomer() {
        // name
        System.out.print("Name: ");
        String name = scanner.nextLine();

        // email
        System.out.print("Email: ");
        String email = scanner.nextLine();

        // phone
        System.out.print("Phone: ");
        String phone = scanner.nextLine();

        // address
        System.out.print("Address: ");
        String address = scanner.nextLine();

        // city
        System.out.print("City: ");
        String city = scanner.nextLine();

        // country
        System.out.print("Country: ");
        String country = scanner.nextLine();

        try {
            statement = conn.prepareStatement("INSERT INTO customers (name, email, phone, address, city, country) VALUES (?, ?, ?, ?, ?, ?)");
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, phone);
            statement.setString(4, address);
            statement.setString(5, city);
            statement.setString(6, country);
            int rows = statement.executeUpdate();

            if(rows == 1) {
                System.out.println("Regristered customer successfully!");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
