package spring.book.mvc.repository;

import org.springframework.data.repository.CrudRepository;
import spring.book.mvc.model.Comment;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    void deleteAllByBookId(long bookId);
}
