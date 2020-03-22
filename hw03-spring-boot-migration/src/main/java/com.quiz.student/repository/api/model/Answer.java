package com.quiz.student.repository.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.MessageSource;

import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"Number", "Answer", "QuestionNumber", "IsCorrect"})
public class Answer {

    @JsonProperty("Number")
    private int id;
    @JsonProperty("Answer")
    private String answerText;
    @JsonProperty("QuestionNumber")
    private int questionId;
    @JsonProperty("IsCorrect")
    private boolean isCorrect;

    public String getLocalizedText(MessageSource messageSource, Locale locale) {
        return messageSource.getMessage(answerText, null, locale);
    }
}
