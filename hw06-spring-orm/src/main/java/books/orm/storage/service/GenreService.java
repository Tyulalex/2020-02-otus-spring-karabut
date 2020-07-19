package books.orm.storage.service;

import books.orm.storage.db.model.Genre;

import java.util.List;

public interface GenreService {

    List<Genre> getGenres();

    void saveOrRefresh(Genre genre);
}
