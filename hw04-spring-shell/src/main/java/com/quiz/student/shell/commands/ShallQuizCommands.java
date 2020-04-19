package com.quiz.student.shell.commands;

import com.quiz.student.shell.domain.Examinee;
import com.quiz.student.shell.domain.QuizResult;
import com.quiz.student.shell.domain.QuizStatus;
import com.quiz.student.shell.repository.api.model.Answer;
import com.quiz.student.shell.repository.api.model.Question;
import com.quiz.student.shell.service.IOService;
import com.quiz.student.shell.service.LocalizationService;
import com.quiz.student.shell.service.QuizService;
import com.quiz.student.shell.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;
import java.util.Map;

@ShellComponent
public class ShallQuizCommands {

    private static final String GREETINGS_LOCALE_CODE = "quiz.greetings";
    private static final String QUIZ_PROPOSAL = "quiz.quiz_start_proposal";
    private static final String WRONG_ANSWER_INPUT = "quiz.error.wrong_answer_input";
    private static final String YOUR_ANSWER_STRING = "quiz.your_answer";
    private static final String ANSWER_VARIANT = "%s. %s";
    private static final String RESULT_OF_QUIZ_LOCALE_CODE = "quiz.result";
    private static final String NUMBER_OF_CORRECT_STRING_LOCALE_CODE = "quiz.number_of_correct_answers";

    private final UserService userService;
    @Qualifier("ConsoleLocalizedIOService")
    private final IOService ioServiceLocalized;
    private final QuizService quizService;
    @Qualifier("consoleIoService")
    private final IOService ioService;
    private final LocalizationService localizationService;

    private Examinee examinee;

    public ShallQuizCommands(UserService userService,
                             @Qualifier("ConsoleLocalizedIOService") IOService ioServiceLocalized,
                             QuizService quizService,
                             @Qualifier("consoleIoService") IOService ioService,
                             LocalizationService localizationService) {
        this.userService = userService;
        this.ioServiceLocalized = ioServiceLocalized;
        this.quizService = quizService;
        this.localizationService = localizationService;
        this.ioService = ioService;
    }

    @ShellMethod(key = {"a", "auth"}, value = "Authenticate user")
    public void authenticateInQuiz() {
        ioServiceLocalized.out(GREETINGS_LOCALE_CODE);
        examinee = userService.getUserInfo();
        ioServiceLocalized.out(QUIZ_PROPOSAL, new String[]{examinee.getFirstName(),
                examinee.getLastName()});

    }

    @ShellMethod(key = {"s", "start", "start-quiz"}, value = "Start quiz")
    @ShellMethodAvailability("authenticationRequiredAvailability")
    public void startQuiz() {
        List<Question> questions = this.quizService.getAllQuestions();
        QuizResult quizResult = new QuizResult(questions.size());
        for (Question question : questions) {
            handleQuestionInQuizAndSaveWIP(question, quizResult);
        }
        examinee.setQuizResult(quizResult);
    }

    @ShellMethod(key = {"g", "get", "get-results"}, value = "Quiz Results")
    @ShellMethodAvailability("passQuizRequiredAvailability")
    public void getQuizResults() {
        QuizResult quizResult = examinee.getQuizResult();
        Integer correctAnswersNum = quizResult.getNumberOfCorrectAnswers();
        ioServiceLocalized.out(NUMBER_OF_CORRECT_STRING_LOCALE_CODE, new String[]{String.valueOf(correctAnswersNum)});
        QuizStatus quizStatus = quizResult.getQuizStatus();
        String localizedQuizStatusMessage = localizationService.getLocalizedText(quizStatus.getI18nTextCode());
        ioServiceLocalized.out(RESULT_OF_QUIZ_LOCALE_CODE, new String[]{localizedQuizStatusMessage});
    }

    public Availability authenticationRequiredAvailability() {
        return examinee != null
                ? Availability.available()
                : Availability.unavailable(localizationService.getLocalizedText("quiz.availability.auth_needed"));
    }

    public Availability passQuizRequiredAvailability() {
        return examinee != null && examinee.getQuizResult() != null
                ? Availability.available()
                : Availability.unavailable(localizationService.getLocalizedText("quiz.availability.get_results"));
    }

    private void handleQuestionInQuizAndSaveWIP(Question question, QuizResult quizResult) {
        ioService.out(String.format(question.getQuestionCode()));
        Map<String, Answer> answersVariants = this.quizService.findAnswerVariantsByQuestion(question);
        for (Map.Entry<String, Answer> entry : answersVariants.entrySet()) {
            String localizedAnswerText = entry.getValue().getAnswerText();
            this.ioService.out(String.format(ANSWER_VARIANT, entry.getKey(), localizedAnswerText));
        }
        String answerId = getAnswerId();
        this.quizService.saveWorkInProgressResult(answersVariants.get(answerId), quizResult);
    }

    private String getAnswerId() {
        int answerId = -1;
        while (isInvalidAnswer(answerId)) {
            this.ioServiceLocalized.out(YOUR_ANSWER_STRING);
            answerId = parseAnswerId(this.ioServiceLocalized.in());
            if (isInvalidAnswer(answerId)) {
                this.ioServiceLocalized.out(WRONG_ANSWER_INPUT);
            }
        }
        return String.valueOf(answerId);
    }

    private boolean isInvalidAnswer(int answerId) {
        return (answerId <= 0 || answerId > 4);
    }

    private int parseAnswerId(String answerId) {
        try {
            return Integer.parseInt(answerId);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }
}
