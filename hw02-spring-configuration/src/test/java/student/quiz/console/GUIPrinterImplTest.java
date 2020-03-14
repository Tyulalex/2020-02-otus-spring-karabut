package student.quiz.console;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import student.quiz.BaseQuizTest;
import student.quiz.config.CSVFileProperties;
import student.quiz.domain.QuizResult;
import student.quiz.domain.QuizStatus;
import student.quiz.repository.api.model.Answer;
import student.quiz.repository.api.model.Question;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("GUI Printer Test")
@ExtendWith(MockitoExtension.class)
class GUIPrinterImplTest extends BaseQuizTest {

    private static final Locale TEST_LOCALE = Locale.ENGLISH;
    private static final String TEST_GREETINGS = "Hello, before starting the quiz, please enter the following information";
    private static final String LOCALIZED_QUESTION_TEXT = "Localized Question :%n Nauru";
    private static final String LOCALIZED_ANSWER_ONE_TEXT = "Localized Answer One Text";
    private static final String LOCALIZED_ANSWER_TWO_TEXT = "Localized Answer Two Text";
    private static final String NUMBER_OF_CORRECT_QUESTIONS = "Number of correct answers: {0}";
    private static final String QUIZ_RESULT = "This is {0} result";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Mock
    private CSVFileProperties csvFileProperties;

    @Mock
    private MessageSource messageSource;

    private GUIPrinterImpl guiPrinter;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        when(csvFileProperties.getLocale()).thenReturn(TEST_LOCALE);
        guiPrinter = new GUIPrinterImpl(messageSource, csvFileProperties);
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Nested
    @DisplayName("Print greeting tests")
    class PrintGreetingsMethodTests {

        @DisplayName("success greeting test printing")
        @Test
        void verifySuccessGreetingPrinting() {
            mockMessageSourceFor("quiz.greetings", TEST_GREETINGS);
            guiPrinter.printGreetings();
            String actualOutput = filteredOutLogInfo(outContent.toString());
            assertThat(actualOutput).isEqualTo(TEST_GREETINGS);
        }
    }

    @Nested
    @DisplayName("Print Question tests")
    class PrintQuestionMethodTests {

        @DisplayName("verify printing question text")
        @Test
        void verifyPrintingQuestionText() {
            Question question = buildQuestion(1, "question.first");
            mockMessageSourceFor(question.getQuestionCode(), LOCALIZED_QUESTION_TEXT);
            guiPrinter.printQuestion(question);
            String actualOutput = filteredOutLogInfo(outContent.toString());
            assertThat(actualOutput).isEqualTo(String.format(LOCALIZED_QUESTION_TEXT));
        }
    }

    @Nested
    @DisplayName("Verify print answer variants tests")
    class PrintAnswerVariantsMethodTests {

        @DisplayName("verify printing answer variants")
        @Test
        void verifyPrintingAnswerVariants() {
            Answer answer1 = buildAnswer(1, "answer.textone");
            Answer answer2 = buildAnswer(2, "answer.texttwo");
            mockMessageSourceFor(answer1.getAnswerText(), LOCALIZED_ANSWER_ONE_TEXT);
            mockMessageSourceFor(answer2.getAnswerText(), LOCALIZED_ANSWER_TWO_TEXT);

            Map<String, Answer> answersVariants = new LinkedHashMap<>();
            answersVariants.put("1", answer1);
            answersVariants.put("2", answer2);

            guiPrinter.printAnswerVariants(answersVariants);
            String actualOutput = filteredOutLogInfo(outContent.toString());
            String expectedOutput = String.format("%s. %s%n%s. %s", "1", LOCALIZED_ANSWER_ONE_TEXT, "2", LOCALIZED_ANSWER_TWO_TEXT);
            assertThat(actualOutput).isEqualTo(expectedOutput);
        }
    }

    @Nested
    @DisplayName("Verify print result method")
    class PrintResultMethodTests {

        QuizResult quizResult;


        @BeforeEach
        void setUp() {
            quizResult = Mockito.mock(QuizResult.class);
        }


        @DisplayName("Verify print result method success")
        @Test
        void verifyPrintResult() {
            when(quizResult.getNumberOfCorrectAnswers()).thenReturn(4);
            when(quizResult.getQuizStatus()).thenReturn(QuizStatus.GOOD);
            mockMessageSourceFor(QuizStatus.GOOD.getI18nTextCode(), "good");
            when(messageSource.getMessage(
                    Mockito.eq("quiz.number_of_correct_answers"),
                    Mockito.eq(new Integer[]{4}),
                    Mockito.eq(TEST_LOCALE)
            )).thenReturn(NUMBER_OF_CORRECT_QUESTIONS);
            when(messageSource.getMessage(
                    Mockito.eq("quiz.result"),
                    Mockito.eq(new String[]{"good"}),
                    Mockito.eq(TEST_LOCALE)
            )).thenReturn(QUIZ_RESULT);

            guiPrinter.printResult(quizResult);
            String actualOutput = filteredOutLogInfo(outContent.toString());
            String firstExpected = String.format(NUMBER_OF_CORRECT_QUESTIONS, 4);
            String secondExpected = String.format(QUIZ_RESULT, "good");
            assertThat(actualOutput).isEqualTo(String.format("%s%n%s", firstExpected, secondExpected));
        }
    }

    private void mockMessageSourceFor(String mockValue, String returnValue) {
        when(messageSource.getMessage(Mockito.eq(mockValue),
                Mockito.isNull(), Mockito.eq(TEST_LOCALE))).thenReturn(returnValue);
    }

    private String filteredOutLogInfo(String outContent) {
        String[] lines = outContent.split("\\r?\\n");
        String[] filteredArray = Arrays.stream(lines).filter(line -> !line.startsWith("log4j:")).toArray(String[]::new);
        return String.format(String.join("%n", filteredArray));
    }

}