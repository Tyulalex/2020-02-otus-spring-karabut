package books.orm.storage.service;

import books.orm.storage.db.model.Book;
import books.orm.storage.db.model.Comment;

public interface BookService {

    String getBooks();

    void saveBook(Book book);

    String findBooksByTitle(String title);

    String findBookById(long id);

    void deleteBookById(long id);

    void updateBookById(long id, String title);

    void addComment(Comment comment);
}
