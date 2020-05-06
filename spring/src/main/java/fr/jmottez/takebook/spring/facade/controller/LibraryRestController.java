package fr.jmottez.takebook.spring.facade.controller;

import fr.jmottez.takebook.domain.ports.input.LibraryBookInputPort;

import fr.jmottez.takebook.spring.facade.transport.assembler.LibraryDTOAssembler;
import fr.jmottez.takebook.spring.facade.transport.objet.LibraryDTO;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.stream.Stream;

@Controller
@RequestMapping(value = "/takebook/rest/library")
public class LibraryRestController {

    @Autowired
    private LibraryBookInputPort inputPort;

    @Autowired
    private LibraryDTOAssembler assembler;

    @RequestMapping(method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE},
            path = "/")
    @ResponseBody
    public Stream<LibraryDTO> findAll() {
        return inputPort
                .libraries()
                .map(model -> assembler.fromModel(model));
    }

}