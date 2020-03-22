package com.quiz.student.repository.impl.dao;

import com.quiz.student.config.CSVFileProperties;
import com.quiz.student.repository.api.dao.AnswerDao;
import com.quiz.student.repository.api.dao.AnswerDaoException;
import com.quiz.student.repository.api.dataextractor.CSVReader;
import com.quiz.student.repository.api.dataextractor.CSVReaderException;
import com.quiz.student.repository.api.dataextractor.FilterData;
import com.quiz.student.repository.api.model.Answer;
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
class AnswerDaoCsvTest {

    private static final Resource ANSWERS_RESOURCE = new ClassPathResource("test.geography/answers.csv");

    private static final int QUESTION_ID = 7;
    private static final String ANSWER_TEXT = "ANSWER_TEXT";
    private static final boolean IS_CORRECT = true;

    @Autowired
    private AnswerDao answerDaoCsv;

    @MockBean
    private CSVFileProperties csvFileProperties;

    @MockBean
    private CSVReader<Answer> csvReader;

    @Test
    @DisplayName("Verify find answer variant by question id returns same result as csvreader get filtered values")
    void findAnswersVariantsByQuestionId() {

        var expectedAnswers = List.of(buildAnswer(1));

        Mockito.when(csvFileProperties.getAnswerResource()).thenReturn(ANSWERS_RESOURCE);
        Mockito.when(csvReader.getFilteredByColumnValueRecords(
                Mockito.any(InputStream.class), Mockito.anyChar(),
                ArgumentMatchers.<Class<Answer>>any(), Mockito.any(FilterData.class))).thenReturn(expectedAnswers);

        var actualAnswers = answerDaoCsv.findAnswersVariantsByQuestionId(QUESTION_ID);

        assertThat(actualAnswers).isEqualTo(expectedAnswers);
        Mockito.verify(csvReader, times(1))
                .getFilteredByColumnValueRecords(Mockito.any(InputStream.class),
                        Mockito.anyChar(), ArgumentMatchers.<Class<Answer>>any(), Mockito.any(FilterData.class));
    }

    @Test
    @DisplayName("Verify find answer variant by question id throws AnswerDaoException")
    void findAnswersVariantsByQuestionIdThrowsException() {

        Mockito.when(csvFileProperties.getAnswerResource()).thenReturn(ANSWERS_RESOURCE);
        Mockito.when(csvReader.getFilteredByColumnValueRecords(
                Mockito.any(InputStream.class), Mockito.anyChar(),
                ArgumentMatchers.<Class<Answer>>any(), Mockito.any(FilterData.class)))
                .thenThrow(CSVReaderException.class);

        assertThrows(AnswerDaoException.class, () -> answerDaoCsv.findAnswersVariantsByQuestionId(QUESTION_ID));
    }

    protected Answer buildAnswer(int answerId) {
        return new Answer(answerId, ANSWER_TEXT, QUESTION_ID, IS_CORRECT);
    }

    @TestConfiguration
    static class AnswerDaoConfiguration {

        @Bean
        public AnswerDao answerDaoCsv(CSVReader<Answer> csvReader,
                                         CSVFileProperties fileProperties) {
            return new AnswerDaoCsv(csvReader, fileProperties);
        }
    }

}