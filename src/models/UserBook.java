package models;

public class UserBook {
    private int userBookId;
    private int userId;
    private int bookId;
    private String status;           // owned / borrowed
    private boolean available;
    private boolean isDigital;
    private String shareMethod;      // email / whatsapp / manual
    private String sharedNote;
    private boolean paymentRequired;
    private String paymentNote;

    // Constructor for adding a new user book
    public UserBook(int userId, int bookId, String status, boolean isDigital, String shareMethod, String sharedNote) {
        this.userId = userId;
        this.bookId = bookId;
        this.status = status;
        this.available = true; // default when added
        this.isDigital = isDigital;
        this.shareMethod = shareMethod;
        this.sharedNote = sharedNote;
    }

    // Full constructor (optional)
    public UserBook(int userBookId, int userId, int bookId, String status, boolean available,
                    boolean isDigital, String shareMethod, String sharedNote,
                    boolean paymentRequired, String paymentNote) {
        this.userBookId = userBookId;
        this.userId = userId;
        this.bookId = bookId;
        this.status = status;
        this.available = available;
        this.isDigital = isDigital;
        this.shareMethod = shareMethod;
        this.sharedNote = sharedNote;
        this.paymentRequired = paymentRequired;
        this.paymentNote = paymentNote;
    }

    // Getters and Setters

    public int getUserBookId() {
        return userBookId;
    }

    public void setUserBookId(int userBookId) {
        this.userBookId = userBookId;
    }

    public int getUserId() {
        return userId;
    }

    public int getBookId() {
        return bookId;
    }

    public String getStatus() {
        return status;
    }

    public boolean isAvailable() {
        return available;
    }

    public boolean isDigital() {
        return isDigital;
    }

    public String getShareMethod() {
        return shareMethod;
    }

    public String getSharedNote() {
        return sharedNote;
    }

    public boolean isPaymentRequired() {
        return paymentRequired;
    }

    public String getPaymentNote() {
        return paymentNote;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setDigital(boolean digital) {
        isDigital = digital;
    }

    public void setShareMethod(String shareMethod) {
        this.shareMethod = shareMethod;
    }

    public void setSharedNote(String sharedNote) {
        this.sharedNote = sharedNote;
    }

    public void setPaymentRequired(boolean paymentRequired) {
        this.paymentRequired = paymentRequired;
    }

    public void setPaymentNote(String paymentNote) {
        this.paymentNote = paymentNote;
    }
}
