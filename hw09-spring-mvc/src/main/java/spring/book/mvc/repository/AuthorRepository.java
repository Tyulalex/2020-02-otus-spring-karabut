package spring.book.mvc.repository;

import org.springframework.data.repository.CrudRepository;
import spring.book.mvc.model.Author;

import java.util.List;

public interface AuthorRepository extends CrudRepository<Author, Long> {

    List<Author> findAll();

    List<Author> findAllByFullName(String fullName);
}
