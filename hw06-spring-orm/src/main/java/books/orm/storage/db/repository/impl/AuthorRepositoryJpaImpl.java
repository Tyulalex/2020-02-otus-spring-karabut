package books.orm.storage.db.repository.impl;

import books.orm.storage.db.model.Author;
import books.orm.storage.db.repository.AuthorRepositoryJpa;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class AuthorRepositoryJpaImpl implements AuthorRepositoryJpa {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Author> findAll() {
        TypedQuery<Author> authorTypedQuery = em.createQuery("select a from Author a", Author.class);
        return authorTypedQuery.getResultList();
    }

    @Override
    public List<Author> findByName(String name) {
        TypedQuery<Author> authorTypedQuery = em.createQuery(
                "select a from Author a where a.fullName =: name", Author.class);
        authorTypedQuery.setParameter("name", name);
        return authorTypedQuery.getResultList();
    }

    @Override
    public Author save(Author author) {
        if (author.getId() == 0) {
            em.persist(author);
            return author;
        } else {
            return em.merge(author);
        }
    }
}
