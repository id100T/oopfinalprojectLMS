package org.lms.view.visitor.book;

import org.lms.book.BookBase;
import org.lms.book.BookDatabase;
import org.lms.user.BookBorrow;
import org.lms.user.BorrowStatus;
import org.lms.user.Visitor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * BorrowQueryPannel provides a panel for visitors to view their borrowed books.
 * It displays information about each borrowed book, including the borrow date and return status.
 */
public class BorrowQueryPannel extends JPanel {

    private JTable borrowTable;            // Table to display borrowed books
    private JScrollPane scrollPane;       // Scroll pane for the borrow table
    private Visitor visitor;              // The visitor whose borrow information is being displayed

    /**
     * Constructor to initialize the panel and display the borrow information for the given visitor.
     * @param visitor The visitor whose borrowed books will be shown.
     */
    public BorrowQueryPannel(Visitor visitor) {
        this.visitor = visitor;

        // Set the layout of the panel to BorderLayout
        setLayout(new BorderLayout());

        // Define column names for the borrow table
        String[] columnNames = {
                "Title", "Author", "Type", "Section",
                "ISBN", "Copy ID", "Borrow Date", "Return Date"
        };

        // Initialize the table model with the column names
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        borrowTable = new JTable(tableModel);
        borrowTable.setRowHeight(30);                // Set row height for better readability
        borrowTable.setFillsViewportHeight(true);    // Fill the viewport height with the table

        // Wrap the table with a scroll pane
        scrollPane = new JScrollPane(borrowTable);
        add(scrollPane, BorderLayout.CENTER);

        // Update the table with borrow data
        updateTable();
    }

    /**
     * Updates the borrow table with the visitor's current borrow information.
     * This includes details about each borrowed book, including borrow dates and return status.
     */
    private void updateTable() {
        DefaultTableModel tableModel = (DefaultTableModel) borrowTable.getModel();
        tableModel.setRowCount(0);  // Clear existing rows in the table

        // Loop through all books borrowed by the visitor
        for (BookBorrow borrow : visitor.getBookBorrows()) {
            // Find the book in the database by ISBN
            BookBase book = BookDatabase.getInstance().findBookByIsbn(borrow.getIsbn());
            if (book == null) {
                continue;  // Skip if the book is not found in the database
            }

            // Create a row of data for the table
            Object[] rowData = {
                    book.getTitle(),                    // Book title
                    book.getAuthor(),                   // Book author
                    book.getType(),                     // Book type
                    book.getSection(),                  // Book section
                    borrow.getIsbn(),                   // ISBN of the borrowed book
                    borrow.getCopyId(),                 // Copy ID of the borrowed book
                    borrow.getBorrowTime(),             // Borrow date
                    borrow.getStatus() == BorrowStatus.BORROW ? "Not returned" : borrow.getReturnTime()  // Return date (if returned)
            };

            // Add the row data to the table model
            tableModel.addRow(rowData);
        }

        // Set minimum widths for the "Borrow Date" and "Return Date" columns
        borrowTable.getColumnModel().getColumn(6).setMinWidth(200);
        borrowTable.getColumnModel().getColumn(7).setMinWidth(200);
    }
}
