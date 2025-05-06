package org.lms.book;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the base information for a book in the library system.
 * This includes book details like title, author, ISBN, type, section, and a list of book copies.
 */
public class BookBase {
    private String title;  // The title of the book
    private String author;  // The author of the book
    private String isbn;  // The ISBN of the book (unique identifier)
    private BookType type;  // The type/category of the book
    private Section section;  // The section of the library where the book belongs
    private List<BookCopy> copies;  // List of copies of this book available in the library

    /**
     * Default constructor for BookBase.
     * Initializes the book object without setting its properties.
     */
    public BookBase() {
    }

    /**
     * Constructor for creating a BookBase with all required properties.
     * This also generates the specified number of book copies based on the ISBN.
     *
     * @param title     The title of the book
     * @param author    The author of the book
     * @param isbn      The ISBN of the book
     * @param type      The type/category of the book
     * @param section   The section where the book is located in the library
     * @param numCopies The number of copies of this book available in the library
     */
    public BookBase(String title, String author, String isbn, BookType type, Section section, int numCopies) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.type = type;
        this.section = section;
        this.copies = new ArrayList<>();

        // Generate copies of the book with unique copy IDs
        for (int i = 1; i <= numCopies; i++) {
            String copyId = isbn + "-" + i;
            copies.add(new BookCopy(copyId));
        }
    }

    /**
     * Attempts to borrow the first available copy of the book.
     * If an available copy is found, its status is set to UNAVAILABLE and true is returned.
     *
     * @return True if a copy was successfully borrowed, false otherwise
     */
    public boolean borrow() {
        for (BookCopy bookCopy : copies) {
            if (bookCopy.getStatus() == BookStatus.AVAILABLE) {
                bookCopy.setStatus(BookStatus.UNAVAILABLE);
                return true;
            }
        }
        return false;  // No available copies to borrow
    }

    /**
     * Returns a borrowed book by changing the status of the corresponding copy to AVAILABLE.
     *
     * @param copyId The unique ID of the book copy being returned
     */
    public void returnBook(String copyId) {
        for (BookCopy bookCopy : copies) {
            if (bookCopy.getCopyId().equals(copyId)) {
                bookCopy.setStatus(BookStatus.AVAILABLE);  // Mark the copy as available again
                break;
            }
        }
    }

    // Getter and Setter methods for all fields

    /**
     * Gets the title of the book.
     *
     * @return The title of the book
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the book.
     *
     * @param title The title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the author of the book.
     *
     * @return The author of the book
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author of the book.
     *
     * @param author The author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets the ISBN of the book.
     *
     * @return The ISBN of the book
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Sets the ISBN of the book.
     *
     * @param isbn The ISBN to set
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Gets the type/category of the book.
     *
     * @return The type of the book
     */
    public BookType getType() {
        return type;
    }

    /**
     * Sets the type/category of the book.
     *
     * @param type The type to set
     */
    public void setType(BookType type) {
        this.type = type;
    }

    /**
     * Gets the section where the book is located in the library.
     *
     * @return The section of the book
     */
    public Section getSection() {
        return section;
    }

    /**
     * Sets the section where the book is located in the library.
     *
     * @param section The section to set
     */
    public void setSection(Section section) {
        this.section = section;
    }

    /**
     * Gets the list of copies available for this book.
     *
     * @return The list of book copies
     */
    public List<BookCopy> getCopies() {
        return copies;
    }

    /**
     * Sets the list of copies for this book.
     *
     * @param copies The list of copies to set
     */
    public void setCopies(List<BookCopy> copies) {
        this.copies = copies;
    }

    /**
     * Returns a string representation of the BookBase object.
     * This is useful for debugging and logging purposes.
     *
     * @return A string representing the book details
     */
    @Override
    public String toString() {
        return "BookBase{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", type=" + type +
                ", section=" + section +
                ", copies=" + copies +
                '}';
    }
}
