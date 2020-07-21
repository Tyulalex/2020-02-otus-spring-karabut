package book.springdata.sql.storage.service.impl;

import book.springdata.sql.storage.model.Book;
import book.springdata.sql.storage.model.Comment;
import book.springdata.sql.storage.repository.BookRepository;
import book.springdata.sql.storage.repository.CommentRepository;
import book.springdata.sql.storage.service.BookFormatterService;
import book.springdata.sql.storage.service.BookService;
import book.springdata.sql.storage.service.exception.BookNotFoundServiceException;
import book.springdata.sql.storage.service.exception.BookServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookFormatterService bookFormatterService;
    private final CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    public String getBooks() {
        try {
            val books = bookRepository.findAll();

            return bookFormatterService.allBooksString(books);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new BookServiceException(ex);
        }
    }

    @Override
    @Transactional
    public void saveBook(Book book) {
        try {
            bookRepository.save(book);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new BookServiceException(ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String findBooksByTitle(String title) {
        try {
            val books = bookRepository.findAllByTitleContaining(title);

            return bookFormatterService.allBooksString(books);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new BookServiceException(ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String findBookById(long id) {
        try {
            val book = bookRepository.findById(id);
            return book.isPresent() ? book.get().toString() : bookFormatterService.noItemString("book", id);
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
                commentRepository.deleteAllByBookId(id);
                bookRepository.delete(book.get());
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new BookServiceException(ex);
        }
    }

    @Override
    @Transactional
    public void updateBookById(long id, String title) {
        try {
            val book = bookRepository.findById(id);
            book.ifPresent(b -> b.setTitle(title));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new BookServiceException(ex);
        }
    }

    @Override
    @Transactional
    public void addComment(Comment comment) {
        try {
            val optionalBook = bookRepository.findById(comment.getBookId());
            if (optionalBook.isEmpty()) {
                throw new BookNotFoundServiceException();
            } else {
                commentRepository.save(comment);
            }
        } catch (Exception ex) {
            if (ex instanceof BookNotFoundServiceException) {
                log.debug("no such book found with {}", comment.getBookId());
                throw ex;
            }
            log.error(ex.getMessage());
            throw new BookServiceException(ex);
        }
    }
}
