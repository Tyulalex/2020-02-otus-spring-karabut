package student.quiz.console;

import student.quiz.domain.QuizResult;
import student.quiz.repository.api.model.Answer;
import student.quiz.repository.api.model.Question;

import java.util.Map;

public interface GUIPrinter {

    void printGreetings();

    void printQuestion(Question question);

    void printQuestionVariants(Map<String, Answer> answersVariants);

    void printText(String text);

    void printResult(QuizResult quizResult);
}
