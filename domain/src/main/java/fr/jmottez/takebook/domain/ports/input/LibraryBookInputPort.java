package fr.jmottez.takebook.domain.ports.input;

import fr.jmottez.takebook.domain.model.Library;
import fr.jmottez.takebook.domain.model.Shelf;
import fr.jmottez.takebook.domain.model.book.Book;
import fr.jmottez.takebook.domain.ports.output.BookOutputPort;
import fr.jmottez.takebook.domain.ports.output.LibraryOutputPort;
import fr.jmottez.takebook.domain.ports.output.ShelfOutputPort;
import fr.jmottez.takebook.domain.BookFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LibraryBookInputPort {

    private LibraryOutputPort libraryRepository;

    private ShelfOutputPort shelfRepository;

    private BookOutputPort bookRepository;
    
    private BookFactory bookFactory;

    public LibraryBookInputPort(LibraryOutputPort libraryRepository, ShelfOutputPort shelfRepository, BookOutputPort bookRepository) {
        this(libraryRepository,
                shelfRepository,
                bookRepository,
                new BookFactory());
    }

    public LibraryBookInputPort(LibraryOutputPort libraryRepository, ShelfOutputPort shelfRepository, BookOutputPort bookRepository, BookFactory bookFactory) {
        this.libraryRepository = libraryRepository;
        this.shelfRepository = shelfRepository;
        this.bookRepository = bookRepository;
        this.bookFactory = bookFactory;
    }

    public Stream<Library> libraries() {
        return libraryRepository
                .findLibraries();
    }

    public Stream<Shelf> shelves(Library library) {
        return shelfRepository
                .findShelvesByLibrary(library);
    }

    public Stream<Book> books(Library library) {
        List<Shelf> shelves = shelves(library).collect(Collectors.toList());
        return bookRepository
                .findBooks()
                .filter(book -> book.isReturned(library)
                        || isStored(book, shelves));
    }

    private boolean isStored(Book book, List<Shelf> shelves) {
        for(Shelf shelf : shelves) {
            if(book.isStored(shelf)) {
                return true;
            }
        }
        return false;
    }


    public Stream<Book> books(String borrower) {
        return bookRepository
                .findBooks()
                .filter(book -> book.isBorrowed(borrower));
    }


    public Book borrowBook(Book book, String borrower) throws LibraryServiceException {
        Book foundBook = reload(book);
        if (foundBook.isStored()) {
            Book borrowed = bookFactory.createBorrowed(foundBook, borrower);
            bookRepository.update(borrowed);
            return borrowed;
        }
        throw new LibraryServiceException("The book is not shelved !");
    }


    public Book returnBook(Book book, Library library) throws LibraryServiceException {
        Book foundBook = reload(book);
        if (foundBook.isBorrowed()) {
            Book returned = bookFactory.createReturned(foundBook, reload(library));
            bookRepository.update(returned);
            return returned;
        }
        throw new LibraryServiceException("The book is not borrowed !");
    }



    public Book putAwayBook(Book book, Shelf shelf) throws LibraryServiceException {
        Book foundBook = reload(book);
        if (foundBook.isReturned()) {
            Book shelved = bookFactory.createShelved(foundBook, reload(shelf));
            bookRepository.update(shelved);
            return shelved;
        }
        throw new LibraryServiceException("The book is not returned !");
    }

    private Library reload(Library library) throws LibraryServiceException {
        return libraryRepository
                .findLibraryById(library.getId())
                .orElseThrow(() -> new LibraryServiceException("The library is not found in the repository !"));
    }

    private Shelf reload(Shelf shelf) throws LibraryServiceException {
        return shelfRepository
                .findShelfById(shelf.getId())
                .orElseThrow(() -> new LibraryServiceException("The shelf is not found in the repository !"));
    }

    private Book reload(Book book) throws LibraryServiceException {
        return bookRepository
                .findBookById(book.getId())
                .orElseThrow(() -> new LibraryServiceException("The book is not found in the repository !"));
    }

}
