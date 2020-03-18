package student.quiz.console;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import student.quiz.config.CSVFileProperties;
import student.quiz.domain.QuizResult;
import student.quiz.domain.QuizStatus;
import student.quiz.exceptions.GUIPrinterException;
import student.quiz.repository.api.model.Answer;
import student.quiz.repository.api.model.Question;

import java.util.Locale;
import java.util.Map;


@Service
@Slf4j
public class GUIPrinterImpl implements GUIPrinter {

    private static String GREETINGS_LOCALE_CODE = "quiz.greetings";
    private static final String NUMBER_OF_CORRECT_STRING_LOCALE_CODE = "quiz.number_of_correct_answers";
    private static final String RESULT_OF_QUIZ_LOCALE_CODE = "quiz.result";
    private static final String ANSWER_VARIANT = "%s. %s";

    private final MessageSource messageSource;
    private final Locale locale;

    public GUIPrinterImpl(MessageSource messageSource, CSVFileProperties csvFileProperties) {
        this.messageSource = messageSource;
        this.locale = csvFileProperties.getLocale();
    }

    @Override
    public void printGreetings() {
        printLocalizedText(GREETINGS_LOCALE_CODE);
    }

    @Override
    public void printQuestion(Question question) {
        try {
            System.out.printf(question.getLocalizedText(this.messageSource, locale));
            System.out.println();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new GUIPrinterException(ex);
        }
    }

    @Override
    public void printAnswerVariants(Map<String, Answer> answersVariants) {
        for (Map.Entry<String, Answer> entry : answersVariants.entrySet()) {
            String localizedAnswerText = entry.getValue().getLocalizedText(messageSource, locale);
            this.printText(String.format(ANSWER_VARIANT, entry.getKey(), localizedAnswerText));
        }
    }

    @Override
    public void printText(String text) {
        System.out.println(text);
    }

    @Override
    public void printLocalizedText(String stringCode) {
        printLocalizedText(stringCode, null);
    }

    private void printLocalizedText(String stringCode, Object[] arr) {
        try {
            String localizedText = messageSource.getMessage(stringCode, arr, locale);
            this.printText(localizedText);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new GUIPrinterException(ex);
        }
    }

    @Override
    public void printResult(QuizResult quizResult) {
        Integer correctAnswersNum = quizResult.getNumberOfCorrectAnswers();
        printLocalizedText(NUMBER_OF_CORRECT_STRING_LOCALE_CODE, new Integer[]{correctAnswersNum});
        QuizStatus quizStatus = quizResult.getQuizStatus();
        String localizedQuizStatusMessage = messageSource.getMessage(quizStatus.getI18nTextCode(), null, locale);
        printLocalizedText(RESULT_OF_QUIZ_LOCALE_CODE, new String[]{localizedQuizStatusMessage});
    }
}
