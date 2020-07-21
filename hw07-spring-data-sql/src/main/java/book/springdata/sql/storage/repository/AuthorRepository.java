package book.springdata.sql.storage.repository;

import book.springdata.sql.storage.model.Author;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuthorRepository extends CrudRepository<Author, Long> {

    List<Author> findAll();

    List<Author> findAllByFullName(String fullName);
}
