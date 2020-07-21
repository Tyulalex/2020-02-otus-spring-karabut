package book.springdata.sql.storage.repository;

import book.springdata.sql.storage.model.Genre;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GenreRepository extends CrudRepository<Genre, Long> {

    List<Genre> findAll();

    List<Genre> findAllByName(String name);
}
