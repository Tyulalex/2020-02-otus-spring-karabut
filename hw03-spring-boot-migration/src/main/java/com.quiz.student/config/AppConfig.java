package com.quiz.student.config;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.quiz.student.service.ConsoleQuizService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public CsvMapper csvMapper() {
        return new CsvMapper();
    }

    @Bean
    public CommandLineRunner starter(ConsoleQuizService consoleQuizService) {
        return args -> consoleQuizService.doQuiz();
    }
}
