package fr.jmottez.takebook.domain;

import fr.jmottez.takebook.domain.model.Library;
import fr.jmottez.takebook.domain.model.Shelf;
import fr.jmottez.takebook.domain.model.book.StoredBook;
import fr.jmottez.takebook.domain.model.book.Book;
import fr.jmottez.takebook.domain.model.book.BorrowedBook;
import fr.jmottez.takebook.domain.model.book.ReturnedBook;

public class BookFactory {

    public Book createReturned(Book book, Library library) {
        ReturnedBook returnedBook = new ReturnedBook();
        merge(book, returnedBook);
        returnedBook.setLibrary(library);
        return returnedBook;
    }

    public Book createBorrowed(Book book, String borrower){
        BorrowedBook borrowedBook = new BorrowedBook();
        merge(book, borrowedBook);
        borrowedBook.setBorrower(borrower);
        return borrowedBook;
    }

    public Book createShelved(Book book, Shelf shelf) {
        StoredBook storedBook = new StoredBook();
        merge(book, storedBook);
        storedBook.setShelf(shelf);
        return storedBook;
    }

    private void merge(Book from, Book to) {
        to.setId(from.getId());
        to.setTitle(from.getTitle());
        to.setAuthorName(from.getAuthorName());
    }



}
