package spring.book.mvc.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.book.mvc.exception.AuthorServiceException;
import spring.book.mvc.model.Author;
import spring.book.mvc.repository.AuthorRepository;
import spring.book.mvc.service.AuthorService;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

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
