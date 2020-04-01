package com.quiz.student.shell.exceptions;

public class QuizBaseException extends RuntimeException {

    public QuizBaseException(Exception ex) {
        super(ex);
    }
}
