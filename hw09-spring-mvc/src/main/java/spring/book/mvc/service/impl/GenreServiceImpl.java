package spring.book.mvc.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.book.mvc.exception.GenreServiceException;
import spring.book.mvc.model.Genre;
import spring.book.mvc.repository.GenreRepository;
import spring.book.mvc.service.GenreService;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;


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
            throw new GenreServiceException(ex);
        }
    }
}
