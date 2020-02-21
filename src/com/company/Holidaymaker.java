package com.company;

// TODO: ta bort while-loopar?
// TODO: lägg in start_date/end_date i room_reservation?

import com.mysql.cj.protocol.Resultset;

import java.sql.*;
import java.util.Scanner;

public class Holidaymaker {
    private ResultSet resultSet;
    private PreparedStatement statement;
    private Connection conn;
    private Scanner scanner = new Scanner(System.in);

    public Holidaymaker() throws SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://localhost/holidaymaker?user=root&password=toor&serverTimezone=CET");
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
        int customerId = -1;

        // find customer id
        while(true) {
            System.out.print("Enter email of customer or empty string to quit: ");
            String email = scanner.nextLine().trim();

            if(email.isBlank()) {
                return;
            }

            customerId = findCustomerIdFromEmail(email);

            if(customerId != -1) {
                break;
            }
            else {
                System.out.println("No customer found with that email! Try again!");
            }
        }

        // get start-date and end-date
        Date startDate = null, endDate = null;
        while(true) {
            try {
                System.out.println("Start date and end date of your booking...");
                System.out.print("Enter start date (yyyy-mm-dd): ");
                startDate = Date.valueOf(scanner.nextLine());

                System.out.print("Enter end date (yyyy-mm-dd): ");
                endDate = Date.valueOf(scanner.nextLine());
            }
            catch(IllegalArgumentException e) {
                System.out.println("ERROR: Date was invalid or incorrect format! Try again!");
                continue;
            }

            if(startDate.equals(endDate) || startDate.after(endDate)) {
                System.out.println("ERROR: Start date must be before end date! Try again");
                continue;
            }

            break;
        }

        // TODO: Insert into bookings-table, and get insertId back
        int thisBookingId = -1;
        try {
            statement = conn.prepareStatement("INSERT INTO bookings (start_date, end_date, customer) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            statement.setDate(1, startDate);
            statement.setDate(2, endDate);
            statement.setInt(3, customerId);

            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            if(resultSet.next()) {
                thisBookingId = resultSet.getInt(1);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            return;
        }

        // hotel profile
        int facilityProfileId = -1;
        try {
            System.out.println("Hotel profiles:");
            resultSet = getAllHotelProfiles();
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id") + ". " + resultSet.getString("profile_string"));
            }
            System.out.print("Enter profile id: ");
            facilityProfileId = Integer.parseInt(scanner.nextLine());
        }
        catch(Exception e) {
            e.printStackTrace();
            return;
        }

        int hotelId = -1;

        // choose hotel
        while(true) { // TODO: remove this while
            try {
                System.out.println("Hotels:");
                statement = conn.prepareStatement("SELECT id, name FROM hotels WHERE facility_profile=?");
                statement.setInt(1, facilityProfileId);
                resultSet = statement.executeQuery();

                while(resultSet.next()) {
                    System.out.println(resultSet.getInt("id") + ". " + resultSet.getString("name"));
                }

                System.out.print("Enter hotel id: ");
                hotelId = Integer.parseInt(scanner.nextLine());
                // TODO: check if hotelId is valid
                break;
            }
            catch(Exception e) {
                e.printStackTrace();
                return;
            }
        }

        // choose room
        while (true) {
            System.out.println("Rooms:");
            resultSet = getAllRoomsInHotel(hotelId);
            try {
                while (resultSet.next()) {
                    System.out.println(resultSet.getInt("room_id") + ". Room_number:" + resultSet.getInt("room_number") + " " + resultSet.getString("designation") + " " + resultSet.getInt("hotel"));
                }
            }
            catch(Exception e) {
                e.printStackTrace();
                return;
            }

            System.out.print("Enter id of room to book: ");
            int roomId = Integer.parseInt(scanner.nextLine());

            // TODO: Insert into room_reservations...
            try {
                statement = conn.prepareStatement("INSERT INTO room_reservations (room, booking) VALUES (?, ?)");
                statement.setInt(1, roomId);
                statement.setInt(2, thisBookingId);
                statement.executeUpdate();
            }
            catch(Exception e) {
                e.printStackTrace();
                return;
            }

            System.out.print("Do you want to book another room? [y/n]: ");
            if(scanner.nextLine().trim().toLowerCase().equals("y")) {
                continue;
            }
            else {
                break;
            }
        } // end while (room reservations)

    }

    private Resultset getAvailableRoomsInHotel(int hotelId, Date filterStartDate, Date filterEndDate, int roomSize) {
        try {
            "SELECT * FROM bookings, room_reservations, rooms, room_types " +
                    "WHERE bookings.id = room_reservations.booking AND room_reservations.room = rooms.id AND rooms.room_type = room_types.id AND room_types.hotel=? " +
                    "AND max_num_guests=? " + /* allow fewer guests than maximum capacity? */
                    "AND (? NOT BETWEEN start_date AND end_date) AND (? NOT BETWEEN start_date AND end_date) " +
                    "AND NOT (? < start_date AND ? > end_date)"
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private ResultSet getAllHotelProfiles() {
        try {
            statement = conn.prepareStatement("SELECT facility_profiles.id, CONCAT('pool:', facility_profiles.pool, ' evening_entertainment:', facility_profiles.evening_entertainment, ' kids_club:', facility_profiles.kids_club, ' restaurant:', facility_profiles.restaurant) AS profile_string FROM facility_profiles");
            return statement.executeQuery();
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private ResultSet getAllRoomsInHotel(int hotelId) {
        try {
            statement = conn.prepareStatement("SELECT rooms.id AS room_id, rooms.room_number AS room_number, room_types.designation, room_types.hotel FROM rooms, room_types " +
                    "WHERE rooms.room_type = room_types.id AND room_types.hotel=?");
            statement.setInt(1, hotelId);
            return statement.executeQuery();
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private ResultSet getAllRoomBookingsInHotel(int hotelId) {
        try {
            statement = conn.prepareStatement("SELECT * FROM bookings, room_reservations, rooms, room_types " +
                    "WHERE bookings.id = room_reservations.booking AND room_reservations.room = rooms.id AND rooms.room_type = room_types.id AND room_types.hotel=?");
            statement.setInt(1, hotelId);
            return statement.executeQuery();
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private int findCustomerIdFromEmail(String email) {
        try {
            statement = conn.prepareStatement("SELECT id FROM customers WHERE email = ?");
            statement.setString(1, email);
            resultSet = statement.executeQuery();

            return resultSet.next() ? resultSet.getInt("id") : -1;
        }
        catch(Exception e) {
            e.printStackTrace();
            return -1;
        }
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
