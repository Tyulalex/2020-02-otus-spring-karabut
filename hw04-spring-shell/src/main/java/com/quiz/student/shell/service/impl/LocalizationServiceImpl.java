package com.quiz.student.shell.service.impl;

import com.quiz.student.shell.config.CSVFileProperties;
import com.quiz.student.shell.service.LocalizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocalizationServiceImpl implements LocalizationService {

    private final MessageSource messageSource;
    private final CSVFileProperties csvFileProperties;

    @Override
    public String getLocalizedText(String textCode) {
        return messageSource.getMessage(textCode, null, csvFileProperties.getLocale());
    }

    @Override
    public String getLocalizedText(String textCode, String[] arr) {
        return messageSource.getMessage(textCode, arr, csvFileProperties.getLocale());
    }
}
