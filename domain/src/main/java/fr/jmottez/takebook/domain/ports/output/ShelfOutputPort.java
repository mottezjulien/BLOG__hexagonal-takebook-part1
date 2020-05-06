package fr.jmottez.takebook.domain.ports.output;

import fr.jmottez.takebook.domain.model.Library;
import fr.jmottez.takebook.domain.model.Shelf;

import java.util.Optional;
import java.util.stream.Stream;

public interface ShelfOutputPort {

    Optional<Shelf> findShelfById(int id);

    Stream<Shelf> findShelvesByLibrary(Library library);

}
