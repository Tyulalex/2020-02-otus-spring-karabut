package student.quiz.console;

import lombok.RequiredArgsConstructor;
import student.quiz.domain.QuizResult;
import student.quiz.domain.QuizStatus;
import student.quiz.repository.api.model.Answer;
import student.quiz.repository.api.model.Question;
import student.quiz.service.QuizService;

import java.util.Map;

@RequiredArgsConstructor
public class GUIPrinterImpl implements GUIPrinter {

    private static String GREETINGS = "Hello, before starting the quiz, please enter the following information";
    private static final String NUMBER_OF_CORRECT_STRING = "Number of correct answers %d";
    private static final String RESULT_OF_QUIZ = "This is %s result";
    private static final String ANSWER_VARIANT = "%s. %s";

    private final QuizService quizService;

    @Override
    public void printGreetings() {
        System.out.println(GREETINGS);
    }

    @Override
    public void printQuestion(Question question) {
        System.out.printf(question.getText());
        System.out.println();
    }

    @Override
    public void printQuestionVariants(Map<String, Answer> answersVariants) {
        for (Map.Entry<String, Answer> entry : answersVariants.entrySet()) {
            this.printText(String.format(ANSWER_VARIANT, entry.getKey(), entry.getValue().getText()));
        }
    }

    @Override
    public void printText(String text) {
        System.out.println(text);
    }

    @Override
    public void printResult(QuizResult quizResult) {
        this.printText(String.format(NUMBER_OF_CORRECT_STRING, quizResult.getNumberOfCorrectAnswers()));
        QuizStatus quizStatus = quizResult.getQuizStatus();
        printText(String.format(RESULT_OF_QUIZ, quizStatus));
    }
}
