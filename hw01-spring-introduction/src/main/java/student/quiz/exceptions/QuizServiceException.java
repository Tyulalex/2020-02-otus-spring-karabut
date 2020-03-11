package student.quiz.exceptions;

public class QuizServiceException extends QuizBaseException {

    public QuizServiceException(Exception ex) {
        super(ex);
    }

    public QuizServiceException(String message) {
        super(message);
    }

    public QuizServiceException() {
        super();
    }
}
