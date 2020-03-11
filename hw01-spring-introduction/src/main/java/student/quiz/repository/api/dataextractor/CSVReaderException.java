package student.quiz.repository.api.dataextractor;

import student.quiz.exceptions.QuizBaseException;

public class CSVReaderException extends QuizBaseException {

    public CSVReaderException(Exception ex) {
        super(ex);
    }
}
