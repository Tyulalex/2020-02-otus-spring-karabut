package books.storage.app.services;

import books.storage.app.db.model.Author;

import java.util.List;

public interface AuthorService {

    List<Author> getAuthors();
}
