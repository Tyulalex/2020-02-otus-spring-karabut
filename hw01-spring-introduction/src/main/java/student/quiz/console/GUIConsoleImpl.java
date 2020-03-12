package student.quiz.console;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import student.quiz.domain.Examinee;
import student.quiz.domain.QuizResult;
import student.quiz.repository.api.model.Answer;
import student.quiz.repository.api.model.Question;
import student.quiz.service.QuizService;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

@RequiredArgsConstructor
public class GUIConsoleImpl implements GUIConsole {

    private static final String ENTER_USER_FIRSTNAME = "Please enter your first name";
    private static final String ENTER_USER_LASTNAME = "Please enter your last name";
    private static final String WRONG_INPUT = "Incorrect input, shall not be empty";
    private static final String WRONG_ANSWER_INPUT = "Incorrect format, please enter from 1-4";
    private static final String YOUR_ANSWER_STRING = "Your answer:";
    private static final Scanner SCANNER = new Scanner(System.in);
    private final GUIPrinter guiPrinter;
    private final QuizService quizService;

    @Override
    public void doQuiz() {
        this.guiPrinter.printGreetings();
        Examinee examinee = getUserInfo();
        List<Question> questions = this.quizService.getAllQuestions();
        QuizResult quizResult = new QuizResult(questions.size());
        for (Question question : questions) {
            handleQuestionInQuizAndSaveWIP(question, quizResult);
        }
        examinee.setQuizResult(quizResult);
        this.guiPrinter.printResult(quizResult);
    }

    private void handleQuestionInQuizAndSaveWIP(Question question, QuizResult quizResult) {
        this.guiPrinter.printQuestion(question);
        Map<String, Answer> answersVariants = this.quizService.findAnswerVariantsByQuestion(question);
        this.guiPrinter.printQuestionVariants(answersVariants);
        int answerId = -1;
        while (answerId <= 0) {
            this.guiPrinter.printText(YOUR_ANSWER_STRING);
            answerId = parseAnswerId(this.SCANNER.nextLine());
            if (answerId <= 0) {
                this.guiPrinter.printText(WRONG_ANSWER_INPUT);
            }
        }
        this.quizService.saveWorkInProgressResult(answersVariants.get(String.valueOf(answerId)), quizResult);

    }

    private int parseAnswerId(String answerId) {
        try {
            return Integer.parseInt(answerId);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    @Override
    public Examinee getUserInfo() {
        String firstName = getUserData(ENTER_USER_FIRSTNAME);
        String lastName = getUserData(ENTER_USER_LASTNAME);
        return new Examinee(lastName, firstName);
    }

    private String getUserData(String userDataRequest) {
        this.guiPrinter.printText(String.format(userDataRequest));
        String userDataValue = null;
        while (StringUtils.isEmpty(userDataValue)) {
            userDataValue = SCANNER.nextLine();
            if (StringUtils.isEmpty(userDataValue)) {
                this.guiPrinter.printText(WRONG_INPUT);
            }
        }
        return StringEscapeUtils.escapeJava(userDataValue);
    }
}
