package book.springdata.sql.storage.service.exception;

public class BookServiceException extends BookStorageBaseException {

    public BookServiceException(Exception ex) {
        super(ex);
    }

    public BookServiceException() {

    }
}
