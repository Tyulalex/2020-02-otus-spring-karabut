package books.orm.storage.gui;

import books.orm.storage.db.model.Author;
import books.orm.storage.db.model.Book;
import books.orm.storage.db.model.Comment;
import books.orm.storage.db.model.Genre;
import books.orm.storage.service.*;
import books.orm.storage.service.exception.BookNotFoundServiceException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class ShellBooksCommands {

    private final BookService bookService;
    private final BookFormatterService bookToStringService;
    private final IOService ioService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final CommentService commentService;

    @ShellMethod(key = {"get-books"}, value = "Get all books")
    public String getBooks() {
        return bookService.getBooks();
    }

    @ShellMethod(key = {"add-book"}, value = "Add new book; params --title (mandatory); --authors, e.g. Mr.Bean, Mr.Black; --genres, e.g. Drama, Fantasy")
    public void addBook(@ShellOption("--title") String title, @ShellOption("--authors") String authorsName, @ShellOption("--genres") String genresName) {
        List<Author> authors = this.fromInputStringToAuthors(authorsName);
        Book book = new Book(title);
        book.setAuthors(authors);
        List<Genre> genres = this.fromInputStringToGenres(genresName);
        book.setGenres(genres);
        book.getAuthors().forEach(authorService::saveOrRefresh);
        book.getGenres().forEach(genreService::saveOrRefresh);
        bookService.saveBook(book);
    }

    @ShellMethod(key = {"find-books-by-title"}, value = "Find books by title, params --title ")
    public String findBooksByTitle(@ShellOption("--title") String title) {
        return bookService.findBooksByTitle(title);
    }

    @ShellMethod(key = {"find-book-by-id"}, value = "Find book by id, params --id")
    public String findBookById(@ShellOption("--id") long id) {
        return bookService.findBookById(id);
    }

    @ShellMethod(key = {"delete-book-by-id"}, value = "Delete book by id, params --id")
    public void deleteBookById(@ShellOption("--id") long id) {
        try {
            bookService.deleteBookById(id);
        } catch (Exception ex) {
            ioService.out(String.format("Exception occurred while deleting book with id %d ", id));
        }
    }

    @ShellMethod(key = {"update-book-by-id"}, value = "Update book by id; params --id, --title")
    public void updateBookById(@ShellOption("--id") long id, @ShellOption("--title") String title) {
        try {
            bookService.updateBookById(id, title);
        } catch (BookNotFoundServiceException ex) {
            ioService.out(String.format("No such book with id=%d", id));
        } catch (Exception ex) {
            ioService.out(String.format("Exception occurred while updating book with id %d ", id));
        }
    }

    @ShellMethod(key = {"get-authors"}, value = "Get all authors")
    public String getAuthors() {
        List<Author> authors = authorService.getAuthors();
        return bookToStringService.authorsToString(authors);
    }

    @ShellMethod(key = {"get-genres"}, value = "Get all genres")
    public String getGenres() {
        List<Genre> genres = genreService.getGenres();
        return bookToStringService.genresToString(genres);
    }


    @ShellMethod(key = {"find-comments-for-book"}, value = "Find comments for book by book id")
    public String findBooksComments(@ShellOption("--bookId") long bookId) {
        return commentService.findCommentsByBookId(bookId);
    }


    @ShellMethod(key = {"find-comment-by-id"}, value = "Find comment by id")
    public String findCommentById(@ShellOption("--id") long commentId) {
        return commentService.findCommentById(commentId);
    }

    @ShellMethod(key = {"add-comment-to-book"}, value = "Add new comment for book by book id")
    public void addCommentToBook(@ShellOption("--bookId") long bookId, @ShellOption("--comment") String comment, @ShellOption("--author") String author) {

        val commentToAdd = new Comment(0, comment, author, bookId);
        try {
            bookService.addComment(commentToAdd);
        } catch (BookNotFoundServiceException ex) {
            ioService.out(String.format("No book with id=%d found", bookId));
        }

    }

    @ShellMethod(key = {"delete-comment"}, value = "Delete comment for book by book id")
    public void deleteCommentToBook(@ShellOption("--id") long commentId) {
        commentService.deleteComment(commentId);
    }

    @ShellMethod(key = {"update-comment-to-book"}, value = "Update comment by id")
    public void updateCommentToBook(@ShellOption("--id") long id, @ShellOption("--comment") String comment) {
        commentService.updateComment(id, comment);
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

    private List<Author> fromInputStringToAuthors(String authorsName) {
        List<String> listNames = parseStringsToList(authorsName);
        List<Author> authors = new ArrayList<>();
        listNames.forEach(name -> authors.add(new Author(name)));
        return authors;
    }
}
