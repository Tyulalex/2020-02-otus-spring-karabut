package com.quiz.student.repository.api.dao;

import com.quiz.student.exceptions.QuizBaseException;

public class QuestionDaoException extends QuizBaseException {

    public QuestionDaoException(Exception ex) {
        super(ex);
    }
}
