package student.quiz.repository.api.dao;

import student.quiz.repository.api.model.Answer;

import java.util.List;

public interface AnswerDao {

    List<Answer> findAnswersVariantsByQuestionId(int questionId);

}
