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
    }
}
