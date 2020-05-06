package fr.jmottez.takebook.spring.facade.transport.assembler;

import fr.jmottez.takebook.domain.model.book.*;
import fr.jmottez.takebook.spring.facade.transport.objet.BookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookDTOAssembler {

    @Autowired
    private LibraryDTOAssembler libraryAssembler;

    @Autowired
    private ShelfDTOAssembler shelfAssembler;

    public BookDTO fromModel(Book model) {
        BookDTO dto = new BookDTO();
        dto.setId(model.getId());
        dto.setTitle(model.getTitle());
        dto.setAuthorName(model.getAuthorName());
        dto.setState(model.getState().name());
        switch (model.getState()) {
            case BORROWED:
                dto.setBorrower(((BorrowedBook)model).getBorrower());
                break;
            case RETURNED:
                dto.setLibrary(((ReturnedBook)model).getLibrary());
                break;
            case STORED:
                dto.setShelf(((StoredBook)model).getShelf());
                break;
        }
        return dto;
    }

    public Book toModel(BookDTO dto) {
        Book model = createBook(dto.getState());
        model.setId(dto.getId());
        model.setTitle(dto.getTitle());
        return model;
    }

    private Book createBook(String stateName) {
        if(stateName != null) {
            switch (BookState.valueOf(stateName)){
                case BORROWED:
                    return new BorrowedBook();
                case RETURNED:
                    return new ReturnedBook();
                case STORED:
                    return new StoredBook();
            }
        }
        throw new IllegalArgumentException("Book State null");
    }

}
