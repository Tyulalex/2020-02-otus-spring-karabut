package spring.book.mvc.repository;

import org.springframework.data.repository.CrudRepository;
import spring.book.mvc.model.Genre;

import java.util.List;

public interface GenreRepository extends CrudRepository<Genre, Long> {

    List<Genre> findAll();

    List<Genre> findAllByName(String name);
}
