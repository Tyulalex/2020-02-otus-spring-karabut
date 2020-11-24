package spring.book.mvc.exception;

public class BookStorageBaseException extends RuntimeException {

    public BookStorageBaseException(Exception ex) {
        super(ex);
    }

    public BookStorageBaseException() {
    }
}
