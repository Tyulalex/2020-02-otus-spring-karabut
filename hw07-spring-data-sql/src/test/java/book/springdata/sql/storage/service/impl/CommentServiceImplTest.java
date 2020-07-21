package book.springdata.sql.storage.service.impl;

import book.springdata.sql.storage.model.Book;
import book.springdata.sql.storage.model.Comment;
import book.springdata.sql.storage.repository.BookRepository;
import book.springdata.sql.storage.repository.CommentRepository;
import book.springdata.sql.storage.service.BookFormatterService;
import book.springdata.sql.storage.service.CommentService;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@Import(CommentServiceImpl.class)
class CommentServiceImplTest {

    @Autowired
    private CommentService commentService;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private BookFormatterService bookFormatterService;

    @MockBean
    private BookRepository bookRepository;

    @Test
    void findCommentById() {
        val comment = new Comment(1, "comment", 1);
        Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        val commentString = commentService.findCommentById(1);
        assertThat(commentString).isEqualTo(comment.toString());
    }

    @Test
    void findCommentsByBookId() {
        val comment = new Comment(1, "comment", 1);
        val book = Optional.of(Book.builder().id(1).title("t").comments(List.of(comment)).build());
        Mockito.when(bookRepository.findById(1L)).thenReturn(book);
        Mockito.when(bookFormatterService.commentsToString(List.of(comment))).thenReturn("books comments");
        val comments = commentService.findCommentsByBookId(1);
        assertThat(comments).isEqualTo("books comments");
    }

    @Test
    void deleteComment() {
        val comment = new Comment(1, "comment", 1);
        Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        Mockito.doNothing().when(commentRepository).delete(comment);
        commentService.deleteComment(1);
        Mockito.verify(commentRepository, times(1)).delete(comment);
    }

    @Test
    void updateComment() {
        val comment = new Comment(1, "comment", 1);
        Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        commentService.updateComment(1, "comment");
        Mockito.verify(commentRepository, times(1)).findById(1L);
    }
}