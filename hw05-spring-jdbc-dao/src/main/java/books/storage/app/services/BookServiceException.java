package books.storage.app.services;

public class BookServiceException extends BookStorageBaseException {

    public BookServiceException(Exception ex) {
        super(ex);
    }
}
