package student.quiz.service;

import student.quiz.domain.QuizResult;
import student.quiz.repository.api.model.Answer;
import student.quiz.repository.api.model.Question;

import java.util.List;
import java.util.Map;

public interface QuizService {

    Map<String, Answer> findAnswerVariantsByQuestion(Question question);

    List<Question> getAllQuestions();

    QuizResult saveWorkInProgressResult(Answer answer, QuizResult quizResult);

}
