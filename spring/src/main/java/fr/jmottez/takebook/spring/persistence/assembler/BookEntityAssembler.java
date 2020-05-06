package fr.jmottez.takebook.spring.persistence.assembler;

import fr.jmottez.takebook.domain.model.book.Book;
import fr.jmottez.takebook.domain.model.book.BorrowedBook;
import fr.jmottez.takebook.domain.model.book.ReturnedBook;
import fr.jmottez.takebook.domain.model.book.StoredBook;
import fr.jmottez.takebook.spring.persistence.entity.BookEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookEntityAssembler {

    @Autowired
    private LibraryEntityAssembler libraryAssembler;

    @Autowired
    private ShelfEntityAssembler shelfAssembler;

    public Book toModel(BookEntity entity) {
        Book model = create(entity);
        model.setId(entity.getId());
        model.setTitle(entity.getTitle());
        model.setAuthorName(entity.getAuthorName());
        return model;
    }

    private Book create(BookEntity entity) {
        switch (entity.getState()) {
            case BORROWED:
                BorrowedBook borrowedBook = new BorrowedBook();
                borrowedBook.setBorrower(entity.getBorrower());
                return borrowedBook;
            case RETURNED:
                ReturnedBook returnedBook = new ReturnedBook();
                returnedBook.setLibrary(libraryAssembler.toModel(entity.getLibrary()));
                return returnedBook;
            case STORED:
                StoredBook storedBook = new StoredBook();
                storedBook.setShelf(shelfAssembler.toModel(entity.getShelf()));
                return storedBook;
            default:
                throw new IllegalArgumentException("entity state not valid");
        }
    }

    public BookEntity fromModel(Book model) {
        BookEntity entity = new BookEntity();
        entity.setId(model.getId());
        entity.setTitle(model.getTitle());
        entity.setAuthorName(model.getAuthorName());
        entity.setState(model.getState());
        switch (model.getState()) {
            case BORROWED:
                entity.setBorrower(((BorrowedBook) model).getBorrower());
                break;
            case RETURNED:
                entity.setLibrary(libraryAssembler.fromModel(((ReturnedBook) model).getLibrary()));
                break;
            case STORED:
                entity.setShelf(shelfAssembler.fromModel(((StoredBook) model).getShelf()));
                break;
        }
        return entity;
    }
}
