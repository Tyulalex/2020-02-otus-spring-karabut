package book.springdata.sql.storage.repository;

import book.springdata.sql.storage.model.Author;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Shall work with author repository")
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    @DisplayName("Shall find all available authors")
    void findAll() {
        val actualAuthorsFound = authorRepository.findAll();
        assertThat(actualAuthorsFound.size()).isEqualTo(3);
        assertThat(actualAuthorsFound.get(0)).isEqualTo(new Author(1, "David Wiesner"));
    }

    @Test
    @DisplayName("Shall find by name")
    void findByName() {
        val actualAuthorsFound = authorRepository.findAllByFullName("David Wiesner");
        assertThat(actualAuthorsFound).isEqualTo(List.of(new Author(1, "David Wiesner")));
    }
}