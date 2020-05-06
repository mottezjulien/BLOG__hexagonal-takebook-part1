package fr.jmottez.takebook.spring.facade.transport.objet;

public class ReturnRequestDTO {

    private BookDTO book;

    private LibraryDTO library;

    public BookDTO getBook() {
        return book;
    }

    public void setBook(BookDTO book) {
        this.book = book;
    }

    public LibraryDTO getLibrary() {
        return library;
    }

    public void setLibrary(LibraryDTO library) {
        this.library = library;
    }
}
