package books.orm.storage.db.repository.impl;

import books.orm.storage.db.model.Author;
import books.orm.storage.db.repository.AuthorRepositoryJpa;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Shall work with author repository")
@Import(AuthorRepositoryJpaImpl.class)
class AuthorRepositoryJpaImplTest {

    @Autowired
    private AuthorRepositoryJpa authorRepositoryJpa;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("Shall find all available authors")
    void findAll() {
        val actualAuthorsFound = authorRepositoryJpa.findAll();
        assertThat(actualAuthorsFound.size()).isEqualTo(3);
        assertThat(actualAuthorsFound.get(0)).isEqualTo(new Author(1, "David Wiesner"));
    }

    @Test
    @DisplayName("Shall find by name")
    void findByName() {
        val actualAuthorsFound = authorRepositoryJpa.findByName("David Wiesner");
        assertThat(actualAuthorsFound).isEqualTo(List.of(new Author(1, "David Wiesner")));
    }

    @Test
    @DisplayName("Shall save new author")
    void saveAuthor() {
        val author = new Author(0, "new author");
        authorRepositoryJpa.save(author);
        assertThat(author.getId()).isEqualTo(4);
        val savedAuthor = testEntityManager.find(Author.class, 4L);
        assertThat(savedAuthor).isNotNull().isEqualTo(author);
    }

    @Test
    @DisplayName("Shall merge new author")
    void mergeAuthor() {
        val author = new Author(1, "David Wiesner");
        authorRepositoryJpa.save(author);
        val query = testEntityManager.getEntityManager().createQuery("select a from Author a", Author.class);
        val authors = query.getResultList();
        assertThat(authors.size()).isEqualTo(3);
    }
}