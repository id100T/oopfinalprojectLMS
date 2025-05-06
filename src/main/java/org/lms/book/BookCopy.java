package org.lms.book;

/**
 * Represents a single copy of a book in the library system.
 * Each book copy has a unique copy ID, a status, and the ID of the visitor who borrowed it.
 */
public class BookCopy {
    private String copyId;  // The unique identifier for this specific copy of the book
    private BookStatus status;  // The current status of the book
    private int borrowVisitorId;  // The ID of the visitor who borrowed the book

    /**
     * Default constructor.
     * Initializes a new BookCopy with default values.
     */
    public BookCopy() {
    }

    /**
     * Constructor for creating a BookCopy with a specified copy ID.
     * By default, the status is set to AVAILABLE.
     *
     * @param copyId The unique identifier for this copy of the book
     */
    public BookCopy(String copyId) {
        this.copyId = copyId;
        this.status = BookStatus.AVAILABLE;  // Default status is AVAILABLE
    }

    /**
     * Gets the unique copy ID of this book copy.
     *
     * @return The copy ID of the book
     */
    public String getCopyId() {
        return copyId;
    }

    /**
     * Sets the unique copy ID for this book copy.
     *
     * @param copyId The copy ID to set
     */
    public void setCopyId(String copyId) {
        this.copyId = copyId;
    }

    /**
     * Gets the current status of the book copy.
     *
     * @return The status of the book copy
     */
    public BookStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of the book copy.
     *
     * @param status The status to set
     */
    public void setStatus(BookStatus status) {
        this.status = status;
    }

    /**
     * Gets the ID of the visitor who borrowed this book copy.
     * This value is only relevant when the book is borrowed.
     *
     * @return The visitor's ID who borrowed the book, or 0 if the book is not borrowed
     */
    public int getBorrowVisitorId() {
        return borrowVisitorId;
    }

    /**
     * Sets the ID of the visitor who borrowed this book copy.
     * This method is used when the book is borrowed.
     *
     * @param borrowVisitorId The visitor's ID to set
     */
    public void setBorrowVisitorId(int borrowVisitorId) {
        this.borrowVisitorId = borrowVisitorId;
    }

    /**
     * Returns a string representation of the BookCopy object.
     * This is useful for debugging and logging purposes.
     *
     * @return A string representing the book copy details
     */
    @Override
    public String toString() {
        return "BookCopy{" +
                "copyId='" + copyId + '\'' +
                ", status=" + status +
                ", borrowVisitorId=" + borrowVisitorId +
                '}';
    }
}
