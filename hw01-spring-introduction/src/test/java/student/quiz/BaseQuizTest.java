package student.quiz;

import student.quiz.repository.api.model.Answer;
import student.quiz.repository.api.model.Question;

public class BaseQuizTest {

    protected static final String QUESTION_TEXT = "TEXT";
    protected static final int QUESTION_ID = 7;
    protected static final String ANSWER_TEXT = "ANSWER_TEXT";
    protected static final boolean IS_CORRECT = true;
    protected static final boolean IS_INCORRECT = false;


    protected Answer buildAnswer(int answerId) {
        return new Answer(answerId, ANSWER_TEXT, QUESTION_ID, IS_CORRECT);
    }

    protected Answer buildAnswer(int answerId, boolean isCorrect) {
        return new Answer(answerId, ANSWER_TEXT, QUESTION_ID, isCorrect);
    }

    protected Question buildQuestion(int questionId) {
        return new Question(questionId, QUESTION_TEXT);
    }
}
