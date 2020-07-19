package book.springdata.sql.storage.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMENT_SEQ_ID")
    @SequenceGenerator(name = "COMMENT_SEQ_ID", allocationSize = 1)
    private long id;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "book_id", nullable = false)
    private long bookId;

    @Override
    public String toString() {
        return String.format("comment: %d. %s", id, comment);
    }
}
