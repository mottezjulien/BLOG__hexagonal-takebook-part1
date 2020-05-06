package fr.jmottez.takebook.spring.facade.transport.objet;

public class PutAwayRequestDTO {

    private BookDTO book;

    private ShelfDTO shelf;

    public BookDTO getBook() {
        return book;
    }

    public void setBook(BookDTO book) {
        this.book = book;
    }

    public ShelfDTO getShelf() {
        return shelf;
    }

    public void setShelf(ShelfDTO shelf) {
        this.shelf = shelf;
    }
}
