package dao;

import models.Book;
import utils.DBConnection;

import java.sql.*;

public class BookDao {
    public int addOrFindBook(Book book) {
        String selectSQL = "SELECT book_id FROM books WHERE title = ? AND author = ?";
        String insertSQL = "INSERT INTO books (title, author, genre, isbn, publication_year) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement selectStmt = conn.prepareStatement(selectSQL);
            selectStmt.setString(1, book.getTitle());
            selectStmt.setString(2, book.getAuthor());
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) return rs.getInt("book_id");

            PreparedStatement insertStmt = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            insertStmt.setString(1, book.getTitle());
            insertStmt.setString(2, book.getAuthor());
            insertStmt.setString(3, book.getGenre());
            insertStmt.setString(4, book.getIsbn());
            insertStmt.setInt(5, book.getPublicationYear());

            insertStmt.executeUpdate();
            ResultSet keys = insertStmt.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }
}
