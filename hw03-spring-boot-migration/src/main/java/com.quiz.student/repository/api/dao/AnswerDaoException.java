package com.quiz.student.repository.api.dao;

import com.quiz.student.exceptions.QuizBaseException;

public class AnswerDaoException extends QuizBaseException {

    public AnswerDaoException(Exception ex) {
        super(ex);
    }
}
