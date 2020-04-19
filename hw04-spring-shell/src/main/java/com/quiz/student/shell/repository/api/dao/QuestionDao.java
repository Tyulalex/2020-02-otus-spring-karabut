package com.quiz.student.shell.repository.api.dao;

import com.quiz.student.shell.repository.api.model.Question;

import java.util.List;

public interface QuestionDao {

    List<Question> getQuestions();

}
