package com.quiz.student.service.impl;

import com.quiz.student.domain.Examinee;
import com.quiz.student.domain.QuizResult;
import com.quiz.student.domain.QuizStatus;
import com.quiz.student.repository.api.model.Answer;
import com.quiz.student.repository.api.model.Question;
import com.quiz.student.service.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ConsoleQuizServiceImpl implements ConsoleQuizService {

    private static final String GREETINGS_LOCALE_CODE = "quiz.greetings";
    private static final String WRONG_ANSWER_INPUT = "quiz.error.wrong_answer_input";
    private static final String YOUR_ANSWER_STRING = "quiz.your_answer";
    private static final String ANSWER_VARIANT = "%s. %s";
    private static final String RESULT_OF_QUIZ_LOCALE_CODE = "quiz.result";
    private static final String NUMBER_OF_CORRECT_STRING_LOCALE_CODE = "quiz.number_of_correct_answers";


    private final UserService userService;
    @Qualifier("ConsoleLocalizedIOService")
    private final IOService ioServiceLocalized;
    @Qualifier("consoleIoService")
    private final IOService ioService;
    private final QuizService quizService;
    private final LocalizationService localizationService;

    public ConsoleQuizServiceImpl(UserService userService,
                                  @Qualifier("ConsoleLocalizedIOService") IOService ioServiceLocalized,
                                  @Qualifier("consoleIoService") IOService ioService,
                                  QuizService quizService,
                                  LocalizationService localizationService) {
        this.userService = userService;
        this.ioServiceLocalized = ioServiceLocalized;
        this.quizService = quizService;
        this.localizationService = localizationService;
        this.ioService = ioService;
    }

    @Override
    public void doQuiz() {
        ioServiceLocalized.out(GREETINGS_LOCALE_CODE);
        Examinee examinee = userService.getUserInfo();
        List<Question> questions = this.quizService.getAllQuestions();
        QuizResult quizResult = new QuizResult(questions.size());
        for (Question question : questions) {
            handleQuestionInQuizAndSaveWIP(question, quizResult);
        }
        examinee.setQuizResult(quizResult);
        this.printResult(quizResult);
    }

    private void handleQuestionInQuizAndSaveWIP(Question question, QuizResult quizResult) {
        this.printQuestion(question);
        Map<String, Answer> answersVariants = this.quizService.findAnswerVariantsByQuestion(question);
        this.printAnswerVariants(answersVariants);
        String answerId = getAnswerId();
        this.quizService.saveWorkInProgressResult(answersVariants.get(answerId), quizResult);
    }

    private String getAnswerId() {
        int answerId = -1;
        while (answerId <= 0) {
            this.ioServiceLocalized.out(YOUR_ANSWER_STRING);
            answerId = parseAnswerId(this.ioServiceLocalized.in());
            if (answerId <= 0) {
                this.ioServiceLocalized.out(WRONG_ANSWER_INPUT);
            }
        }
        return String.valueOf(answerId);
    }

    private int parseAnswerId(String answerId) {
        try {
            return Integer.parseInt(answerId);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    private void printQuestion(Question question) {
        ioService.out(String.format(question.getQuestionCode()));
    }

    private void printAnswerVariants(Map<String, Answer> answersVariants) {
        for (Map.Entry<String, Answer> entry : answersVariants.entrySet()) {
            String localizedAnswerText = entry.getValue().getAnswerText();
            this.ioService.out(String.format(ANSWER_VARIANT, entry.getKey(), localizedAnswerText));
        }
    }

    private void printResult(QuizResult quizResult) {
        Integer correctAnswersNum = quizResult.getNumberOfCorrectAnswers();
        ioServiceLocalized.out(NUMBER_OF_CORRECT_STRING_LOCALE_CODE, new String[]{String.valueOf(correctAnswersNum)});
        QuizStatus quizStatus = quizResult.getQuizStatus();
        String localizedQuizStatusMessage = localizationService.getLocalizedText(quizStatus.getI18nTextCode());
        ioServiceLocalized.out(RESULT_OF_QUIZ_LOCALE_CODE, new String[]{localizedQuizStatusMessage});
    }
}
