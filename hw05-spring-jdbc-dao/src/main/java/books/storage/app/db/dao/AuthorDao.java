package books.storage.app.db.dao;

import books.storage.app.db.model.Author;

import java.util.List;

public interface AuthorDao {

    List<Author> getAuthors();

    void findAuthorOrSave(Author author);

    List<Author> findAuthorsByName(String authorName);

    List<Author> findAuthorsByBookId(long bookId);
}
