package book.springdata.sql.storage.repository;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Shall work with book repository")
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Book repository shall find all book")
    void findAll() {
        val books = bookRepository.findAll();
        assertThat(books.size()).isEqualTo(2);
        assertThat(books.get(0).getTitle()).isEqualTo("three pigs");
    }

    @Test
    @DisplayName("Book repository shall find books by title")
    void findAllByTitleContaining() {
        val books = bookRepository.findAllByTitleContaining("three");
        assertThat(books.size()).isEqualTo(1);
        assertThat(books.get(0).getId()).isEqualTo(1);
    }
}