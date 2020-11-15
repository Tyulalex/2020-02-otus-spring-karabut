package spring.book.mvc.service.impl;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import spring.book.mvc.model.Author;
import spring.book.mvc.repository.AuthorRepository;
import spring.book.mvc.service.AuthorService;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@Import(AuthorServiceImpl.class)
class AuthorServiceImplTest {

    @MockBean
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorService authorService;

    @Test
    void saveOrRefreshWhenAuthorFound() {
        val author = new Author("author");
        val authors = List.of(new Author(1, "author"));
        Mockito.when(authorRepository.findAllByFullName("author")).thenReturn(authors);
        authorService.saveOrRefresh(author);
        assertThat(author.getId()).isEqualTo(1);
        Mockito.verify(authorRepository, times(1)).findAllByFullName("author");
        Mockito.verify(authorRepository, never()).save(author);
    }

    @Test
    void saveOrRefreshWhenAuthorNotFound() {
        val author = new Author("author");
        Mockito.when(authorRepository.findAllByFullName("author")).thenReturn(Collections.emptyList());
        Mockito.when(authorRepository.save(author)).thenReturn(author);
        authorService.saveOrRefresh(author);
        Mockito.verify(authorRepository, times(1)).findAllByFullName("author");
        Mockito.verify(authorRepository, times(1)).save(author);
    }

}