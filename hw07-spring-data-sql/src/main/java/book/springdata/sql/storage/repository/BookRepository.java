package book.springdata.sql.storage.repository;

import book.springdata.sql.storage.model.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {

    List<Book> findAll();

    List<Book> findAllByTitleContaining(String title);
}
