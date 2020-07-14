package books.storage.app.services.impl;

import books.storage.app.db.dao.AuthorDao;
import books.storage.app.db.dao.AuthorDaoException;
import books.storage.app.db.model.Author;
import books.storage.app.services.AuthorService;
import books.storage.app.services.AuthorServiceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Import(AuthorServiceImpl.class)
@DisplayName("Shall operate with author dao and provide author service")
class AuthorServiceImplTest {

    @Autowired
    private AuthorService authorService;

    @MockBean
    private AuthorDao authorDao;

    @Test
    @DisplayName("Shall return same result as authorDaoService")
    void getAuthors() {
        List<Author> authorList = List.of(
                new Author(1, "test1"), new Author(2, "test2"));

        when(authorDao.getAuthors()).thenReturn(authorList);
        assertThat(authorService.getAuthors()).isEqualTo(authorList);
    }

    @Test
    @DisplayName("Shall throw an exception when dao throws exception")
    void getAuthorsException() {
        when(authorDao.getAuthors()).thenThrow(AuthorDaoException.class);
        assertThrows(AuthorServiceException.class, () -> authorService.getAuthors());
    }
}