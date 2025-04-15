package models;

public class Payment {
    private int borrowId;
    private double amount;
    private String method;
    private String note;
    private String status;

    public Payment(int borrowId, double amount, String method, String note, String status) {
        this.borrowId = borrowId;
        this.amount = amount;
        this.method = method;
        this.note = note;
        this.status = status;
    }

    public Payment(int borrowId, double amount, boolean isPaymentRequired) {
    }

    public int getBorrowId() {
        return borrowId;
    }

    public double getAmount() {
        return amount;
    }

    public String getMethod() {
        return method;
    }

    public String getNote() {
        return note;
    }

    public String getStatus() {
        return status;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
