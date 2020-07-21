package book.springdata.sql.storage.service.impl;

import book.springdata.sql.storage.model.Author;
import book.springdata.sql.storage.model.Book;
import book.springdata.sql.storage.model.Comment;
import book.springdata.sql.storage.model.Genre;
import book.springdata.sql.storage.repository.BookRepository;
import book.springdata.sql.storage.repository.CommentRepository;
import book.springdata.sql.storage.service.BookFormatterService;
import book.springdata.sql.storage.service.BookService;
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
    private BookRepository bookRepository;

    @MockBean
    private BookFormatterService bookFormatterService;

    @MockBean
    private CommentRepository commentRepositoryJpa;

    @Test
    @DisplayName("Get Books shall return books with authors and genres")
    void getBooks() {
        val books = getDefaultListOfBooks();
        Mockito.when(bookRepository.findAll()).thenReturn(books);
        Mockito.when(bookFormatterService.allBooksString(books)).thenReturn(LIST_BOOKS_STRING);
        assertThat(bookService.getBooks()).isEqualTo(LIST_BOOKS_STRING);
    }

    @Test
    @DisplayName("Shall returns books by title")
    void findBooksByTitle() {
        List<Book> books = List.of(Book.builder().id(1).title("title1").build(),
                Book.builder().id(2).title("title3").build());
        Mockito.when(bookRepository.findAllByTitleContaining("qwerty")).thenReturn(books);
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
        Mockito.when(bookRepository.save(any(Book.class))).thenReturn(book);
        bookService.saveBook(book);
        Mockito.verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    @DisplayName("Shall returns books by id")
    void findBookById() {
        val book = Optional.of(Book.builder().id(1).title("new title").build());
        Mockito.when(bookRepository.findById(1L)).thenReturn(book);
        assertThat(bookService.findBookById(1)).isEqualTo("1. \"new title\"");
        Mockito.verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Shall delete book")
    void deleteBookById() {
        val book = Book.builder().id(1).title("title").build();
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.ofNullable(book));
        Mockito.doNothing().when(bookRepository).delete(book);
        bookService.deleteBookById(1);
        Mockito.verify(bookRepository, times(1)).delete(book);
    }

    @Test
    @DisplayName("Shall update a book")
    void updateBookById() {
        val book = Book.builder().id(1).title("title").build();
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.ofNullable(book));
        bookService.updateBookById(1, "new title");
        Mockito.verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Shall add new comment")
    void addNewComment() {
        val comment = new Comment(0, "comment", 1);
        Mockito.when(bookRepository.findById(comment.getBookId())).thenReturn(Optional.of(new Book()));
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