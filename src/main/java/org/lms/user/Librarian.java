package org.lms.user;

/**
 * The Librarian class represents a user with administrative privileges.
 * It extends the UserBase class and assigns the ADMIN role to the user.
 */
public class Librarian extends UserBase {

    // Constructor to initialize a librarian with username and password
    public Librarian(String username, String password) {
        super(username, password, Role.ADMIN);  // Assign the ADMIN role to the librarian
    }

    // Overriding the toString method to provide a custom string representation of the Librarian object
    @Override
    public String toString() {
        return "Librarian{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}
