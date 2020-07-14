package books.storage.app.services.impl;

import books.storage.app.db.dao.GenreDao;
import books.storage.app.db.dao.GenreDaoException;
import books.storage.app.db.model.Genre;
import books.storage.app.services.GenreService;
import books.storage.app.services.GenreServiceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Import(GenreServiceImpl.class)
@DisplayName("Shall operate with genre dao and provide genre service")
class GenreServiceImplTest {

    @Autowired
    private GenreService genreService;

    @MockBean
    private GenreDao genreDao;

    @Test
    @DisplayName("shall return genres")
    void getGenres() {
        List<Genre> genreList = List.of(
                new Genre(1, "Genre1"), new Genre(2, "Genre2"));
        when(genreDao.getGenres()).thenReturn(genreList);
        assertThat(genreService.getGenres()).isEqualTo(genreList);
    }

    @Test
    @DisplayName("shall throws exception when dao throws exception")
    void getGenresThrowsException() {
        when(genreDao.getGenres()).thenThrow(GenreDaoException.class);
        assertThrows(GenreServiceException.class, () -> genreService.getGenres());
    }
}