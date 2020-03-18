package student.quiz.repository.api.model;

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
@JsonPropertyOrder({"Number", "Question"})
public class Question implements Comparable<Question> {

    @JsonProperty("Number")
    private int id;
    @JsonProperty("Question")
    private String questionCode;

    public String getLocalizedText(MessageSource messageSource, Locale locale) {
        return messageSource.getMessage(questionCode, null, locale);
    }

    @Override
    public int compareTo(Question o) {
        return (this.id - o.id);
    }
}
