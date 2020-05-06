package fr.jmottez.takebook.spring.persistence.repository;

import fr.jmottez.takebook.domain.model.Library;
import fr.jmottez.takebook.domain.model.Shelf;
import fr.jmottez.takebook.domain.ports.output.ShelfOutputPort;
import fr.jmottez.takebook.spring.persistence.assembler.LibraryEntityAssembler;
import fr.jmottez.takebook.spring.persistence.assembler.ShelfEntityAssembler;
import fr.jmottez.takebook.spring.persistence.repository.crud.ShelfCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Stream;

@Repository
public class ShelfOutputSpringAdapter implements ShelfOutputPort {

    private Logger logger = Logger.getLogger(ShelfOutputSpringAdapter.class.getSimpleName());

    @Autowired
    private LibraryEntityAssembler libraryAssembler;

    @Autowired
    private ShelfEntityAssembler assembler;

    @Autowired
    private ShelfCrudRepository crudRepository;

    @Override
    public Stream<Shelf> findShelvesByLibrary(Library library) {
        return crudRepository.findByLibrary(libraryAssembler.fromModel(library))
                .stream()
                .map(entity -> assembler.toModel(entity));
    }

    @Override
    public Optional<Shelf> findShelfById(int id) {
        return crudRepository
                .findById(id)
                .map(entity -> assembler.toModel(entity));
    }

}
