package fr.jmottez.takebook.domain.model.book;

public class BorrowedBook extends Book {

    private String borrower;

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public String getBorrower() {
        return borrower;
    }

    @Override
    public BookState getState() {
        return BookState.BORROWED;
    }

}
