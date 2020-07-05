package books.storage.app.services;

import books.storage.app.db.model.Author;
import books.storage.app.db.model.Book;
import books.storage.app.db.model.Genre;

import java.util.List;
import java.util.Optional;

public interface BookToStringService {

    String allBooksString(List<Book> books);

    String bookAsString(Optional<Book> book, long id);

    String authorsToString(List<Author> authors);

    String genresToString(List<Genre> genres);
}
