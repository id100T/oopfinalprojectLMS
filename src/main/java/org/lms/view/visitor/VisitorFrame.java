package org.lms.view.visitor;

import org.lms.user.BookBorrow;
import org.lms.user.BorrowStatus;
import org.lms.user.Visitor;
import org.lms.user.VisitorDatabase;
import org.lms.view.MainFrame;
import org.lms.view.visitor.book.BorrowQueryPannel;
import org.lms.view.visitor.book.SearchBooksPannel;
import org.lms.view.visitor.edit.VisitorEditPanel;

import javax.swing.*;
import java.awt.*;

/**
 * VisitorFrame represents the main user interface for a logged-in visitor.
 * It provides functionalities such as modifying or deleting account info,
 * searching books, and querying borrow records, as well as logging out.
 */
public class VisitorFrame extends JFrame {
    private JButton modifyAccountButton;
    private JButton deleteAccountButton;
    private JButton searchBooksButton;
    private JButton borrowQueryButton;
    private JButton backButton;
    private JPanel buttonPanel;

    public Visitor visitor;

    // Constructor: initializes the UI with provided visitor
    public VisitorFrame(Visitor visitor) {
        this.visitor = visitor;
        setTitle("Visitor");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Top panel with visitor ID display
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel idLabel = new JLabel("ID: " + visitor.getVisitorId());
        idLabel.setFont(new Font("Arial", Font.BOLD, 14));
        topPanel.add(idLabel);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center panel with functional buttons
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add Modify Account button
        modifyAccountButton = createButton("Modify Account");
        modifyAccountButton.addActionListener(e -> onModifyButtonClicked());
        buttonPanel.add(modifyAccountButton);

        // Add Delete Account button
        deleteAccountButton = createButton("Delete Account");
        deleteAccountButton.addActionListener(e -> onDeleteButtonClicked());
        buttonPanel.add(deleteAccountButton);

        // Add Search Books button
        searchBooksButton = createButton("Search Books");
        searchBooksButton.addActionListener(e -> onSearchBooksButtonClicked());
        buttonPanel.add(searchBooksButton);

        // Add Borrow Query button
        borrowQueryButton = createButton("Borrow Query");
        borrowQueryButton.addActionListener(e -> onBorrowQueryButtonClicked());
        buttonPanel.add(borrowQueryButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Bottom panel with Logout button
        backButton = new JButton("Logout");
        backButton.setFont(new Font("Arial", Font.PLAIN, 13));
        backButton.addActionListener(e -> onBackButtonClicked());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    // Helper method to create styled buttons
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(200, 40));
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        return button;
    }

    // Action when Modify Account button is clicked
    private void onModifyButtonClicked() {
        VisitorEditPanel editPanel = new VisitorEditPanel(visitor);
        int result = JOptionPane.showConfirmDialog(this, editPanel, "Edit Account",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            if (editPanel.updateVisitor(visitor)) {
                boolean success = VisitorDatabase.getInstance().editVisitor(visitor);
                if (success) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Account updated successfully.",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Failed to update account.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
    }

    // Action when Delete Account button is clicked
    private void onDeleteButtonClicked() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete your account?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Ensure all borrowed books are returned before deletion
            for (BookBorrow bookBorrow : visitor.getBookBorrows()) {
                if (bookBorrow.getStatus() == BorrowStatus.BORROW) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Please return all borrowed books before deleting the account.",
                            "Cannot Delete Account",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }
            }

            boolean success = VisitorDatabase.getInstance().deleteVisitor(visitor.getVisitorId());
            if (success) {
                JOptionPane.showMessageDialog(
                        this,
                        "Account deleted successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                onBackButtonClicked(); // Redirect to login screen
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to delete account.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    // Opens the book search dialog
    private void onSearchBooksButtonClicked() {
        JDialog searchBooksDialog = new JDialog(this, "Search Books", true);
        searchBooksDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        SearchBooksPannel searchPanel = new SearchBooksPannel();
        searchBooksDialog.getContentPane().add(searchPanel);
        searchBooksDialog.setSize(1000, 600);
        searchBooksDialog.setLocationRelativeTo(this);
        searchBooksDialog.setVisible(true);
    }

    // Opens the borrow query dialog
    private void onBorrowQueryButtonClicked() {
        JDialog searchBooksDialog = new JDialog(this, "Borrow Query", true);
        searchBooksDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        BorrowQueryPannel borrowQueryPannel = new BorrowQueryPannel(visitor);
        searchBooksDialog.getContentPane().add(borrowQueryPannel);
        searchBooksDialog.setSize(1000, 600);
        searchBooksDialog.setLocationRelativeTo(this);
        searchBooksDialog.setVisible(true);
    }

    // Logout and return to login screen
    private void onBackButtonClicked() {
        dispose();
        MainFrame.open();
    }

    // Static method to open this frame
    public static void open(Visitor visitor) {
        new VisitorFrame(visitor).setVisible(true);
    }
}
