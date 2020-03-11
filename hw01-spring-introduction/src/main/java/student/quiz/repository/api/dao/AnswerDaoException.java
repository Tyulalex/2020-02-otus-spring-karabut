package student.quiz.repository.api.dao;

import student.quiz.exceptions.QuizBaseException;

public class AnswerDaoException extends QuizBaseException {

    public AnswerDaoException(Exception ex) {
        super(ex);
    }
}
