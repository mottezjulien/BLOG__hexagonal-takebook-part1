package fr.jmottez.takebook.spring.facade.transport.objet;

public class BorrowRequestDTO {

    private BookDTO book;

    private String borrower;

    public BookDTO getBook() {
        return book;
    }

    public void setBook(BookDTO book) {
        this.book = book;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }
}
