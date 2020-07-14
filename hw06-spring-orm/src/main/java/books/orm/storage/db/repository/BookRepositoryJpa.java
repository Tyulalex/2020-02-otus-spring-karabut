package books.orm.storage.db.repository;

import books.orm.storage.db.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepositoryJpa {

    List<Book> findAll();

    Optional<Book> findById(long id);

    Book save(Book book);

    void deleteById(long id);

    void updateTitleById(long id, String title);

    List<Book> findBooksByTitle(String title);
}
