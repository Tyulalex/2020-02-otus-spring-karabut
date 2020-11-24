package spring.book.mvc.service;

import spring.book.mvc.model.Genre;

public interface GenreService {

    void saveOrRefresh(Genre genre);
}
