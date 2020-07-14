package books.orm.storage.service.impl;

import books.orm.storage.db.model.Author;
import books.orm.storage.db.model.Book;
import books.orm.storage.db.model.Comment;
import books.orm.storage.db.model.Genre;
import books.orm.storage.db.repository.BookRepositoryJpa;
import books.orm.storage.db.repository.CommentRepositoryJpa;
import books.orm.storage.service.BookFormatterService;
import books.orm.storage.service.BookService;
import lombok.val;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@DisplayName("Book Service shall operate with book repository and book string formatter")
@ExtendWith(SpringExtension.class)
@Import(BookServiceImpl.class)
class BookServiceImplTest {

    private static final String LIST_BOOKS_STRING = "1.books\n2.book";

    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepositoryJpa bookRepositoryJpa;

    @MockBean
    private BookFormatterService bookFormatterService;

    @MockBean
    private CommentRepositoryJpa commentRepositoryJpa;

    @Test
    @DisplayName("Get Books shall return books with authors and genres")
    void getBooks() {
        val books = getDefaultListOfBooks();
        Mockito.when(bookRepositoryJpa.findAll()).thenReturn(books);
        Mockito.when(bookFormatterService.allBooksString(books)).thenReturn(LIST_BOOKS_STRING);
        assertThat(bookService.getBooks()).isEqualTo(LIST_BOOKS_STRING);
    }

    @Test
    @DisplayName("Shall returns books by title")
    void findBooksByTitle() {
        List<Book> books = List.of(Book.builder().id(1).title("title1").build(),
                Book.builder().id(2).title("title3").build());
        Mockito.when(bookRepositoryJpa.findBooksByTitle("qwerty")).thenReturn(books);
        Mockito.when(bookFormatterService.allBooksString(books)).thenReturn(LIST_BOOKS_STRING);
        assertThat(bookService.findBooksByTitle("qwerty")).isEqualTo(LIST_BOOKS_STRING);
    }

    @Test
    @DisplayName("Shall save a book")
    void saveBook() {

        Book book = new Book("new title");
        book.setAuthors(
                List.of(new Author(1L, "new author"),
                        new Author(2L, "another author"))
        );
        book.setGenres(
                List.of(new Genre(1L, "new genre"), new Genre(2L, "another genre"))
        );
        Mockito.when(bookRepositoryJpa.save(any(Book.class))).thenReturn(book);
        bookService.saveBook(book);
        Mockito.verify(bookRepositoryJpa, times(1)).save(any(Book.class));
    }

    @Test
    @DisplayName("Shall returns books by id")
    void findBookById() {
        val book = Optional.of(Book.builder().id(1).title("new title").build());
        Mockito.when(bookRepositoryJpa.findById(1)).thenReturn(book);
        assertThat(bookService.findBookById(1)).isEqualTo("1. \"new title\"");
        Mockito.verify(bookRepositoryJpa, times(1)).findById(1);
    }

    @Test
    @DisplayName("Shall delete book")
    void deleteBookById() {
        Mockito.doNothing().when(bookRepositoryJpa).deleteById(1);
        bookService.deleteBookById(1);
        Mockito.verify(bookRepositoryJpa, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Shall update a book")
    void updateBookById() {
        Mockito.doNothing().when(bookRepositoryJpa).updateTitleById(1, "new title");
        bookService.updateBookById(1, "new title");
        Mockito.verify(bookRepositoryJpa, times(1)).updateTitleById(1, "new title");
    }

    @Test
    @DisplayName("Shall add new comment")
    void addNewComment() {
        val comment = new Comment(0, "comment", "author", 1);
        Mockito.when(bookRepositoryJpa.findById(comment.getBookId())).thenReturn(Optional.of(new Book()));
        Mockito.when(commentRepositoryJpa.save(comment)).thenReturn(comment);
        bookService.addComment(comment);
        Mockito.verify(commentRepositoryJpa, times(1)).save(comment);
    }


    private List<Book> getDefaultListOfBooks() {
        val book1 = Book.builder().id(1).title("test1")
                .authors(List.of(new Author(1, "qwerty")))
                .genres(List.of(new Genre(1, "asdfg")))
                .build();
        val book2 = Book.builder().id(2).title("test1").build();

        return List.of(book1, book2);
    }
}