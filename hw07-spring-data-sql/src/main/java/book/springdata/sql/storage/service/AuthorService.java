package book.springdata.sql.storage.service;

import book.springdata.sql.storage.model.Author;

import java.util.List;

public interface AuthorService {

    List<Author> getAuthors();

    void saveOrRefresh(Author author);
}
