package student.quiz.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import student.quiz.domain.QuizResult;
import student.quiz.exceptions.QuizServiceException;
import student.quiz.repository.api.dao.AnswerDao;
import student.quiz.repository.api.dao.QuestionDao;
import student.quiz.repository.api.model.Answer;
import student.quiz.repository.api.model.Question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final AnswerDao answerDao;
    private final QuestionDao questionDao;

    @Override
    public Map<String, Answer> findAnswerVariantsByQuestion(Question question) {
        try {
            List<Answer> answersList = answerDao.findAnswersVariantsByQuestionId(question.getId());
            Map<String, Answer> answersWithNumber = new HashMap<>();
            for (int i = 1; i <= answersList.size(); i++) {
                answersWithNumber.put(String.valueOf(i), answersList.get(i - 1));
            }
            return answersWithNumber;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new QuizServiceException(ex);
        }
    }

    @Override
    public List<Question> getAllQuestions() {
        try {
            return questionDao.getQuestions();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new QuizServiceException(ex);
        }
    }

    @Override
    public QuizResult saveWorkInProgressResult(Answer answer, @NonNull QuizResult quizResult) {
        boolean isCorrectAnswer = answer.isCorrect();
        if (isCorrectAnswer) {
            int currentPoints = quizResult.getNumberOfCorrectAnswers();
            quizResult.setNumberOfCorrectAnswers(currentPoints + 1);
        }
        int questionsAnswered = quizResult.getNumberOfQuestionsAnswered();
        quizResult.setNumberOfQuestionsAnswered(questionsAnswered + 1);
        log.info(quizResult.toString());
        return quizResult;
    }
}
