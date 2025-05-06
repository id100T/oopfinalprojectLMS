package org.lms.book;

/**
 * The BookStatus enum represents the availability status of a book in the library.
 */
public enum BookStatus {
    AVAILABLE("Available"),   // The book is available for borrowing
    UNAVAILABLE("Unavailable");  // The book is not available for borrowing

    private final String status;  // The status of the book

    // Constructor to initialize the book status
    BookStatus(String status) {
        this.status = status;
    }

    // Getter method to return the book status
    public String getStatus() {
        return this.status;
    }

    // Override toString to return the book status as a string
    @Override
    public String toString() {
        return status;
    }
}
