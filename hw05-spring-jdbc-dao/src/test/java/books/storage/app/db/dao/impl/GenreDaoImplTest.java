package books.storage.app.db.dao.impl;

import books.storage.app.db.dao.GenreDao;
import books.storage.app.db.model.Genre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@DisplayName("Should operate with genre table")
@Import(GenreDaoImpl.class)
class GenreDaoImplTest {

    @Autowired
    private GenreDao genreDao;

    @Autowired
    private NamedParameterJdbcOperations jdbcOperations;

    @Test
    @DisplayName("Should return genre if already exists in database")
    void findGenreOrSave() {
        int count = getGenresCount();
        Genre genre = new Genre("Poem");
        genreDao.findGenreOrSave(genre);
        assertThat(genre.getId()).isEqualTo(2);
        int newCount = getGenresCount();
        assertThat(newCount).isEqualTo(count);
    }

    @Test
    @DisplayName("Should save genre if not in database")
    void saveNewGenre() {
        int count = getGenresCount();
        Genre genre = new Genre("New Genre");
        genreDao.findGenreOrSave(genre);
        assertThat(genre.getId()).isEqualTo(4);
        int newCount = getGenresCount();
        assertThat(newCount).isEqualTo(count + 1);
    }

    @Test
    @DisplayName("Should return genders by name")
    void findGenresByName() {
        List<Genre> genres = genreDao.findGenresByName("Poem");
        assertThat(genres.size()).isEqualTo(1);
        assertThat(genres.get(0)).isEqualTo(new Genre(2, "Poem"));
    }

    @Test
    @DisplayName("Should return genders by book id")
    void findGenresByBookId() {
        List<Genre> genres = genreDao.findGenresByBookId(2);
        assertThat(genres.size()).isEqualTo(2);
        assertThat(genres.get(0)).isEqualTo(new Genre(2, "Poem"));
        assertThat(genres.get(1)).isEqualTo(new Genre(3, "novel in verses"));
    }

    @Test
    @DisplayName("Should return empty list of genders by book id if book id not present")
    void findGenresByBookIdReturnsEmptyListIfBookNotFound() {
        List<Genre> genres = genreDao.findGenresByBookId(20);
        assertThat(genres.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should return all genres available")
    void getGenres() {
        List<Genre> genres = genreDao.getGenres();
        assertThat(genres.size()).isEqualTo(3);
        assertThat(genres.get(0)).isEqualTo(new Genre(1, "Children's picture book"));
    }

    private int getGenresCount() {
        return jdbcOperations.getJdbcOperations().queryForObject("select count(*) from genres", Integer.class);
    }
}