package models;

public class Review {
    private int userBookId;
    private int reviewerId;
    private int rating;
    private String comment;

    public Review(int userBookId, int reviewerId, int rating, String comment) {
        this.userBookId = userBookId;
        this.reviewerId = reviewerId;
        this.rating = rating;
        this.comment = comment;
    }

    // Getters
    public int getUserBookId() { return userBookId; }
    public int getReviewerId() { return reviewerId; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
}
