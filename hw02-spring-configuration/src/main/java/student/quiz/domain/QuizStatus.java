package student.quiz.domain;

import lombok.Getter;

public enum QuizStatus {

    EXCELLENT("quiz.status.excellent"),
    GOOD("quiz.status.good"),
    BAD("quiz.status.bad"),
    VERY_BAD("quiz.status.very_bad");

    @Getter
    private final String i18nTextCode;

    QuizStatus(String i18nTextCode) {
        this.i18nTextCode = i18nTextCode;
    }

}
