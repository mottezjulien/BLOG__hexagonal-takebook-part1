package fr.jmottez.takebook.domain.model.book;

import fr.jmottez.takebook.domain.model.Library;

public class ReturnedBook extends Book {

    private Library library;

    @Override
    public BookState getState() {
        return BookState.RETURNED;
    }

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }
}
