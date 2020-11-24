package spring.book.mvc.exception;

public class BookNotFoundServiceException extends BookServiceException {

    public BookNotFoundServiceException(Exception ex) {
        super(ex);
    }

    public BookNotFoundServiceException(){
    }
}
