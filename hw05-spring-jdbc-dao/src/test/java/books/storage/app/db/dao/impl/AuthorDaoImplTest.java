package books.storage.app.db.dao.impl;

import books.storage.app.db.dao.AuthorDao;
import books.storage.app.db.model.Author;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@DisplayName("Should operate with author table")
@Import(AuthorDaoImpl.class)
class AuthorDaoImplTest {

    @Autowired
    private AuthorDao authorDao;

    @Autowired
    private NamedParameterJdbcOperations jdbcOperations;

    @Test
    @DisplayName("Should return all authors available")
    void getAuthors() {
        List<Author> authorList = authorDao.getAuthors();
        assertThat(authorList.size()).isEqualTo(3);
        assertThat(authorList.get(0)).isEqualTo(new Author(1, "David Wiesner"));
    }

    @Test
    @DisplayName("Should return author if already exists in database")
    void findAuthorOrSave() {
        int count = getAuthorsCount();
        Author author = new Author("David Wiesner");
        authorDao.findAuthorOrSave(author);
        assertThat(author.getId()).isEqualTo(1);
        int newCount = getAuthorsCount();
        assertThat(newCount).isEqualTo(count);
    }

    @Test
    @DisplayName("Should insert new author is not exists")
    void saveNewAuthor() {
        int count = getAuthorsCount();
        Author author = new Author("New Author");
        authorDao.findAuthorOrSave(author);
        int newCount = getAuthorsCount();
        assertThat(newCount).isEqualTo(count + 1);
        assertThat(author.getId()).isEqualTo(4);
    }

    @Test
    @DisplayName("Should return authors by name")
    void findAuthorsByName() {
        List<Author> authors = authorDao.findAuthorsByName("David Wiesner");
        assertThat(authors.size()).isEqualTo(1);
        assertThat(authors.get(0)).isEqualTo(new Author(1, "David Wiesner"));
    }

    @Test
    @DisplayName("Should return empty list of authors if nothing found by name")
    void findAuthorsByNameEmpty() {
        List<Author> authors = authorDao.findAuthorsByName("Not Existing");
        assertThat(authors.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should return authors by book id")
    void findAuthorsByBookId() {
        List<Author> authors = authorDao.findAuthorsByBookId(2);
        assertThat(authors.size()).isEqualTo(2);
        assertThat(authors.get(0)).isEqualTo(new Author(2, "Alexander Pushkin"));
        assertThat(authors.get(1)).isEqualTo(new Author(3, "Anna Maude (Translator)"));
    }

    private int getAuthorsCount() {
        return jdbcOperations.getJdbcOperations().queryForObject("select count(*) from authors", Integer.class);
    }
}