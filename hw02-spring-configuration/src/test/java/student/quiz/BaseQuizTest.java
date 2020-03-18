package student.quiz;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
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

    protected Answer buildAnswer(int answerId, String textCode){
        return new Answer(answerId, textCode, QUESTION_ID, IS_CORRECT);
    }

    protected Question buildQuestion(int questionId) {
        return buildQuestion(questionId, QUESTION_TEXT);
    }

    protected Question buildQuestion(int questionId, String questionTextCode){
        return new Question(questionId, questionTextCode);
    }

    public MessageSource testMessageSource() {
        ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
        ms.setBasename("/i18n-test/messages-test");
        ms.setDefaultEncoding("UTF-8");
        return ms;
    }
}
