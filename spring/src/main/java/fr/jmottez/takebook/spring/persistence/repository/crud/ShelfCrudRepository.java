package fr.jmottez.takebook.spring.persistence.repository.crud;

import fr.jmottez.takebook.spring.persistence.entity.LibraryEntity;
import fr.jmottez.takebook.spring.persistence.entity.ShelfEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ShelfCrudRepository extends CrudRepository<ShelfEntity, Integer> {

    List<ShelfEntity> findByLibrary(LibraryEntity library);

}

