package books.orm.storage.service.impl;

import books.orm.storage.db.model.Book;
import books.orm.storage.db.model.Comment;
import books.orm.storage.db.repository.BookRepositoryJpa;
import books.orm.storage.db.repository.CommentRepositoryJpa;
import books.orm.storage.service.BookFormatterService;
import books.orm.storage.service.BookService;
import books.orm.storage.service.exception.BookNotFoundServiceException;
import books.orm.storage.service.exception.BookServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepositoryJpa bookRepositoryJpa;
    private final BookFormatterService bookFormatterService;
    private final CommentRepositoryJpa commentRepositoryJpa;

    @Override
    @Transactional(readOnly = true)
    public String getBooks() {
        try {
            val books = bookRepositoryJpa.findAll();

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
            bookRepositoryJpa.save(book);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new BookServiceException(ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String findBooksByTitle(String title) {
        try {
            val books = bookRepositoryJpa.findBooksByTitle(title);

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
            val book = bookRepositoryJpa.findById(id);
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
            val book = bookRepositoryJpa.findById(id);
            if (book.isPresent()) {
                commentRepositoryJpa.deleteAllByBookId(id);
                bookRepositoryJpa.delete(book.get());
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
            val book = bookRepositoryJpa.findById(id);
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
            val optionalBook = bookRepositoryJpa.findById(comment.getBookId());
            if (optionalBook.isEmpty()) {
                throw new BookNotFoundServiceException();
            } else {
                commentRepositoryJpa.save(comment);
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
