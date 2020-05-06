package fr.jmottez.takebook.console;

import fr.jmottez.takebook.domain.model.Library;
import fr.jmottez.takebook.domain.model.Shelf;
import fr.jmottez.takebook.domain.model.book.Book;
import fr.jmottez.takebook.domain.model.book.BorrowedBook;
import fr.jmottez.takebook.domain.model.book.ReturnedBook;
import fr.jmottez.takebook.domain.model.book.StoredBook;
import fr.jmottez.takebook.domain.ports.input.LibraryBookInputPort;
import fr.jmottez.takebook.domain.ports.input.LibraryServiceException;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConsoleFacade {

    private static final String LABEL_ACTION_LIST_LIBRARY = "LISTLIB";
    private static final String LABEL_ACTION_LIST_SHELF_LIBRARY = "SHELVESLIB";
    private static final String LABEL_ACTION_BOOKS_LIBRARY = "BOOKSLIB";
    private static final String LABEL_ACTION_BOOKS_BORROWER = "BOOKSBORROWER";
    private static final String LABEL_ACTION_BORROW_BOOK = "BORROWBOOK";
    private static final String LABEL_ACTION_RETURN_BOOK = "RETURNBOOK";
    private static final String LABEL_ACTION_PUTAWAY_BOOK = "PUTAWAYBOOK";


    private final LibraryBookInputPort inputPort;
    private final Scanner scanner;
    private final PrintStream out;

    public ConsoleFacade(LibraryBookInputPort inputPort) {
        this(inputPort, System.in, System.out);
    }

    public ConsoleFacade(LibraryBookInputPort inputPort, InputStream in, PrintStream out) {
        this.inputPort = inputPort;
        this.scanner = new Scanner(in);
        this.out = out;
        init();
    }

    private void init() {
        while (true) {
            out.println(descriptionAction());
            doAction(scanner.nextLine());
        }
    }

    private void doAction(String labelAction) {
        switch (labelAction) {
            case LABEL_ACTION_LIST_LIBRARY:
                librariesAction();
                break;
            case LABEL_ACTION_LIST_SHELF_LIBRARY:
                shelvesByLibraryAction();
                break;
            case LABEL_ACTION_BOOKS_LIBRARY:
                booksByLibraryAction();
                break;
            case LABEL_ACTION_BOOKS_BORROWER:
                booksByBorrowerAction();
                break;
            case LABEL_ACTION_BORROW_BOOK:
                borrowBookAction();
                break;
            case LABEL_ACTION_RETURN_BOOK:
                returnBookAction();
                break;
            case LABEL_ACTION_PUTAWAY_BOOK:
                putAwayBookAction();
                break;
            default:
                unknowAction(labelAction);
                break;
        }
        out.println();
    }

    private String descriptionAction() {
        String desc = "Pour lister l'ensemble des bibliothèques, saisissez dans la console " + LABEL_ACTION_LIST_LIBRARY + "\n";
        desc += "Pour lister l'ensemble des étagères d'une bibliothèque, saisissez dans la console " + LABEL_ACTION_LIST_SHELF_LIBRARY + "\n";
        desc += "Pour lister l'ensemble des livres d'une bibliothèque, saisissez dans la console " + LABEL_ACTION_BOOKS_LIBRARY + "\n";
        desc += "Pour lister l'ensemble des livres empruntés par un utilisateur, saisissez dans la console " + LABEL_ACTION_BOOKS_BORROWER + "\n";
        desc += "Pour emprunter un livre, saisissez dans la console " + LABEL_ACTION_BORROW_BOOK + "\n";
        desc += "Pour retourner un livre, saisissez dans la console " + LABEL_ACTION_RETURN_BOOK + "\n";
        desc += "Pour ranger un livre, saisissez dans la console " + LABEL_ACTION_PUTAWAY_BOOK + "\n";
        return desc;
    }

    private void librariesAction() {
        out.println("Voici la liste des bibliothèques (identifiant -> nom):");
        inputPort.libraries()
                .forEach(library -> out.println(library.getId() + " -> " + library.getName()));
    }

    private void shelvesByLibraryAction() {
        out.println("Saisissez l'identifiant de la bibliothèque");
        try {
            Library library = new Library();
            library.setId(Integer.parseInt(scanner.nextLine()));

            Stream<Shelf> stream = inputPort.shelves(library);

            List<Shelf> list = stream.collect(Collectors.toList());
            if (list.isEmpty()) {
                out.println("Aucune étagère a été trouvé.");
            } else {
                out.println("Voici la liste des étagères de la bibliothèque (identifiant -> label):");
                list.forEach(shelf ->
                        out.println(shelf.getId() + " -> " + shelf.getLabel())
                );
            }

        } catch (NumberFormatException exception) {
            out.println("Identifiant non valide (valeur numérique uniquement)");
        }
    }

    private void booksByLibraryAction() {
        out.println("Saisissez l'identifiant de la bibliothèque");
        try {
            Library library = new Library();
            library.setId(Integer.parseInt(scanner.nextLine()));
            Stream<Book> stream = inputPort.books(library);
            out.println(displayBooks(stream));
        } catch (NumberFormatException exception) {
            out.println("Identifiant non valide (valeur numérique uniquement)");
        }
    }

    private void booksByBorrowerAction() {
        out.println("Saisissez l'utilisateur:");
        out.println(displayBooks(inputPort.books(scanner.nextLine())));
    }

    private String displayBooks(Stream<Book> stream) {
        List<Book> list = stream.collect(Collectors.toList());
        if (list.isEmpty()) {
            return "Aucun livre a été trouvé.";
        }
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("Voici la liste des livres (identifiant -> titre -> auteur -> détails):\n");
        list.forEach(book ->
                strBuilder.append(book.getId() + " -> " + book.getTitle() + " -> " + book.getAuthorName() + " -> " + displayDetailsBook(book) + "\n")
        );
        return strBuilder.toString();
    }

    private String displayDetailsBook(Book book) {
        switch (book.getState()) {
            case BORROWED:
                return "Emprunté par " + ((BorrowedBook) book).getBorrower();
            case RETURNED:
                return "Retourné à " + ((ReturnedBook) book).getLibrary().getName();
            case STORED:
                Shelf shelf = ((StoredBook) book).getShelf();
                return "Rangé à " + shelf.getLabel() + " (Bibliothèque: " + shelf.getLibrary().getName() + ")";
            default:
                return "";
        }
    }

    private void borrowBookAction() {
        StoredBook book = new StoredBook();
        try {
            out.println("Saisissez l'identifiant du livre à emprunter");
            book.setId(Integer.parseInt(scanner.nextLine()));

            out.println("Saisissez le nom de l'emprunteur");
            Book borrowBook = inputPort.borrowBook(book, scanner.nextLine());
            out.println("Le livre " + borrowBook.getTitle() + " a été emprunté");

        } catch (NumberFormatException exception) {
            out.println("Identifiant non valide (valeur numérique uniquement)");
        } catch (LibraryServiceException e) {
            out.println("Une erreur a été levée: " + e.getMessage());
        }
    }

    private void returnBookAction() {
        try {
            out.println("Saisissez l'identifiant du livre à retourner");
            BorrowedBook book = new BorrowedBook();
            book.setId(Integer.parseInt(scanner.nextLine()));

            out.println("Saisissez l'identifiant de la bibliothèque");
            Library library = new Library();
            library.setId(Integer.parseInt(scanner.nextLine()));
            Book returnedBook = inputPort.returnBook(book, library);
            out.println("Le livre " + returnedBook.getTitle() + " a été retourné");

        } catch (NumberFormatException exception) {
            out.println("Identifiant non valide (valeur numérique uniquement)");
        } catch (LibraryServiceException e) {
            out.println("Une erreur a été levée: " + e.getMessage());
        }
    }

    private void putAwayBookAction() {
        try {
            out.println("Saisissez l'identifiant du livre à ranger");
            ReturnedBook book = new ReturnedBook();
            book.setId(Integer.parseInt(scanner.nextLine()));

            out.println("Saisissez l'identifiant de l'étagère");
            Shelf shelf = new Shelf();
            shelf.setId(Integer.parseInt(scanner.nextLine()));
            Book shelvedBook = inputPort.putAwayBook(book, shelf);
            out.println("Le livre " + shelvedBook.getTitle() + " a été rangé");

        } catch (NumberFormatException exception) {
            out.println("Identifiant non valide (valeur numérique uniquement)");
        } catch (LibraryServiceException e) {
            out.println("Une erreur a été levée: " + e.getMessage());
        }
    }

    private void unknowAction(String action) {
        out.println("L'action " + action + " n'est pas connue");
    }

}
