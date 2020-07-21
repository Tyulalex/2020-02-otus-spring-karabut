package book.springdata.sql.storage.service;


import book.springdata.sql.storage.model.Book;
import book.springdata.sql.storage.model.Comment;

public interface BookService {

    String getBooks();

    void saveBook(Book book);

    String findBooksByTitle(String title);

    String findBookById(long id);

    void deleteBookById(long id);

    void updateBookById(long id, String title);

    void addComment(Comment comment);
}
