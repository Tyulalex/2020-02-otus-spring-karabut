package com.quiz.student.shell.domain;

import lombok.Data;

@Data
public class Examinee {

    private final String firstName;
    private final String lastName;
    private QuizResult quizResult;

    public Examinee(String lastName, String firstName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
