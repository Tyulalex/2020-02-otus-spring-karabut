package student.quiz.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import student.quiz.BaseQuizTest;
import student.quiz.domain.QuizResult;
import student.quiz.exceptions.QuizServiceException;
import student.quiz.repository.api.dao.AnswerDao;
import student.quiz.repository.api.dao.AnswerDaoException;
import student.quiz.repository.api.dao.QuestionDao;
import student.quiz.repository.api.dao.QuestionDaoException;
import student.quiz.repository.api.model.Answer;
import student.quiz.repository.api.model.Question;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

@DisplayName("Quiz Service Test")
@ExtendWith(MockitoExtension.class)
class QuizServiceImplTest extends BaseQuizTest {

    @Mock
    private AnswerDao answerDao;

    @Mock
    private QuestionDao questionDao;

    @InjectMocks
    private QuizServiceImpl quizService;

    @Nested
    @DisplayName("Verify get all question method works as expected")
    class GetAllQuestionMethodTest {


        @Test
        @DisplayName("Verify get all question when questionDao returns non empty result")
        void verifyGetAllQuestionSuccess() {
            var expectedList = List.of(buildQuestion(QUESTION_ID), buildQuestion(QUESTION_ID + 1));
            Mockito.when(questionDao.getQuestions()).thenReturn(expectedList);
            var actialList = quizService.getAllQuestions();
            assertThat(actialList).isEqualTo(expectedList);
            Mockito.verify(questionDao, times(1)).getQuestions();
        }

        @Test
        @DisplayName("Verify QuizServiceException is thrown when questionDao throws exception")
        void verifyQuizServiceExceptionInGetAllQuestion() {
            Mockito.when(questionDao.getQuestions()).thenThrow(QuestionDaoException.class);
            assertThrows(QuizServiceException.class, quizService::getAllQuestions);
        }
    }

    @Nested
    @DisplayName("Verify find answer variants by question")
    class FindAnswerVariantsByQuestion {


        @Test
        @DisplayName("Verify find answer variants when answerDao returns non empty result")
        void verifyFindAnswerVariantsSuccess() {
            var expectedAnswers = List.of(
                    buildAnswer(1),
                    buildAnswer(2));
            Mockito.when(answerDao.findAnswersVariantsByQuestionId(QUESTION_ID)).thenReturn(expectedAnswers);

            var enumeratedAnswerMap = quizService.findAnswerVariantsByQuestion(buildQuestion(QUESTION_ID));
            Mockito.verify(answerDao, times(1)).findAnswersVariantsByQuestionId(QUESTION_ID);

            assertThat(enumeratedAnswerMap)
                    .isNotEmpty()
                    .hasSize(2).
                    isInstanceOf(HashMap.class);

            assertThat(enumeratedAnswerMap.get("1")).isEqualTo(buildAnswer(1));
            assertThat(enumeratedAnswerMap.get("2")).isEqualTo(buildAnswer(2));
        }

        @Test
        @DisplayName("Verify find answer variants throws QuizServiceException when answerDao throws Exception")
        void verifyFindAnswerVariantsThrowsQuizServiceException() {
            Mockito.when(answerDao.findAnswersVariantsByQuestionId(QUESTION_ID)).thenThrow(AnswerDaoException.class);
            assertThrows(QuizServiceException.class, () -> quizService.findAnswerVariantsByQuestion(buildQuestion(QUESTION_ID)));
        }

    }


    @Nested
    @DisplayName("Verify save work in progress result method")
    class SaveWorkInProgressResultMethod {


        private static final int TOTAL_QUESTIONS = 10;


        @Test
        @DisplayName("Verify numberOfAnsweredQuestion and correctQuestion increased if answer is correct")
        void saveWorkInProgressResultCorrectAnswer() {
            var quizResult = buildQuizResult();
            var answer = Mockito.mock(Answer.class);
            Mockito.when(answer.isCorrect()).thenReturn(IS_CORRECT);
            var initialNumOfCorrectAnswers = quizResult.getNumberOfCorrectAnswers();
            var initialNumOfQuestionAnswered = quizResult.getNumberOfQuestionsAnswered();
            quizService.saveWorkInProgressResult(answer, quizResult);
            assertThat(quizResult.getNumberOfCorrectAnswers()).isEqualTo(initialNumOfCorrectAnswers + 1);
            assertThat(quizResult.getNumberOfQuestionsAnswered()).isEqualTo(initialNumOfQuestionAnswered + 1);
        }

        @Test
        @DisplayName("Verify numberOfAnsweredQuestion increased and correctQuestion not if answer is incorrect")
        void saveWorkInProgressResultIncorrectAnswer() {
            var quizResult = buildQuizResult();
            var answer = Mockito.mock(Answer.class);
            Mockito.when(answer.isCorrect()).thenReturn(IS_INCORRECT);
            var initialNumOfCorrectAnswers = quizResult.getNumberOfCorrectAnswers();
            var initialNumOfQuestionAnswered = quizResult.getNumberOfQuestionsAnswered();
            quizService.saveWorkInProgressResult(answer, quizResult);
            assertThat(quizResult.getNumberOfCorrectAnswers()).isEqualTo(initialNumOfCorrectAnswers);
            assertThat(quizResult.getNumberOfQuestionsAnswered()).isEqualTo(initialNumOfQuestionAnswered + 1);
        }


        QuizResult buildQuizResult() {
            return new QuizResult(TOTAL_QUESTIONS);
        }
    }
}