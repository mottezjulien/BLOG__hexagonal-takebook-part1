package fr.jmottez.takebook.console;

import fr.jmottez.takebook.domain.model.Library;
import fr.jmottez.takebook.domain.model.Shelf;
import fr.jmottez.takebook.domain.model.book.Book;
import fr.jmottez.takebook.domain.model.book.BorrowedBook;
import fr.jmottez.takebook.domain.model.book.ReturnedBook;
import fr.jmottez.takebook.domain.model.book.StoredBook;
import fr.jmottez.takebook.domain.ports.output.BookOutputPort;
import fr.jmottez.takebook.domain.ports.output.LibraryOutputPort;
import fr.jmottez.takebook.domain.ports.output.ShelfOutputPort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ConsoleInternOutputAdapters implements LibraryOutputPort, ShelfOutputPort, BookOutputPort {

    private List<Library> libraries = new ArrayList<>();

    private List<Book> books = new ArrayList<>();

    private List<Shelf> shelves = new ArrayList<>();

    public ConsoleInternOutputAdapters() {
        init();
    }

    private void init() {
        Library libraryA = new Library();
        libraryA.setId(101);
        libraryA.setName("Librarie A");
        libraries.add(libraryA);

        Shelf shelfA0 = new Shelf();
        shelfA0.setId(501);
        shelfA0.setLabel("LibA001");
        shelfA0.setLibrary(libraryA);
        shelves.add(shelfA0);

        Shelf shelfA1 = new Shelf();
        shelfA1.setId(502);
        shelfA1.setLabel("LibA002");
        shelfA1.setLibrary(libraryA);
        shelves.add(shelfA1);

        Library libraryB = new Library();
        libraryB.setId(102);
        libraryB.setName("Librarie B");
        libraries.add(libraryB);

        Shelf shelfB0 = new Shelf();
        shelfB0.setId(503);
        shelfB0.setLabel("LibB001");
        shelfB0.setLibrary(libraryB);
        shelves.add(shelfB0);

        ReturnedBook book0 = new ReturnedBook();
        book0.setId(1001);
        book0.setTitle("Mémoire de Pierre");
        book0.setAuthorName("Pierre");
        book0.setLibrary(libraryA);
        books.add(book0);

        BorrowedBook book1 = new BorrowedBook();
        book1.setId(1002);
        book1.setTitle("La terre très très loin");
        book1.setAuthorName("McDonnor");
        book1.setBorrower("Sophie");
        books.add(book1);

        StoredBook book2 = new StoredBook();
        book2.setId(1003);
        book2.setTitle("La vie trop cool");
        book2.setAuthorName("Jack");
        book2.setShelf(shelfB0);
        books.add(book2);
    }

    @Override
    public Stream<Library> findLibraries() {
        return libraries.stream();
    }

    @Override
    public Optional<Library> findLibraryById(int id) {
        return libraries
                .stream()
                .filter(library -> library.getId() == id)
                .findAny();
    }

    @Override
    public Optional<Shelf> findShelfById(int id) {
        return shelves.stream()
                .filter(shelf -> shelf.getId() == id)
                .findAny();
    }

    @Override
    public Stream<Shelf> findShelvesByLibrary(Library library) {
        return shelves.stream()
                .filter(shelf -> shelf.getLibrary().equals(library));
    }

    @Override
    public Stream<Book> findBooks() {
        return books.stream();
    }

    @Override
    public Optional<Book> findBookById(int id) {
        return books.stream()
                .filter(book -> book.getId() == id)
                .findAny();
    }

    @Override
    public void update(Book book) {
        findBookById(book.getId())
                .ifPresent(foundBook -> {
                    books.remove(foundBook);
                    books.add(book);
                });
    }

}

