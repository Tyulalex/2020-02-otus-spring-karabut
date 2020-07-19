package book.springdata.sql.storage.service.impl;

import book.springdata.sql.storage.model.Author;
import book.springdata.sql.storage.model.Book;
import book.springdata.sql.storage.model.Comment;
import book.springdata.sql.storage.model.Genre;
import book.springdata.sql.storage.service.BookFormatterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
public class BookFormatterServiceImpl implements BookFormatterService {

    private static final String NO_BOOKS = "No Books Found";
    private static final String NO_AUTHORS = "No Authors Found";
    private static final String NO_GENRES = "No Genres Found";
    private static final String NO_COMMENTS = "No Comments Found";

    @Override
    public String allBooksString(List<Book> books) {

        return listToString(books, NO_BOOKS);
    }

    @Override
    public String authorsToString(List<Author> authors) {
        return listToString(authors, NO_AUTHORS);
    }

    @Override
    public String genresToString(List<Genre> genres) {
        return listToString(genres, NO_GENRES);
    }

    @Override
    public String commentsToString(List<Comment> comments) {
        return listToString(comments, NO_COMMENTS);
    }

    @Override
    public String noItemString(String itemName, long id) {
        return String.format("No such %s with id=%d", itemName, id);
    }

    private <T> String listToString(List<T> objects, String notFoundMessage) {
        if (objects.isEmpty()) {
            return notFoundMessage;
        }
        StringJoiner joiner = new StringJoiner("\n");
        objects.forEach(o -> joiner.add(o.toString()));
        return joiner.toString();
    }
}
