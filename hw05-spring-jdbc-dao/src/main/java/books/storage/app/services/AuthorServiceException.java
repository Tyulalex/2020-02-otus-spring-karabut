package books.storage.app.services;

public class AuthorServiceException extends RuntimeException {

    public AuthorServiceException(Exception ex) {
        super(ex);
    }

}
