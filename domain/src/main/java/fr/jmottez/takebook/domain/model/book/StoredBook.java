package fr.jmottez.takebook.domain.model.book;

import fr.jmottez.takebook.domain.model.Shelf;

public class StoredBook extends Book {

    private Shelf shelf;

    @Override
    public BookState getState() {
        return BookState.STORED;
    }

    public Shelf getShelf() {
        return shelf;
    }

    public void setShelf(Shelf shelf) {
        this.shelf = shelf;
    }

}
