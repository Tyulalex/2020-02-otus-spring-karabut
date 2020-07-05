package books.storage.app.commands;

import books.storage.app.db.model.Author;
import books.storage.app.db.model.Book;
import books.storage.app.db.model.Genre;
import books.storage.app.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ShellComponent
@RequiredArgsConstructor
public class ShellBooksCommands {

    private final BookService bookService;
    private final BookToStringService bookToStringService;
    private final IOService ioService;
    private final AuthorService authorService;
    private final GenreService genreService;

    @ShellMethod(key = {"get-books"}, value = "Get all books")
    public String getBooks() {
        List<Book> books = this.bookService.getBooks();
        return bookToStringService.allBooksString(books);
    }

    @ShellMethod(key = {"add-book"}, value = "Add new book; params --title (mandatory); --authors, e.g. Mr.Bean, Mr.Black; --genres, e.g. Drama, Fantasy")
    public void addBook(@ShellOption("--title") String title, @ShellOption("--authors") String authorsName, @ShellOption("--genres") String genresName) {
        List<Author> authors = this.fromInputStringToAuthors(authorsName);
        Book book = new Book(title);
        book.setAuthors(authors);
        List<Genre> genres = this.fromInputStringToGenres(genresName);
        book.setGenres(genres);
        this.bookService.saveBook(book);
    }

    private List<Author> fromInputStringToAuthors(String authorsName) {
        List<String> listNames = parseStringsToList(authorsName);
        List<Author> authors = new ArrayList<>();
        listNames.forEach(name -> authors.add(new Author(name)));
        return authors;
    }

    private List<String> parseStringsToList(String strings) {
        return Arrays.asList(strings.split("\\s*,\\s*"));
    }

    private List<Genre> fromInputStringToGenres(String genresName) {
        List<String> listNames = Arrays.asList(genresName.split("\\s*,\\s*"));
        List<Genre> genres = new ArrayList<>();
        listNames.forEach(name -> genres.add(new Genre(name)));
        return genres;
    }

    @ShellMethod(key = {"find-books-by-title"}, value = "Find books by title, params --title ")
    public String findBooksByTitle(@ShellOption("--title") String title) {
        List<Book> books = this.bookService.findBooksByTitle(title);
        return bookToStringService.allBooksString(books);
    }

    @ShellMethod(key = {"find-book-by-id"}, value = "Find book by id, params --id")
    public String findBookById(@ShellOption("--id") long id) {
        Optional<Book> book = this.bookService.findBookById(id);
        return bookToStringService.bookAsString(book, id);
    }

    @ShellMethod(key = {"delete-book-by-id"}, value = "Delete book by id, params --id")
    public void deleteBookById(@ShellOption("--id") long id) {
        try {
            this.bookService.deleteBookById(id);
        } catch (Exception ex) {
            ioService.out(String.format("Exception occurred while deleting book with id %d ", id));
        }
    }

    @ShellMethod(key = {"update-book-by-id"}, value = "Update book by id; params --id, --title")
    public void updateBookById(@ShellOption("--id") long id, @ShellOption("--title") String title) {
        try {
            this.bookService.updateBookById(id, title);
        } catch (BookNotFoundServiceException ex) {
            ioService.out(String.format("No such book with id=%d", id));
        } catch (Exception ex) {
            ioService.out(String.format("Exception occurred while updating book with id %d ", id));
        }
    }

    @ShellMethod(key = {"get-authors"}, value = "Get all authors")
    public String getAuthors() {
        List<Author> authors = authorService.getAuthors();
        return this.bookToStringService.authorsToString(authors);
    }

    @ShellMethod(key = {"get-genres"}, value = "Get all genres")
    public String getGenres() {
        List<Genre> genres = genreService.getGenres();
        return this.bookToStringService.genresToString(genres);
    }
}
