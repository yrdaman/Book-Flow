package models;

import java.sql.Date;

public class Borrow {
    private int borrowId;
    private int lenderId;
    private int borrowerId;
    private int userBookId;
    private Date borrowDate;
    private Date dueDate;
    private Date actualReturnDate;
    private String status;






    public Borrow(int borrowId, int lenderId, int borrowerId, int userBookId, Date borrowDate, Date dueDate,String status) {
        this.borrowId = borrowId;
        this.lenderId = lenderId;
        this.borrowerId = borrowerId;
        this.userBookId = userBookId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    public int getBorrowId() {
        return borrowId;
    }
    public void setBorrowId(int borrowId) {
        this.borrowId = borrowId;
    }
    public int getLenderId() {
        return lenderId;
    }
    public void setLenderId(int lenderId) {
        this.lenderId = lenderId;
    }
    public int getBorrowerId() {
        return borrowerId;
    }
    public void setBorrowerId(int borrowerId) {
        this.borrowerId = borrowerId;
    }
    public int getUserBookId() {
        return userBookId;
    }
    public void setUserBookId(int userBookId) {
        this.userBookId = userBookId;
    }
    public Date getBorrowDate() {
        return borrowDate;
    }
    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }
    public Date getDueDate() {
        return dueDate;
    }
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

}
