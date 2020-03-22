package com.quiz.student.repository.impl.dataextractor;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.quiz.student.repository.api.dataextractor.CSVReader;
import com.quiz.student.repository.api.dataextractor.CSVReaderException;
import com.quiz.student.repository.api.dataextractor.FilterData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class CSVReaderImpl<T> implements CSVReader {

    private final CsvMapper csvMapper;

    @Override
    public List<T> getAllRecords(InputStream inputStream, char delimiter, Class clazz) {
        CsvSchema csvSchema = this.csvMapper.schemaFor(clazz).withSkipFirstDataRow(true)
                .withColumnSeparator(delimiter).withoutQuoteChar();
        try {
            MappingIterator<T> objectIter =
                    this.csvMapper.readerFor(clazz).with(csvSchema).readValues(inputStream);

            return objectIter.readAll();
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
            throw new CSVReaderException(ex);
        }
    }

    @Override
    public List<T> getFilteredByColumnValueRecords(InputStream inputStream, char delimiter,
                                                   Class clazz, FilterData filterData) {

        List<T> objList = new ArrayList<>();
        CsvSchema csvSchema = this.csvMapper.schemaFor(clazz).withSkipFirstDataRow(true)
                .withColumnSeparator(delimiter).withoutQuoteChar();
        try (InputStream in = new BufferedInputStream(inputStream)) {

            MappingIterator<T> it = this.csvMapper.readerFor(clazz).with(csvSchema).readValues(in);
            while (it.hasNextValue()) {
                T obj = it.next();

                Field field = obj.getClass().getDeclaredField(filterData.getColumnName());
                field.setAccessible(true);
                Object fieldValue = field.get(obj);
                if (String.valueOf(fieldValue).equals(filterData.getColumnValue())) {
                    objList.add(obj);
                }
            }
            return objList;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new CSVReaderException(ex);
        }
    }
}
