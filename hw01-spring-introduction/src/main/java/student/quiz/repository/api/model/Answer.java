package student.quiz.repository.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"Number", "Answer", "QuestionNumber", "IsCorrect"})
public class Answer {

    @JsonProperty("Number")
    private int id;
    @JsonProperty("Answer")
    private String text;
    @JsonProperty("QuestionNumber")
    private int questionId;
    @JsonProperty("IsCorrect")
    private boolean isCorrect;
}
