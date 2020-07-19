package books.orm.storage.db.repository.impl;

import books.orm.storage.db.model.Comment;
import books.orm.storage.db.repository.CommentRepositoryJpa;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Optional;

@Repository
public class CommentRepositoryJpaImpl implements CommentRepositoryJpa {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() <= 0) {
            em.persist(comment);
            return comment;
        } else {
            return em.merge(comment);
        }
    }

    @Override
    public void delete(Comment comment) {
        em.remove(comment);
    }

    @Override
    public Optional<Comment> findById(long id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }

    @Override
    public void deleteAllByBookId(long bookId) {
        Query query = em.createQuery("delete from Comment c where c.bookId=:id");
        query.setParameter("id", bookId);
        query.executeUpdate();
    }

}
