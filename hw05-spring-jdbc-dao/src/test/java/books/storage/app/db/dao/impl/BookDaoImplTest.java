package books.storage.app.db.dao.impl;

import books.storage.app.db.dao.BookDao;
import books.storage.app.db.dao.BookNotFoundDaoException;
import books.storage.app.db.model.Author;
import books.storage.app.db.model.Book;
import books.storage.app.db.model.Genre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@DisplayName("Should operate with book table")
@Import(BookDaoImpl.class)
class BookDaoImplTest {

    @Autowired
    private BookDao bookDao;

    @Autowired
    private NamedParameterJdbcOperations jdbcOperations;

    @Test
    @DisplayName("Should return all books")
    void getBooks() {
        List<Book> books = bookDao.getBooks();
        assertThat(books.size()).isEqualTo(2);
        assertThat(books.get(0)).isEqualTo(new Book(1, "three pigs"));
        assertThat(books.get(1)).isEqualTo(new Book(2, "EUGENE ONEGIN"));
    }

    @Test
    @DisplayName("Should save new book with existing Author and existing Genre")
    void saveBook() {
        int authorCount = getAuthorsCount();
        int booksCount = getBooksCount();
        int genreCount = getGenresCount();
        Book book = new Book("new book");
        book.setGenres(List.of(new Genre(2, "Poem")));
        book.setAuthors(List.of(new Author(1, "David Wiesner")));
        bookDao.saveBook(book);
        assertThat(getAuthorsCount()).isEqualTo(authorCount);
        assertThat(getBooksCount()).isEqualTo(booksCount + 1);
        assertThat(getGenresCount()).isEqualTo(genreCount);

        int author_id = searchBooksAuthorsByBookId(3);
        assertThat(author_id).isEqualTo(1);

        int genre_id = searchBooksGenresByBookId(3);
        assertThat(genre_id).isEqualTo(2);
    }


    @Test
    @DisplayName("Shall delete a book by id")
    void deleteBookById() {
        assertThat(getBooksCount()).isEqualTo(2);
        bookDao.deleteBookById(2);
        assertThat(getBooksCount()).isEqualTo(1);
        int countBook = jdbcOperations.queryForObject(
                "select count(*) from BOOKS where id=:id",
                Map.of("id", 2), Integer.class
        );
        assertThat(countBook).isEqualTo(0);
        int genreBooks = jdbcOperations.queryForObject(
                "select count(*) from BOOKS_GENRES where book_id=:id",
                Map.of("id", 2), Integer.class
        );
        assertThat(genreBooks).isEqualTo(0);
        int authorsBooks = jdbcOperations.queryForObject(
                "select count(*) from BOOKS_AUTHORS where book_id=:id",
                Map.of("id", 2), Integer.class
        );
        assertThat(authorsBooks).isEqualTo(0);
    }

    @Test
    @DisplayName("Should find books by title")
    void findBooksByTitle() {
        List<Book> books = bookDao.findBooksByTitle("EUGENE");
        assertThat(books.size()).isEqualTo(1);
        assertThat(books.get(0)).isEqualTo(new Book(2, "EUGENE ONEGIN"));
    }

    @Test
    @DisplayName("Should throw exception if no book by such id")
    void findBookByIdThrowsException() {
        assertThrows(BookNotFoundDaoException.class, () -> bookDao.findBookById(100));
    }

    @Test
    @DisplayName("Should return book with authors and genres")
    void findBookById() {
        Book book = bookDao.findBookById(2);
        assertThat(book).isEqualTo(new Book(2, "EUGENE ONEGIN"));
    }

    @Test
    @DisplayName("should update book by id with new title")
    void updateBookById() {
        bookDao.updateBookById(1, "new title");
        Book book = jdbcOperations.queryForObject(
                "select id, title from books where id=:id",
                Map.of("id", 1),
                new BookDaoImpl.BookMapper()
        );
        assertThat(book).isEqualTo(new Book(1, "new title"));
    }

    private int getAuthorsCount() {
        return jdbcOperations.getJdbcOperations().queryForObject("select count(*) from authors", Integer.class);
    }

    private int getBooksCount() {
        return jdbcOperations.getJdbcOperations().queryForObject("select count(*) from books", Integer.class);
    }

    private int getGenresCount() {
        return jdbcOperations.getJdbcOperations().queryForObject("select count(*) from genres", Integer.class);
    }

    private int searchBooksGenresByBookId(int bookId) {
        return jdbcOperations.queryForObject(
                "select genre_id from BOOKS_GENRES where book_id=:book_id",
                Map.of("book_id", bookId),
                Integer.class
        );
    }

    private int searchBooksAuthorsByBookId(int bookId) {
        return jdbcOperations.queryForObject(
                "select author_id from BOOKS_AUTHORS where book_id=:book_id",
                Map.of("book_id", bookId),
                Integer.class
        );
    }
}