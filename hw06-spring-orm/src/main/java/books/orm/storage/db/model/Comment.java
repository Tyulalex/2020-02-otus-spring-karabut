package books.orm.storage.db.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "COMMENTS")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMENT_SEQ_ID")
    @SequenceGenerator(name = "COMMENT_SEQ_ID", allocationSize = 1)
    private long id;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "book_id", nullable = false)
    private long bookId;

    @Override
    public String toString() {
        return String.format("Author: %s wrote a comment: %d. %s", author, id, comment);
    }
}
