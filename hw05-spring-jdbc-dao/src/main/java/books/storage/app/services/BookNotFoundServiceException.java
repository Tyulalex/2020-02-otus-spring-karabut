package books.storage.app.services;

public class BookNotFoundServiceException extends BookServiceException {
    public BookNotFoundServiceException(Exception ex) {
        super(ex);
    }
}
