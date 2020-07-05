package books.storage.app.db.dao;

import books.storage.app.db.model.Book;

import java.util.List;

public interface BookDao {

    List<Book> getBooks();

    void saveBook(Book book);

    void deleteBookById(long id);

    List<Book> findBooksByTitle(String title);

    Book findBookById(long id);

    void updateBookById(long id, String title);
}
