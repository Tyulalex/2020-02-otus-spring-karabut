package book.springdata.sql.storage.service;

import book.springdata.sql.storage.model.Author;
import book.springdata.sql.storage.model.Book;
import book.springdata.sql.storage.model.Comment;
import book.springdata.sql.storage.model.Genre;

import java.util.List;

public interface BookFormatterService {

    String allBooksString(List<Book> books);

    String authorsToString(List<Author> authors);

    String genresToString(List<Genre> genres);

    String commentsToString(List<Comment> comments);

    String noItemString(String itemName, long id);
}
