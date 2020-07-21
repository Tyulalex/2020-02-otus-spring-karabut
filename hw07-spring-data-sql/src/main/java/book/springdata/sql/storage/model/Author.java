package book.springdata.sql.storage.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTHOR_SEQ_ID")
    @SequenceGenerator(name = "AUTHOR_SEQ_ID", allocationSize = 1)
    private long id;

    @Column(name = "author_name", nullable = false, unique = true)
    private String fullName;

    public Author(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return String.format("%d. %s", id, fullName);
    }
}
