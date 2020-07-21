package book.springdata.sql.storage.service;


import book.springdata.sql.storage.model.Genre;

import java.util.List;

public interface GenreService {

    List<Genre> getGenres();

    void saveOrRefresh(Genre genre);
}
