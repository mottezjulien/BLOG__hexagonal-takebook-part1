package fr.jmottez.takebook.spring;

import fr.jmottez.takebook.domain.model.book.BookState;
import fr.jmottez.takebook.spring.persistence.entity.BookEntity;
import fr.jmottez.takebook.spring.persistence.entity.LibraryEntity;
import fr.jmottez.takebook.spring.persistence.entity.ShelfEntity;
import fr.jmottez.takebook.spring.persistence.repository.crud.BookCrudRepository;
import fr.jmottez.takebook.spring.persistence.repository.crud.LibraryCrudRepository;
import fr.jmottez.takebook.spring.persistence.repository.crud.ShelfCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SpringAppStartupRunner implements CommandLineRunner {

    @Autowired
    private BookCrudRepository bookRepository;

    @Autowired
    private ShelfCrudRepository shelfRepository;

    @Autowired
    private LibraryCrudRepository libraryRepository;

    @Override
    public void run(String... args) throws Exception {
        LibraryEntity libraryA = new LibraryEntity();
        libraryA.setName("Library A");
        libraryRepository.save(libraryA);

        LibraryEntity libraryB = new LibraryEntity();
        libraryB.setName("Library B");
        libraryRepository.save(libraryB);

        ShelfEntity shelfA0 = new ShelfEntity();
        shelfA0.setLabel("shelfA0");
        shelfA0.setLibrary(libraryA);
        shelfRepository.save(shelfA0);

        ShelfEntity shelfA1 = new ShelfEntity();
        shelfA1.setLabel("shelfA1");
        shelfA1.setLibrary(libraryA);
        shelfRepository.save(shelfA1);

        ShelfEntity shelfB0 = new ShelfEntity();
        shelfB0.setLabel("shelfB0");
        shelfB0.setLibrary(libraryB);
        shelfRepository.save(shelfB0);

        BookEntity book0 = new BookEntity();
        book0.setState(BookState.STORED);
        book0.setTitle("Mon livre rangé !!");
        book0.setAuthorName("Pierre");
        book0.setShelf(shelfA0);
        bookRepository.save(book0);

        BookEntity book1 = new BookEntity();
        book1.setState(BookState.BORROWED);
        book1.setTitle("Ce livre est emprunté !!");
        book1.setAuthorName("Paul");
        book1.setBorrower("Me");
        bookRepository.save(book1);

        BookEntity book2 = new BookEntity();
        book2.setState(BookState.RETURNED);
        book2.setTitle("Le livre retourné dans la lib B");
        book2.setAuthorName("Pierre");
        book2.setLibrary(libraryB);
        bookRepository.save(book2);

        BookEntity book3 = new BookEntity();
        book3.setState(BookState.RETURNED);
        book3.setTitle("Le livre retourné dans la lib A");
        book3.setAuthorName("Jack");
        book3.setLibrary(libraryA);
        bookRepository.save(book3);

    }
}