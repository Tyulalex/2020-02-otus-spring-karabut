package books.storage.app.services;

public class BookServiceException extends RuntimeException {

    public BookServiceException(Exception ex) {
        super(ex);
    }
}
