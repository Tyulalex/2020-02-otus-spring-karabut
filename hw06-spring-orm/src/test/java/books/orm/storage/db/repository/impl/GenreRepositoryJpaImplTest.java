package books.orm.storage.db.repository.impl;

import books.orm.storage.db.model.Genre;
import books.orm.storage.db.repository.GenreRepositoryJpa;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Jpa Repository for Genre")
@DataJpaTest
@Import(GenreRepositoryJpaImpl.class)
class GenreRepositoryJpaImplTest {

    @Autowired
    private GenreRepositoryJpa genreRepositoryJpa;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("Shall find all available genres")
    void findAll() {
        val actualGenresFound = genreRepositoryJpa.findAll();
        assertThat(actualGenresFound.size()).isEqualTo(3);
        assertThat(actualGenresFound.get(0)).isEqualTo(new Genre(1, "Children's picture book"));
    }

    @Test
    @DisplayName("Shall find by name")
    void findByName() {
        val actualGenresFound = genreRepositoryJpa.findByName("Poem");
        assertThat(actualGenresFound).isEqualTo(List.of(new Genre(2, "Poem")));
    }

    @Test
    @DisplayName("Shall save new genre")
    void saveGenre() {
        val genre = new Genre(0, "new genre");
        genreRepositoryJpa.save(genre);
        assertThat(genre.getId()).isEqualTo(4);
        val savedGenre = testEntityManager.find(Genre.class, 4L);
        assertThat(savedGenre).isNotNull().isEqualTo(genre);
    }

    @Test
    @DisplayName("Shall merge new genre")
    void mergeGenre() {
        val genre = new Genre(2, "Poem");
        genreRepositoryJpa.save(genre);
        val query = testEntityManager.getEntityManager().createQuery("select g from Genre g", Genre.class);
        val genres = query.getResultList();
        assertThat(genres.size()).isEqualTo(3);
    }
}