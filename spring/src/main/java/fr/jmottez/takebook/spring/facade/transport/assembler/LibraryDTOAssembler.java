package fr.jmottez.takebook.spring.facade.transport.assembler;

import fr.jmottez.takebook.domain.model.Library;
import fr.jmottez.takebook.spring.facade.transport.objet.LibraryDTO;
import org.springframework.stereotype.Component;

@Component
public class LibraryDTOAssembler {

    public LibraryDTO fromModel(Library model) {
        LibraryDTO dto = new LibraryDTO();
        dto.setId(model.getId());
        dto.setName(model.getName());
        return dto;
    }

    public Library toModel(LibraryDTO dto) {
        Library model = new Library();
        model.setId(dto.getId());
        model.setName(dto.getName());
        return model;
    }

}
