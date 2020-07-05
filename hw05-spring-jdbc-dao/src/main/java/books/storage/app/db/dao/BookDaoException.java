package books.storage.app.db.dao;

public class BookDaoException extends RuntimeException {

    public BookDaoException(Exception ex) {
        super(ex);
    }

    public BookDaoException() {
    }
}
