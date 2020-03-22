package com.quiz.student.service;

public interface LocalizationService {

    String getLocalizedText(String text);

    String getLocalizedText(String textCode, String[] arr);
}
