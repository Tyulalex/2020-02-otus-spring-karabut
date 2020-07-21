package book.springdata.sql.storage.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "genres")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENRE_SEQ_ID")
    @SequenceGenerator(name = "GENRE_SEQ_ID", allocationSize = 1)
    private long id;
    @Column(name = "genre_name", nullable = false, unique = true)
    private String name;

    public Genre(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("%d. %s", id, name);
    }
}
