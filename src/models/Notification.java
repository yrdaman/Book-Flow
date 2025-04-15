package models;

public class Notification {
    private  final int userId;
    private final String message;

    public Notification(int userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public int getUserId() { return userId; }
    public String getMessage() { return message; }
}
