package books.orm.storage.service;

import books.orm.storage.db.model.Author;
import books.orm.storage.db.model.Book;
import books.orm.storage.db.model.Comment;
import books.orm.storage.db.model.Genre;

import java.util.List;

public interface BookFormatterService {

    String allBooksString(List<Book> books);

    String authorsToString(List<Author> authors);

    String genresToString(List<Genre> genres);

    String commentsToString(List<Comment> comments);

    String noItemString(String itemName, long id);
}
