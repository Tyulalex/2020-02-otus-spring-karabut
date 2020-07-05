package books.storage.app.db.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Book {

    private long id;
    private final String title;
    private List<Author> authors;
    private List<Genre> genres;

    public Book(String title) {
        this.title = title;
        this.authors = new ArrayList<>();
        this.genres = new ArrayList<>();
    }

    public Book(long id, String title) {
        this(title);
        this.id = id;
    }


    @Override
    public String toString() {
        if ((authors.isEmpty() || authors == null) && (genres.isEmpty() || genres == null)) {
            return String.format("%d. \"%s\"", id, title);
        }
        return String.format("%d. \"%s\". Authors: %s. Genre: %s", id, title, authors, genres);
    }
}
