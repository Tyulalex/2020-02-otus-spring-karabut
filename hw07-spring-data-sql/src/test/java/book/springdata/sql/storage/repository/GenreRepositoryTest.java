package book.springdata.sql.storage.repository;

import book.springdata.sql.storage.model.Genre;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Jpa Repository for Genre")
@DataJpaTest
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Test
    @DisplayName("Shall find all available genres")
    void findAll() {
        val actualGenresFound = genreRepository.findAll();
        assertThat(actualGenresFound.size()).isEqualTo(3);
        assertThat(actualGenresFound.get(0)).isEqualTo(new Genre(1, "Children's picture book"));
    }

    @Test
    @DisplayName("Shall find by name")
    void findAllByName() {
        val actualGenresFound = genreRepository.findAllByName("Poem");
        assertThat(actualGenresFound).isEqualTo(List.of(new Genre(2, "Poem")));
    }
}