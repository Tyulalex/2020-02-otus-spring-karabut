package student.quiz.console;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;
import student.quiz.domain.Examinee;
import student.quiz.domain.QuizResult;
import student.quiz.repository.api.model.Answer;
import student.quiz.repository.api.model.Question;
import student.quiz.service.QuizService;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

@RequiredArgsConstructor
@Service("guiConsole")
public class GUIConsoleImpl implements GUIConsole {

    private static final String ENTER_USER_FIRSTNAME = "quiz.enter_first_name";
    private static final String ENTER_USER_LASTNAME = "quiz.enter_last_name";
    private static final String WRONG_INPUT = "quiz.error.wrong_input";
    private static final String WRONG_ANSWER_INPUT = "quiz.error.wrong_answer_input";
    private static final String YOUR_ANSWER_STRING = "quiz.your_answer";
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
        this.guiPrinter.printAnswerVariants(answersVariants);
        int answerId = -1;
        while (answerId <= 0) {
            this.guiPrinter.printLocalizedText(YOUR_ANSWER_STRING);
            answerId = parseAnswerId(this.SCANNER.nextLine());
            if (answerId <= 0) {
                this.guiPrinter.printLocalizedText(WRONG_ANSWER_INPUT);
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
        this.guiPrinter.printLocalizedText(String.format(userDataRequest));
        String userDataValue = null;
        while (StringUtils.isEmpty(userDataValue)) {
            userDataValue = SCANNER.nextLine();
            if (StringUtils.isEmpty(userDataValue)) {
                this.guiPrinter.printLocalizedText(WRONG_INPUT);
            }
        }
        return StringEscapeUtils.escapeJava(userDataValue);
    }
}
