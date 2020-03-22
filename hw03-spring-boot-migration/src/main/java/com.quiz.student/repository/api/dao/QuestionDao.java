package com.quiz.student.repository.api.dao;

import com.quiz.student.repository.api.model.Question;

import java.util.List;

public interface QuestionDao {

    List<Question> getQuestions();

}
