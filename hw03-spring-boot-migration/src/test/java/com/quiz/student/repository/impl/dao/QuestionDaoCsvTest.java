package com.quiz.student.repository.impl.dao;

import com.quiz.student.config.CSVFileProperties;
import com.quiz.student.repository.api.dao.QuestionDao;
import com.quiz.student.repository.api.dao.QuestionDaoException;
import com.quiz.student.repository.api.dataextractor.CSVReader;
import com.quiz.student.repository.api.dataextractor.CSVReaderException;
import com.quiz.student.repository.api.model.Question;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
class QuestionDaoCsvTest {

    private static final String QUESTION_TEXT = "TEXT";
    private static final Resource QUESTIONS_RESOURCE = new ClassPathResource("test.geography/questions.csv");

    @Autowired
    private QuestionDao questionDaoCsv;

    @MockBean
    private CSVFileProperties csvFileProperties;

    @MockBean
    private CSVReader<Question> csvReader;


    @DisplayName("get question return same result as csv reader get all records with params")
    @Test
    void getQuestions() {
        var expectedQuestions = List.of(buildQuestion(1));
        Mockito.when(csvFileProperties.getQuestionResource()).thenReturn(QUESTIONS_RESOURCE);

        Mockito.when(csvReader.getAllRecords(Mockito.any(InputStream.class), Mockito.anyChar(),
                ArgumentMatchers.<Class<Question>>any())).thenReturn(expectedQuestions);

        var actualQuestion = questionDaoCsv.getQuestions();
        assertThat(actualQuestion).isEqualTo(expectedQuestions);

        Mockito.verify(csvReader, times(1))
                .getAllRecords(Mockito.any(InputStream.class), Mockito.anyChar(), ArgumentMatchers.<Class<Question>>any());
    }

    @Test
    @DisplayName("Verify find get all questions throws QuestionDaoException")
    void getAllQuestionsThrowsException() {

        Mockito.when(csvFileProperties.getQuestionResource()).thenReturn(QUESTIONS_RESOURCE);
        Mockito.when(csvReader.getAllRecords(Mockito.any(InputStream.class), Mockito.anyChar(),
                ArgumentMatchers.<Class<Question>>any())).thenThrow(CSVReaderException.class);

        assertThrows(QuestionDaoException.class, () -> questionDaoCsv.getQuestions());
    }

    @TestConfiguration
    static class QuestionDaoConfiguration {

        @Bean
        public QuestionDao questionDaoCsv(CSVReader<Question> csvReader,
                                        CSVFileProperties fileProperties) {
            return new QuestionDaoCsv(csvReader, fileProperties);
        }
    }

    private Question buildQuestion(int questionId) {
        return buildQuestion(questionId, QUESTION_TEXT);
    }

    private Question buildQuestion(int questionId, String questionTextCode) {
        return new Question(questionId, questionTextCode);
    }

}