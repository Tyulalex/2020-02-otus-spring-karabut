package com.quiz.student.shell.repository.impl.dao;

import com.quiz.student.shell.config.CSVFileProperties;
import com.quiz.student.shell.repository.api.dao.QuestionDao;
import com.quiz.student.shell.repository.api.dao.QuestionDaoException;
import com.quiz.student.shell.repository.api.dataextractor.CSVReader;
import com.quiz.student.shell.repository.api.model.Question;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
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
