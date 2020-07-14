package books.storage.app.services.impl;

import books.storage.app.db.dao.GenreDao;
import books.storage.app.db.model.Genre;
import books.storage.app.services.GenreService;
import books.storage.app.services.GenreServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreServiceImpl implements GenreService {

    private final GenreDao genreDao;

    @Override
    @Transactional(readOnly = true)
    public List<Genre> getGenres() {
        try {
            return genreDao.getGenres();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new GenreServiceException(ex);
        }
    }
}
