package books.storage.app.services;

import books.storage.app.db.model.Genre;

import java.util.List;

public interface GenreService {

    List<Genre> getGenres();
}
