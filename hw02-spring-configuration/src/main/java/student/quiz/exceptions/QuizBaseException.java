package student.quiz.exceptions;

public class QuizBaseException extends RuntimeException {

    public QuizBaseException(Exception ex) {
        super(ex);
    }
}
