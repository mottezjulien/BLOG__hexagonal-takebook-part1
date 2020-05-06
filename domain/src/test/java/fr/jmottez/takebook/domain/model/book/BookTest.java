package fr.jmottez.takebook.domain.model.book;

import fr.jmottez.takebook.domain.model.Library;
import fr.jmottez.takebook.domain.model.Shelf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookTest {

    private Book book = new Book() {
        @Override
        public BookState getState() {
            return null;
        }
    };

    @Test
    public void isNotReturnedIfBorrowed() {
        BorrowedBook book = new BorrowedBook();

        Library library = new Library();
        library.setId(123);
        assertFalse(book.isReturned(library));
    }

    @Test
    public void isNotReturnedIfStored() {
        StoredBook book = new StoredBook();

        Library library = new Library();
        library.setId(123);
        assertFalse(book.isReturned(library));
    }

    @Test
    public void isNotReturnedIfReturnedAndDifferentLibrary() {
        ReturnedBook book = new ReturnedBook();
        Library requestLibrary = new Library();
        requestLibrary.setId(321);
        book.setLibrary(requestLibrary);

        Library library = new Library();
        library.setId(123);
        assertFalse(book.isReturned(library));
    }

    @Test
    public void isReturnedIfReturnedAndSameLibrary() {
        ReturnedBook book = new ReturnedBook();
        Library requestLibrary = new Library();
        requestLibrary.setId(123);
        book.setLibrary(requestLibrary);

        Library library = new Library();
        library.setId(123);
        assertTrue(book.isReturned(library));
    }


    @Test
    public void isNotStoredIfBorrowed() {
        BorrowedBook book = new BorrowedBook();

        Shelf shelf = new Shelf();
        shelf.setId(789);
        assertFalse(book.isStored(shelf));
    }

    @Test
    public void isNotStoredIfReturned() {
        ReturnedBook book = new ReturnedBook();

        Shelf shelf = new Shelf();
        shelf.setId(789);
        assertFalse(book.isStored(shelf));
    }

    @Test
    public void isNotStoredIfStoredAndDifferentShelf() {
        StoredBook book = new StoredBook();
        Shelf requestShelf = new Shelf();
        requestShelf.setId(987);
        book.setShelf(requestShelf);

        Shelf shelf = new Shelf();
        shelf.setId(789);
        assertFalse(book.isStored(shelf));
    }

    @Test
    public void isStoredIfStoredAndSameShelf() {
        StoredBook book = new StoredBook();
        Shelf requestShelf = new Shelf();
        requestShelf.setId(789);
        book.setShelf(requestShelf);

        Shelf shelf = new Shelf();
        shelf.setId(789);
        assertTrue(book.isStored(shelf));
    }

    @Test
    public void isNotBorrowedIfShelved() {
        StoredBook book = new StoredBook();
        assertFalse(book.isBorrowed("any"));
    }

    @Test
    public void isNotBorrowedIfReturned() {
        ReturnedBook book = new ReturnedBook();
        assertFalse(book.isBorrowed("any"));
    }

    @Test
    public void isNotBorrowedIfNotSameUser() {
        BorrowedBook book = new BorrowedBook();
        book.setBorrower("other");
        assertFalse(book.isBorrowed("any"));
    }

    @Test
    public void isBorrowedIfSameUser() {
        BorrowedBook book = new BorrowedBook();
        book.setBorrower("any");
        assertTrue(book.isBorrowed("any"));
    }

}