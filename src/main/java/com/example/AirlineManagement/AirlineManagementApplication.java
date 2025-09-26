package com.example.AirlineManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.sql.*;

@SpringBootApplication
@CrossOrigin(origins = "*")
public class AirlineManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirlineManagementApplication.class, args);

		try (Connection conn = DBConnection.getConnection()) {
			if (conn != null) {
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM PASSENGERS LIMIT 5");

				while (rs.next()) {
					System.out.println(
							rs.getInt("passenger_id") + " " +
									rs.getString("first_name") + " " +
									rs.getString("last_name")
					);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

