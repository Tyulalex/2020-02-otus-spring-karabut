package spring.book.mvc.service.impl;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import spring.book.mvc.dto.BookDto;
import spring.book.mvc.model.Author;
import spring.book.mvc.model.Book;
import spring.book.mvc.model.Genre;
import spring.book.mvc.repository.BookRepository;
import spring.book.mvc.service.AuthorService;
import spring.book.mvc.service.BookService;
import spring.book.mvc.service.GenreService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@Import(BookServiceImpl.class)
class BookServiceImplTest {

    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @Test
    void getBooks() {
        val books = getDefaultListOfBooks();
        Mockito.when(bookRepository.findAll()).thenReturn(books);

        assertThat(bookService.getBooks()).isEqualTo(getBooksDtos());
    }

    @Test
    void findBookById() {
        val book = Optional.of(Book.builder().id(1).title("new title").build());
        Mockito.when(bookRepository.findById(1L)).thenReturn(book);
        assertThat(bookService.findBookById(1)).isEqualTo(new BookDto(1, "new title", "", ""));
        Mockito.verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void deleteBookById() {
        val book = Book.builder().id(1).title("title").build();
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.ofNullable(book));
        Mockito.doNothing().when(bookRepository).delete(book);
        bookService.deleteBookById(1);
        Mockito.verify(bookRepository, times(1)).delete(book);
    }

    @Test
    void updateBookById() {
        val book = Book.builder().id(1).title("title").build();
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.ofNullable(book));
        bookService.updateBookById(new BookDto(1, "new title", "", ""));
        Mockito.verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void saveBook() {
        Book book = new Book("new title");
        book.setAuthors(
                List.of(new Author(1L, "new author"),
                        new Author(2L, "another author"))
        );
        book.setGenres(
                List.of(new Genre(1L, "new genre"), new Genre(2L, "another genre"))
        );

        val bookDto = new BookDto(1, "test1", "qwerty", "asdfg");
        Mockito.when(bookRepository.save(any(Book.class))).thenReturn(book);
        bookService.saveBook(bookDto);
        Mockito.verify(bookRepository, times(1)).save(any(Book.class));
    }

    private List<Book> getDefaultListOfBooks() {
        val book1 = Book.builder().id(1).title("test1")
                .authors(List.of(new Author(1, "qwerty")))
                .genres(List.of(new Genre(1, "asdfg")))
                .build();
        val book2 = Book.builder().id(2).title("test2")
                .authors(List.of(new Author(1, "qwerty")))
                .genres(List.of(new Genre(1, "asdfg")))
                .build();

        return List.of(book1, book2);
    }

    private List<BookDto> getBooksDtos() {
        val bookDto1 = new BookDto(1, "test1", "qwerty", "asdfg");
        val bookDto2 = new BookDto(2, "test2", "qwerty", "asdfg");
        return List.of(bookDto1, bookDto2);
    }
}