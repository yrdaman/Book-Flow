package dao;

import models.Review;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Date;

public class ReviewDao {
    public void addReview(Review review) {
        String sql = "INSERT INTO book_reviews (user_book_id, reviewer_id, rating, comment, review_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, review.getUserBookId());
            stmt.setInt(2, review.getReviewerId());
            stmt.setInt(3, review.getRating());
            stmt.setString(4, review.getComment());
            stmt.setDate(5, new Date(System.currentTimeMillis()));

            stmt.executeUpdate();
            System.out.println("✅ Review added successfully!");

        } catch (Exception e) {
            System.out.println("❌ Failed to leave review: " + e.getMessage());
        }
    }


}
