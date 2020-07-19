package books.orm.storage.db.repository.impl;

import books.orm.storage.db.model.Book;
import books.orm.storage.db.repository.BookRepositoryJpa;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class BookRepositoryJpaImpl implements BookRepositoryJpa {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Book> findAll() {
        TypedQuery<Book> query = em.createQuery("select b from Book b", Book.class);
        return query.getResultList();
    }

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Override
    public Book save(Book book) {
        if (book.getId() <= 0) {
            em.persist(book);
            return book;
        } else {
            return em.merge(book);
        }
    }

    @Override
    public void delete(Book book) {
        em.remove(book);
    }

    @Override
    public List<Book> findBooksByTitle(String title) {
        TypedQuery<Book> query = em.createQuery("select b from Book b where b.title like :title", Book.class);
        query.setParameter("title", "%" + title + "%");
        return query.getResultList();
    }
}
