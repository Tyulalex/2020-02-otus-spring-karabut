package books.storage.app.db.dao.impl;

import books.storage.app.db.dao.GenreDao;
import books.storage.app.db.dao.GenreDaoException;
import books.storage.app.db.model.Genre;
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
@Slf4j
@RequiredArgsConstructor
public class GenreDaoImpl implements GenreDao {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public void findGenreOrSave(Genre genre) {
        List<Genre> genres;
        try {
            String genre_name = genre.getName();
            genres = this.findGenresByName(genre_name);
            if (genres.isEmpty()) {
                this.jdbcOperations.update(
                        "insert into genres(`id`, `genre_name`) values (GENRE_SEQ_ID.nextval, :genre_name)",
                        Map.of("genre_name", genre_name));
                genres = this.findGenresByName(genre_name);
            }
            genre.setId(genres.get(0).getId());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new GenreDaoException(ex);
        }
    }

    @Override
    public List<Genre> findGenresByName(String genreName) {
        try {
            return jdbcOperations.query("select id, genre_name from genres where genre_name=:genre_name",
                    Map.of("genre_name", genreName),
                    new GenreMapper());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new GenreDaoException(ex);
        }
    }

    @Override
    public List<Genre> findGenresByBookId(long bookId) {
        try {
            return jdbcOperations.query(
                    "select id, genre_name from genres as g " +
                            "join books_genres as bg on bg.genre_id = g.id " +
                            "where bg.book_id = :bookId",
                    Map.of("bookId", bookId), new GenreMapper());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new GenreDaoException(ex);
        }
    }

    @Override
    public List<Genre> getGenres() {
        try {
            return jdbcOperations.query("select id, genre_name from genres", new GenreMapper());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new GenreDaoException(ex);
        }
    }

    private static class GenreMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String genreName = rs.getString("genre_name");
            return new Genre(id, genreName);
        }
    }
}
