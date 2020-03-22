package com.quiz.student.exceptions;

public class QuizServiceException extends QuizBaseException {

    public QuizServiceException(Exception ex) {
        super(ex);
    }
}
