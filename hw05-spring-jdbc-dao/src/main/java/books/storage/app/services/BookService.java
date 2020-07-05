package books.storage.app.services;

import books.storage.app.db.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    List<Book> getBooks();

    void saveBook(Book book);

    List<Book> findBooksByTitle(String title);

    Optional<Book> findBookById(long id);

    void deleteBookById(long id);

    void updateBookById(long id, String title);
}
