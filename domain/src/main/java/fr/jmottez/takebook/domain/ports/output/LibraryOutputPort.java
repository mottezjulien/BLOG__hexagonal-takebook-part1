package fr.jmottez.takebook.domain.ports.output;

import fr.jmottez.takebook.domain.model.Library;

import java.util.Optional;
import java.util.stream.Stream;

public interface LibraryOutputPort {

    Stream<Library> findLibraries();

    Optional<Library> findLibraryById(int id);

}
