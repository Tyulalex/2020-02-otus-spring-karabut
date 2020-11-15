package spring.book.mvc.exception;

public class GenreServiceException extends BookStorageBaseException {

    public GenreServiceException(Exception ex) {
        super(ex);
    }
}
