package student.quiz.exceptions;

public class QuizServiceException extends QuizBaseException {

    public QuizServiceException(Exception ex) {
        super(ex);
    }
}
