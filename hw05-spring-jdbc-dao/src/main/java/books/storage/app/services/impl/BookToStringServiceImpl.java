package books.storage.app.services.impl;

import books.storage.app.db.model.Author;
import books.storage.app.db.model.Book;
import books.storage.app.db.model.Genre;
import books.storage.app.services.BookToStringService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookToStringServiceImpl implements BookToStringService {

    private static final String NO_BOOKS = "No Books Found";
    private static final String NO_AUTHORS = "No Authors Found";
    private static final String NO_GENRES = "No Genres Found";

    @Override
    public String allBooksString(List<Book> books) {
        return listToString(books, NO_BOOKS);
    }

    @Override
    public String bookAsString(Optional<Book> book, long id) {
        return book.isPresent() ? book.get().toString() : String.format("No such book with id=%d", id);
    }

    @Override
    public String authorsToString(List<Author> authors) {
        return listToString(authors, NO_AUTHORS);
    }

    @Override
    public String genresToString(List<Genre> genres) {
        return listToString(genres, NO_GENRES);
    }

    private <T> String listToString(List<T> objects, String notFoundMessage) {
        if (objects.isEmpty()) {
            return notFoundMessage;
        }
        StringBuilder stringBuilder = new StringBuilder();
        objects.forEach(stringBuilder::append);
        return stringBuilder.toString();
    }

}
