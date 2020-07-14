package books.storage.app.services;

public class BookStorageBaseException extends RuntimeException {

    public BookStorageBaseException(Exception ex) {
        super(ex);
    }

    public BookStorageBaseException() {
    }
}
