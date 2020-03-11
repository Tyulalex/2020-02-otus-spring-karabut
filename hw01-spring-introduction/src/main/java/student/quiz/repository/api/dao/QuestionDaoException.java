package student.quiz.repository.api.dao;

import student.quiz.exceptions.QuizBaseException;

public class QuestionDaoException extends QuizBaseException {

    public QuestionDaoException(Exception ex) {
        super(ex);
    }
}
