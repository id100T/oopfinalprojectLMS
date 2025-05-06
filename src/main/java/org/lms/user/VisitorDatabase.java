package org.lms.user;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Singleton class that manages the visitor database.
 * Handles operations such as adding, editing, deleting visitors, and managing book borrow/return actions.
 */
public class VisitorDatabase {
    private static VisitorDatabase instance;
    private List<Visitor> visitors;
    private static final String DATABASE_FILE = "VisitorDatabase.json";

    /**
     * Private constructor to prevent direct instantiation.
     * Initializes the list of visitors and loads data from the file.
     */
    private VisitorDatabase() {
        visitors = new ArrayList<>();
        loadFromFile();
    }

    /**
     * Returns the singleton instance of the VisitorDatabase.
     *
     * @return The single instance of VisitorDatabase
     */
    public static synchronized VisitorDatabase getInstance() {
        if (instance == null) {
            instance = new VisitorDatabase();
        }
        return instance;
    }

    /**
     * Adds a new visitor to the database.
     *
     * @param visitor The visitor to be added
     */
    public void addVisitor(Visitor visitor) {
        int newVisitorId = getNextVisitorId(); // Get the next available visitor ID
        visitor.setVisitorId(newVisitorId); // Set the ID for the new visitor
        visitors.add(visitor); // Add visitor to the list
        saveToFile(); // Save the updated list to the file
    }

    /**
     * Generates the next available visitor ID by finding the highest existing ID and incrementing it.
     *
     * @return The next visitor ID
     */
    private int getNextVisitorId() {
        int maxId = 1000;
        for (Visitor v : visitors) {
            if (v.getVisitorId() > maxId) {
                maxId = v.getVisitorId();
            }
        }
        return maxId + 1;
    }

    /**
     * Edits an existing visitor's information.
     *
     * @param editedVisitor The visitor object with updated information
     * @return true if the edit is successful, false otherwise
     */
    public boolean editVisitor(Visitor editedVisitor) {
        for (int i = 0; i < visitors.size(); i++) {
            if (visitors.get(i).getVisitorId() == editedVisitor.getVisitorId()) {
                visitors.set(i, editedVisitor); // Update the visitor
                saveToFile(); // Save the changes to the file
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes a visitor by their ID.
     *
     * @param visitorId The ID of the visitor to be deleted
     * @return true if the visitor is deleted successfully, false otherwise
     */
    public boolean deleteVisitor(int visitorId) {
        for (int i = 0; i < visitors.size(); i++) {
            if (visitors.get(i).getVisitorId() == visitorId) {
                visitors.remove(i); // Remove the visitor
                saveToFile(); // Save the updated list to the file
                return true;
            }
        }
        return false;
    }

    /**
     * Finds a visitor by their ID.
     *
     * @param visitorId The ID of the visitor
     * @return The visitor object, or null if not found
     */
    public Visitor findVisitorById(int visitorId) {
        for (Visitor visitor : visitors) {
            if (visitor.getVisitorId() == visitorId) {
                return visitor;
            }
        }
        return null;
    }

    /**
     * Finds a visitor by their username.
     *
     * @param username The username of the visitor
     * @return The visitor object, or null if not found
     */
    public Visitor findVisitorByUsername(String username) {
        for (Visitor visitor : visitors) {
            if (visitor.getUsername().equals(username)) {
                return visitor;
            }
        }
        return null;
    }

    /**
     * Allows a visitor to borrow a book by ISBN and copy ID.
     *
     * @param isbn The ISBN of the book to borrow
     * @param copyId The ID of the book copy to borrow
     * @param visitorId The ID of the visitor borrowing the book
     * @return true if the borrow operation is successful, false otherwise
     */
    public boolean borrowBook(String isbn, String copyId, int visitorId) {
        Visitor visitor = findVisitorById(visitorId);
        if (visitor != null) {
            BookBorrow bookBorrow = new BookBorrow();
            bookBorrow.setIsbn(isbn);
            bookBorrow.setCopyId(copyId);
            bookBorrow.setStatus(BorrowStatus.BORROW);
            bookBorrow.setBorrowTime(new Date());
            visitor.getBookBorrows().add(bookBorrow); // Add the book borrow to the visitor's record
            saveToFile(); // Save the updated data to the file
            return true;
        }
        return false;
    }

    /**
     * Allows a visitor to return a borrowed book by ISBN and copy ID.
     *
     * @param isbn The ISBN of the book to return
     * @param copyId The ID of the book copy to return
     * @param visitorId The ID of the visitor returning the book
     * @return true if the return operation is successful, false otherwise
     */
    public boolean returnBook(String isbn, String copyId, int visitorId) {
        Visitor visitor = findVisitorById(visitorId);
        if (visitor != null) {
            for (BookBorrow bookBorrow : visitor.getBookBorrows()) {
                if (bookBorrow.getIsbn().equals(isbn) && bookBorrow.getCopyId().equals(copyId)) {
                    if (bookBorrow.getStatus() == BorrowStatus.BORROW) {
                        bookBorrow.setStatus(BorrowStatus.RETURN);
                        bookBorrow.setReturnTime(new Date());
                        saveToFile(); // Save the updated data to the file
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Saves the list of visitors to a file in JSON format.
     */
    private void saveToFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(DATABASE_FILE), visitors); // Write the list of visitors to the file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the list of visitors from the file.
     */
    private void loadFromFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new File(DATABASE_FILE);
            if (file.exists()) {
                visitors = objectMapper.readValue(file,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, Visitor.class)); // Read the list of visitors from the file
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
