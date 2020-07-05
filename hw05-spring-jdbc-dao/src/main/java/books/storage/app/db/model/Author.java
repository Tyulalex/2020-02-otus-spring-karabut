package books.storage.app.db.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Author {

    private long id;
    private String fullName;

    public Author(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return String.format("%d. %s", id, fullName);
    }
}
