package spring.book.mvc.service;

import spring.book.mvc.dto.BookDto;

import java.util.List;

public interface BookService {

    List<BookDto> getBooks();

    BookDto findBookById(long id);

    void deleteBookById(long id);

    void updateBookById(BookDto bookDto);

    void saveBook(BookDto bookDto);
}
