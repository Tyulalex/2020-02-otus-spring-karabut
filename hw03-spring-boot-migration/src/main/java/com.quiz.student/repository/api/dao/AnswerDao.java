package com.quiz.student.repository.api.dao;

import com.quiz.student.repository.api.model.Answer;

import java.util.List;

public interface AnswerDao {

    List<Answer> findAnswersVariantsByQuestionId(int questionId);

}
