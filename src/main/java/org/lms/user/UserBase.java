package org.lms.user;

/**
 * UserBase class represents a basic user with essential properties such as username, password, and role.
 * It serves as a parent class for other specific user types like `Visitor`, `Librarian`, etc.
 */
public class UserBase {
    String username; // The username of the user
    String password; // The password of the user
    Role role; // The role of the user

    /**
     * Constructor to initialize the UserBase object with a username, password, and role.
     *
     * @param username The username of the user
     * @param password The password of the user
     * @param role The role of the user
     */
    public UserBase(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Default constructor for UserBase.
     */
    public UserBase() {

    }

    // Getters and Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Returns a string representation of the UserBase object.
     *
     * @return A string containing the username, password, and role.
     */
    @Override
    public String toString() {
        return "UserBase{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}
