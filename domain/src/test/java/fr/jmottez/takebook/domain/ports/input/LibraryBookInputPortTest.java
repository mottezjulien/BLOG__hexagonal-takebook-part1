package fr.jmottez.takebook.domain.ports.input;

import fr.jmottez.takebook.domain.BookFactory;
import fr.jmottez.takebook.domain.model.Library;
import fr.jmottez.takebook.domain.model.Shelf;
import fr.jmottez.takebook.domain.model.book.Book;
import fr.jmottez.takebook.domain.ports.output.BookOutputPort;
import fr.jmottez.takebook.domain.ports.output.LibraryOutputPort;
import fr.jmottez.takebook.domain.ports.output.ShelfOutputPort;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LibraryBookInputPortTest {

    private LibraryOutputPort libraryRepository = mock(LibraryOutputPort.class);

    private ShelfOutputPort shelfRepository = mock(ShelfOutputPort.class);

    private BookOutputPort bookRepository = mock(BookOutputPort.class);

    private BookFactory bookFactory = mock(BookFactory.class);

    private LibraryBookInputPort inputPort = new LibraryBookInputPort(libraryRepository, shelfRepository, bookRepository, bookFactory);

    @Test
    void verifyLibrariesIsLibrairyRepository() {
        List<Library> list = new ArrayList<>();
        list.add(library("anyName0"));
        list.add(library("anyName1"));
        list.add(library("anyName2"));
        when(libraryRepository.findLibraries()).thenReturn(list.stream());

        List<Library> findAll = inputPort.libraries().collect(Collectors.toList());

        assertAll("find All",
                () -> assertEquals(findAll.size(), 3),
                () -> assertEquals(findAll.get(0), list.get(0)),
                () -> assertEquals(findAll.get(1), list.get(1)),
                () -> assertEquals(findAll.get(2), list.get(2))
        );
    }

    @Test
    void findBooksByLibrary() {

        List<Book> books = new ArrayList<>();
        Book book0 = mock(Book.class);
        books.add(book0);
        Book book1 = mock(Book.class);
        books.add(book1);
        Book book2 = mock(Book.class);
        books.add(book2);
        when(bookRepository.findBooks()).thenReturn(books.stream());

        Library library = mock(Library.class);

        when(book0.isReturned(library)).thenReturn(false);
        when(book1.isReturned(library)).thenReturn(true);
        when(book2.isReturned(library)).thenReturn(false);

        Shelf shelf = mock(Shelf.class);

        when(book0.isStored(shelf)).thenReturn(false);
        when(book1.isStored(shelf)).thenReturn(false);
        when(book2.isStored(shelf)).thenReturn(true);

        when(shelfRepository.findShelvesByLibrary(library)).thenReturn(Stream.of(shelf));

        List<Book> findBooks = inputPort.books(library)
                .collect(Collectors.toList());

        assertAll("find Books",
                () -> assertEquals(findBooks.size(), 2),
                () -> assertEquals(findBooks.get(0), book1),
                () -> assertEquals(findBooks.get(1), book2)
        );
    }

    @Test
    public void findBooksByBorrower() {

        String borrower = "any";

        List<Book> books = new ArrayList<>();
        Book book0 = mock(Book.class);
        books.add(book0);
        Book book1 = mock(Book.class);
        books.add(book1);
        Book book2 = mock(Book.class);
        books.add(book2);

        when(book0.isBorrowed(borrower)).thenReturn(true);
        when(book1.isBorrowed(borrower)).thenReturn(false);
        when(book2.isBorrowed(borrower)).thenReturn(true);

        when(bookRepository.findBooks()).thenReturn(books.stream());

        List<Book> findBooks = inputPort.books(borrower)
                .collect(Collectors.toList());

        assertAll("find Books",
                () -> assertEquals(findBooks.size(), 2),
                () -> assertEquals(findBooks.get(0), book0),
                () -> assertEquals(findBooks.get(1), book2)
        );

    }

    @Test
    void borrowIfShelved() throws LibraryServiceException {
        Book shelved = mock(Book.class);
        Book borrowed = mock(Book.class);
        String borrower = "borrower0";

        when(shelved.getId()).thenReturn(123);
        when(shelved.isStored()).thenReturn(true);

        when(bookRepository.findBookById(123)).thenReturn(Optional.of(shelved));
        when(bookFactory.createBorrowed(shelved, borrower)).thenReturn(borrowed);

        Book response = inputPort.borrowBook(shelved, borrower);
        assertEquals(response, borrowed);
        verify(bookRepository).update(borrowed);
    }


    @Test
    void notBorrowIfNotShelved() {
        Book shelved = mock(Book.class);
        Book borrowed = mock(Book.class);
        String borrower = "borrower0";

        when(shelved.getId()).thenReturn(123);
        when(shelved.isStored()).thenReturn(false);

        when(bookRepository.findBookById(123)).thenReturn(Optional.of(shelved));
        when(bookFactory.createBorrowed(shelved, borrower)).thenReturn(borrowed);

        assertThrows(LibraryServiceException.class, () -> inputPort.borrowBook(shelved, borrower));
    }


    @Test
    void returnBookIfBorrowed() throws LibraryServiceException {
        Book returned = mock(Book.class);

        Book borrowed = mock(Book.class);
        when(borrowed.getId()).thenReturn(456);
        when(borrowed.isBorrowed()).thenReturn(true);

        Library library = mock(Library.class);
        when(library.getId()).thenReturn(101);

        when(bookRepository.findBookById(456)).thenReturn(Optional.of(borrowed));
        when(bookFactory.createReturned(borrowed, library)).thenReturn(returned);

        when(libraryRepository.findLibraryById(101)).thenReturn(Optional.of(library));

        Book response = inputPort.returnBook(borrowed, library);
        assertEquals(response, returned);
        verify(bookRepository).update(returned);
    }


    @Test
    void notReturnBookIfNotBorrowed() {
        Book returned = mock(Book.class);

        Book borrowed = mock(Book.class);
        when(borrowed.getId()).thenReturn(456);
        when(borrowed.isBorrowed()).thenReturn(false);

        Library library = mock(Library.class);
        when(library.getId()).thenReturn(101);

        when(bookRepository.findBookById(456)).thenReturn(Optional.of(borrowed));
        when(bookFactory.createReturned(borrowed, library)).thenReturn(returned);

        when(libraryRepository.findLibraryById(101)).thenReturn(Optional.of(library));

        assertThrows(LibraryServiceException.class, () -> inputPort.returnBook(borrowed, library));
    }


    @Test
    void putAwayBookIfReturned() throws LibraryServiceException {
        Book shelved = mock(Book.class);

        Book returned = mock(Book.class);
        when(returned.getId()).thenReturn(789);
        when(returned.isReturned()).thenReturn(true);

        Shelf shelf = mock(Shelf.class);
        when(shelf.getId()).thenReturn(201);

        when(bookRepository.findBookById(789)).thenReturn(Optional.of(returned));
        when(bookFactory.createShelved(returned, shelf)).thenReturn(shelved);

        when(shelfRepository.findShelfById(201)).thenReturn(Optional.of(shelf));

        Book response = inputPort.putAwayBook(returned, shelf);
        assertEquals(response, shelved);
        verify(bookRepository).update(shelved);
    }

    @Test
    void notPutAwayBookIfNotReturned() {
        Book shelved = mock(Book.class);

        Book returned = mock(Book.class);
        when(returned.getId()).thenReturn(789);
        when(returned.isReturned()).thenReturn(false);

        Shelf shelf = mock(Shelf.class);
        when(shelf.getId()).thenReturn(201);

        when(bookRepository.findBookById(789)).thenReturn(Optional.of(returned));
        when(bookFactory.createShelved(returned, shelf)).thenReturn(shelved);

        when(shelfRepository.findShelfById(201)).thenReturn(Optional.of(shelf));

        assertThrows(LibraryServiceException.class, () -> inputPort.putAwayBook(returned, shelf));
    }

    private Library library(String name) {
        Library library = new Library();
        library.setName(name);
        return library;
    }

}