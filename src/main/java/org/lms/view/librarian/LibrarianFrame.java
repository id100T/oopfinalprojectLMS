package org.lms.view.librarian;

import org.lms.book.BookStatus;
import org.lms.user.BookBorrow;
import org.lms.user.BorrowStatus;
import org.lms.user.Visitor;
import org.lms.user.VisitorDatabase;
import org.lms.view.MainFrame;
import org.lms.view.librarian.book.edit.BookOperationButtonRenderer;
import org.lms.view.librarian.book.edit.EditBookPanel;
import org.lms.book.BookBase;
import org.lms.book.BookCopy;
import org.lms.book.BookDatabase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * LibrarianFrame provides the interface for the librarian to manage books.
 * It supports adding, searching, borrowing, returning, and deleting books.
 */
public class LibrarianFrame extends JFrame {

    private JTable booksTable;               // Table to display books and their copies
    private JScrollPane scrollPane;          // Scroll pane for the books table
    private JButton addBooksButton;          // Button to add new books
    private JButton backButton;              // Button to go back to the main frame
    private JButton searchButton;            // Button to initiate the search
    private JTextField searchTextField;      // Text field for entering search queries

    private List<BookBase> tableBookBaseRowList = new ArrayList<BookBase>();  // Stores books displayed in the table
    private List<BookCopy> tableBookCopyRowList = new ArrayList<BookCopy>();  // Stores book copies displayed in the table

    /**
     * Constructor to initialize the Librarian frame.
     * Sets up UI components, layout, and event listeners.
     */
    public LibrarianFrame() {
        setTitle("Librarian");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Add books button to open the book addition dialog
        addBooksButton = new JButton("Add Books");
        addBooksButton.setPreferredSize(new Dimension(120, 30));
        addBooksButton.addActionListener(e -> onAddBooksButtonClicked());

        // Search field for entering search queries
        searchTextField = new JTextField(20);
        searchTextField.setPreferredSize(new Dimension(120, 30));

        // Search button to trigger the search
        searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(120, 30));
        searchButton.addActionListener(e -> updateTable());

        // Back button to return to the main frame
        backButton = new JButton("Logout");
        backButton.setPreferredSize(new Dimension(120, 30));
        backButton.addActionListener(e -> onBackButtonClicked());

        // Panel containing all buttons at the top
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addBooksButton);
        buttonPanel.add(searchTextField);
        buttonPanel.add(searchButton);
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        // Table to display book information
        String[] columnNames = {
                "Title", "Author",
                "ISBN", "Type",
                "Section", "Copy ID",
                "Status", "Operation"
        };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        booksTable = new JTable(tableModel);
        booksTable.setFillsViewportHeight(true);
        scrollPane = new JScrollPane(booksTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        booksTable.setRowHeight(30);

        // Set the content of the window
        setContentPane(mainPanel);

        // Update the table initially with the available book data
        updateTable();
    }

    /**
     * Updates the table based on the current search query.
     * Filters books by title, author, or ISBN.
     */
    public void updateTable() {
        String searchQuery = searchTextField.getText().trim();

        // Get the instance of the book database
        BookDatabase bookDatabase = BookDatabase.getInstance();
        List<BookBase> books = bookDatabase.getAllBooks();

        // Get the table model and reset the table rows
        DefaultTableModel tableModel = (DefaultTableModel) booksTable.getModel();
        tableModel.setRowCount(0);
        tableBookBaseRowList = new ArrayList<>();
        tableBookCopyRowList = new ArrayList<>();

        // Loop through each book to check if it matches the search query
        for (BookBase book : books) {
            boolean matchesSearchQuery = false;
            if (searchQuery.isEmpty()) {
                matchesSearchQuery = true;
            } else {
                if (book.getTitle().toLowerCase().contains(searchQuery.toLowerCase()) ||
                        book.getAuthor().toLowerCase().contains(searchQuery.toLowerCase()) ||
                        book.getIsbn().toLowerCase().contains(searchQuery.toLowerCase())) {
                    matchesSearchQuery = true;
                }
            }

            // Skip books that do not match the search query
            if (!matchesSearchQuery) {
                continue;
            }

            // Add book copies to the table
            for (BookCopy copy : book.getCopies()) {
                Object[] rowData = {
                        book.getTitle(),
                        book.getAuthor(),
                        book.getIsbn(),
                        book.getType(),
                        book.getSection(),
                        copy.getCopyId(),
                        copy.getStatus(),
                        "Operation"
                };
                tableModel.addRow(rowData);
                tableBookBaseRowList.add(book);
                tableBookCopyRowList.add(copy);
            }
        }

        // Set custom renderer and editor for the operation column
        booksTable.getColumnModel()
                .getColumn(7)
                .setCellRenderer(new BookOperationButtonRenderer(this));
        booksTable.getColumnModel()
                .getColumn(7)
                .setCellEditor(new BookOperationButtonRenderer(this));
        booksTable.getColumnModel()
                .getColumn(7).setMinWidth(300);
    }

    /**
     * Opens the dialog to add a new book.
     */
    private void onAddBooksButtonClicked() {
        JDialog addBookDialog = new JDialog(this, "Add New Book", true);
        addBookDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Create the EditBookPanel and pass a callback to update the table after adding a book
        EditBookPanel editBookPanel = new EditBookPanel(() -> updateTable());

        addBookDialog.getContentPane().add(editBookPanel);
        addBookDialog.setSize(400, 300);
        addBookDialog.setLocationRelativeTo(this);
        addBookDialog.setVisible(true);
    }

    /**
     * Handles the event when the edit button is clicked for a specific book.
     * Opens the edit book dialog for modifying the selected book's details.
     *
     * @param row the row index of the selected book
     */
    public void onEditButtonClicked(int row) {
        BookBase bookToEdit = tableBookBaseRowList.get(row);

        // Open the edit book dialog
        JDialog editDialog = new JDialog(this, "Edit Book", true);
        EditBookPanel editBookPanel = new EditBookPanel(() -> updateTable(), bookToEdit);
        editDialog.add(editBookPanel);
        editDialog.setSize(400, 300);
        editDialog.setLocationRelativeTo(this);
        editDialog.setVisible(true);
    }

    /**
     * Handles the event when the borrow button is clicked for a specific book copy.
     * It checks availability, asks for visitor ID, and processes the borrow request.
     *
     * @param row the row index of the selected book copy
     */
    public void onBorrowButtonClicked(int row) {
        BookBase bookBase = tableBookBaseRowList.get(row);
        BookCopy bookCopy = tableBookCopyRowList.get(row);

        // Check if the book copy is available for borrowing
        if (bookCopy.getStatus() == BookStatus.UNAVAILABLE) {
            JOptionPane.showMessageDialog(
                    null,
                    "This book has already been borrowed.",
                    "Unavailable",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Request the visitor ID
        String visitorIdInput = JOptionPane.showInputDialog(
                null,
                "Enter your Visitor ID:",
                "Borrow Book",
                JOptionPane.QUESTION_MESSAGE
        );

        // Validate the visitor ID
        if (visitorIdInput == null || visitorIdInput.trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    "Borrowing cancelled or ID is empty.",
                    "Cancelled",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        int visitorId;
        try {
            visitorId = Integer.parseInt(visitorIdInput.trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "Invalid ID format. Please enter a valid number.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Check if the visitor exists
        Visitor visitor = VisitorDatabase.getInstance().findVisitorById(visitorId);
        if (visitor == null) {
            JOptionPane.showMessageDialog(
                    null,
                    "Visitor ID not found. Please enter a valid ID.",
                    "Invalid Visitor",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Check if the visitor has reached their borrowing limit
        int bookBorrowCount = 0;
        for (BookBorrow bookBorrow : visitor.getBookBorrows()) {
            if (bookBorrow.getStatus() == BorrowStatus.BORROW) {
                bookBorrowCount++;
            }
            if (bookBorrowCount >= 10) {
                break;
            }
        }
        if (bookBorrowCount >= 10) {
            JOptionPane.showMessageDialog(
                    null,
                    "Each visitor can borrow up to 10 books.",
                    "Borrow Limit Reached",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Process the borrow request
        boolean success = BookDatabase.getInstance().borrowBook(
                bookBase.getIsbn(),
                bookCopy.getCopyId(),
                visitor.getVisitorId()
        );
        if (!success) {
            JOptionPane.showMessageDialog(
                    null,
                    "This book has already been borrowed.",
                    "Unavailable",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Update the visitor's borrowed books
        VisitorDatabase.getInstance().borrowBook(
                bookBase.getIsbn(),
                bookCopy.getCopyId(),
                visitor.getVisitorId()
        );

        JOptionPane.showMessageDialog(
                null,
                "Book borrowed successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );

        // Update the table to reflect the borrow operation
        updateTable();
    }

    /**
     * Handles the event when the return button is clicked for a specific book copy.
     * It checks the current status and processes the return request.
     *
     * @param row the row index of the selected book copy
     */
    public void onReturnButtonClicked(int row) {
        BookBase bookBase = tableBookBaseRowList.get(row);
        BookCopy bookCopy = tableBookCopyRowList.get(row);

        // Check if the book copy is not borrowed
        if (bookCopy.getStatus() == BookStatus.AVAILABLE) {
            JOptionPane.showMessageDialog(
                    null,
                    "This book has already been returned.",
                    "Already Returned",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Return the book and update the visitor's borrowing status
        BookDatabase.getInstance().returnBook(
                bookBase.getIsbn(),
                bookCopy.getCopyId()
        );

        int visitorId = bookCopy.getBorrowVisitorId();

        Visitor visitor = VisitorDatabase.getInstance().findVisitorById(visitorId);
        if (visitor == null) {
            return;
        }

        VisitorDatabase.getInstance().returnBook(
                bookBase.getIsbn(),
                bookCopy.getCopyId(),
                visitor.getVisitorId()
        );

        JOptionPane.showMessageDialog(
                null,
                "Book returned successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );

        // Update the table to reflect the borrow operation
        updateTable();
    }

    /**
     * Handles the event when the delete button is clicked for a specific book copy.
     *
     * @param row the row index of the selected book copy
     */
    public void onDeleteButtonClicked(int row) {
        BookBase bookBase = tableBookBaseRowList.get(row);
        BookCopy bookCopy = tableBookCopyRowList.get(row);

        // Ask for confirmation before deleting
        int option = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this book?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {

            if (bookCopy.getStatus() == BookStatus.UNAVAILABLE) {
                JOptionPane.showMessageDialog(
                        null,
                        "Please return the book before deleting it.!",
                        "Error",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            // Delete the book copy
            BookDatabase bookDatabase = BookDatabase.getInstance();
            bookDatabase.deleteBook(bookBase.getIsbn(), bookCopy.getCopyId());

            updateTable();
        }
    }

    /**
     * Handles the event when the back button is clicked.
     * Returns to the main frame.
     */
    private void onBackButtonClicked() {
        dispose();
        MainFrame.open();
    }

    /**
     * Opens the librarian frame.
     */
    public static void open() {
        new LibrarianFrame().setVisible(true);
    }
}
