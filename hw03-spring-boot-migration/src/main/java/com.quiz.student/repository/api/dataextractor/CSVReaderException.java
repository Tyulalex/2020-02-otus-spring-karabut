package com.quiz.student.repository.api.dataextractor;

import com.quiz.student.exceptions.QuizBaseException;

public class CSVReaderException extends QuizBaseException {

    public CSVReaderException(Exception ex) {
        super(ex);
    }
}
