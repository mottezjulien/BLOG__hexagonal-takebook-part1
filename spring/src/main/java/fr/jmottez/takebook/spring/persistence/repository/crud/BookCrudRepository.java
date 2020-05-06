package fr.jmottez.takebook.spring.persistence.repository.crud;
import fr.jmottez.takebook.spring.persistence.entity.BookEntity;
import org.springframework.data.repository.CrudRepository;

public interface BookCrudRepository extends CrudRepository<BookEntity, Integer> {

}

