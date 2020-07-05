package books.storage.app.services.impl;

import books.storage.app.db.dao.AuthorDao;
import books.storage.app.db.dao.BookDao;
import books.storage.app.db.dao.BookNotFoundDaoException;
import books.storage.app.db.dao.GenreDao;
import books.storage.app.db.model.Author;
import books.storage.app.db.model.Book;
import books.storage.app.db.model.Genre;
import books.storage.app.services.BookNotFoundServiceException;
import books.storage.app.services.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@DisplayName("Book Service shall operate with book dao")
@ExtendWith(SpringExtension.class)
@Import(BookServiceImpl.class)
class BookServiceImplTest {

    @Autowired
    private BookService bookService;

    @MockBean
    private AuthorDao authorDao;

    @MockBean
    private GenreDao genreDao;

    @MockBean
    private BookDao bookDao;

    @Test
    @DisplayName("Shall save a book")
    void saveBook() {

        Book book = new Book("new title");
        book.setAuthors(
                List.of(new Author("new author"),
                        new Author("another author"))
        );

        book.setGenres(
                List.of(new Genre("new genre"), new Genre("another genre"))
        );

        Mockito.doNothing().when(authorDao).findAuthorOrSave(any(Author.class));
        Mockito.doNothing().when(genreDao).findGenreOrSave(any(Genre.class));
        Mockito.doNothing().when(bookDao).saveBook(any(Book.class));


        bookService.saveBook(book);
        Mockito.verify(authorDao, times(2)).findAuthorOrSave(any(Author.class));
        Mockito.verify(genreDao, times(2)).findGenreOrSave(any(Genre.class));
        Mockito.verify(bookDao, times(1)).saveBook(any(Book.class));
    }


    @Test
    @DisplayName("Shall give all books")
    void getBooks() {
        List<Book> books = List.of(new Book(1, "ter"), new Book(2, "trtr"));
        Mockito.when(bookDao.getBooks()).thenReturn(books);
        assertThat(bookService.getBooks()).isEqualTo(books);
    }


    @Test
    @DisplayName("Shall returns books by title")
    void findBooksByTitle() {
        List<Book> books = List.of(new Book(1, "qwerty"), new Book(2, "dqwerty"));
        Mockito.when(bookDao.findBooksByTitle("qwerty")).thenReturn(books);
        assertThat(bookService.findBooksByTitle("qwerty")).isEqualTo(books);
    }

    @Test
    @DisplayName("Shall returns books by id")
    void findBookById() {
        Book book = new Book(1, "qwerty");
        Mockito.when(bookDao.findBookById(1)).thenReturn(book);
        assertThat(bookService.findBookById(1)).isEqualTo(Optional.of(book));
    }

    @Test
    @DisplayName("Shall return optional empty if no book by id")
    void findBookByIdThrowsNotFoundException() {
        Mockito.doThrow(BookNotFoundDaoException.class).when(bookDao).findBookById(1);
        assertThat(bookService.findBookById(1)).isEmpty();
    }

    @Test
    @DisplayName("Shall delete book")
    void deleteBookById() {
        Mockito.doNothing().when(bookDao).deleteBookById(1);
        bookService.deleteBookById(1);
        Mockito.verify(bookDao, times(1)).deleteBookById(1);
    }

    @Test
    @DisplayName("Shall update a book")
    void updateBookById() {
        Mockito.doNothing().when(bookDao).updateBookById(1, "new title");
        bookService.updateBookById(1, "new title");
        Mockito.verify(bookDao, times(1)).updateBookById(1, "new title");
    }

    @Test
    @DisplayName("Shall throws an exception book")
    void updateBookByIdthrowsException() {
        Mockito.doThrow(BookNotFoundDaoException.class).when(bookDao).updateBookById(1, "new title");
        assertThrows(BookNotFoundServiceException.class, () -> bookService.updateBookById(1, "new title"));
    }
}