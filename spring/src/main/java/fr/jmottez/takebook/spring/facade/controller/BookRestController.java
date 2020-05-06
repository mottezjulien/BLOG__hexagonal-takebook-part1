package fr.jmottez.takebook.spring.facade.controller;

import fr.jmottez.takebook.domain.ports.input.LibraryBookInputPort;
import fr.jmottez.takebook.domain.ports.input.LibraryServiceException;
import fr.jmottez.takebook.spring.facade.transport.assembler.LibraryDTOAssembler;
import fr.jmottez.takebook.spring.facade.transport.assembler.ShelfDTOAssembler;
import fr.jmottez.takebook.spring.facade.transport.assembler.BookDTOAssembler;
import fr.jmottez.takebook.spring.facade.transport.objet.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.stream.Stream;

@Controller
@RequestMapping(value = "/takebook/rest/book")
public class BookRestController {

    @Autowired
    private LibraryBookInputPort inputPort;

    @Autowired
    private LibraryDTOAssembler libraryDTOAssembler;

    @Autowired
    private ShelfDTOAssembler shelfDTOAssembler;

    @Autowired
    private BookDTOAssembler bookDTOAssembler;

    @RequestMapping(method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            path = "/library/")
    @ResponseBody
    public Stream<BookDTO> findByLibrary(@RequestBody LibraryDTO dto) {
        return inputPort
                .books(libraryDTOAssembler.toModel(dto))
                .map(model -> bookDTOAssembler.fromModel(model));
    }

    @RequestMapping(method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.TEXT_PLAIN_VALUE},
            path = "/borrower/")
    @ResponseBody
    public Stream<BookDTO> findByBorrower(@RequestBody String borrower) {
        return inputPort
                .books(borrower)
                .map(model -> bookDTOAssembler.fromModel(model));
    }

    @RequestMapping(method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            path = "/borrow/")
    @ResponseBody
    public BookDTO borrow(@RequestBody BorrowRequestDTO request) throws LibraryServiceException {
        return bookDTOAssembler.fromModel(inputPort.borrowBook(bookDTOAssembler.toModel(request.getBook()), request.getBorrower()));
    }

    @RequestMapping(method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            path = "/return/")
    @ResponseBody
    public BookDTO doReturn(@RequestBody ReturnRequestDTO request) throws LibraryServiceException {
        return bookDTOAssembler.fromModel(inputPort.returnBook(bookDTOAssembler.toModel(request.getBook()), libraryDTOAssembler.toModel(request.getLibrary())));
    }

    @RequestMapping(method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            path = "/putAway/")
    @ResponseBody
    public BookDTO putAway(@RequestBody PutAwayRequestDTO request) throws LibraryServiceException {
        return bookDTOAssembler.fromModel(inputPort.putAwayBook(bookDTOAssembler.toModel(request.getBook()), shelfDTOAssembler.toModel(request.getShelf())));
    }

}