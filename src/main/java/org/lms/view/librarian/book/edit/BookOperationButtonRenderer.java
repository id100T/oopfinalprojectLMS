package org.lms.view.librarian.book.edit;

import org.lms.view.librarian.LibrarianFrame;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;

/**
 * BookOperationButtonRenderer is a custom renderer and editor for rendering and editing
 * buttons within a table cell. It provides buttons for operations such as edit, delete,
 * borrow, and return for each row in the table.
 */
public class BookOperationButtonRenderer extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
    private JPanel panel; // Panel that contains the buttons
    private JButton editButton; // Button for editing a book
    private JButton deleteButton; // Button for deleting a book
    private JButton borrowButton; // Button for borrowing a book
    private JButton returnButton; // Button for returning a book
    private LibrarianFrame parentFrame; // Reference to the parent frame (LibrarianFrame)
    private int currentRow; // Current row index in the table

    /**
     * Constructor for BookOperationButtonRenderer.
     * Initializes the panel and buttons, and sets up action listeners for button clicks.
     *
     * @param parentFrame The parent frame that will handle the button click actions.
     */
    public BookOperationButtonRenderer(LibrarianFrame parentFrame) {
        this.parentFrame = parentFrame;

        // Initialize the panel and buttons
        panel = new JPanel(new GridBagLayout());
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        borrowButton = new JButton("Borrow");
        returnButton = new JButton("Return");

        // Set up action listeners for each button
        editButton.addActionListener(e -> {
            fireEditingStopped(); // Stop editing when button is clicked
            parentFrame.onEditButtonClicked(currentRow); // Trigger the edit action on the parent frame
        });

        deleteButton.addActionListener(e -> {
            fireEditingStopped(); // Stop editing when button is clicked
            parentFrame.onDeleteButtonClicked(currentRow); // Trigger the delete action on the parent frame
        });

        borrowButton.addActionListener(e -> {
            fireEditingStopped(); // Stop editing when button is clicked
            parentFrame.onBorrowButtonClicked(currentRow); // Trigger the borrow action on the parent frame
        });

        returnButton.addActionListener(e -> {
            fireEditingStopped(); // Stop editing when button is clicked
            parentFrame.onReturnButtonClicked(currentRow); // Trigger the return action on the parent frame
        });

        // Add buttons to the panel
        panel.add(editButton);
        panel.add(deleteButton);
        panel.add(borrowButton);
        panel.add(returnButton);
    }

    /**
     * This method is used by the JTable to render the button panel within the table cell.
     *
     * @param table The JTable that contains the cell
     * @param value The value of the cell (not used in this case)
     * @param isSelected Whether the cell is selected
     * @param hasFocus Whether the cell has focus
     * @param row The row index of the cell
     * @param column The column index of the cell
     * @return The component to be rendered, which is the panel containing buttons
     */
    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {
        return panel; // Return the panel containing the buttons
    }

    /**
     * This method is used by the JTable to set the button panel for editing the cell.
     *
     * @param table The JTable that contains the cell
     * @param value The value of the cell (not used in this case)
     * @param isSelected Whether the cell is selected
     * @param row The row index of the cell
     * @param column The column index of the cell
     * @return The component to be used for editing, which is the panel containing buttons
     */
    @Override
    public Component getTableCellEditorComponent(
            JTable table, Object value,
            boolean isSelected,
            int row, int column) {
        this.currentRow = row; // Set the current row when editing starts
        return panel; // Return the panel containing the buttons
    }

    /**
     * This method is used by the JTable to get the value of the cell after editing.
     * In this case, it does not return any value as the cell is used for button operations.
     *
     * @return null, as the cell does not have a value to return
     */
    @Override
    public Object getCellEditorValue() {
        return null; // No value is associated with this cell
    }
}
