package org.lms.user;

import java.util.ArrayList;
import java.util.List;

/**
 * Visitor class extends UserBase and represents a library visitor.
 * It includes personal details, book borrow history, and methods to manage visitor information.
 */
public class Visitor extends UserBase {
    private int visitorId;
    private String fullName;
    private Gender gender;
    private int age;
    private String phone;
    private String address;
    private List<BookBorrow> bookBorrows = new ArrayList<BookBorrow>(); // List to track borrowed books

    /**
     * Default constructor for Visitor.
     */
    public Visitor() {
        super();
    }

    /**
     * Constructor to initialize the Visitor object with personal details.
     *
     * @param username The username of the visitor
     * @param password The password of the visitor
     * @param fullName The full name of the visitor
     * @param gender   The gender of the visitor
     * @param age      The age of the visitor
     * @param phone    The phone number of the visitor
     * @param address  The address of the visitor
     */
    public Visitor(String username, String password, String fullName, Gender gender, int age, String phone, String address) {
        super(username, password, Role.VISITOR); // Calls the constructor of the superclass UserBase with the role set to VISITOR
        this.fullName = fullName;
        this.gender = gender;
        this.age = age;
        this.phone = phone;
        this.address = address;
    }

    // Getters and Setters for the fields

    public int getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(int visitorId) {
        this.visitorId = visitorId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<BookBorrow> getBookBorrows() {
        if (bookBorrows == null) {
            bookBorrows = new ArrayList<>(); // If the list is null, initialize it
        }
        return bookBorrows;
    }

    /**
     * Returns a string representation of the Visitor object.
     *
     * @return String with all relevant visitor details
     */
    @Override
    public String toString() {
        return "Visitor{" +
                "visitorId=" + visitorId +
                ", fullName='" + fullName + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", bookBorrows=" + bookBorrows +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}
