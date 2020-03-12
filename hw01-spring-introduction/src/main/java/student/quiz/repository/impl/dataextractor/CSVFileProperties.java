package student.quiz.repository.impl.dataextractor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;

@Getter
@RequiredArgsConstructor
public class CSVFileProperties {
    private final char delimiter;
    private final Resource questionResource;
    private final Resource answerResource;
}
