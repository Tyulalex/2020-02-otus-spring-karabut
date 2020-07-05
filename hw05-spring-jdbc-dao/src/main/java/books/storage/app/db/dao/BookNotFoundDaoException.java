package books.storage.app.db.dao;

public class BookNotFoundDaoException extends BookDaoException {
    public BookNotFoundDaoException(Exception ex) {
        super(ex);
    }

    public BookNotFoundDaoException() {
    }
}
