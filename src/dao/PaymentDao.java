package dao;

import models.Payment;
import utils.DBConnection;

import java.sql.*;

public class PaymentDao {

    public void createPayment(Payment payment) {
        String sql = "INSERT INTO payments (borrow_id, amount, payment_method, payment_note, payment_status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, payment.getBorrowId());
            stmt.setDouble(2, payment.getAmount());
            stmt.setString(3, payment.getMethod());
            stmt.setString(4, payment.getNote());
            stmt.setString(5, payment.getStatus());
            stmt.executeUpdate();
            System.out.println("✅ Payment record created.");
        } catch (Exception e) {
            System.out.println("❌ Error creating payment: " + e.getMessage());
        }
    }

    public void markPaymentPaid(int borrowId) {
        String sql = "UPDATE payments SET payment_status = 'paid' WHERE borrow_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, borrowId);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Payment marked as paid.");
            } else {
                System.out.println("⚠️ No payment record found.");
            }
        } catch (Exception e) {
            System.out.println("❌ Error updating payment: " + e.getMessage());
        }
    }

    public void showTransactionsForUser(int userId) {
        String sql = "SELECT bk.title, p.amount, p.payment_method, p.payment_note, p.payment_status, p.created_at " +
                "FROM payments p " +
                "JOIN borrows b ON p.borrow_id = b.borrow_id " +
                "JOIN user_books ub ON b.user_book_id = ub.user_book_id " +
                "JOIN books bk ON ub.book_id = bk.book_id " +
                "WHERE b.borrower_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n📋 Transaction History:\n");

            // Table Header
            System.out.printf("╔════╦════════════════════════════╦════════════╦════════════╦════════════╦════════════════════════════╦══════════════════════╗%n");
            System.out.printf("║ ID ║          Title             ║   Amount   ║   Method   ║   Status   ║            Note            ║         Date         ║%n");
            System.out.printf("╠════╬════════════════════════════╬════════════╬════════════╬════════════╬════════════════════════════╬══════════════════════╣%n");

            int index = 1;
            while (rs.next()) {
                String title = truncate(rs.getString("title"), 28);
                String method = truncate(rs.getString("payment_method"), 10);
                String note = truncate(rs.getString("payment_note"), 26);
                String status = truncate(rs.getString("payment_status"), 10);
                String date = rs.getTimestamp("created_at").toString();
                double amount = rs.getDouble("amount");

                // Blue ANSI color code
                String blue = "\u001B[34m";
                String reset = "\u001B[0m";

                String amountDisplay = (amount == 0.0) ? blue + "Free Flow" + reset : String.format("₹%.2f", amount);

                System.out.printf("║ %-2d ║ %-26s ║ %-10s ║ %-10s ║ %-10s ║ %-26s ║ %-20s ║%n",
                        index, title, amountDisplay, method, status, note, date);
                index++;
            }

            // Footer
            System.out.printf("╚════╩════════════════════════════╩════════════╩════════════╩════════════╩════════════════════════════╩══════════════════════╝%n");

        } catch (Exception e) {
            System.out.println("❌ Error fetching transactions: " + e.getMessage());
        }
    }

    private String truncate(String str, int max) {
        if (str == null) return "";
        return str.length() > max ? str.substring(0, max - 1) + "…" : str;
    }


    public void showPendingPaymentsForUser(int userId) {
        String sql = "SELECT bk.title, p.amount, p.payment_method, p.payment_note, p.created_at " +
                "FROM payments p " +
                "JOIN borrows b ON p.borrow_id = b.borrow_id " +
                "JOIN user_books ub ON b.user_book_id = ub.user_book_id " +
                "JOIN books bk ON ub.book_id = bk.book_id " +
                "WHERE b.borrower_id = ? AND p.payment_status = 'pending'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n🕓 Pending Payments:");
            while (rs.next()) {
                System.out.println("📚 Book: " + rs.getString("title"));
                System.out.println("💸 Amount: ₹" + rs.getDouble("amount"));
                System.out.println("📲 Method: " + rs.getString("payment_method"));
                System.out.println("🔗 Note: " + rs.getString("payment_note"));
                System.out.println("🕒 Date: " + rs.getTimestamp("created_at"));
                System.out.println("--------------------------------------------");
            }
        } catch (Exception e) {
            System.out.println("❌ Error fetching pending payments: " + e.getMessage());
        }
    }
}
