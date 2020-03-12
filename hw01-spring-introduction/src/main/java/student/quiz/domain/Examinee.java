package student.quiz.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Examinee {

    private final String lastName;
    private final String firstName;
    private QuizResult quizResult;
}
