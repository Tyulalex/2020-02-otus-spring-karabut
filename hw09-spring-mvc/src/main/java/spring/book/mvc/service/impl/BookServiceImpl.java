package spring.book.mvc.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.book.mvc.dto.BookDto;
import spring.book.mvc.exception.BookNotFoundServiceException;
import spring.book.mvc.exception.BookServiceException;
import spring.book.mvc.model.Book;
import spring.book.mvc.repository.BookRepository;
import spring.book.mvc.service.AuthorService;
import spring.book.mvc.service.BookService;
import spring.book.mvc.service.GenreService;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final GenreService genreService;

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> getBooks() {
        try {
            val booksModels = bookRepository.findAll();

            List<BookDto> bookDtos = new ArrayList<>();
            for (Book book : booksModels) {
                bookDtos.add(BookDto.fromBook(book));
            }

            return bookDtos;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new BookServiceException(ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BookDto findBookById(long id) {
        try {
            val book = bookRepository.findById(id).orElseThrow(BookNotFoundServiceException::new);

            return BookDto.fromBook(book);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new BookServiceException(ex);
        }
    }

    @Override
    @Transactional
    public void deleteBookById(long id) {
        try {
            val book = bookRepository.findById(id);
            if (book.isPresent()) {
                //commentRepository.deleteAllByBookId(id);
                bookRepository.delete(book.get());
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new BookServiceException(ex);
        }
    }

    @Override
    @Transactional
    public void updateBookById(BookDto bookDto) {
        try {
            val book = bookRepository.findById(bookDto.getId());
            book.ifPresent(b -> b.setTitle(bookDto.getTitle()));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new BookServiceException(ex);
        }
    }

    @Override
    @Transactional
    public void saveBook(BookDto bookDto) {
        try {
            val book = bookDto.toBook();
            book.getAuthors().forEach(authorService::saveOrRefresh);
            book.getGenres().forEach(genreService::saveOrRefresh);
            bookRepository.save(book);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new BookServiceException(ex);
        }
    }
}
