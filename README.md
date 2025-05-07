Library Management System

Team Members: 
Gavin Qi
Eric Zhang

Description of Project:
We are creating a GUI based application for library management, the two types of user would be the librarians and library visitors. The application would deal with book related and personal data management. We will use a file to store the data.
Features:
1: Librarians
Librarians will deal with book management, and track book borrowing information.
Add Books: Add a new book to the library system by entering the book’s author, name, ISBN, type (selected from a predefined list), section (S1 / S2 / S3), and status (default = Available), automatically assign a copy ID to each identical book (if only one copy, the copy ID is ISBN+1; the ID does not change if a book is deleted)
View Books: Display the full catalogue of the books, showing author, name, ISBN, type, section, copy ID, status of each copy.
Modify Books: Edit a book’s author, name, ISBN, type, section
Search Books: Search a book by entering a book’s author, name, ISBN
Delete Books: Delete a book, if there are multiple copies, delete the copy ID not the book, if there is no copy left, delete the book. 
Issue Books: Register a book loan for a visitor, automatically assign a borrowing date, assign the book to a visitor ID, status become unavailable, each visitor can borrow 10 at most at once.
Return Books: Close a book loan when it is returned, automatically assign a borrowing date, book status become available

2: Library Visitors
Library Visitors will have their profile and can use the system to search for books, borrow books, and return books
Create Profile:  Create a personal profile with name, gender, age, phone and address; the system assigns a unique visitor ID.
Modify Profile: Update/fix user profile’s name, gender, age, address
Delete Profile: Delete the profile if you do not want to use this library anymore.
Search Books: search a book by author, name, ISBN, type
View Books: Browse the complete catalogue of a book with full details (name, ISBN, type, section, status, etc.).
View Status: Display a personal borrowing summary showing borrowed books, and the remaining quota of books available for borrowing.
