package student.quiz.domain;

import lombok.Data;

@Data
public class Examinee {

    private String firstName;
    private String lastName;
    private QuizResult quizResult;

    public Examinee(String lastName, String firstName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
