package com.quiz.student.service;

public interface IOService {

    void out(String text);

    void out(String template, String[] arr);

    String in();
}
