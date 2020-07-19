package books.orm.storage.service.impl;

import books.orm.storage.db.model.Author;
import books.orm.storage.db.repository.AuthorRepositoryJpa;
import books.orm.storage.service.AuthorService;
import books.orm.storage.service.exception.AuthorServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepositoryJpa authorRepositoryJpa;

    @Override
    public List<Author> getAuthors() {
        try {
            return authorRepositoryJpa.findAll();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new AuthorServiceException(ex);
        }
    }

    @Override
    @Transactional
    public void saveOrRefresh(Author author) {
        try {
            val authors = authorRepositoryJpa.findByName(author.getFullName());
            if (!authors.isEmpty()) {
                author.setId(authors.get(0).getId());
            } else {
                authorRepositoryJpa.save(author);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new AuthorServiceException(ex);
        }
    }
}
