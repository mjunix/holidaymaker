package com.company;

import java.sql.*;

public class Holidaymaker {
    ResultSet resultSet;
    PreparedStatement statement;
    Connection conn;

    public Holidaymaker() throws SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://localhost/holidaymaker?user=root&password=toor&serverTimezone=UTC");
    }
}
