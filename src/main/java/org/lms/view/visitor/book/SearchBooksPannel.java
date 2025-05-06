package org.lms.view.visitor.book;

import org.lms.book.BookBase;
import org.lms.book.BookCopy;
import org.lms.book.BookDatabase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * SearchBooksPannel provides a panel for visitors to search for books in the system.
 * It supports searching by book title, author, or ISBN, and displays the book copies in a table.
 */
public class SearchBooksPannel extends JPanel {

    private JTable booksTable;               // Table to display book information
    private JScrollPane scrollPane;          // Scroll pane for the table
    private JButton searchButton;            // Button to trigger the search
    private JTextField searchTextField;      // Text field for entering search queries

    public SearchBooksPannel() {
        // Set the layout of the panel to BorderLayout
        setLayout(new BorderLayout());

        // Initialize the search text field
        searchTextField = new JTextField(20);
        searchTextField.setPreferredSize(new Dimension(120, 30));

        // Initialize the search button and add an action listener
        searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(120, 30));
        searchButton.addActionListener(e -> updateTable());

        // Create the button panel at the top for the search box and button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(searchTextField);
        buttonPanel.add(searchButton);
        add(buttonPanel, BorderLayout.NORTH);

        // Define column names for the table
        String[] columnNames = {
                "Title", "Author",
                "ISBN", "Type",
                "Section", "Copy ID",
                "Status"
        };

        // Initialize the table and its model
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        booksTable = new JTable(tableModel);
        booksTable.setRowHeight(30);              // Set row height
        booksTable.setFillsViewportHeight(true);  // Fill the viewport height

        // Wrap the table with a scroll pane
        scrollPane = new JScrollPane(booksTable);
        add(scrollPane, BorderLayout.CENTER);

        // Load the book data initially
        updateTable();
    }

    /**
     * Updates the table based on the search query entered in the search field.
     * It supports fuzzy matching for title, author, or ISBN.
     */
    private void updateTable() {
        String searchQuery = searchTextField.getText().trim();  // Get the search query entered by the user
        BookDatabase bookDatabase = BookDatabase.getInstance(); // Get the book database instance
        List<BookBase> books = bookDatabase.getAllBooks();      // Get all books in the database

        // Get the table model and clear existing data
        DefaultTableModel tableModel = (DefaultTableModel) booksTable.getModel();
        tableModel.setRowCount(0);

        // Loop through all books
        for (BookBase book : books) {
            // Check if the book matches the search query
            boolean matchesSearch = searchQuery.isEmpty()
                    || book.getTitle().toLowerCase().contains(searchQuery.toLowerCase())
                    || book.getAuthor().toLowerCase().contains(searchQuery.toLowerCase())
                    || book.getIsbn().toLowerCase().contains(searchQuery.toLowerCase());

            if (!matchesSearch){
                continue; // Skip books that do not match the search query
            }

            // If the book matches, add its copies to the table
            for (BookCopy copy : book.getCopies()) {
                Object[] rowData = {
                        book.getTitle(),
                        book.getAuthor(),
                        book.getIsbn(),
                        book.getType(),
                        book.getSection(),
                        copy.getCopyId(),
                        copy.getStatus()
                };
                tableModel.addRow(rowData); // Add the row data to the table
            }
        }
    }
}
