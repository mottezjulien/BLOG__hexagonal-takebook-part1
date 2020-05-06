package fr.jmottez.takebook.console;

import fr.jmottez.takebook.domain.ports.input.LibraryBookInputPort;

public class ConsoleApp {
    public static void main(String[] args){
        ConsoleInternOutputAdapters repository = new ConsoleInternOutputAdapters();
        LibraryBookInputPort libraryService = new LibraryBookInputPort(repository, repository, repository);
        new ConsoleFacade(libraryService);
    }
}
