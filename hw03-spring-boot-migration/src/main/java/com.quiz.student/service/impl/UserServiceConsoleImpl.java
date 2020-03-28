package com.quiz.student.service.impl;

import com.quiz.student.domain.Examinee;
import com.quiz.student.service.IOService;
import com.quiz.student.service.UserService;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserServiceConsoleImpl implements UserService {

    private static final String ENTER_USER_FIRSTNAME = "quiz.enter_first_name";
    private static final String ENTER_USER_LASTNAME = "quiz.enter_last_name";
    private static final String WRONG_INPUT = "quiz.error.wrong_input";

    @Qualifier("ConsoleLocalizedIOService")
    private final IOService ioService;

    public UserServiceConsoleImpl(@Qualifier("ConsoleLocalizedIOService") IOService ioService) {
        this.ioService = ioService;
    }

    @Override
    public Examinee getUserInfo() {
        String firstName = getUserData(ENTER_USER_FIRSTNAME);
        String lastName = getUserData(ENTER_USER_LASTNAME);
        return new Examinee(lastName, firstName);
    }

    private String getUserData(String userDataRequest) {
        this.ioService.out(String.format(userDataRequest));
        String userDataValue = null;
        while (StringUtils.isEmpty(userDataValue)) {
            userDataValue = ioService.in();
            if (StringUtils.isEmpty(userDataValue)) {
                this.ioService.out(WRONG_INPUT);
            }
        }
        return StringEscapeUtils.escapeJava(userDataValue);
    }
}
