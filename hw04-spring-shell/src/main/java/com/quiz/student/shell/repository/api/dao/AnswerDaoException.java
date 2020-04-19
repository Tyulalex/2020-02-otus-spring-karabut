package com.quiz.student.shell.repository.api.dao;

import com.quiz.student.shell.exceptions.QuizBaseException;

public class AnswerDaoException extends QuizBaseException {

    public AnswerDaoException(Exception ex) {
        super(ex);
    }
}
