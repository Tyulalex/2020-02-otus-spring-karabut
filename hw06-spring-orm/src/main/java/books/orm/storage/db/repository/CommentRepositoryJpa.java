package books.orm.storage.db.repository;

import books.orm.storage.db.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepositoryJpa {

    List<Comment> findAllByBookId(long bookId);

    Comment save(Comment comment);

    void deleteById(long id);

    Optional<Comment> findById(long id);

    void updateById(long id, String comment);

    void deleteAllByBookId(long bookId);
}
