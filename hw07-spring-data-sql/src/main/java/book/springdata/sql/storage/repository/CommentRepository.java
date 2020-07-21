package book.springdata.sql.storage.repository;

import book.springdata.sql.storage.model.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    void deleteAllByBookId(long bookId);
}
