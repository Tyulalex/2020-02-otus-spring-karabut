package books.orm.storage.service.exception;

public class BookStorageBaseException extends RuntimeException {

    public BookStorageBaseException(Exception ex) {
        super(ex);
    }

    public BookStorageBaseException() {
    }
}
