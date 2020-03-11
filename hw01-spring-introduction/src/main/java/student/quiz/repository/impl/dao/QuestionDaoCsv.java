package student.quiz.repository.impl.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import student.quiz.repository.api.dao.QuestionDao;
import student.quiz.repository.api.dao.QuestionDaoException;
import student.quiz.repository.api.dataextractor.CSVReader;
import student.quiz.repository.api.model.Question;
import student.quiz.repository.impl.dataextractor.CSVFileProperties;

import java.io.InputStream;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class QuestionDaoCsv implements QuestionDao {

    private final CSVReader<Question> csvReader;
    private final CSVFileProperties fileProperties;

    @Override
    public List<Question> getQuestions() {
        try {
            InputStream fileInput = fileProperties.getQuestionResource().getInputStream();
            return this.csvReader.getAllRecords(fileInput, this.fileProperties.getDelimiter(), Question.class);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new QuestionDaoException(ex);
        }
    }
}
