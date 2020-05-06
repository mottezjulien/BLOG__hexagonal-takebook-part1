package fr.jmottez.takebook.spring.persistence.repository;

import fr.jmottez.takebook.domain.model.book.Book;
import fr.jmottez.takebook.domain.ports.output.BookOutputPort;
import fr.jmottez.takebook.spring.persistence.assembler.BookEntityAssembler;
import fr.jmottez.takebook.spring.persistence.repository.crud.BookCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Repository
public class BookOutputSpringAdapter implements BookOutputPort {

    @Autowired
    private BookEntityAssembler assembler;

    @Autowired
    private BookCrudRepository crudRepository;

    @Override
    public Stream<Book> findBooks() {
        return StreamSupport
                .stream(crudRepository.findAll().spliterator(), false)
                .map(entity -> assembler.toModel(entity));
    }

    @Override
    public Optional<Book> findBookById(int id) {
        return crudRepository
                .findById(id)
                .map(entity -> assembler.toModel(entity));
    }

    @Override
    public void update(Book book) {
        crudRepository.save(assembler.fromModel(book));
    }
}

