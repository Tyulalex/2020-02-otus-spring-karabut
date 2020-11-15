package spring.book.mvc.exception;

public class BookServiceException extends BookStorageBaseException {

    public BookServiceException(Exception ex) {
        super(ex);
    }

    public BookServiceException() {

    }
}
