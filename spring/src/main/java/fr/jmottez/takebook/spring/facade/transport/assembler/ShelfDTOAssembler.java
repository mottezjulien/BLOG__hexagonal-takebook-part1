package fr.jmottez.takebook.spring.facade.transport.assembler;

import fr.jmottez.takebook.domain.model.Shelf;
import fr.jmottez.takebook.spring.facade.transport.objet.ShelfDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShelfDTOAssembler {

    @Autowired
    private LibraryDTOAssembler libraryAssembler;

    public Shelf toModel(ShelfDTO dto) {
        Shelf model = new Shelf();
        model.setId(dto.getId());
        model.setLabel(dto.getLabel());
        model.setLibrary(libraryAssembler.toModel(dto.getLibrary()));
        return model;
    }

    public ShelfDTO fromModel(Shelf model) {
        ShelfDTO dto = new ShelfDTO();
        dto.setId(model.getId());
        dto.setLabel(model.getLabel());
        dto.setLibrary(libraryAssembler.fromModel(model.getLibrary()));
        return dto;
    }
}
