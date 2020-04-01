package com.quiz.student.shell.repository.api.dao;

import com.quiz.student.shell.repository.api.model.Answer;

import java.util.List;

public interface AnswerDao {

    List<Answer> findAnswersVariantsByQuestionId(int questionId);

}
