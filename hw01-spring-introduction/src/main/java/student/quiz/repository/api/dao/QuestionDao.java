package student.quiz.repository.api.dao;

import student.quiz.repository.api.model.Question;

import java.util.List;

public interface QuestionDao {

    List<Question> getQuestions();

}
