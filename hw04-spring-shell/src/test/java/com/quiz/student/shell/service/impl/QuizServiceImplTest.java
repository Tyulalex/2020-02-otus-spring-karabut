package com.quiz.student.shell.service.impl;

import com.quiz.student.shell.domain.QuizResult;
import com.quiz.student.shell.exceptions.QuizServiceException;
import com.quiz.student.shell.repository.api.dao.AnswerDao;
import com.quiz.student.shell.repository.api.dao.AnswerDaoException;
import com.quiz.student.shell.repository.api.dao.QuestionDao;
import com.quiz.student.shell.repository.api.dao.QuestionDaoException;
import com.quiz.student.shell.repository.api.model.Answer;
import com.quiz.student.shell.repository.api.model.Question;
import com.quiz.student.shell.service.QuizService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
class QuizServiceImplTest {
    private static final int TOTAL_QUESTIONS = 10;
    private static final String QUESTION_TEXT = "TEXT";
    private static final int QUESTION_ID = 7;
    protected static final String ANSWER_TEXT = "ANSWER_TEXT";
    private static final boolean IS_CORRECT = true;
    private static final boolean IS_INCORRECT = false;

    @Autowired
    private QuizService quizService;

    @MockBean
    private AnswerDao answerDao;

    @MockBean
    private QuestionDao questionDao;

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

    private Question buildQuestion(int questionId) {
        return buildQuestion(questionId, QUESTION_TEXT);
    }

    private Question buildQuestion(int questionId, String questionTextCode) {
        return new Question(questionId, questionTextCode);
    }

    private QuizResult buildQuizResult() {
        return new QuizResult(TOTAL_QUESTIONS);
    }

    private Answer buildAnswer(int answerId) {
        return new Answer(answerId, ANSWER_TEXT, QUESTION_ID, IS_CORRECT);
    }

    @TestConfiguration
    static class TestConfigurationQuizService {

        @Bean
        public QuizService quizService(AnswerDao answerDaoCSv, QuestionDao questionDaoCSV) {
            return new QuizServiceImpl(answerDaoCSv, questionDaoCSV);
        }
    }

}