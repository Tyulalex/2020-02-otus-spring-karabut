package book.springdata.sql.storage.service.impl;

import book.springdata.sql.storage.model.Genre;
import book.springdata.sql.storage.repository.GenreRepository;
import book.springdata.sql.storage.service.GenreService;
import book.springdata.sql.storage.service.exception.AuthorServiceException;
import book.springdata.sql.storage.service.exception.GenreServiceException;
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

    private final GenreRepository genreRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Genre> getGenres() {
        try {
            return genreRepository.findAll();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new GenreServiceException(ex);
        }
    }

    @Override
    @Transactional
    public void saveOrRefresh(Genre genre) {
        try {
            val genres = genreRepository.findAllByName(genre.getName());
            if (!genres.isEmpty()) {
                genre.setId(genres.get(0).getId());
            } else {
                genreRepository.save(genre);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new AuthorServiceException(ex);
        }
    }
}
