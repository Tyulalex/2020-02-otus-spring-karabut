package books.storage.app.db.dao.impl;

import books.storage.app.db.dao.BookDao;
import books.storage.app.db.dao.BookDaoException;
import books.storage.app.db.dao.BookNotFoundDaoException;
import books.storage.app.db.model.Author;
import books.storage.app.db.model.Book;
import books.storage.app.db.model.Genre;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
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
public class BookDaoImpl implements BookDao {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public List<Book> getBooks() {
        try {
            return jdbcOperations.query("select * from books", new BookMapper());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new BookDaoException(ex);
        }
    }

    @Override
    public void saveBook(Book book) {
        try {
            Map<String, Object> params = Map.of("title", book.getTitle());
            this.jdbcOperations.update("insert into books(`id`, `title`) values (BOOK_SEQ_ID.nextval, :title)", params);
            for (Author author : book.getAuthors()) {
                this.jdbcOperations.update(
                        "insert into books_authors(`author_id`, `book_id`) values (:author_id, BOOK_SEQ_ID.currval)",
                        Map.of("author_id", author.getId())
                );
            }
            for (Genre genre : book.getGenres()) {
                this.jdbcOperations.update(
                        "insert into books_genres(`genre_id`, `book_id`) values (:genre_id, BOOK_SEQ_ID.currval)",
                        Map.of("genre_id", genre.getId())
                );
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new BookDaoException(ex);
        }
    }

    @Override
    public void deleteBookById(long id) {
        try {
            this.jdbcOperations.update("delete from books_genres where book_id = :id", Map.of("id", id));
            this.jdbcOperations.update("delete from books_authors where book_id = :id", Map.of("id", id));
            this.jdbcOperations.update("delete from books where id = :id", Map.of("id", id));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new BookDaoException(ex);
        }

    }

    @Override
    public List<Book> findBooksByTitle(String title) {
        try {
            return jdbcOperations.query("select * from books where title like :title",
                    Map.of("title", "%" + title + "%"), new BookMapper());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new BookDaoException(ex);
        }
    }

    @Override
    public Book findBookById(long id) {
        try {
            return jdbcOperations.queryForObject("select * from books where id = :id",
                    Map.of("id", id), new BookMapper());
        } catch (IncorrectResultSizeDataAccessException ex) {
            if (ex.getActualSize() == 0) {
                log.debug(String.format("no book with id = %d", id));
                throw new BookNotFoundDaoException(ex);
            } else {
                log.error(ex.getMessage());
                throw new BookDaoException(ex);
            }

        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new BookDaoException(ex);
        }
    }

    @Override
    public void updateBookById(long id, String title) {
        try {
            int result = jdbcOperations.update(
                    "update books set title=:title where id=:id",
                    Map.of("title", title, "id", id)
            );
            if (result < 1) {
                throw new BookNotFoundDaoException();
            }
        } catch (BookNotFoundDaoException ex) {
            log.debug(String.format("no book with id = %d", id));
            throw new BookNotFoundDaoException();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new BookDaoException(ex);
        }
    }

    static class BookMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String title = resultSet.getString("title");
            return new Book(id, title);
        }
    }
}
