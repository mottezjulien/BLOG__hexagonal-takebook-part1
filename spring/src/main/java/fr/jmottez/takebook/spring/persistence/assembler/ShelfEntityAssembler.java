package fr.jmottez.takebook.spring.persistence.assembler;

import fr.jmottez.takebook.domain.model.Shelf;
import fr.jmottez.takebook.spring.persistence.entity.ShelfEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShelfEntityAssembler {

    @Autowired
    private LibraryEntityAssembler libraryAssembler;

    public Shelf toModel(ShelfEntity entity) {
        Shelf model = new Shelf();
        model.setId(entity.getId());
        model.setLabel(entity.getLabel());
        model.setLibrary(libraryAssembler.toModel(entity.getLibrary()));
        return model;
    }

    public ShelfEntity fromModel(Shelf model) {
        ShelfEntity entity = new ShelfEntity();
        entity.setId(model.getId());
        entity.setLabel(model.getLabel());
        entity.setLibrary(libraryAssembler.fromModel(model.getLibrary()));
        return entity;
    }
}
