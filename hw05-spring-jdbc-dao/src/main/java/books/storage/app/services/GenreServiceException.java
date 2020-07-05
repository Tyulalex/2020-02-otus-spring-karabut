package books.storage.app.services;

public class GenreServiceException extends RuntimeException {

    public GenreServiceException(Exception ex) {
        super(ex);
    }
}
