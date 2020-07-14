package books.orm.storage.db.repository.impl;

import books.orm.storage.db.model.Author;
import books.orm.storage.db.model.Book;
import books.orm.storage.db.model.Genre;
import books.orm.storage.db.repository.BookRepositoryJpa;
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
@Import(BookRepositoryJpaImpl.class)
@DisplayName("Shall work with book repository")
class BookRepositoryJpaImplTest {

    @Autowired
    private BookRepositoryJpa bookRepositoryJpa;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("Book repository shall find all book")
    void findAll() {
        val books = bookRepositoryJpa.findAll();
        assertThat(books.size()).isEqualTo(2);
        assertThat(books.get(0).getTitle()).isEqualTo("three pigs");
    }

    @Test
    @DisplayName("Book repository shall find by id")
    void findById() {
        val book = bookRepositoryJpa.findById(1);
        assertThat(book).isPresent();
        assertThat(book.get().getAuthors().size()).isEqualTo(1);
        assertThat(book.get().getAuthors().get(0)).isEqualTo(new Author(1, "David Wiesner"));
        assertThat(book.get().getGenres().size()).isEqualTo(1);
        assertThat(book.get().getGenres().get(0)).isEqualTo(new Genre(1, "Children's picture book"));
        assertThat(book.get().getId()).isEqualTo(1);
        assertThat(book.get().getTitle()).isEqualTo("three pigs");
    }

    @Test
    @DisplayName("Book repository shall return empty optional if not found by id")
    void findByIdNotFound() {
        val emptyBook = bookRepositoryJpa.findById(10);
        assertThat(emptyBook).isEmpty();
    }

    @Test
    @DisplayName("Book repository shall find save a book")
    void save() {
        val book = Book.builder()
                .title("new book")
                .authors(List.of(new Author(1, "David Wiesner")))
                .genres(List.of(new Genre(1, "Children's picture book")))
                .build();
        bookRepositoryJpa.save(book);
        val savedBook = testEntityManager.find(Book.class, 3L);
        assertThat(savedBook.getTitle()).isEqualTo("new book");
        assertThat(savedBook.getGenres().size()).isEqualTo(1);
        assertThat(savedBook.getGenres().get(0)).isEqualTo(new Genre(1, "Children's picture book"));
        assertThat(savedBook.getAuthors().size()).isEqualTo(1);
        assertThat(savedBook.getAuthors().get(0)).isEqualTo(new Author(1, "David Wiesner"));
        assertThat(savedBook.getId()).isEqualTo(3);
    }

    @Test
    @DisplayName("Book repository shall delete by Id")
    void deleteById() {
        bookRepositoryJpa.deleteById(2);
        val deletedBook = testEntityManager.find(Book.class, 2L);
        assertThat(deletedBook).isNull();
    }

    @Test
    @DisplayName("Book repository shall update title by Id")
    void updateTitleById() {
        bookRepositoryJpa.updateTitleById(2, "new title");
        val updatedBook = testEntityManager.find(Book.class, 2L);
        assertThat(updatedBook.getTitle()).isEqualTo("new title");
    }

    @Test
    @DisplayName("Book repository shall find books by title")
    void findBooksByTitle() {
        val books = bookRepositoryJpa.findBooksByTitle("three");
        assertThat(books.size()).isEqualTo(1);
        assertThat(books.get(0).getId()).isEqualTo(1);
    }
}