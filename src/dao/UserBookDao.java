package dao;

import models.UserBook;

import utils.DBConnection;
import java.sql.*;

public class UserBookDao {

    // Adds a new book entry to the user's bookshelf
    public void addUserBook(UserBook userBook) {
        String sql = "INSERT INTO user_books (user_id, book_id, status, available, is_digital, share_method, shared_note, payment_required, payment_note) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userBook.getUserId());
            stmt.setInt(2, userBook.getBookId());
            stmt.setString(3, userBook.getStatus());
            stmt.setBoolean(4, userBook.isAvailable());
            stmt.setBoolean(5, userBook.isDigital());
            stmt.setString(6, userBook.getShareMethod());
            stmt.setString(7, userBook.getSharedNote());
            stmt.setBoolean(8, userBook.isPaymentRequired());
            stmt.setString(9, userBook.getPaymentNote());

            stmt.executeUpdate();
            System.out.println("✅ Book added to your shelf!");

        } catch (SQLException e) {
            System.out.println("❌ Error adding book: " + e.getMessage());
        }
    }

    // Displays all available books from other users
    public void showAvailableBooks() {
        String sql = "SELECT ub.user_book_id, b.title, b.author, b.book_id, u.name AS owner, ub.share_method, ub.shared_note, ub.payment_note, ub.payment_required " +
                "FROM user_books ub " +
                "JOIN books b ON ub.book_id = b.book_id " +
                "JOIN users u ON ub.user_id = u.user_id " +
                "WHERE ub.status = 'owned' AND ub.available = true";
        // Note: The above query assumes that 'owned' is the status for books owned by users.

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n📚 Available Books:\n");

            // Table header
            System.out.printf("╔════════════╦════════════════════════════╦═══════════════════════╦══════════════╦══════════════╦══════════════╦═══════════════════════════╗%n");
            System.out.printf("║   Index    ║           Title            ║         Author        ║   Book ID    ║ Share Method ║ Payment Note ║ Note                      ║%n");
            System.out.printf("╠════════════╬════════════════════════════╬═══════════════════════╬══════════════╬══════════════╬══════════════╬═══════════════════════════╣%n");

            int index = 1;
            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                String title = truncate(rs.getString("title"), 26);
                String author = truncate(rs.getString("author"), 21);
                String share = truncate(rs.getString("share_method"), 12);
                String note = truncate(rs.getString("shared_note"), 25);
                String payment = truncate(rs.getString("payment_note"), 22);

                System.out.printf("║ %-10d ║ %-26s ║ %-21s ║ %-12d ║ %-12s ║ %-12s ║ %-25s ║%n",
                        index, title, author, bookId, share, payment, note);

                index++;
            }

            System.out.printf("╚════════════╩════════════════════════════╩═══════════════════════╩══════════════╩══════════════╩══════════════╩═══════════════════════════╝%n");

        } catch (SQLException e) {
            System.out.println("❌ Error retrieving books: " + e.getMessage());
        }
    }

    // Truncates long text fields to fit within display limits
    public static String truncate(String str, int max) {
        if (str == null) return "";
        return str.length() > max ? str.substring(0, max - 1) + "…" : str;
    }

    // Displays the user's own bookshelf
    public void showMyShelf(int userId) {
        String sql = "SELECT ub.user_book_id, b.title, b.author, b.book_id, ub.status, ub.available " +
                "FROM user_books ub " +
                "JOIN books b ON ub.book_id = b.book_id " +
                "WHERE ub.user_id = ?";


        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n📚 My Shelf:\n");

            // Table header
            System.out.printf("╔════════════╦════════════════════════════╦═══════════════════════╦══════════════╦══════════════╗%n");
            System.out.printf("║   Index    ║           Title            ║         Author        ║   Book ID    ║   Status     ║%n");
            System.out.printf("╠════════════╬════════════════════════════╬═══════════════════════╬══════════════╬══════════════╣%n");

            int index = 1;
            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                String title = truncate(rs.getString("title"), 26);
                String author = truncate(rs.getString("author"), 21);
                String status = truncate(rs.getString("status"), 12);

                System.out.printf("║ %-10d ║ %-26s ║ %-21s ║ %-12d ║ %-12s ║%n",
                        index, title, author, bookId, status);

                index++;
            }

            System.out.printf("╚════════════╩════════════════════════════╩═══════════════════════╩══════════════╩══════════════╝%n");

        } catch (SQLException e) {
            System.out.println("❌ Error retrieving books: " + e.getMessage());
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

            String top =    "╔════════════╦════════════════════════════════╦══════════════════════╦══════════════╦══════════════╦════════════╗";
            String header = "║ Borrow ID  ║ Book Title                     ║ Lender               ║ Borrow Date  ║ Due Date     ║ Status     ║";
            String mid =    "╠════════════╬════════════════════════════════╬══════════════════════╬══════════════╬══════════════╬════════════╣";
            String bottom = "╚════════════╩════════════════════════════════╩══════════════════════╩══════════════╩══════════════╩════════════╝";

            System.out.println(top);
            System.out.println(header);
            System.out.println(mid);

            while (rs.next()) {
                System.out.format("║ %-10s ║ %-30s ║ %-20s ║ %-12s ║ %-12s ║ %-10s ║\n",
                        rs.getInt("borrow_id"),
                        truncate(rs.getString("title"), 30),
                        truncate(rs.getString("lender_name"), 20),
                        rs.getDate("borrow_date"),
                        rs.getDate("due_date"),
                        rs.getString("status"));
            }

            System.out.println(bottom);

        } catch (Exception e) {
            System.out.println("❌ Error retrieving borrowed books: " + e.getMessage());
        }
    }


    public void removeBookFromShelf(int userId, int bookId) {
        String sql = "DELETE FROM user_books WHERE user_id = ? AND book_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Book removed from your shelf.");
            } else {
                System.out.println("⚠️ Book not found or you are not the owner.");
            }

        } catch (Exception e) {
            System.out.println("❌ Failed to delete book: " + e.getMessage());
        }
    }

    public int getLenderIdByUserBookId(int userBookId) {
        String sql = "SELECT user_id FROM user_books WHERE user_book_id = ?";
        int lenderId = -1;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userBookId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                lenderId = rs.getInt("user_id");
            }

        } catch (SQLException e) {
            System.out.println("E Error retrieving lender ID: " + e.getMessage());
        }

        return lenderId;
    }
}
