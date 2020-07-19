package books.orm.storage.service.impl;

import books.orm.storage.db.model.Author;
import books.orm.storage.db.repository.AuthorRepositoryJpa;
import books.orm.storage.service.AuthorService;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@Import(AuthorServiceImpl.class)
class AuthorServiceImplTest {

    @MockBean
    private AuthorRepositoryJpa authorRepositoryJpa;

    @Autowired
    private AuthorService authorService;

    @Test
    void getAuthors() {
        val authors = List.of(new Author(1, "author"));
        Mockito.when(authorRepositoryJpa.findAll()).thenReturn(authors);
        val actualAuthors = authorService.getAuthors();
        assertThat(actualAuthors).isEqualTo(authors);
    }

    @Test
    void saveOrRefreshWhenAuthorFound() {
        val author = new Author("author");
        val authors = List.of(new Author(1, "author"));
        Mockito.when(authorRepositoryJpa.findByName("author")).thenReturn(authors);
        authorService.saveOrRefresh(author);
        assertThat(author.getId()).isEqualTo(1);
        Mockito.verify(authorRepositoryJpa, times(1)).findByName("author");
        Mockito.verify(authorRepositoryJpa, never()).save(author);
    }

    @Test
    void saveOrRefreshWhenAuthorNotFound() {
        val author = new Author("author");
        Mockito.when(authorRepositoryJpa.findByName("author")).thenReturn(Collections.emptyList());
        Mockito.when(authorRepositoryJpa.save(author)).thenReturn(author);
        authorService.saveOrRefresh(author);
        Mockito.verify(authorRepositoryJpa, times(1)).findByName("author");
        Mockito.verify(authorRepositoryJpa, times(1)).save(author);
    }
}