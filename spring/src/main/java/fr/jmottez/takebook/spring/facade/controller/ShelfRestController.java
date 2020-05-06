package fr.jmottez.takebook.spring.facade.controller;

import fr.jmottez.takebook.domain.ports.input.LibraryBookInputPort;
import fr.jmottez.takebook.spring.facade.transport.assembler.LibraryDTOAssembler;
import fr.jmottez.takebook.spring.facade.transport.assembler.ShelfDTOAssembler;
import fr.jmottez.takebook.spring.facade.transport.objet.LibraryDTO;
import fr.jmottez.takebook.spring.facade.transport.objet.ShelfDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.stream.Stream;

@Controller
@RequestMapping(value = "/takebook/rest/shelf")
public class ShelfRestController {

    @Autowired
    private LibraryBookInputPort inputPort;

    @Autowired
    private LibraryDTOAssembler libraryAssembler;

    @Autowired
    private ShelfDTOAssembler assembler;

    @RequestMapping(method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            path = "/")
    @ResponseBody
    public Stream<ShelfDTO> findAll(@RequestBody LibraryDTO library) {
        return inputPort
                .shelves(libraryAssembler.toModel(library))
                .map(model -> assembler.fromModel(model));
    }

}