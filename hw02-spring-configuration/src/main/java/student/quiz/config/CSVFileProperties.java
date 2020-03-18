package student.quiz.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Getter
@Component
@PropertySource("classpath:csvfiles.properties")
public class CSVFileProperties {
    private final char delimiter;
    private final Resource questionResource;
    private final Resource answerResource;
    private Locale locale;

    public CSVFileProperties(@Value("${quiz.delimiter.csv}") char delimiter,
                             @Value("${quiz.questions.csv}") Resource questionResource,
                             @Value("${quiz.answers.csv}") Resource answerResource,
                             @Value("${locale}") Locale locale) {
        this.delimiter = delimiter;
        this.questionResource = questionResource;
        this.answerResource = answerResource;
        this.locale = locale;
    }
}
