package com.quiz.student.shell.repository.api.dataextractor;

import com.quiz.student.shell.exceptions.QuizBaseException;

public class CSVReaderException extends QuizBaseException {

    public CSVReaderException(Exception ex) {
        super(ex);
    }
}
