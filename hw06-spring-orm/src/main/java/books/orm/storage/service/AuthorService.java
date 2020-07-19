package books.orm.storage.service;

import books.orm.storage.db.model.Author;

import java.util.List;

public interface AuthorService {

    List<Author> getAuthors();

    void saveOrRefresh(Author author);
}
