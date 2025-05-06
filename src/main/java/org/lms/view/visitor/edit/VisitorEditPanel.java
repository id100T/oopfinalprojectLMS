package org.lms.view.visitor.edit;

import org.lms.user.Gender;
import org.lms.user.Visitor;

import javax.swing.*;
import java.awt.*;

/**
 * VisitorEditPanel represents a form panel for editing a visitor's profile.
 * It allows modification of full name, gender, age, and address.
 */
public class VisitorEditPanel extends JPanel {
    // Input fields for visitor details
    private JTextField nameField;
    private JComboBox<Gender> genderField;
    private JTextField ageField;
    private JTextField addressField;

    // Constructor takes a Visitor object and pre-fills fields with existing data
    public VisitorEditPanel(Visitor visitor) {
        // Use a GridLayout with 2 columns and dynamic rows, 5px gaps
        setLayout(new GridLayout(0, 2, 5, 5));

        // Full Name input
        add(new JLabel("Full Name:"));
        nameField = new JTextField(visitor.getFullName());
        add(nameField);

        // Gender dropdown
        add(new JLabel("Gender:"));
        genderField = new JComboBox<>(Gender.values());
        genderField.setSelectedItem(visitor.getGender());
        add(genderField);

        // Age input
        add(new JLabel("Age:"));
        ageField = new JTextField(String.valueOf(visitor.getAge()));
        add(ageField);

        // Address input
        add(new JLabel("Address:"));
        addressField = new JTextField(visitor.getAddress());
        add(addressField);
    }

    // Updates the given Visitor object with new values from the input fields
    public boolean updateVisitor(Visitor visitor) {
        try {
            // Parse age input
            int age = Integer.parseInt(ageField.getText().trim());

            // Update visitor fields with values from input
            visitor.setFullName(nameField.getText().trim());
            visitor.setGender((Gender) genderField.getSelectedItem());
            visitor.setAge(age);
            visitor.setAddress(addressField.getText().trim());

            return true; // Update successful
        } catch (NumberFormatException e) {
            // Show error dialog if age is not a valid number
            JOptionPane.showMessageDialog(this,
                    "Age must be a valid number.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
