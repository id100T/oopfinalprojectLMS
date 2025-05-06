package org.lms.user;

/**
 * The BorrowStatus enum represents the status of a book borrow/return action.
 */
public enum BorrowStatus {
    BORROW("Borrow"),   // Represents a borrowed book
    RETURN("Return");   // Represents a returned book

    private final String status; // The string representation of the status

    // Constructor to assign the status string to each enum constant
    BorrowStatus(String status) {
        this.status = status;
    }

    // Getter to retrieve the status value
    public String getStatus() {
        return this.status;
    }

    // Override toString to return the status as a string
    @Override
    public String toString() {
        return status;
    }
}
