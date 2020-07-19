package books.orm.storage.db.repository.impl;

import books.orm.storage.db.model.Genre;
import books.orm.storage.db.repository.GenreRepositoryJpa;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class GenreRepositoryJpaImpl implements GenreRepositoryJpa {

    @PersistenceContext
    private EntityManager em;


    @Override
    public List<Genre> findAll() {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g", Genre.class);
        return query.getResultList();
    }

    @Override
    public List<Genre> findByName(String name) {
        TypedQuery<Genre> authorTypedQuery = em.createQuery(
                "select a from Genre a where a.name =: name", Genre.class);
        authorTypedQuery.setParameter("name", name);
        return authorTypedQuery.getResultList();
    }

    @Override
    public Genre save(Genre genre) {
        if (genre.getId() == 0) {
            em.persist(genre);
            return genre;
        } else {
            return em.merge(genre);
        }
    }
}
