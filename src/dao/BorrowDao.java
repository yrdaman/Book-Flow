package dao;

import models.Borrow;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BorrowDao {
    // Method to request a borrow
    public int requestBorrow(Borrow borrow) {
        if (borrow.getLenderId() == borrow.getBorrowerId()) {
            System.out.println("⚠️ You cannot borrow your own book.");
            return -1;
        }

        String sql = "INSERT INTO borrows (lender_id, borrower_id, user_book_id, borrow_date, due_date, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, borrow.getLenderId());
            stmt.setInt(2, borrow.getBorrowerId());
            stmt.setInt(3, borrow.getUserBookId());
            stmt.setDate(4, borrow.getBorrowDate());
            stmt.setDate(5, borrow.getDueDate());
            stmt.setString(6, borrow.getStatus());

            stmt.executeUpdate();

            var keys = stmt.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);

        } catch (SQLException e) {
            System.out.println("❌ Borrow insert failed: " + e.getMessage());
        }

        return -1;
    }

    // Method to view all borrow requests for a specific lender
    public void viewMyRequests(int borrowerId) {
        String sql = "SELECT b.borrow_id, bk.title, u.name AS lender_name, b.borrow_date, b.due_date, b.status " +
                "FROM borrows b " +
                "JOIN user_books ub ON b.user_book_id = ub.user_book_id " +
                "JOIN books bk ON ub.book_id = bk.book_id " +
                "JOIN users u ON b.lender_id = u.user_id " +
                "WHERE b.borrower_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, borrowerId);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n📋 Your Borrow Requests:");
            System.out.println("══════════════════════════════════════════════════════════════════════════════════════════════════");
            System.out.printf("%-10s | %-30s | %-15s | %-12s | %-12s | %-10s%n",
                    "Borrow ID", "Book Title", "Lender", "From", "To", "Status");
            System.out.println("══════════════════════════════════════════════════════════════════════════════════════════════════");

            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                System.out.printf("%-10d | %-30s | %-15s | %-12s | %-12s | %-10s%n",
                        rs.getInt("borrow_id"),
                        rs.getString("title"),
                        rs.getString("lender_name"),
                        rs.getDate("borrow_date"),
                        rs.getDate("due_date"),
                        rs.getString("status"));
            }

            if (!hasResults) {
                System.out.println("🙅‍♂️ No borrow requests found.");
            }

            System.out.println("══════════════════════════════════════════════════════════════════════════════════════════════════");

        } catch (Exception e) {
            System.out.println("❌ Error retrieving borrow requests: " + e.getMessage());
        }
    }

    // Method for returning a book
    public void returnBook(int borrowId) {
        String sql = "UPDATE borrows SET status = 'returned', actual_return_date = CURDATE() WHERE borrow_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, borrowId);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("✅ Book successfully marked as returned.");
            } else {
                System.out.println("⚠️ No record found or already returned.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error returning book: " + e.getMessage());
        }
    }
    // Method to respond to a borrow request
    public void respondToRequest(int borrowId, boolean accept) {
        String newStatus = accept ? "accepted" : "rejected";
        String sql = "UPDATE borrows SET status = ? WHERE borrow_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setInt(2, borrowId);

            int updated = stmt.executeUpdate();
            if (updated > 0) {
                System.out.println("✅ Borrow request " + newStatus + ".");
            } else {
                System.out.println("⚠️ Borrow ID not found.");
            }

        } catch (Exception e) {
            System.out.println("❌ Failed to respond: " + e.getMessage());
        }
    }


    public int getUserBookIdByBorrowId(int returnBorrowId) {
        String sql = "SELECT user_book_id FROM borrows WHERE borrow_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, returnBorrowId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("user_book_id");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error retrieving user book ID: " + e.getMessage());
        }
        return -1;
    }

    public void showMyRequests(int userId) {
        String sql = "SELECT b.borrow_id, bk.title, u.name AS borrower_name, b.borrow_date, b.due_date, b.status " +
                "FROM borrows b " +
                "JOIN user_books ub ON b.user_book_id = ub.user_book_id " +
                "JOIN books bk ON ub.book_id = bk.book_id " +
                "JOIN users u ON b.borrower_id = u.user_id " +
                "WHERE b.lender_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n📋 Your Borrow Requests:");
            while (rs.next()) {
                System.out.println("Borrow ID: " + rs.getInt("borrow_id") +
                        ", Book: " + rs.getString("title") +
                        ", Borrower: " + rs.getString("borrower_name") +
                        ", From: " + rs.getDate("borrow_date") +
                        ", To: " + rs.getDate("due_date") +
                        ", Status: " + rs.getString("status"));
            }

        } catch (Exception e) {
            System.out.println("❌ Error retrieving borrow requests: " + e.getMessage());
        }
    }

    public void acceptRequest(int borrowId) {
        String sql = "UPDATE borrows SET status = 'accepted' WHERE borrow_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, borrowId);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("✅ Borrow request accepted.");
            } else {
                System.out.println("⚠️ No record found or already accepted.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error accepting request: " + e.getMessage());
        }
    }

    public void rejectRequest(int borrowId) {
        String sql = "UPDATE borrows SET status = 'rejected' WHERE borrow_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, borrowId);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("✅ Borrow request rejected.");
            } else {
                System.out.println("⚠️ No record found or already rejected.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error rejecting request: " + e.getMessage());
        }
    }

    public void showBorrowedBooks(int userId) {
        String sql = "SELECT b.borrow_id, bk.title, u.name AS lender_name, b.borrow_date, b.due_date, b.status " +
                "FROM borrows b " +
                "JOIN user_books ub ON b.user_book_id = ub.user_book_id " +
                "JOIN books bk ON ub.book_id = bk.book_id " +
                "JOIN users u ON b.lender_id = u.user_id " +
                "WHERE b.borrower_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n📚 Your Borrowed Books:");

            String format = "║ %-10s ║ %-30s ║ %-15s ║ %-12s ║ %-12s ║ %-10s ║%n";
            String separator = "╠" + "════════════╬" + "════════════════════════════════╬" +"═════════════════╬" + "══════════════╬" + "══════════════╬" + "════════════╣";

            // Header
            System.out.println("╔════════════╦════════════════════════════════╦═════════════════╦══════════════╦══════════════╦════════════╗");
            System.out.printf(format, "Borrow ID", "Book Title", "Lender", "From", "To", "Status");
            System.out.println(separator);

            // Rows
            boolean hasRows = false;
            while (rs.next()) {
                hasRows = true;
                System.out.printf(format,
                        rs.getInt("borrow_id"),
                        rs.getString("title"),
                        rs.getString("lender_name"),
                        rs.getDate("borrow_date"),
                        rs.getDate("due_date"),
                        rs.getString("status"));
            }

            if (!hasRows) {
                System.out.println("║ No borrowed books found.                                                                  ║");
            }

            // Footer
            System.out.println("╚════════════╩════════════════════════════════╩═════════════════╩══════════════╩══════════════╩════════════╝");

        } catch (Exception e) {
            System.out.println("❌ Error retrieving borrowed books: " + e.getMessage());
        }
    }

}
