package org.lms.book;

/**
 * The BookType enum represents various types of books available in the library.
 */
public enum BookType {
    TECHNOLOGY("Technology"),  // Technology books
    SCIENCE("Science"),        // Science books
    LITERATURE("Literature"),  // Literature books
    HISTORY("History"),        // History books
    ART("Art"),                // Art books
    FICTION("Fiction");        // Fiction books

    private String typeName;  // The name of the book type

    // Constructor to initialize the book type name
    BookType(String typeName) {
        this.typeName = typeName;
    }

    // Getter method for book type name
    public String getTypeName() {
        return typeName;
    }

    // Override toString to return the book type name as a string
    @Override
    public String toString() {
        return typeName;
    }
}
