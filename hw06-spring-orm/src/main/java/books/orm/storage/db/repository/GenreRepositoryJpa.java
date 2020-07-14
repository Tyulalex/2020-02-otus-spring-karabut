package books.orm.storage.db.repository;

import books.orm.storage.db.model.Genre;

import java.util.List;

public interface GenreRepositoryJpa {

    List<Genre> findAll();

    Genre save(Genre genre);

    List<Genre> findByName(String name);
}
