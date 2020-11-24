package spring.book.mvc.exception;

public class AuthorServiceException extends BookStorageBaseException {

    public AuthorServiceException(Exception ex) {
        super(ex);
    }
}
