package book.springdata.sql.storage.service.impl;

import book.springdata.sql.storage.model.Author;
import book.springdata.sql.storage.repository.AuthorRepository;
import book.springdata.sql.storage.service.AuthorService;
import book.springdata.sql.storage.service.exception.AuthorServiceException;
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

    private final AuthorRepository authorRepository;

    @Override
    public List<Author> getAuthors() {
        try {
            return authorRepository.findAll();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new AuthorServiceException(ex);
        }
    }

    @Override
    @Transactional
    public void saveOrRefresh(Author author) {
        try {
            val authors = authorRepository.findAllByFullName(author.getFullName());
            if (!authors.isEmpty()) {
                author.setId(authors.get(0).getId());
            } else {
                authorRepository.save(author);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new AuthorServiceException(ex);
        }
    }
}
