package books.storage.app.db.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Genre {

    private long id;
    private final String name;

    public Genre(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("%d. %s", id, name);
    }
}
