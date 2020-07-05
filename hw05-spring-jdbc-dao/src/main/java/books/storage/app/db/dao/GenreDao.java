package books.storage.app.db.dao;

import books.storage.app.db.model.Genre;

import java.util.List;

public interface GenreDao {

    void findGenreOrSave(Genre genre);

    List<Genre> findGenresByName(String genreName);

    List<Genre> findGenresByBookId(long bookId);

    List<Genre> getGenres();
}
