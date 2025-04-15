package dao;

import models.Notification;
import utils.DBConnection;

import java.sql.*;

public class NotificationDao {

    public void createNotification(Notification notification) {
        String sql = "INSERT INTO notifications (user_id, message) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, notification.getUserId());
            stmt.setString(2, notification.getMessage());
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("❌ Failed to create notification: " + e.getMessage());
        }
    }

    public void showUnread(int userId) {
        String sql = "SELECT notification_id, message FROM notifications WHERE user_id = ? AND is_read = false";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("📢 Notification: " + rs.getString("message"));
                markAsRead(rs.getInt("notification_id"));
            }

        } catch (Exception e) {
            System.out.println("❌ Error showing notifications: " + e.getMessage());
        }
    }

    public void markAsRead(int notificationId) {
        String sql = "UPDATE notifications SET is_read = true WHERE notification_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, notificationId);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("❌ Error marking notification as read: " + e.getMessage());
        }
    }
    public void cleanOldNotifications(int userId) {
        String sql = "DELETE FROM notifications " +
                "WHERE user_id = ? AND notification_id NOT IN (" +
                "  SELECT notification_id FROM (" +
                "    SELECT notification_id FROM notifications " +
                "    WHERE user_id = ? ORDER BY created_at DESC LIMIT 15" +
                "  ) AS recent" +
                ")";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId); // First ?
            stmt.setInt(2, userId); // Second ?

            stmt.executeUpdate();
            System.out.println("🧹 Old notifications cleaned!");

        } catch (Exception e) {
            System.out.println("❌ Error cleaning notifications: " + e.getMessage());
        }
    }




    public void showAll(int userId) {
        String sql = "SELECT notification_id, message FROM notifications WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("📢 Notification: " + rs.getString("message"));
            }

        } catch (Exception e) {
            System.out.println("❌ Error showing notifications: " + e.getMessage());
        }
    }
}
