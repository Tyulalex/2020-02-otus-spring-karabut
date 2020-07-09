package books.storage.app.db.dao;

import books.storage.app.services.BookStorageBaseException;

public class AuthorDaoException extends BookStorageBaseException {

    public AuthorDaoException(Exception ex) {
        super(ex);
    }

}
