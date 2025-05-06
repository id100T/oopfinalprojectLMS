package org.lms.view;

import org.lms.user.Role;
import org.lms.user.Visitor;
import org.lms.user.VisitorDatabase;
import org.lms.view.librarian.LibrarianFrame;
import org.lms.view.visitor.VisitorFrame;
import org.lms.view.visitor.register.VisitorRegisterPanel;

import javax.swing.*;
import java.awt.*;

/**
 * MainFrame represents the login window for the Library Management System.
 * It supports login for both Admin and Visitor roles, and provides visitor registration.
 */
public class MainFrame extends JFrame {

    // admin username and password
    String ADMIN_USERNAME = "admin";
    String ADMIN_PASSWORD = "123456";

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<Role> roleComboBox;
    private JButton registerButton;

    /**
     * Constructor sets up the login GUI and components.
     */
    public MainFrame() {
        try {
            // Set the look and feel to the system's native appearance
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        setTitle("Library Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 330);
        setLocationRelativeTo(null);// Center the window

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Title label
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Username field with titled border
        usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameField.setBorder(BorderFactory.createTitledBorder("Username"));

        // Password field with titled border
        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));

        // Role selection combo box (Admin or Visitor)
        roleComboBox = new JComboBox<>(Role.values());
        roleComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        roleComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        roleComboBox.setBorder(BorderFactory.createTitledBorder("Select Role"));

        // Register button, only visible when Visitor role is selected
        registerButton = new JButton("Register");
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setVisible(false);
        registerButton.addActionListener(e -> openVisitorRegistration());

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(e -> handleLogin());

        // Show register button only if role is VISITOR
        roleComboBox.addActionListener(e -> {
            Role selectedRole = (Role) roleComboBox.getSelectedItem();
            registerButton.setVisible(selectedRole == Role.VISITOR);
        });

        // Add all components to the panel
        panel.add(usernameField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(passwordField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(roleComboBox);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(registerButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(loginButton);

        // Set panel as the content pane
        setContentPane(panel);
    }

    /**
     * Handles login logic based on username, password, and role.
     */
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        Role selectedRole = (Role) roleComboBox.getSelectedItem();

//        if (selectedRole == Role.ADMIN) {
//            dispose();
//            LibrarianFrame.open();
//            return;
//        }
//        if (selectedRole == Role.VISITOR) {
//            dispose();
//            Visitor visitor = VisitorDatabase.getInstance().getAllVisitors().get(0);
//            VisitorFrame.open(visitor);
//            return;
//        }

        // uasername and password basic validation
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Username and password cannot be empty.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Admin login
        if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password) && selectedRole == Role.ADMIN) {
            dispose();// Close login window
            LibrarianFrame.open(); // Open librarian interface
            return;
        }
        // Visitor login
        else if (selectedRole == Role.VISITOR) {
            Visitor visitor = VisitorDatabase.getInstance().findVisitorByUsername(username);
            if (visitor != null) {
                if (visitor.getPassword().equals(password)) {
                    dispose();// Close login window
                    VisitorFrame.open(visitor);  // Open visitor interface
                }
            }
            return;
        }

        // Login failed
        JOptionPane.showMessageDialog(this,
                "Login Failed.",
                "Login Failed",
                JOptionPane.ERROR_MESSAGE);

    }

    /**
     * Opens the visitor registration panel in a modal dialog.
     */
    private void openVisitorRegistration() {
        JDialog registerDialog = new JDialog(this, "Visitor Registration", true);
        registerDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        VisitorRegisterPanel registerPanel = new VisitorRegisterPanel();
        registerDialog.getContentPane().add(registerPanel);
        registerDialog.setSize(400, 500);
        // Center on MainFrame
        registerDialog.setLocationRelativeTo(this);
        registerDialog.setVisible(true);
    }

    /**
     * Launches the MainFrame (login window).
     */
    public static void open() {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
