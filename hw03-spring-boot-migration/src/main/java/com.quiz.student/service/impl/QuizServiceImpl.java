package com.quiz.student.service.impl;

import com.quiz.student.domain.QuizResult;
import com.quiz.student.exceptions.QuizServiceException;
import com.quiz.student.repository.api.dao.AnswerDao;
import com.quiz.student.repository.api.dao.QuestionDao;
import com.quiz.student.repository.api.model.Answer;
import com.quiz.student.repository.api.model.Question;
import com.quiz.student.service.QuizService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class QuizServiceImpl implements QuizService {

    private final AnswerDao answerDao;
    private final QuestionDao questionDao;

    @Override
    public Map<String, Answer> findAnswerVariantsByQuestion(Question question) {
        try {
            List<Answer> answersList = answerDao.findAnswersVariantsByQuestionId(question.getId());
            Map<String, Answer> answersWithNumber = new LinkedHashMap<>();
            for (int i = 1; i <= answersList.size(); i++) {
                answersWithNumber.put(String.valueOf(i), answersList.get(i - 1));
            }
            return answersWithNumber;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new QuizServiceException(ex);
        }
    }

    @Override
    public List<Question> getAllQuestions() {
        try {
            return questionDao.getQuestions();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new QuizServiceException(ex);
        }
    }

    @Override
    public QuizResult saveWorkInProgressResult(Answer answer, @NonNull QuizResult quizResult) {
        boolean isCorrectAnswer = answer.isCorrect();
        if (isCorrectAnswer) {
            int currentPoints = quizResult.getNumberOfCorrectAnswers();
            quizResult.setNumberOfCorrectAnswers(++currentPoints);
        }
        int questionsAnswered = quizResult.getNumberOfQuestionsAnswered();
        quizResult.setNumberOfQuestionsAnswered(++questionsAnswered);
        log.info(quizResult.toString());
        return quizResult;
    }
}
