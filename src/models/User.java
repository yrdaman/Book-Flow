package models;

public class User {
    private int userId;
    private String name;
    private String email;
    private String passwordHash;
    private String location;

    // Constructor (for registration)
    public User(String name, String email, String passwordHash, String location) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.location = location;
    }

    // Constructor (with ID, optional)
    public User(int userId, String name, String email, String passwordHash, String location) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.location = location;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
