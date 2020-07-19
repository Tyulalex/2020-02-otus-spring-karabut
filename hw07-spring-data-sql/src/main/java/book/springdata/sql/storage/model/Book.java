package book.springdata.sql.storage.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Data
@Entity
@Table(name = "books")
@Builder
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOOK_SEQ_ID")
    @SequenceGenerator(name = "BOOK_SEQ_ID", allocationSize = 1)
    private long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(targetEntity = Author.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "BOOKS_AUTHORS", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private List<Author> authors;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(targetEntity = Genre.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "BOOKS_GENRES", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres;

    @OneToMany(targetEntity = Comment.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private List<Comment> comments;

    public Book(String title) {
        this.title = title;
        this.authors = new ArrayList<>();
        this.genres = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public Book() {
    }

    @Override
    public String toString() {
        if (authors == null || authors.isEmpty() && (genres == null || genres.isEmpty())) {
            return String.format("%d. \"%s\"", id, title);
        }
        return String.format("%d. \"%s\". Authors: %s. Genre: %s", id, title, joiner(authors), joiner(genres));
    }

    private <T> StringJoiner joiner(List<T> objectsList) {
        StringJoiner joiner = new StringJoiner(", ");
        objectsList.forEach(s -> joiner.add(s.toString()));
        return joiner;
    }
}
