package com.quiz.student.service.impl;

import com.quiz.student.service.IOService;
import org.springframework.stereotype.Service;

import java.io.PrintStream;
import java.util.Scanner;

@Service("consoleIoService")
public class ConsoleIOServiceImpl implements IOService {

    private final PrintStream out;
    private final Scanner sc;

    public ConsoleIOServiceImpl() {
        out = System.out;
        sc = new Scanner(System.in);
    }


    @Override
    public void out(String text) {
        out.println(text);
    }

    @Override
    public void out(String template, String[] arr) {
        String text = String.format(template, arr);
        out.println(text);
    }

    @Override
    public String in() {
        return sc.nextLine();
    }
}
