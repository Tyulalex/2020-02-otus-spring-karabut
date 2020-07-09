package books.storage.app.db.dao;

import books.storage.app.services.BookStorageBaseException;

public class BookDaoException extends BookStorageBaseException {

    public BookDaoException(Exception ex) {
        super(ex);
    }

    public BookDaoException() {
    }
}
