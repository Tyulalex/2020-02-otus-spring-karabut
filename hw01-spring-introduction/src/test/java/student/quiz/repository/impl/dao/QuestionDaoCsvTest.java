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
import student.quiz.repository.api.dao.QuestionDaoException;
import student.quiz.repository.api.dataextractor.CSVReader;
import student.quiz.repository.api.dataextractor.CSVReaderException;
import student.quiz.repository.api.model.Question;
import student.quiz.repository.impl.dataextractor.CSVFileProperties;

import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

@DisplayName("Question Dao Test")
@ExtendWith(MockitoExtension.class)
class QuestionDaoCsvTest extends BaseQuizTest {

    private static final Resource QUESTIONS_RESOURCE = new ClassPathResource("test.geography/questions.csv");

    @Mock
    private CSVReader<Question> csvReader;
    @Mock
    private CSVFileProperties fileProperties;

    @InjectMocks
    private QuestionDaoCsv questionDaoCsv;

    @Nested
    @DisplayName("Get all questions")
    class GetAllQuestions {

        @DisplayName("get question return same result as csv reader get all records with params")
        @Test
        void getQuestions() {
            List<Question> expectedQuestions = List.of(buildQuestion(1));
            Mockito.when(fileProperties.getQuestionResource()).thenReturn(QUESTIONS_RESOURCE);

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

            Mockito.when(fileProperties.getQuestionResource()).thenReturn(QUESTIONS_RESOURCE);
            Mockito.when(csvReader.getAllRecords(Mockito.any(InputStream.class), Mockito.anyChar(),
                    ArgumentMatchers.<Class<Question>>any())).thenThrow(CSVReaderException.class);

            assertThrows(QuestionDaoException.class, () -> questionDaoCsv.getQuestions());
        }

    }
}
