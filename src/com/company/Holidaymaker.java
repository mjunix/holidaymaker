package com.company;

import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class Holidaymaker {
    private ResultSet resultSet;
    private PreparedStatement statement;
    private Connection conn;
    private final Scanner scanner = new Scanner(System.in);

    public Holidaymaker() throws SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://localhost/holidaymaker?user=root&password=toor&serverTimezone=CET");
        showMainMenu();
    }

    private void showMainMenu() {
        while(true) {
            System.out.println("\nMain menu:");
            System.out.println("1. Register customer");
            System.out.println("2. Make reservation");
            System.out.println("3. Delete reservation");
            System.out.println("4. Show reservations");
            System.out.println("5. Show customers");
            System.out.println("0. Exit");

            int choice = getIntegerFromUser("Choice: ");

            switch (choice) {
                case 1:
                    registerCustomer();
                    break;
                case 2:
                    makeReservation();
                    break;
                case 3:
                    deleteReservation();
                    break;
                case 4:
                    showReservations();
                    break;
                case 5:
                    showCustomers();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Unknown choice, try again!");
            }
        }
    }

    private void registerCustomer() {
        // name
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();

        // email (must be unique)
        String email = null;
        while(true) {
            System.out.print("Email: ");
            email = scanner.nextLine().trim();

            try {
                statement = conn.prepareStatement("SELECT id FROM customers WHERE email=?");
                statement.setString(1, email);
                resultSet = statement.executeQuery();

                if(resultSet.next()) {
                    System.out.println("ERROR: A customer with that email already registered! Please use another email!");
                    continue;
                }
                else {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        // phone
        System.out.print("Phone: ");
        String phone = scanner.nextLine().trim();

        // address
        System.out.print("Address: ");
        String address = scanner.nextLine().trim();

        // city
        System.out.print("City: ");
        String city = scanner.nextLine().trim();

        // country
        System.out.print("Country: ");
        String country = scanner.nextLine().trim();

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
                System.out.println("Registered customer successfully!");
            }
        } catch(Exception e) {
            e.printStackTrace();
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
        Date bookingStartDate = null, bookingEndDate = null;
        while(true) {
            try {
                System.out.println("Start date and end date of your booking...");
                System.out.print("Enter start date (yyyy-mm-dd): ");
                bookingStartDate = Date.valueOf(scanner.nextLine());

                if(bookingStartDate.before(Date.valueOf(LocalDate.now()))) {
                    System.out.println("ERROR: Start date cannot be in the past! Try again!");
                    continue;
                }

                System.out.print("Enter end date (yyyy-mm-dd): ");
                bookingEndDate = Date.valueOf(scanner.nextLine());
            }
            catch(IllegalArgumentException e) {
                System.out.println("ERROR: Date was invalid or incorrect format! Try again!");
                continue;
            }

            if(bookingStartDate.equals(bookingEndDate) || bookingStartDate.after(bookingEndDate)) {
                System.out.println("ERROR: Start date must be before end date! Try again");
                continue;
            }

            break;
        }

        // create booking
        int thisBookingId = -1;
        try {
            statement = conn.prepareStatement("INSERT INTO reservations (start_date, end_date, customer) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            statement.setDate(1, bookingStartDate);
            statement.setDate(2, bookingEndDate);
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

            facilityProfileId = getIntegerFromUser("Enter profile id: ");
        }
        catch(Exception e) {
            e.printStackTrace();
            return;
        }

        int hotelId = -1;

        // choose hotel
        try {
            System.out.println("Hotels:");
            statement = conn.prepareStatement("SELECT id, name FROM hotels WHERE facility_profile=?");
            statement.setInt(1, facilityProfileId);
            resultSet = statement.executeQuery();

            while(resultSet.next()) {
                System.out.println(resultSet.getInt("id") + ". " + resultSet.getString("name"));
            }

            hotelId = getIntegerFromUser("Enter hotel id: ");
            // TODO: check if hotelId is valid
        }
        catch(Exception e) {
            e.printStackTrace();
            return;
        }


        // choose room
        while (true) {
            System.out.println("\nRoom reservation...");
            int roomSize = getIntegerFromUser("How many people will stay in the room (or zero to quit): ");

            if(roomSize == 0) {
                break;
            }

            System.out.println("Available rooms:");
            resultSet = getAvailableRoomsInHotel(hotelId, bookingStartDate, bookingEndDate, roomSize);
            try {
                if(!resultSet.next()) {
                    System.out.println("There are no available rooms that matches your criteria! Try again!");
                    continue;
                }

                do {
                    System.out.println(resultSet.getInt("id") + ". Room_number:" + resultSet.getInt("room_number") + " Room_size:" + resultSet.getString("designation"));
                } while (resultSet.next());
            }
            catch(Exception e) {
                e.printStackTrace();
                return;
            }

            int roomId = getIntegerFromUser("Enter id of room to book: ");

            try {
                statement = conn.prepareStatement("INSERT INTO room_reservations (room, reservation) VALUES (?, ?)");
                statement.setInt(1, roomId);
                statement.setInt(2, thisBookingId);
                statement.executeUpdate();
            }
            catch(Exception e) {
                e.printStackTrace();
                return;
            }

            System.out.print("Do you want to book another room? [y/n]: ");
            if(!scanner.nextLine().trim().toLowerCase().equals("y")) {
                break;
            }
        } // end while (room reservations)

        // if no room reservations were made, delete booking
        try {
            statement = conn.prepareStatement("SELECT COUNT(*) AS num_room_reservations FROM room_reservations WHERE reservation=?");
            statement.setInt(1, thisBookingId);
            resultSet = statement.executeQuery();
            resultSet.next();

            int num_room_reservations = resultSet.getInt("num_room_reservations");

            if(num_room_reservations == 0){
                statement = conn.prepareStatement("DELETE FROM reservations WHERE id=?");
                statement.setInt(1, thisBookingId);
                statement.executeUpdate();
                System.out.println("(Deleted booking since no room reservations were made.)");
                return;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        System.out.println("Created reservation successfully!");
    }

    private void deleteReservation() {
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

        try {
            statement = conn.prepareStatement("SELECT * FROM reservations WHERE customer=? AND start_date >= CURDATE()");
            statement.setInt(1, customerId);
            resultSet = statement.executeQuery();

            if(!resultSet.next()) {
                System.out.println("No reservations can be deleted for this user!");
                return;
            }

            System.out.println("Reservations:");

            do {
                System.out.println(resultSet.getInt("id") + ". " + resultSet.getDate("start_date") + " - " + resultSet.getDate("end_date"));
            } while(resultSet.next());

            int reservationId = getIntegerFromUser("Enter id of reservation to delete: ");

            statement = conn.prepareStatement("DELETE FROM reservations WHERE id=?");
            statement.setInt(1, reservationId);
            statement.executeUpdate();
            System.out.println("Deleted reservation");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showReservations() {
        try {
            statement = conn.prepareStatement(
                    "SELECT reservations.id, reservations.start_date, reservations.end_date, customers.name FROM reservations INNER JOIN customers ON reservations.customer=customers.id ORDER BY reservations.id DESC");
            resultSet = statement.executeQuery();
            System.out.println("id  start_date   end_date   customer");
            System.out.println("------------------------------------");
            while(resultSet.next()) {
                System.out.println(resultSet.getInt("id") + ". " + resultSet.getDate("start_date") + " - " + resultSet.getDate("end_date") + " " + resultSet.getString("name"));
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void showCustomers() {
        try {
            statement = conn.prepareStatement(
                    "SELECT * FROM customers ORDER BY id DESC");
            resultSet = statement.executeQuery();

            System.out.println("id. name\temail\t\t\t\tphone\taddress\t\t\tcity\t\tcountry");
            System.out.println("-----------------------------------------------------------------------------");

            while(resultSet.next()) {
                System.out.printf("%d. %s\t%s\t%s\t%s\t%s\t%s\n",
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("address"),
                        resultSet.getString("city"),
                        resultSet.getString("country")
                        );
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private ResultSet getAvailableRoomsInHotel(int hotelId, Date bookingStartDate, Date bookingEndDate, int roomSize) {
        try {
            statement = conn.prepareStatement(
            "SELECT * FROM room_reservations_with_dates RIGHT JOIN rooms_with_room_types ON room_id=id " +
                    "WHERE hotel = ? AND max_num_guests=? " +
                    "AND ((start_date IS NULL AND end_date IS NULL) OR (NOT (? < end_date AND ? > start_date)))");
                     /* https://stackoverflow.com/questions/2545947/ */
            statement.setInt(1, hotelId);
            statement.setInt(2, roomSize);
            statement.setDate(3, bookingStartDate);
            statement.setDate(4, bookingEndDate);
            return statement.executeQuery();
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private ResultSet getAllHotelProfiles() {
        try {
            statement = conn.prepareStatement("SELECT facility_profiles.id, CONCAT('pool:', facility_profiles.pool, ', evening_entertainment:', facility_profiles.evening_entertainment, ', kids_club:', facility_profiles.kids_club, ', restaurant:', facility_profiles.restaurant) AS profile_string FROM facility_profiles");
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

    private int getIntegerFromUser(String prompt) {
        while(true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input);
            }
            catch(NumberFormatException e) {
                System.out.println("ERROR: \"" + input  +"\" is not a valid integer! Try again!");
            }
        }
    }
}
