package fr.jmottez.takebook.domain.model.book;

import fr.jmottez.takebook.domain.model.Library;
import fr.jmottez.takebook.domain.model.Shelf;

public abstract class Book {

    private int id;

    private String title;

    private String authorName;

    public abstract BookState getState();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public boolean isReturned() {
        return getState() == BookState.RETURNED;
    }

    public boolean isBorrowed() {
        return getState() == BookState.BORROWED;
    }

    public boolean isStored() {
        return getState() == BookState.STORED;
    }

    public boolean isReturned(Library library) {
        return isReturned()
                && ((ReturnedBook)this).getLibrary().equals(library);
    }

    public boolean isStored(Shelf shelf) {
        return isStored()
                && ((StoredBook)this).getShelf().equals(shelf);
    }

    public boolean isBorrowed(String borrower) {
        if (getState() == BookState.BORROWED) {
            return ((BorrowedBook) this).getBorrower().equals(borrower);
        }
        return false;
    }



}

