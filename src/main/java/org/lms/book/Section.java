package org.lms.book;

/**
 * The Section enum represents different sections of a library or bookshelf.
 */
public enum Section {
    S1("S1"),  // Section 1
    S2("S2"),  // Section 2
    S3("S3");  // Section 3

    private String sectionName;  // The name of the section

    // Constructor to initialize the section name
    Section(String sectionName) {
        this.sectionName = sectionName;
    }

    // Getter method for section name
    public String getSectionName() {
        return sectionName;
    }

    // Override toString to return the section name as a string
    @Override
    public String toString() {
        return sectionName;
    }
}
