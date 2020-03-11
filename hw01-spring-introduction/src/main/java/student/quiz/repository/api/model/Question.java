package student.quiz.repository.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"Number", "Question"})
public class Question implements Comparable<Question> {

    @JsonProperty("Number")
    private int id;
    @JsonProperty("Question")
    private String text;

    @Override
    public int compareTo(Question o) {
        return (this.id - o.id);
    }
}
