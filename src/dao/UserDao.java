package dao;

import models.User;
import utils.DBConnection;

import java.sql.*;
import java.util.List;

public class UserDao {

    // Register a new user
    public void registerUser(User user) {
        String sql = "INSERT INTO users (name, email, password_hash, location) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPasswordHash());
            stmt.setString(4, user.getLocation());

            stmt.executeUpdate();
            System.out.println("✅ Registered successfully!");

        } catch (SQLException e) {
            System.out.println("❌ Registration error: " + e.getMessage());
        }
    }

    // Login and return user_id if credentials match
    public int loginUser(String email, String hashedPassword) {
        String sql = "SELECT user_id FROM users WHERE email = ? AND password_hash = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, hashedPassword);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("✅ Login successful. Welcome!");
                return rs.getInt("user_id");
            } else {
                System.out.println("❌ Invalid credentials.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Login error: " + e.getMessage());
        }

        return -1; // login failed
    }

    // Optional: Fetch all other user IDs (for notifications)
    public List<Integer> getAllUserIdsExcept(int excludeUserId) {
        List<Integer> ids = new java.util.ArrayList<>();
        String sql = "SELECT user_id FROM users WHERE user_id != ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, excludeUserId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ids.add(rs.getInt("user_id"));
            }

        } catch (SQLException e) {
            System.out.println("❌ Error fetching users: " + e.getMessage());
        }
        return ids;
    }
}
