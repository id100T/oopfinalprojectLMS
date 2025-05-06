package org.lms.book;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A singleton class representing a book database.
 * This class provides methods to manage books, including adding, editing, deleting, borrowing, and returning books.
 */
public class BookDatabase {
    private static BookDatabase instance;  // Singleton instance of BookDatabase
    private List<BookBase> bookBases;  // List to store all the books in the database
    private static final String DATABASE_FILE = "BookDatabase.json";  // Path to the configuration file storing the books

    /**
     * Private constructor for initializing the book list and loading data from file.
     */
    private BookDatabase() {
        bookBases = new ArrayList<>();
        loadFromFile();
    }

    /**
     * Gets the singleton instance of the BookDatabase.
     * If the instance does not exist, it creates a new one.
     *
     * @return The singleton instance of BookDatabase
     */
    public static synchronized BookDatabase getInstance() {
        if (instance == null) {
            instance = new BookDatabase();
        }
        return instance;
    }

    /**
     * Adds a new book to the database or adds copies to an existing book.
     * If the book already exists, it updates the book by adding new copies.
     *
     * @param bookBase The book to be added to the database
     */
    public void addBook(BookBase bookBase) {
        boolean exists = false;
        for (BookBase existingBookBase : bookBases) {
            if (existingBookBase.getIsbn().equals(bookBase.getIsbn())) {
                // Find the highest existing copy ID and generate new ones for the new copies
                int maxId = 0;
                for (BookCopy copy : existingBookBase.getCopies()) {
                    try {
                        String[] split = copy.getCopyId().split("-");
                        int id = Integer.parseInt(split[split.length - 1]);
                        if (id > maxId) {
                            maxId = id;
                        }
                    } catch (NumberFormatException ignored) {

                    }
                }

                // Add new copies to the existing book
                for (int i = 0; i < bookBase.getCopies().size(); i++) {
                    BookCopy bookCopy = new BookCopy();
                    String copyId = existingBookBase.getIsbn() + "-" + (maxId + i + 1);
                    bookCopy.setCopyId(copyId);
                    bookCopy.setStatus(BookStatus.AVAILABLE);
                    existingBookBase.getCopies().add(bookCopy);
                }

                exists = true;
                break;
            }
        }

        // If the book doesn't exist, add it as a new entry
        if (!exists) {
            bookBases.add(bookBase);
        }

        saveToFile();
    }

    /**
     * Edits an existing book in the database.
     * The book's author, title, type, and section are updated.
     *
     * @param editedBookBase The edited book details
     * @return true if the book was successfully edited, false otherwise
     */
    public boolean editBook(BookBase editedBookBase) {
        BookBase bookBase = findBookByIsbn(editedBookBase.getIsbn());
        if (bookBase != null) {
            bookBase.setAuthor(editedBookBase.getAuthor());
            bookBase.setTitle(editedBookBase.getTitle());
            bookBase.setType(editedBookBase.getType());
            bookBase.setSection(editedBookBase.getSection());
            saveToFile();
            return true;
        }
        return false;
    }

    /**
     * Finds a book by its ISBN.
     *
     * @param isbn The ISBN of the book to search for
     * @return The BookBase object if found, otherwise null
     */
    public BookBase findBookByIsbn(String isbn) {
        for (BookBase bookBase : bookBases) {
            if (bookBase.getIsbn().equals(isbn)) {
                return bookBase;
            }
        }
        return null;
    }

    /**
     * Gets all the books in the database.
     *
     * @return A list of all books in the database
     */
    public List<BookBase> getAllBooks() {
        return new ArrayList<>(bookBases);
    }

    /**
     * Deletes a book or a specific copy of a book from the database.
     * If all copies of a book are removed, the entire book is deleted.
     *
     * @param isbn   The ISBN of the book to delete
     * @param copyId The ID of the copy to delete
     * @return true if the book or copy was successfully deleted, false otherwise
     */
    public boolean deleteBook(String isbn, String copyId) {
        BookBase bookBase = findBookByIsbn(isbn);
        if (bookBase != null) {
            List<BookCopy> copies = bookBase.getCopies();
            for (BookCopy copy : copies) {
                if (copy.getCopyId().equals(copyId)) {
                    copies.remove(copy);
                    if (copies.isEmpty()) {
                        bookBases.remove(bookBase);
                    }
                    saveToFile();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Saves the current state of the book database to a JSON file.
     */
    private void saveToFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(DATABASE_FILE), bookBases);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the book data from the JSON file into the database.
     */
    private void loadFromFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new File(DATABASE_FILE);
            if (file.exists()) {
                bookBases = objectMapper.readValue(
                        file,
                        objectMapper.getTypeFactory()
                                .constructCollectionType(List.class, BookBase.class)
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Borrows a book by changing the status of the specified copy to UNAVAILABLE and associating it with a visitor.
     *
     * @param isbn      The ISBN of the book to borrow
     * @param copyId    The copy ID of the book to borrow
     * @param visitorId The ID of the visitor borrowing the book
     * @return true if the book was successfully borrowed, false otherwise
     */
    public boolean borrowBook(String isbn, String copyId, int visitorId) {
        for (BookBase bookBase : bookBases) {
            if (bookBase.getIsbn().equals(isbn)) {
                for (BookCopy copy : bookBase.getCopies()) {
                    if (copy.getCopyId().equals(copyId)) {
                        if (copy.getStatus() == BookStatus.AVAILABLE) {
                            copy.setStatus(BookStatus.UNAVAILABLE);
                            copy.setBorrowVisitorId(visitorId);
                            saveToFile();
                            return true;
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns a borrowed book by changing the status of the specified copy to AVAILABLE.
     *
     * @param isbn   The ISBN of the book being returned
     * @param copyId The copy ID of the book being returned
     * @return true if the book was successfully returned, false otherwise
     */
    public boolean returnBook(String isbn, String copyId) {
        for (BookBase bookBase : bookBases) {
            if (bookBase.getIsbn().equals(isbn)) {
                for (BookCopy copy : bookBase.getCopies()) {
                    if (copy.getCopyId().equals(copyId)) {
                        if (copy.getStatus() == BookStatus.UNAVAILABLE) {
                            copy.setStatus(BookStatus.AVAILABLE);
                            saveToFile();
                            return true;
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        return false;
    }
}
