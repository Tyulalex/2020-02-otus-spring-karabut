package student.quiz.repository.impl.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import student.quiz.BaseQuizTest;
import student.quiz.config.CSVFileProperties;
import student.quiz.repository.api.dao.AnswerDaoException;
import student.quiz.repository.api.dataextractor.CSVReader;
import student.quiz.repository.api.dataextractor.CSVReaderException;
import student.quiz.repository.api.dataextractor.FilterData;
import student.quiz.repository.api.model.Answer;

import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

@DisplayName("Answer Dao Test")
@ExtendWith(MockitoExtension.class)
class AnswerDaoCsvTest extends BaseQuizTest {

    private static final Resource ANSWERS_RESOURCE = new ClassPathResource("test.geography/answers.csv");

    @Mock
    private CSVReader<Answer> csvReader;
    @Mock
    private CSVFileProperties fileProperties;

    @InjectMocks
    private AnswerDaoCsv answerDaoCsv;

    @Nested
    @DisplayName("Find Answer Variants By Question Id")
    class FindAnswerVariantsByQuestionId {


        @Test
        @DisplayName("Verify find answer variant by question id returns same result as csvreader get filtered values")
        void findAnswersVariantsByQuestionId() {

            var expectedAnswers = List.of(buildAnswer(1));

            Mockito.when(fileProperties.getAnswerResource()).thenReturn(ANSWERS_RESOURCE);
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

            Mockito.when(fileProperties.getAnswerResource()).thenReturn(ANSWERS_RESOURCE);
            Mockito.when(csvReader.getFilteredByColumnValueRecords(
                    Mockito.any(InputStream.class), Mockito.anyChar(),
                    ArgumentMatchers.<Class<Answer>>any(), Mockito.any(FilterData.class)))
                    .thenThrow(CSVReaderException.class);

            assertThrows(AnswerDaoException.class, () -> answerDaoCsv.findAnswersVariantsByQuestionId(QUESTION_ID));
        }
    }
}
