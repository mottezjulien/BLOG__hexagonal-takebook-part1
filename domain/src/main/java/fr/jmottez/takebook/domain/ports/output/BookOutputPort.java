package fr.jmottez.takebook.domain.ports.output;

import fr.jmottez.takebook.domain.model.book.Book;

import java.util.Optional;
import java.util.stream.Stream;

public interface BookOutputPort {

    Stream<Book> findBooks();

    Optional<Book> findBookById(int id);

    void update(Book book);


}
