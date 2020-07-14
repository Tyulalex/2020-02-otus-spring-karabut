package books.storage.app.db.dao.impl;

import books.storage.app.db.dao.AuthorDao;
import books.storage.app.db.dao.AuthorDaoException;
import books.storage.app.db.model.Author;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Slf4j
public class AuthorDaoImpl implements AuthorDao {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public List<Author> getAuthors() {
        try {
            return jdbcOperations.query("select id, author_name from authors", new AuthorMapper());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new AuthorDaoException(ex);
        }
    }

    @Override
    public void findAuthorOrSave(Author author) {
        List<Author> authors;
        try {
            authors = this.findAuthorsByName(author.getFullName());
            if (authors.isEmpty()) {
                this.jdbcOperations.update(
                        "insert into authors(`id`, `author_name`) values (AUTHOR_SEQ_ID.nextval, :author_name)",
                        Map.of("author_name", author.getFullName()));
                authors = this.findAuthorsByName(author.getFullName());
            }
            author.setId(authors.get(0).getId());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new AuthorDaoException(ex);
        }
    }


    @Override
    public List<Author> findAuthorsByName(String authorName) {
        try {
            return jdbcOperations.query("select id, author_name from authors where author_name=:author_name",
                    Map.of("author_name", authorName),
                    new AuthorMapper());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new AuthorDaoException(ex);
        }
    }

    @Override
    public List<Author> findAuthorsByBookId(long bookId) {
        try {
            return jdbcOperations.query(
                    "select id, author_name from authors as a " +
                            "join books_authors as ba on ba.author_id = a.id " +
                            "where ba.book_id = :bookId",
                    Map.of("bookId", bookId), new AuthorMapper());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new AuthorDaoException(ex);
        }
    }

    private static class AuthorMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String fullName = resultSet.getString("author_name");
            return new Author(id, fullName);
        }
    }
}
