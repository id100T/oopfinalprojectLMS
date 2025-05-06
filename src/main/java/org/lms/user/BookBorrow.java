package org.lms.user;

import java.util.Date;

/**
 * The BookBorrow class represents a borrowing record for a specific book copy.
 */
public class BookBorrow {
    private String isbn;             // The ISBN of the book
    private String copyId;           // The ID of the book copy
    private BorrowStatus status;     // The status (borrowed or returned)
    private Date borrowTime;         // The time the book was borrowed
    private Date returnTime;         // The time the book was returned

    // Getter and setter for the ISBN
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    // Getter and setter for the copy ID
    public String getCopyId() {
        return copyId;
    }

    public void setCopyId(String copyId) {
        this.copyId = copyId;
    }

    // Getter and setter for the status
    public BorrowStatus getStatus() {
        return status;
    }

    public void setStatus(BorrowStatus status) {
        this.status = status;
    }

    // Getter and setter for the borrow time
    public Date getBorrowTime() {
        return borrowTime;
    }

    public void setBorrowTime(Date borrowTime) {
        this.borrowTime = borrowTime;
    }

    // Getter and setter for the return time
    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    @Override
    public String toString() {
        return "BookBorrow{" +
                "isbn='" + isbn + '\'' +
                ", copyId='" + copyId + '\'' +
                ", status=" + status +
                ", borrowTime=" + borrowTime +
                ", returnTime=" + returnTime +
                '}';
    }
}
