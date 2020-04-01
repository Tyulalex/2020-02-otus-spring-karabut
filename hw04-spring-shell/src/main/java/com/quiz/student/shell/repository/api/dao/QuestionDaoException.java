package com.quiz.student.shell.repository.api.dao;

import com.quiz.student.shell.exceptions.QuizBaseException;

public class QuestionDaoException extends QuizBaseException {

    public QuestionDaoException(Exception ex) {
        super(ex);
    }
}
