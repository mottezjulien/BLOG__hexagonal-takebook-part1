package fr.jmottez.takebook.spring;

import fr.jmottez.takebook.domain.ports.input.LibraryBookInputPort;
import fr.jmottez.takebook.domain.ports.output.BookOutputPort;
import fr.jmottez.takebook.domain.ports.output.LibraryOutputPort;
import fr.jmottez.takebook.domain.ports.output.ShelfOutputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class SpringApp {

    @Autowired
    private LibraryOutputPort libraryRepository;

    @Autowired
    private ShelfOutputPort shelfRepository;

    @Autowired
    private BookOutputPort bookRepository;

    @Bean
    public LibraryBookInputPort libraryBookInputPort() {
        return new LibraryBookInputPort(libraryRepository, shelfRepository, bookRepository);
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringApp.class, args);
    }

}
