package book.springdata.sql.storage.repository;

import book.springdata.sql.storage.model.Comment;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.TypedQuery;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Shall work with comment repository")
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void deleteAllByBookId() {
        TypedQuery<Comment> query = testEntityManager.getEntityManager().createQuery("select c from Comment c where c.bookId=1L", Comment.class);
        List<Comment> comments = query.getResultList();
        commentRepository.deleteAllByBookId(1L);
        val deletedComment1 = testEntityManager.find(Comment.class, comments.get(0).getId());
        assertThat(deletedComment1).isNull();
        val deletedComment2 = testEntityManager.find(Comment.class, comments.get(1).getId());
        assertThat(deletedComment2).isNull();
    }
}