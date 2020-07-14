package books.orm.storage.service.impl;

import books.orm.storage.db.model.Comment;
import books.orm.storage.db.repository.CommentRepositoryJpa;
import books.orm.storage.service.BookFormatterService;
import books.orm.storage.service.CommentService;
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
    private CommentRepositoryJpa commentRepositoryJpa;

    @MockBean
    private BookFormatterService bookFormatterService;

    @Test
    void findCommentById() {
        val comment = new Comment(1, "comment", "author", 1);
        Mockito.when(commentRepositoryJpa.findById(1)).thenReturn(Optional.of(comment));
        val commentString = commentService.findCommentById(1);
        assertThat(commentString).isEqualTo(comment.toString());
    }

    @Test
    void findCommentsByBookId() {
        val comment = new Comment(1, "comment", "author", 1);
        Mockito.when(commentRepositoryJpa.findAllByBookId(1)).thenReturn(List.of(comment));
        Mockito.when(bookFormatterService.commentsToString(List.of(comment))).thenReturn("books comments");
        val comments = commentService.findCommentsByBookId(1);
        assertThat(comments).isEqualTo("books comments");
    }

    @Test
    void deleteComment() {
        Mockito.doNothing().when(commentRepositoryJpa).deleteById(1);
        commentService.deleteComment(1);
        Mockito.verify(commentRepositoryJpa, times(1)).deleteById(1);
    }

    @Test
    void updateComment() {
        Mockito.doNothing().when(commentRepositoryJpa).updateById(1, "comment");
        commentService.updateComment(1, "comment");
        Mockito.verify(commentRepositoryJpa, times(1)).updateById(1, "comment");
    }
}