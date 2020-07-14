package books.orm.storage.db.repository.impl;

import books.orm.storage.db.model.Comment;
import books.orm.storage.db.repository.CommentRepositoryJpa;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Shall work with comment repository")
@Import(CommentRepositoryJpaImpl.class)
class CommentRepositoryJpaImplTest {

    @Autowired
    private CommentRepositoryJpa commentRepositoryJpa;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void findAllByBookId() {
        val comments = commentRepositoryJpa.findAllByBookId(1);
        assertThat(comments.size()).isEqualTo(2);
        assertThat(comments.get(0)).isEqualTo(new Comment(1, "good book", "Jack Black", 1));
    }

    @Test
    void save() {
        val comment = new Comment(0, "new comment", "new author", 1);
        commentRepositoryJpa.save(comment);
        assertThat(comment.getId()).isEqualTo(3);
        val savedComment = testEntityManager.find(Comment.class, 3L);
        assertThat(savedComment).isNotNull().isEqualTo(comment);
    }

    @Test
    void deleteById() {
        commentRepositoryJpa.deleteById(1);
        val deletedComment = testEntityManager.find(Comment.class, 1L);
        assertThat(deletedComment).isNull();
    }

    @Test
    void findById() {
        val comment = commentRepositoryJpa.findById(1L);
        assertThat(comment).isPresent().isEqualTo(Optional.of(new Comment(1, "good book", "Jack Black", 1)));
    }

    @Test
    void updateById() {
        String updatedComment = "updated comment";
        commentRepositoryJpa.updateById(1L, updatedComment);
        val comment = testEntityManager.find(Comment.class, 1L);
        assertThat(comment).isEqualTo(new Comment(1, updatedComment, "Jack Black", 1));
    }
}