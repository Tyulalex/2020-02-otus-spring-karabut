package books.storage.app.services.impl;

import books.storage.app.db.dao.AuthorDao;
import books.storage.app.db.dao.BookDao;
import books.storage.app.db.dao.BookNotFoundDaoException;
import books.storage.app.db.dao.GenreDao;
import books.storage.app.db.model.Author;
import books.storage.app.db.model.Book;
import books.storage.app.db.model.Genre;
import books.storage.app.services.BookNotFoundServiceException;
import books.storage.app.services.BookService;
import books.storage.app.services.BookServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    @Override
    @Transactional(readOnly = true)
    public List<Book> getBooks() {
        try {
            return bookDao.getBooks();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new BookServiceException(ex);
        }
    }

    @Override
    @Transactional
    public void saveBook(Book book) {
        try {
            book.getAuthors().forEach(authorDao::findAuthorOrSave);
            book.getGenres().forEach(genreDao::findGenreOrSave);
            bookDao.saveBook(book);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new BookServiceException(ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findBooksByTitle(String title) {
        try {
            return bookDao.findBooksByTitle(title);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new BookServiceException(ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findBookById(long id) {
        try {
            Book book = bookDao.findBookById(id);
            List<Author> authors = authorDao.findAuthorsByBookId(id);
            book.setAuthors(authors);
            List<Genre> genres = genreDao.findGenresByBookId(id);
            book.setGenres(genres);
            return Optional.of(book);
        } catch (BookNotFoundDaoException ex) {
            return Optional.empty();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new BookServiceException(ex);
        }
    }

    @Override
    @Transactional
    public void deleteBookById(long id) {
        try {
            bookDao.deleteBookById(id);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new BookServiceException(ex);
        }
    }

    @Override
    @Transactional
    public void updateBookById(long id, String title) {
        try {
            bookDao.updateBookById(id, title);
        } catch (BookNotFoundDaoException ex) {
            throw new BookNotFoundServiceException(ex);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new BookServiceException(ex);
        }
    }
}
