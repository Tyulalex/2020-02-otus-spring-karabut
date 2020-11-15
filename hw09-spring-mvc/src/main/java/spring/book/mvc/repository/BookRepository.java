package spring.book.mvc.repository;

import org.springframework.data.repository.CrudRepository;
import spring.book.mvc.model.Book;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {

    List<Book> findAll();

    List<Book> findAllByTitleContaining(String title);
}
