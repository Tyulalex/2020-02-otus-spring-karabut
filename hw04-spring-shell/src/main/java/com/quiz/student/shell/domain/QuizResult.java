package com.quiz.student.shell.domain;

import lombok.Data;

@Data
public class QuizResult {

    private final int totalQuestions;
    private int numberOfCorrectAnswers;
    private int numberOfQuestionsAnswered;

    public QuizResult(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int percentageOfCorrectAnswers() {
        return numberOfCorrectAnswers * 100 / numberOfQuestionsAnswered;
    }

    public QuizStatus getQuizStatus() {
        int percentageOfCorrectAnswers = this.percentageOfCorrectAnswers();
        if (percentageOfCorrectAnswers == 100) {
            return QuizStatus.EXCELLENT;
        } else if (percentageOfCorrectAnswers >= 80) {
            return QuizStatus.GOOD;
        } else if (percentageOfCorrectAnswers >= 50) {
            return QuizStatus.BAD;
        } else {
            return QuizStatus.VERY_BAD;
        }
    }
}
