package org.lms.view.librarian.book.edit;

import org.lms.book.*;

import javax.swing.*;
import java.awt.*;

/**
 * EditBookPanel is a JPanel used for adding or editing book details in the library system.
 * It provides fields to input book details such as author, title, ISBN, book type, section, and quantity.
 * The panel supports both adding a new book and editing an existing one.
 */
public class EditBookPanel extends JPanel {
    private JTextField authorField, titleField, isbnField; // Text fields for author, title, and ISBN.
    private JComboBox<BookType> typeComboBox; // Combo box for selecting book type.
    private JComboBox<Section> sectionComboBox; // Combo box for selecting section.
    private JTextField quantityField; // Text field for specifying quantity.

    private BookEditListener listener = null; // Listener to notify when the book has been edited.
    private BookBase bookToEdit = null; // Book object to edit if provided.

    /**
     * Default constructor, initializes the panel with no listener or book to edit.
     */
    public EditBookPanel() {
        this(null, null);
    }

    /**
     * Constructor that initializes the panel with a listener for edit events.
     * @param listener BookEditListener instance.
     */
    public EditBookPanel(BookEditListener listener) {
        this(listener, null);
    }

    /**
     * Constructor that initializes the panel with a listener and a book to edit.
     * @param listener BookEditListener instance.
     * @param bookToEdit BookBase instance representing the book to edit.
     */
    public EditBookPanel(BookEditListener listener, BookBase bookToEdit) {
        this.listener = listener;
        this.bookToEdit = bookToEdit;

        setLayout(new GridBagLayout()); // Using GridBagLayout for component arrangement.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Adding space around components.

        // Author label and text field
        JLabel authorLabel = new JLabel("Author:");
        authorField = new JTextField(20); // Create a text field for the author name.
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(authorLabel, gbc); // Add label to the grid.

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(authorField, gbc); // Add text field to the grid.

        // Title label and text field
        JLabel titleLabel = new JLabel("Title:");
        titleField = new JTextField(20); // Create a text field for the book title.
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(titleLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(titleField, gbc);

        // ISBN label and text field
        JLabel isbnLabel = new JLabel("ISBN:");
        isbnField = new JTextField(20); // Create a text field for ISBN.
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add(isbnLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(isbnField, gbc);

        // Book type label and combo box
        JLabel typeLabel = new JLabel("Book Type:");
        typeComboBox = new JComboBox<>(BookType.values()); // Create combo box for book types.
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        add(typeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(typeComboBox, gbc);

        // Section label and combo box
        JLabel sectionLabel = new JLabel("Section:");
        sectionComboBox = new JComboBox<>(Section.values()); // Create combo box for sections.
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        add(sectionLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(sectionComboBox, gbc);

        // Quantity label and text field
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityField = new JTextField(5); // Create a text field for quantity input.
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        add(quantityLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        add(quantityField, gbc);

        // Add/Edit button
        JButton addButton = new JButton(bookToEdit == null ? "Add Book" : "Edit Book"); // Button changes based on bookToEdit.
        addButton.setAlignmentX(CENTER_ALIGNMENT);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        add(addButton, gbc);

        // Button action listener for adding or editing a book
        addButton.addActionListener(e -> {
            String author = authorField.getText(); // Get author from text field.
            String title = titleField.getText(); // Get title from text field.
            String isbn = isbnField.getText(); // Get ISBN from text field.
            BookType type = (BookType) typeComboBox.getSelectedItem(); // Get selected book type.
            Section section = (Section) sectionComboBox.getSelectedItem(); // Get selected section.
            int quantity;


            // Validate required fields
            if (author.isEmpty() || type == null || title.isEmpty() || isbn.isEmpty() || section == null) {
                JOptionPane.showMessageDialog(this,
                        "Please complete all fields.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            return;
            }

            try {
                quantity = Integer.parseInt(quantityField.getText()); // Parse quantity.
                if (quantity <= 0) { // Ensure quantity is positive.
                    JOptionPane.showMessageDialog(null, "Quantity must be a positive number.");
                    return;
                }
            } catch (NumberFormatException ex) { // Handle invalid quantity input.
                JOptionPane.showMessageDialog(null, "Invalid quantity.");
                return;
            }


    

            // If editing an existing book, update its details. Otherwise, add a new book.
            if (bookToEdit == null) {
                BookBase newBook = new BookBase(title, author, isbn, type, section, quantity);
                BookDatabase bookDatabase = BookDatabase.getInstance();
                bookDatabase.addBook(newBook); // Add new book to the database.
                JOptionPane.showMessageDialog(null, "Book added successfully!");
                clearFields(); // Clear input fields.
            } else {
                bookToEdit.setAuthor(author);
                bookToEdit.setTitle(title);
                bookToEdit.setIsbn(isbn);
                bookToEdit.setType(type);
                bookToEdit.setSection(section);

                BookDatabase bookDatabase = BookDatabase.getInstance();
                bookDatabase.editBook(bookToEdit); // Edit existing book in the database.

                JOptionPane.showMessageDialog(null, "Book updated successfully!");
            }

            // Notify listener if available.
            if (listener != null) {
                listener.onBookEdited();
            }
        });

        // If bookToEdit is provided, pre-fill the fields with the existing book's data.
        if (bookToEdit != null) {
            authorField.setText(bookToEdit.getAuthor());
            titleField.setText(bookToEdit.getTitle());
            isbnField.setText(bookToEdit.getIsbn());
            typeComboBox.setSelectedItem(bookToEdit.getType());
            sectionComboBox.setSelectedItem(bookToEdit.getSection());
            quantityField.setText(String.valueOf(bookToEdit.getCopies().size())); // Show quantity based on copies.

            // Disable editing of quantity fields for existing books.
           // isbnField.setEditable(false);
            quantityField.setEditable(false);
        }
    }

    /**
     * Clears the input fields in the panel.
     */
    private void clearFields() {
        authorField.setText("");
        titleField.setText("");
        isbnField.setText("");
        quantityField.setText("");
    }
}
