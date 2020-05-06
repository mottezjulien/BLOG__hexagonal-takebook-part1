package fr.jmottez.takebook.spring.persistence.assembler;

import fr.jmottez.takebook.domain.model.Library;
import fr.jmottez.takebook.spring.persistence.entity.LibraryEntity;
import org.springframework.stereotype.Component;

@Component
public class LibraryEntityAssembler {

    public Library toModel(LibraryEntity entity) {
        Library model = new Library();
        model.setId(entity.getId());
        model.setName(entity.getName());
        return model;
    }

    public LibraryEntity fromModel(Library model) {
        LibraryEntity entity = new LibraryEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());
        return entity;
    }

}
