package books.storage.app.db.dao;

import books.storage.app.services.BookStorageBaseException;

public class GenreDaoException extends BookStorageBaseException {

    public GenreDaoException(Exception ex) {
        super(ex);
    }
}
