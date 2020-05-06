package fr.jmottez.takebook.spring.persistence.repository;

import fr.jmottez.takebook.domain.model.Library;
import fr.jmottez.takebook.domain.ports.output.LibraryOutputPort;
import fr.jmottez.takebook.spring.persistence.assembler.LibraryEntityAssembler;
import fr.jmottez.takebook.spring.persistence.repository.crud.LibraryCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Repository
public class LibraryOutputSpringAdapter implements LibraryOutputPort {

    private Logger logger = Logger.getLogger(LibraryOutputSpringAdapter.class.getSimpleName());

    @Autowired
    private LibraryEntityAssembler assembler;

    @Autowired
    private LibraryCrudRepository crudRepository;

    @Override
    public Stream<Library> findLibraries() {
        return StreamSupport
                .stream(crudRepository.findAll().spliterator(), false)
                .map(entity -> assembler.toModel(entity));
    }

    @Override
    public Optional<Library> findLibraryById(int id) {
        return crudRepository
                .findById(id)
                .map(entity -> assembler.toModel(entity));
    }

}

