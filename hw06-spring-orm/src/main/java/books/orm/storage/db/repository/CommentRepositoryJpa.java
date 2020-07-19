package books.orm.storage.db.repository;

import books.orm.storage.db.model.Comment;

import java.util.Optional;

public interface CommentRepositoryJpa {

    Comment save(Comment comment);

    void delete(Comment comment);

    Optional<Comment> findById(long id);

    void deleteAllByBookId(long bookId);
}
