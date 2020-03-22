package com.quiz.student.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.Locale;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "csv")
public class CSVFileProperties {
    private char delimiter;
    private Map<String, Map<String, Resource>> files;
    private Resource questionResource;
    private Resource answerResource;
    private Locale locale;

    public Resource getQuestionResource() {
        Resource questionResource = files.get(locale.toString()).get("questions_csv");
        if (questionResource == null){
            questionResource = getDefault("questions_csv");
        }
        return questionResource;
    }

    public Resource getAnswerResource(){
        Resource answerResource = files.get(locale.toString()).get("answers_csv");
        if (answerResource == null){
            answerResource = getDefault("answers_csv");
        }
        return answerResource;
    }

    public Resource getDefault(String fileName){
        return files.get("default").get(fileName);
    }
}
