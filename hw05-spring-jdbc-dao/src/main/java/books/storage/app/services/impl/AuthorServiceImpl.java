package books.storage.app.services.impl;

import books.storage.app.db.dao.AuthorDao;
import books.storage.app.db.model.Author;
import books.storage.app.services.AuthorService;
import books.storage.app.services.AuthorServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao authorDao;

    @Override
    @Transactional(readOnly = true)
    public List<Author> getAuthors() {
        try {
            return authorDao.getAuthors();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new AuthorServiceException(ex);
        }
    }
}
