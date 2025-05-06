package org.lms.view.visitor.register;

import org.lms.user.Gender;
import org.lms.user.Role;
import org.lms.user.VisitorDatabase;
import org.lms.user.Visitor;

import javax.swing.*;
import java.awt.*;

/**
 * VisitorRegisterPanel represents the registration form for new visitors.
 * It collects user information such as name, gender, age, contact details,
 * username, and password, and registers the user into the system.
 */

public class VisitorRegisterPanel extends JPanel {

    public VisitorRegisterPanel() {
        // Set vertical layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // Add padding

        // Create input field for full name
        JTextField fullNameField = new JTextField();
        fullNameField.setBorder(BorderFactory.createTitledBorder("Full Name"));

        // Create combo box for gender selection
        JComboBox<Gender> genderField = new JComboBox<>(Gender.values());
        genderField.setBorder(BorderFactory.createTitledBorder("Gender"));

        // Create input field for age
        JTextField ageField = new JTextField();
        ageField.setBorder(BorderFactory.createTitledBorder("Age"));

        // Create input field for phone number
        JTextField phoneField = new JTextField();
        phoneField.setBorder(BorderFactory.createTitledBorder("Phone"));

        // Create input field for address
        JTextField addressField = new JTextField();
        addressField.setBorder(BorderFactory.createTitledBorder("Address"));

        // Create input field for username
        JTextField usernameField = new JTextField();
        usernameField.setBorder(BorderFactory.createTitledBorder("Username"));

        // Create input field for password
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));

        // Create register button
        JButton registerButton = new JButton("Register");
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button

        // Add action listener for registration logic
        registerButton.addActionListener(e -> {
            // Get user input values
            String fullName = fullNameField.getText().trim();
            Gender gender = (Gender) genderField.getSelectedItem();
            String ageText = ageField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            // Validate required fields
            if (fullName.isEmpty() || gender == null || ageText.isEmpty() || phone.isEmpty()
                    || address.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please complete all fields.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate age is a number
            int age;
            try {
                age = Integer.parseInt(ageText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Invalid age. Please enter a number.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check for duplicate username
            if (VisitorDatabase.getInstance().findVisitorByUsername(username) != null) {
                JOptionPane.showMessageDialog(this,
                        "Username already exists.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create and register new visitor
            Visitor visitor = new Visitor(username, password, fullName, gender, age, phone, address);
            VisitorDatabase.getInstance().addVisitor(visitor);

            // Notify user of success
            JOptionPane.showMessageDialog(this,
                    "Registration successful!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            // Close the registration window
            Window topLevelWindow = SwingUtilities.getWindowAncestor(this);
            if (topLevelWindow != null) {
                topLevelWindow.dispose();
            }
        });

        // Add all components to the panel with spacing
        add(fullNameField);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(genderField);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(ageField);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(phoneField);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(addressField);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(usernameField);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(passwordField);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(registerButton);
    }
}
