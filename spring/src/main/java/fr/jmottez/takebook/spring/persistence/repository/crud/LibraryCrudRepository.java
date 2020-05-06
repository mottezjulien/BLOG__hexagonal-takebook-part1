package fr.jmottez.takebook.spring.persistence.repository.crud;

import fr.jmottez.takebook.spring.persistence.entity.LibraryEntity;
import org.springframework.data.repository.CrudRepository;

public interface LibraryCrudRepository extends CrudRepository<LibraryEntity, Integer> {

}

