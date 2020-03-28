package com.quiz.student.service.impl;

import com.quiz.student.exceptions.ConsoleLocalizedIOServiceException;
import com.quiz.student.service.IOService;
import com.quiz.student.service.LocalizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("ConsoleLocalizedIOService")
@Slf4j
public class ConsoleLocalizedIOServiceImpl implements IOService {

    private final IOService ioService;
    private final LocalizationService localizationService;

    public ConsoleLocalizedIOServiceImpl(@Qualifier("consoleIoService") IOService ioService,
                                         LocalizationService localizationService) {
        this.ioService = ioService;
        this.localizationService = localizationService;
    }

    @Override
    public void out(String text) {
        this.outLocalizedText(text, null);
    }

    @Override
    public void out(String template, String[] arr) {
        this.outLocalizedText(template, arr);
    }

    @Override
    public String in() {
        return ioService.in();
    }

    private void outLocalizedText(String stringCode, String[] arr) {
        try {
            String localizedText = localizationService.getLocalizedText(stringCode, arr);
            ioService.out(localizedText);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new ConsoleLocalizedIOServiceException(ex);
        }
    }
}
