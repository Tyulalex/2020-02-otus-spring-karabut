package books.orm.storage.service.impl;

import books.orm.storage.db.model.Genre;
import books.orm.storage.db.repository.GenreRepositoryJpa;
import books.orm.storage.service.GenreService;
import books.orm.storage.service.exception.AuthorServiceException;
import books.orm.storage.service.exception.GenreServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreServiceImpl implements GenreService {

    private final GenreRepositoryJpa genreRepositoryJpa;

    @Override
    @Transactional(readOnly = true)
    public List<Genre> getGenres() {
        try {
            return genreRepositoryJpa.findAll();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new GenreServiceException(ex);
        }
    }

    @Override
    @Transactional
    public void saveOrRefresh(Genre genre) {
        try {
            val genres = genreRepositoryJpa.findByName(genre.getName());
            if (!genres.isEmpty()) {
                genre.setId(genres.get(0).getId());
            } else {
                genreRepositoryJpa.save(genre);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new AuthorServiceException(ex);
        }
    }
}
