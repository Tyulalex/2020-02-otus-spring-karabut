package student.quiz.exceptions;

public class QuizBaseException extends RuntimeException {

    public QuizBaseException(Exception ex) {
        super(ex);
    }

    public QuizBaseException(String message) {
        super(message);
    }

    public QuizBaseException() {
        super();
    }
}
