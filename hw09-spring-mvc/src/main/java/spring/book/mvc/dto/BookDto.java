package spring.book.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;
import spring.book.mvc.model.Author;
import spring.book.mvc.model.Book;
import spring.book.mvc.model.Genre;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    private long id;
    private String title;
    private String authors;
    private String genres;


    public Book toBook() {
        val book = new Book();
        book.setTitle(title);
        book.setAuthors(toAuthors(authors));
        book.setGenres(toGenres(genres));
        return book;
    }

    public static BookDto fromBook(Book book) {
        val bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthors(listToString(book.getAuthors()));
        bookDto.setGenres(listToString(book.getGenres()));
        return bookDto;
    }

    private List<Genre> toGenres(String genresName) {
        List<String> listNames = parseStringsToList(genresName);
        List<Genre> genres = new ArrayList<>();
        listNames.forEach(name -> genres.add(new Genre(name)));
        return genres;
    }

    private List<Author> toAuthors(String authorsName) {
        List<String> listNames = parseStringsToList(authorsName);
        List<Author> authors = new ArrayList<>();
        listNames.forEach(name -> authors.add(new Author(name)));
        return authors;
    }

    private List<String> parseStringsToList(String strings) {
        return Arrays.asList(strings.split("\\s*,\\s*"));
    }

    private static <T> String listToString(List<T> objects) {
        if (objects == null || objects.isEmpty()) {
            return "";
        }
        StringJoiner joiner = new StringJoiner(", ");
        objects.forEach(o -> joiner.add(o.toString()));
        return joiner.toString();
    }
}
