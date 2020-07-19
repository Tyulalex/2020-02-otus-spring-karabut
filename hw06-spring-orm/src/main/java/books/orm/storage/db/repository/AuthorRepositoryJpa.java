package books.orm.storage.db.repository;

import books.orm.storage.db.model.Author;

import java.util.List;

public interface AuthorRepositoryJpa {

    List<Author> findAll();

    List<Author> findByName(String name);

    Author save(Author author);
}
