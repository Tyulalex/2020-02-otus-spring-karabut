package com.quiz.student.shell.service;

import com.quiz.student.shell.domain.QuizResult;
import com.quiz.student.shell.repository.api.model.Answer;
import com.quiz.student.shell.repository.api.model.Question;

import java.util.List;
import java.util.Map;

public interface QuizService {

    Map<String, Answer> findAnswerVariantsByQuestion(Question question);

    List<Question> getAllQuestions();

    QuizResult saveWorkInProgressResult(Answer answer, QuizResult quizResult);

}
