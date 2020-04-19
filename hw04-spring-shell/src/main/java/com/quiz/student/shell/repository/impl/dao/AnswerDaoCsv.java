package com.quiz.student.shell.repository.impl.dao;

import com.quiz.student.shell.config.CSVFileProperties;
import com.quiz.student.shell.repository.api.dao.AnswerDao;
import com.quiz.student.shell.repository.api.dao.AnswerDaoException;
import com.quiz.student.shell.repository.api.dataextractor.CSVReader;
import com.quiz.student.shell.repository.api.dataextractor.FilterData;
import com.quiz.student.shell.repository.api.model.Answer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AnswerDaoCsv implements AnswerDao {

    private static final String QUESTION_ID = "questionId";

    private final CSVReader<Answer> csvReader;
    private final CSVFileProperties fileProperties;


    @Override
    public List<Answer> findAnswersVariantsByQuestionId(int questionId) {
        try {
            InputStream fileInput = fileProperties.getAnswerResource().getInputStream();
            FilterData filterData = new FilterData(QUESTION_ID, String.valueOf(questionId));
            List<Answer> answers = this.csvReader.getFilteredByColumnValueRecords(
                    fileInput, fileProperties.getDelimiter(), Answer.class, filterData);

            return answers;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new AnswerDaoException(ex);
        }
    }
}
