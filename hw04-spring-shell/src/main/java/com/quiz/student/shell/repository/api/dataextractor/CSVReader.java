package com.quiz.student.shell.repository.api.dataextractor;

import java.io.InputStream;
import java.util.List;

public interface CSVReader<T> {

    List<T> getAllRecords(InputStream inputStream, char delimiter, Class clazz);

    List<T> getFilteredByColumnValueRecords(InputStream inputStream, char delimiter, Class Clazz, FilterData filterData);

}
